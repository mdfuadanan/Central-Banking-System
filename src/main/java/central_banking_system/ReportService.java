package central_banking_system;


import java.util.List;

public class ReportService {
    public String generate(ReportType type, List<CommercialBank> banks, List<Customer> customers,
                           List<Account> accounts, List<Loan> loans, List<Transaction> transactions,
                           Reserve reserve, List<String> alerts) {
        return switch (type) {
            case BANK -> bankReport(banks);
            case CUSTOMER -> "Customer reports are available only inside the Commercial Bank portal.";
            case TRANSACTION -> transactionReport(transactions);
            case RESERVE -> reserveReport(reserve);
            case FRAUD -> fraudReport(transactions, alerts);
            case LOAN -> loanReport(loans);
            case INVESTMENT_RISK -> investmentRiskReport(banks);
            case PERFORMANCE -> performanceReport(banks, loans, transactions);
            default -> summaryReport(banks, customers, loans, transactions, reserve);
        };
    }

    private String bankReport(List<CommercialBank> banks) {
        StringBuilder out = new StringBuilder("Bank Report\n");
        for (CommercialBank bank : banks) {
            out.append(bank.getId()).append(" - ").append(bank.getName())
                    .append(" | Status: ").append(bank.getStatus())
                    .append(" | Risk: ").append(bank.getRiskLevel())
                    .append(" | Net worth: ").append(NumberUtil.money(bank.getNetWorth()))
                    .append("\n");
        }
        return out.toString();
    }

    private String transactionReport(List<Transaction> transactions) {
        StringBuilder out = new StringBuilder("Transaction Report\n");
        for (Transaction transaction : transactions) {
            out.append(transaction.getTransactionId()).append(" | ")
                    .append(transaction.getSenderBankId()).append(" -> ")
                    .append(transaction.getReceiverBankId()).append(" | ")
                    .append(NumberUtil.money(transaction.getAmount())).append(" | ")
                    .append(transaction.getStatus()).append(" | ")
                    .append(transaction.getDescription()).append("\n");
        }
        return out.toString();
    }

    private String reserveReport(Reserve reserve) {
        return "Reserve Report\nNational reserve: " + NumberUtil.money(reserve.getNationalReserve())
                + "\nMinimum required reserve: " + NumberUtil.money(reserve.getMinimumRequiredReserve())
                + "\nReserve warning: " + (reserve.isLow() ? "LOW RESERVE" : "Normal")
                + "\nLast updated: " + reserve.getLastUpdated();
    }

    private String fraudReport(List<Transaction> transactions, List<String> alerts) {
        long flagged = transactions.stream().filter(t -> t.getStatus() == TransactionStatus.FLAGGED).count();
        long failed = transactions.stream().filter(t -> t.getStatus() == TransactionStatus.FAILED).count();
        StringBuilder out = new StringBuilder("Fraud and AML Report\nFlagged transfers: ")
                .append(flagged).append("\nFailed transfers: ").append(failed).append("\nRecent alerts:\n");
        for (String alert : alerts) {
            out.append("- ").append(alert).append("\n");
        }
        return out.toString();
    }

    private String loanReport(List<Loan> loans) {
        StringBuilder out = new StringBuilder("Loan Report\n");
        for (Loan loan : loans) {
            out.append(loan.getLoanId()).append(" | Bank: ").append(loan.getBankId())
                    .append(" | Amount: ").append(NumberUtil.money(loan.getAmount()))
                    .append(" | Risk: ").append(loan.getRiskLevel())
                    .append(" | Status: ").append(loan.getStatus()).append("\n");
        }
        return out.toString();
    }

    private String investmentRiskReport(List<CommercialBank> banks) {
        long low = banks.stream().filter(b -> b.getRiskLevel() == RiskLevel.LOW).count();
        long medium = banks.stream().filter(b -> b.getRiskLevel() == RiskLevel.MEDIUM).count();
        long high = banks.stream().filter(b -> b.getRiskLevel() == RiskLevel.HIGH || b.getRiskLevel() == RiskLevel.CRITICAL).count();
        return "Investment Risk Report\nLow risk banks: " + low
                + "\nMedium risk banks: " + medium
                + "\nHigh/Critical risk banks: " + high;
    }

    private String performanceReport(List<CommercialBank> banks, List<Loan> loans, List<Transaction> transactions) {
        double totalNetWorth = banks.stream().mapToDouble(CommercialBank::getNetWorth).sum();
        double totalLoans = loans.stream().mapToDouble(Loan::getAmount).sum();
        double transferVolume = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        long activeBanks = banks.stream().filter(b -> b.getStatus() == BankStatus.ACTIVE).count();
        return "Performance Report\nActive banks: " + activeBanks
                + "\nCombined net worth: " + NumberUtil.money(totalNetWorth)
                + "\nLoan portfolio: " + NumberUtil.money(totalLoans)
                + "\nTransaction volume: " + NumberUtil.money(transferVolume);
    }

    private String summaryReport(List<CommercialBank> banks, List<Customer> customers, List<Loan> loans,
                                 List<Transaction> transactions, Reserve reserve) {
        return "System Summary\nBanks: " + banks.size()
                + "\nLoans: " + loans.size()
                + "\nTransactions: " + transactions.size()
                + "\nNational reserve: " + NumberUtil.money(reserve.getNationalReserve());
    }
}
