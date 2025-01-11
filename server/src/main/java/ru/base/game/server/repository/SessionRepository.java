package ru.base.game.server.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Repository
public class SessionRepository {
    private final Map<String, Session> bySessionId = new HashMap<>();
    private final Map<String, Set<Session>> byUserId = new HashMap<>();

    public Stream<Session> sessions() {
        return bySessionId.values().stream();
    }

    public void addSession(String sessionId) {
        LocalSession session = new LocalSession(sessionId);
        bySessionId.put(sessionId, session);
        Set<Session> set = byUserId.computeIfAbsent(extractUserId(session), k -> new HashSet<>());
        set.add(session);
    }

    public void deleteSession(Session session) {
        deleteSession(session.sessionId());
    }

    public void deleteSession(String sessionId) {
        Session session = bySessionId.remove(sessionId);
        if (session == null) {
            return;
        }
        String userId = extractUserId(session);
        Set<Session> set = byUserId.get(userId);
        if (set != null) {
            set.remove(session);
            if (set.isEmpty()) {
                byUserId.remove(userId);
            }
        }
    }

    public Optional<String> userIdBySessionId(String sessionId) {
        Session session = bySessionId.get(sessionId);
        if (session == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(extractUserId(session));
    }

    public boolean hasUser(String userId) {
        return byUserId.containsKey(userId);
    }

    public void handleByUserId(String userId, SessionHandler handler) {
        Set<Session> list = byUserId.get(userId);
        if (list != null && !list.isEmpty()) {
            list.forEach(handler::handle);
        }
    }

    private @Nullable String extractUserId(Session session) {
        return session.sessionId();
    }

    int bySessionIdSize() {
        return bySessionId.size();
    }

    int byUserIdSize() {
        return byUserId.size();
    }

    public interface SessionHandler {
        void handle(Session session);
    }

    public interface Session {
        String sessionId();
    }

    private record LocalSession(String sessionId) implements Session {
    }
}
