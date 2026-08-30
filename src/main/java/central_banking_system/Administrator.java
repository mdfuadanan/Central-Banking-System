package central_banking_system;

import java.time.LocalDate;

public class Administrator {
    private String administratorId;
    private String name;
    private String password;
    private String email;
    private boolean enabled;
    private LocalDate createdOn;
    private LocalDate lastPasswordReset;

    public Administrator(String administratorId, String name, String password, String email,
                         boolean enabled, LocalDate createdOn, LocalDate lastPasswordReset) {
        this.administratorId = administratorId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.createdOn = createdOn;
        this.lastPasswordReset = lastPasswordReset;
    }

    public String getAdministratorId() {
        return administratorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.lastPasswordReset = LocalDate.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public LocalDate getLastPasswordReset() {
        return lastPasswordReset;
    }
}
