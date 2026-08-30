package central_banking_system;

import java.time.LocalDate;

public class LiquidityOperation {
    private String operationId;
    private OperationType operationType;
    private double amount;
    private String targetBankId;
    private LocalDate date;
    private OperationStatus status;

    public LiquidityOperation(String operationId, OperationType operationType, double amount,
                              String targetBankId, LocalDate date, OperationStatus status) {
        this.operationId = operationId;
        this.operationType = operationType;
        this.amount = amount;
        this.targetBankId = targetBankId;
        this.date = date;
        this.status = status;
    }

    public String getOperationId() {
        return operationId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public String getTargetBankId() {
        return targetBankId;
    }

    public LocalDate getDate() {
        return date;
    }

    public OperationStatus getStatus() {
        return status;
    }
}
