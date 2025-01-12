package ru.base.game.server.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public SocketHandler socketHandler() {
        return new SocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler(), "/ws").setAllowedOrigins("*");
    }

    @Slf4j
    public static final class SocketHandler extends TextWebSocketHandler {
        private static final Gson GSON = new GsonBuilder().create();

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            if (log.isDebugEnabled()) {
                log.debug("{}: {}", session.getId(), message);
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            //TODO: deregister session
            if (log.isDebugEnabled()) {
                log.debug("Deregister {}: {}", session.getId(), status);
            }
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            //TODO: register session
            if (log.isDebugEnabled()) {
                log.debug("Register {}", session.getId());
            }
        }
    }
}
