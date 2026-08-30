package central_banking_system;

import java.time.LocalDateTime;

public class UserSession {
    private final String token;
    private final UserRole role;
    private final String userId;
    private final String displayName;
    private final LocalDateTime createdAt;

    public UserSession(String token, UserRole role, String userId, String displayName, LocalDateTime createdAt) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public UserRole getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
