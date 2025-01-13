package ru.base.game.server.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.base.game.server.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final UserRepository userRepository;

    @Autowired
    public WebSocketConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SocketHandler socketHandler() {
        return new SocketHandler(userRepository);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Slf4j
    public static final class SocketHandler extends TextWebSocketHandler {
        private static final Gson GSON = new GsonBuilder().create();
        private final Map<String, List<WebSocketSession>> sessions = new HashMap<>();
        private final UserRepository userRepository;

        public SocketHandler(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            if (log.isDebugEnabled()) {
                log.debug("{}: {}", session.getId(), message);
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            Principal principal = session.getPrincipal();
            Objects.requireNonNull(principal);
            sessions.computeIfAbsent(principal.getName(), k -> new ArrayList<>()).remove(session);
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            Principal principal = session.getPrincipal();
            Objects.requireNonNull(principal);
            sessions.computeIfAbsent(principal.getName(), k -> new ArrayList<>()).add(session);
        }
    }
}
