package central_banking_system;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private UserRole recipientRole;
    private String recipientId;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;

    public Notification(String notificationId, UserRole recipientRole, String recipientId,
                        String message, LocalDateTime createdAt, boolean read) {
        this.notificationId = notificationId;
        this.recipientRole = recipientRole;
        this.recipientId = recipientId;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public UserRole getRecipientRole() {
        return recipientRole;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
