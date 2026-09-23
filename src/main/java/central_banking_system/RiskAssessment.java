package central_banking_system;


import java.util.List;

public class RiskAssessment {
    public RiskResult evaluate(CommercialBank bank, List<Loan> loans, double requestedAmount) {
        double score = 0;
        StringBuilder explanation = new StringBuilder();

        double reserveRatio = bank.getCurrentBalance() == 0 ? 0 : bank.getReserveBalance() / bank.getCurrentBalance();
        if (reserveRatio >= 0.15) {
            score += 5;
            explanation.append("reserve strong; ");
        } else if (reserveRatio >= 0.10) {
            score += 15;
            explanation.append("reserve acceptable; ");
        } else if (reserveRatio >= 0.05) {
            score += 25;
            explanation.append("reserve weak; ");
        } else {
            score += 35;
            explanation.append("reserve critical; ");
        }

        double debtRatio = bank.getAssets() == 0 ? 1 : bank.getLiabilities() / bank.getAssets();
        if (debtRatio < 0.35) {
            score += 5;
            explanation.append("debt low; ");
        } else if (debtRatio < 0.60) {
            score += 15;
            explanation.append("debt moderate; ");
        } else if (debtRatio < 0.80) {
            score += 25;
            explanation.append("debt high; ");
        } else {
            score += 35;
            explanation.append("debt dangerous; ");
        }

        score += creditRatingPenalty(bank.getCreditRating());
        explanation.append("rating ").append(bank.getCreditRating()).append("; ");

        double outstanding = 0;
        int previousLoans = 0;
        for (Loan loan : loans) {
            if (loan.getBankId().equals(bank.getId())
                    && (loan.getStatus() == LoanStatus.PENDING || loan.getStatus() == LoanStatus.APPROVED
                    || loan.getStatus() == LoanStatus.DISBURSED)) {
                outstanding += loan.getAmount();
                previousLoans++;
            }
        }
        if (previousLoans > 2) {
            score += 15;
            explanation.append("many previous loans; ");
        } else if (previousLoans > 0) {
            score += 8;
            explanation.append("some previous loans; ");
        }

        if (requestedAmount > bank.getCapital() * 0.35) {
            score += 15;
            explanation.append("requested amount high against capital; ");
        }
        if (outstanding > bank.getCapital() * 0.40) {
            score += 15;
            explanation.append("outstanding loan exposure high; ");
        }

        RiskLevel level = levelFor(score);
        return new RiskResult(Math.min(100, score), level, explanation.toString());
    }

    private double creditRatingPenalty(String rating) {
        String normalized = rating == null ? "" : rating.trim().toUpperCase();
        if (normalized.startsWith("A")) {
            return 5;
        }
        if (normalized.startsWith("B")) {
            return 15;
        }
        if (normalized.startsWith("C")) {
            return 25;
        }
        return 35;
    }

    private RiskLevel levelFor(double score) {
        if (score < 35) {
            return RiskLevel.LOW;
        }
        if (score < 60) {
            return RiskLevel.MEDIUM;
        }
        if (score < 80) {
            return RiskLevel.HIGH;
        }
        return RiskLevel.CRITICAL;
    }
}
