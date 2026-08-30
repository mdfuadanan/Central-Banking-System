package central_banking_system;

import java.util.List;

public class LoginManager {
    public LoginResult loginGovernor(String userId, String password, Governor governor) {
        if (governor.getGovernorId().equalsIgnoreCase(userId) && governor.getPassword().equals(password)) {
            return new LoginResult(true, UserRole.GOVERNOR, governor.getGovernorId(), governor.getName(), "");
        }
        return new LoginResult(false, null, "", "", "Invalid Governor login information");
    }

    public LoginResult loginAdministrator(String userId, String password, List<Administrator> administrators) {
        for (Administrator administrator : administrators) {
            if (administrator.getAdministratorId().equalsIgnoreCase(userId)
                    && administrator.getPassword().equals(password)) {
                if (!administrator.isEnabled()) {
                    return new LoginResult(false, UserRole.ADMINISTRATOR, administrator.getAdministratorId(),
                            administrator.getName(), "Administrator account is disabled by the Governor");
                }
                return new LoginResult(true, UserRole.ADMINISTRATOR, administrator.getAdministratorId(),
                        administrator.getName(), "");
            }
        }
        return new LoginResult(false, null, "", "", "Invalid Administrator login information");
    }

    public LoginResult loginCommercialBank(String userId, String password, List<CommercialBank> banks) {
        for (CommercialBank bank : banks) {
            if (bank.getId().equalsIgnoreCase(userId) && bank.getPassword().equals(password)) {
                if (bank.getStatus() != BankStatus.ACTIVE) {
                    return new LoginResult(false, UserRole.COMMERCIAL_BANK, bank.getId(), bank.getName(),
                            "Commercial bank is " + bank.getStatus().name().replace('_', ' ') + " and cannot operate");
                }
                return new LoginResult(true, UserRole.COMMERCIAL_BANK, bank.getId(), bank.getName(), "");
            }
        }
        return new LoginResult(false, null, "", "", "Invalid Commercial Bank login information");
    }

    public static class LoginResult {
        private final boolean success;
        private final UserRole role;
        private final String userId;
        private final String displayName;
        private final String message;

        public LoginResult(boolean success, UserRole role, String userId, String displayName, String message) {
            this.success = success;
            this.role = role;
            this.userId = userId;
            this.displayName = displayName;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
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

        public String getMessage() {
            return message;
        }
    }
}
