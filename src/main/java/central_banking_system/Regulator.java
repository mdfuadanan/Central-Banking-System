package central_banking_system;

import java.util.List;

public class Regulator {
    private String regulatorId;
    private String name;
    private String contactEmail;
    private String phone;

    public Regulator(String regulatorId, String name, String contactEmail, String phone) {
        this.regulatorId = regulatorId;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
    }

    public String approveBank(CommercialBank bank) {
        bank.setStatus(BankStatus.ACTIVE);
        return bank.getName() + " approved by " + name;
    }

    public String imposeRestriction(CommercialBank bank) {
        bank.setStatus(BankStatus.SUSPENDED);
        return bank.getName() + " suspended by " + name;
    }

    public List<Report> viewReports(List<Report> reports) {
        return reports;
    }

    public String getRegulatorId() {
        return regulatorId;
    }

    public String getName() {
        return name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPhone() {
        return phone;
    }
}
