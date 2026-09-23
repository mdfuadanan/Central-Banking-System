package central_banking_system;

public class Customer {
    private String customerId;
    private String bankId;
    private String name;
    private String email;
    private CustomerStatus status;
    private String accountId;

    public Customer(String customerId, String bankId, String name, String email, CustomerStatus status, String accountId) {
        this.customerId = customerId;
        this.bankId = bankId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getBankId() {
        return bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
