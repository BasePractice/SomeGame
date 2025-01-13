package ru.base.game.server.configuration;

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
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ru.base.game.server.service.UserGameService;

import java.security.Principal;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final UserGameService userGameService;

    @Autowired
    public WebSocketConfiguration(UserGameService userGameService) {
        this.userGameService = userGameService;
    }

    @Bean
    public SocketHandler socketHandler() {
        return new SocketHandler(userGameService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Slf4j
    public static final class SocketHandler extends AbstractWebSocketHandler {
        private final UserGameService userGameService;

        public SocketHandler(UserGameService userGameService) {
            this.userGameService = userGameService;
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
            Principal principal = session.getPrincipal();
            Objects.requireNonNull(principal);
            userGameService.handleMessage(principal, message.getPayload());
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
            Principal principal = session.getPrincipal();
            Objects.requireNonNull(principal);
            userGameService.removeSession(principal, session);
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            Principal principal = session.getPrincipal();
            Objects.requireNonNull(principal);
            userGameService.addSession(principal, session);
        }
    }
}
