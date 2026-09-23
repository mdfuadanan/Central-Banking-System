package central_banking_system;

public abstract class FinancialInstitution {
    private String id;
    private String name;
    private String address;
    private double capital;

    protected FinancialInstitution(String id, String name, String address, double capital) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capital = capital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = Math.max(0, capital);
    }

    public abstract String getFinancialHealthLabel();
}
