package central_banking_system;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String senderBankId;
    private String receiverBankId;
    private double amount;
    private LocalDateTime dateTime;
    private TransactionStatus status;
    private String description;

    public Transaction(String transactionId, String senderBankId, String receiverBankId, double amount,
                       LocalDateTime dateTime, TransactionStatus status, String description) {
        this.transactionId = transactionId;
        this.senderBankId = senderBankId;
        this.receiverBankId = receiverBankId;
        this.amount = amount;
        this.dateTime = dateTime;
        this.status = status;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSenderBankId() {
        return senderBankId;
    }

    public String getReceiverBankId() {
        return receiverBankId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
