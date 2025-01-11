package ru.base.game.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.base.game.server.repository.SessionRepository;

@Slf4j
@Service
public class SocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRepository sessionRepository;

    @Autowired
    public SocketService(SimpMessagingTemplate messagingTemplate, SessionRepository sessionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.sessionRepository = sessionRepository;
    }

    public void sendMessage(String userId, String message) {
        if (sessionRepository.hasUser(userId)) {
            sessionRepository.handleByUserId(userId, session -> {
                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                headerAccessor.setSessionId(session.sessionId());
                headerAccessor.setLeaveMutable(true);
                messagingTemplate.convertAndSendToUser(session.sessionId(), "/topic/messages",
                    message, headerAccessor.getMessageHeaders());
            });
            log.info("Do  : {}", message);
        } else {
            log.info("Miss: {}", message);
        }
    }

}
