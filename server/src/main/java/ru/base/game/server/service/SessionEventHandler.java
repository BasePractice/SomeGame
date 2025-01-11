package ru.base.game.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.base.game.server.repository.SessionRepository;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("PMD.UnusedLocalVariable")
@Slf4j
@Component
public class SessionEventHandler {
    private final SimpMessagingTemplate template;
    private final SimpUserRegistry registry;
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionEventHandler(SimpMessagingTemplate template, SimpUserRegistry registry, SessionRepository sessionRepository) {
        this.template = template;
        this.registry = registry;
        this.sessionRepository = sessionRepository;
    }

    @EventListener(classes = SessionConnectEvent.class)
    public void sessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String simpSessionId = headers.getSessionId();
        List<SimpSession> sessions = registry.getUsers().stream().map(user -> user.getSession(simpSessionId))
            .collect(Collectors.toList());
        sessionRepository.addSession(simpSessionId);
        if (log.isInfoEnabled()) {
            log.info(event.toString());
        }
    }

    @EventListener(classes = SessionDisconnectEvent.class)
    public void sessionDisconnected(SessionDisconnectEvent event) {
        sessionRepository.deleteSession(event.getSessionId());
    }
}
