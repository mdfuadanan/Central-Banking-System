package central_banking_system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CentralBank extends FinancialInstitution {
    private String governor;
    private LocalDate establishedDate;

    public CentralBank(String id, String name, String governor, String address, LocalDate establishedDate) {
        super(id, name, address, 0);
        this.governor = governor;
        this.establishedDate = establishedDate;
    }

    public void registerBank(List<CommercialBank> banks, CommercialBank bank) {
        banks.add(bank);
    }

    public void updateBank(CommercialBank bank, String name, String address, double capital) {
        bank.setName(name);
        bank.setAddress(address);
        bank.setCapital(capital);
    }

    public LiquidityOperation injectMoney(String operationId, CommercialBank bank, double amount) {
        bank.setCurrentBalance(bank.getCurrentBalance() + amount);
        bank.setAssets(bank.getAssets() + amount);
        bank.recalculateNetWorth();
        return new LiquidityOperation(operationId, OperationType.INJECTION, amount, bank.getId(), LocalDate.now(), OperationStatus.COMPLETED);
    }

    public Report generateReport(String reportId, ReportType type, String title, String data) {
        return new TextReport(reportId, type, title, LocalDate.now(), data);
    }

    public List<CommercialBank> monitorBanks(List<CommercialBank> banks) {
        return new ArrayList<>(banks);
    }

    @Override
    public String getFinancialHealthLabel() {
        return "Stable";
    }

    public String getGovernor() {
        return governor;
    }

    public void setGovernor(String governor) {
        this.governor = governor;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }
}
