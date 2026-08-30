package central_banking_system;

import java.time.LocalDate;

public abstract class Report {
    private String reportId;
    private ReportType reportType;
    private String title;
    private LocalDate generatedOn;

    protected Report(String reportId, ReportType reportType, String title, LocalDate generatedOn) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.title = title;
        this.generatedOn = generatedOn;
    }

    public abstract String generate();

    public String view() {
        return generate();
    }

    public String export(String format) {
        return "Exported " + title + " as " + format;
    }

    public String getReportId() {
        return reportId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getGeneratedOn() {
        return generatedOn;
    }
}
