package central_banking_system;

import java.text.DecimalFormat;

public class NumberUtil {
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    private NumberUtil() {
    }

    public static String money(double value) {
        return MONEY.format(value);
    }

    public static String percent(double value) {
        return MONEY.format(value) + "%";
    }

    public static double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}
