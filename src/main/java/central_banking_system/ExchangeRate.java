package central_banking_system;

public class ExchangeRate {
    private final String currencyCode;
    private final String currencyName;
    private final double referenceRate;
    private final double buyingRate;
    private final double sellingRate;
    private final String lastUpdated;
    private final String source;
    private final boolean live;

    public ExchangeRate(String currencyCode, String currencyName, double referenceRate, double buyingRate,
                        double sellingRate, String lastUpdated, String source, boolean live) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.referenceRate = referenceRate;
        this.buyingRate = buyingRate;
        this.sellingRate = sellingRate;
        this.lastUpdated = lastUpdated;
        this.source = source;
        this.live = live;
    }

    public ExchangeRate withLive(boolean live) {
        return new ExchangeRate(currencyCode, currencyName, referenceRate, buyingRate, sellingRate, lastUpdated, source, live);
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public double getReferenceRate() {
        return referenceRate;
    }

    public double getBuyingRate() {
        return buyingRate;
    }

    public double getSellingRate() {
        return sellingRate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getSource() {
        return source;
    }

    public boolean isLive() {
        return live;
    }
}
