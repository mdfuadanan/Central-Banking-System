package central_banking_system;

public class RiskResult {
    private double score;
    private RiskLevel level;
    private String explanation;

    public RiskResult(double score, RiskLevel level, String explanation) {
        this.score = score;
        this.level = level;
        this.explanation = explanation;
    }

    public double getScore() {
        return score;
    }

    public RiskLevel getLevel() {
        return level;
    }

    public String getExplanation() {
        return explanation;
    }
}
