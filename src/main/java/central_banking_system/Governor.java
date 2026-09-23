package central_banking_system;

public class Governor {
    private String governorId;
    private String name;
    private String password;
    private String email;

    public Governor(String governorId, String name, String password, String email) {
        this.governorId = governorId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getGovernorId() {
        return governorId;
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
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
