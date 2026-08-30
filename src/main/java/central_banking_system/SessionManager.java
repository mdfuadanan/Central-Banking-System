package central_banking_system;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession create(UserRole role, String userId, String displayName) {
        String token = UUID.randomUUID().toString();
        UserSession session = new UserSession(token, role, userId, displayName, LocalDateTime.now());
        sessions.put(token, session);
        return session;
    }

    public UserSession find(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return sessions.get(token);
    }

    public void destroy(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
