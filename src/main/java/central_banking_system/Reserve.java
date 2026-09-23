package central_banking_system;

import java.time.LocalDate;

public class Reserve {
    private double nationalReserve;
    private double minimumRequiredReserve;
    private LocalDate lastUpdated;

    public Reserve(double nationalReserve, double minimumRequiredReserve, LocalDate lastUpdated) {
        this.nationalReserve = nationalReserve;
        this.minimumRequiredReserve = minimumRequiredReserve;
        this.lastUpdated = lastUpdated;
    }

    public void supplyFunds(double amount) {
        nationalReserve = Math.max(0, nationalReserve - amount);
        lastUpdated = LocalDate.now();
    }

    public void addReserve(double amount) {
        nationalReserve += amount;
        lastUpdated = LocalDate.now();
    }

    public boolean isLow() {
        return nationalReserve < minimumRequiredReserve;
    }

    public double getNationalReserve() {
        return nationalReserve;
    }

    public void setNationalReserve(double nationalReserve) {
        this.nationalReserve = nationalReserve;
        this.lastUpdated = LocalDate.now();
    }

    public double getMinimumRequiredReserve() {
        return minimumRequiredReserve;
    }

    public void setMinimumRequiredReserve(double minimumRequiredReserve) {
        this.minimumRequiredReserve = minimumRequiredReserve;
        this.lastUpdated = LocalDate.now();
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
