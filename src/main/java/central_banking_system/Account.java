package central_banking_system;

public class Account {
    private String accountId;
    private String customerId;
    private String bankId;
    private String accountType;
    private double balance;
    private boolean active;

    public Account(String accountId, String customerId, String bankId, String accountType, double balance, boolean active) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.bankId = bankId;
        this.accountType = accountType;
        this.balance = balance;
        this.active = active;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getBankId() {
        return bankId;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
