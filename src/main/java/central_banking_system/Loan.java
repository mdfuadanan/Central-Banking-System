package central_banking_system;

import java.time.LocalDate;

public class Loan {
    private String loanId;
    private String bankId;
    private LoanType type;
    private double amount;
    private double interestRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status;
    private double riskScore;
    private RiskLevel riskLevel;
    private String comments;

    public Loan(String loanId, String bankId, LoanType type, double amount, double interestRate,
                LocalDate startDate, LocalDate endDate, LoanStatus status, double riskScore,
                RiskLevel riskLevel, String comments) {
        this.loanId = loanId;
        this.bankId = bankId;
        this.type = type;
        this.amount = amount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.comments = comments;
    }

    public void approve(double interestRate, String comments) {
        this.status = LoanStatus.APPROVED;
        this.interestRate = interestRate;
        this.comments = comments;
    }

    public void reject(String comments) {
        this.status = LoanStatus.REJECTED;
        this.comments = comments;
    }

    public void disburse() {
        this.status = LoanStatus.DISBURSED;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getBankId() {
        return bankId;
    }

    public LoanType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
