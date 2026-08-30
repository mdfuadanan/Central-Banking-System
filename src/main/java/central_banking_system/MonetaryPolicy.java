package central_banking_system;

import java.time.LocalDate;

public class MonetaryPolicy {
    private String policyId;
    private String policyName;
    private double interestRate;
    private double reserveRequirement;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String description;

    public MonetaryPolicy(String policyId, String policyName, double interestRate, double reserveRequirement,
                          LocalDate effectiveFrom, LocalDate effectiveTo, String description) {
        this.policyId = policyId;
        this.policyName = policyName;
        this.interestRate = interestRate;
        this.reserveRequirement = reserveRequirement;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.description = description;
    }

    public String getPolicyId() {
        return policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getReserveRequirement() {
        return reserveRequirement;
    }

    public void setReserveRequirement(double reserveRequirement) {
        this.reserveRequirement = reserveRequirement;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
