package central_banking_system;

import java.time.LocalDate;

public class CommercialBank extends FinancialInstitution {
    private String licenseNo;
    private BankType type;
    private BankStatus status;
    private RiskLevel riskLevel;
    private LocalDate activatedOn;
    private double currentBalance;
    private double reserveBalance;
    private double assets;
    private double liabilities;
    private double netWorth;
    private String creditRating;
    private String password;
    private String contactEmail;

    public CommercialBank(String bankId, String name, String licenseNo, BankType type, String address,
                          double capital, BankStatus status, RiskLevel riskLevel, LocalDate activatedOn,
                          double currentBalance, double reserveBalance, double assets, double liabilities,
                          String creditRating, String password, String contactEmail) {
        super(bankId, name, address, capital);
        this.licenseNo = licenseNo;
        this.type = type;
        this.status = status;
        this.riskLevel = riskLevel;
        this.activatedOn = activatedOn;
        this.currentBalance = currentBalance;
        this.reserveBalance = reserveBalance;
        this.assets = assets;
        this.liabilities = liabilities;
        this.creditRating = creditRating;
        this.password = password;
        this.contactEmail = contactEmail;
        recalculateNetWorth();
    }

    public Loan requestLoan(String loanId, LoanType loanType, double amount) {
        return new Loan(loanId, getId(), loanType, amount, 0, LocalDate.now(), LocalDate.now().plusYears(1),
                LoanStatus.PENDING, 0, RiskLevel.MEDIUM, "Waiting for central bank decision");
    }

    public String submitReport(Report report) {
        return getName() + " submitted " + report.getReportType() + " report";
    }

    public void recalculateNetWorth() {
        this.netWorth = assets - liabilities;
    }

    @Override
    public String getFinancialHealthLabel() {
        if (riskLevel == RiskLevel.LOW && netWorth > 0 && reserveBalance >= currentBalance * 0.10) {
            return "Healthy";
        }
        if (riskLevel == RiskLevel.CRITICAL || netWorth < 0) {
            return "Critical";
        }
        if (riskLevel == RiskLevel.HIGH || reserveBalance < currentBalance * 0.05) {
            return "Weak";
        }
        return "Watch";
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public BankType getType() {
        return type;
    }

    public void setType(BankType type) {
        this.type = type;
    }

    public BankStatus getStatus() {
        return status;
    }

    public void setStatus(BankStatus status) {
        this.status = status;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDate getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(LocalDate activatedOn) {
        this.activatedOn = activatedOn;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getReserveBalance() {
        return reserveBalance;
    }

    public void setReserveBalance(double reserveBalance) {
        this.reserveBalance = reserveBalance;
    }

    public double getAssets() {
        return assets;
    }

    public void setAssets(double assets) {
        this.assets = assets;
    }

    public double getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(double liabilities) {
        this.liabilities = liabilities;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
