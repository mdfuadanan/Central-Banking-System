package central_banking_system;

import java.time.LocalDate;

public class TextReport extends Report {
    private String data;

    public TextReport(String reportId, ReportType reportType, String title, LocalDate generatedOn, String data) {
        super(reportId, reportType, title, generatedOn);
        this.data = data;
    }

    @Override
    public String generate() {
        return data;
    }

    public String getData() {
        return data;
    }
}
