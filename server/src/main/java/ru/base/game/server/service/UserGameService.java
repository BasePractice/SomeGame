package ru.base.game.server.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.base.game.server.dto.EventObject;
import ru.base.game.server.repository.UserRepository;

import java.security.Principal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("PMD.CloseResource")
public interface UserGameService {
    void handleMessage(Principal principal, String message);

    void addSession(Principal principal, WebSocketSession session);

    void removeSession(Principal principal, WebSocketSession session);

    void sendMessage(String username, String message);

    void sendEvent(String username, EventObject<?> object);

    @Slf4j
    @Service("UserGameService.Default")
    final class Default implements UserGameService {
        private final ExecutorService executor = Executors.newCachedThreadPool();
        private final Map<String, List<WebSocketSession>> sessions = new HashMap<>();
        private final Map<String, GameCircle> games = new ConcurrentHashMap<>();
        private final UserRepository userRepository;
        private final Gson gson;

        @Autowired
        public Default(UserRepository userRepository, Gson gson) {
            this.userRepository = userRepository;
            this.gson = gson;
            executor.submit(() -> {
                while (true) {
                    List<GameCircle> circles = new ArrayList<>(games.values());
                    for (GameCircle circle : circles) {
                        if (circle.isDead()) {
                            circle.stop();
                            games.remove(circle.username);
                        }
                    }
                    Thread.sleep(Duration.ofMinutes(2).toMillis());
                }
            });
        }

        @Override
        public void handleMessage(Principal principal, String message) {
            createGameIfNotExists(principal).addCommand(message);
        }

        @Override
        public void addSession(Principal principal, WebSocketSession session) {
            sessions.computeIfAbsent(principal.getName(), k -> new ArrayList<>()).add(session);
            createGameIfNotExists(principal);
        }

        private GameCircle createGameIfNotExists(Principal principal) {
            return games.computeIfAbsent(principal.getName(), k -> {
                GameCircle gameCircle = new GameCircle(principal.getName(), this);
                executor.execute(gameCircle);
                return gameCircle;
            });
        }

        @Override
        public void removeSession(Principal principal, WebSocketSession session) {
            sessions.computeIfAbsent(principal.getName(), k -> new ArrayList<>()).remove(session);
        }

        @SneakyThrows
        @Override
        public void sendMessage(String username, String message) {
            List<WebSocketSession> sessionList = sessions.get(username);
            if (sessionList != null) {
                for (WebSocketSession session : sessionList) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        }

        @Override
        public void sendEvent(String username, EventObject<?> object) {
            if (log.isDebugEnabled()) {
                log.debug("Sending event {} to {}", object, username);
            }
            sendMessage(username, gson.toJson(object));
        }
    }
}
