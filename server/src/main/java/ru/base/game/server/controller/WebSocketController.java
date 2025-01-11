package ru.base.game.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ru.base.game.server.repository.SessionRepository;

@Slf4j
@Controller
public class WebSocketController {
    private final SessionRepository sessionRepository;

    @Autowired
    public WebSocketController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @MessageMapping("/command")
    public ResponseEntity<String> send(@Payload String command, @Header("simpSessionId") String sessionId) {
        sessionRepository.userIdBySessionId(sessionId).ifPresent(userId -> {
            //
        });
        log.info("[{}]: {}", sessionId, command);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
