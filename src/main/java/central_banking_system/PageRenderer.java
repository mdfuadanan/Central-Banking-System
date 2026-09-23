package central_banking_system;


import java.util.ArrayList;
import java.util.List;

public class PageRenderer {
    public String homePage() {
        return homePage(defaultExchangeRates());
    }

    public String homePage(List<ExchangeRate> exchangeRates) {
        ExchangeRate usd = exchangeRate(exchangeRates, "USD", "USD Exchange Rate", 117.80, 117.20, 118.40, "18 Jul 2026");
        ExchangeRate eur = exchangeRate(exchangeRates, "EUR", "EUR Exchange Rate", 127.925, 127.10, 128.75, "18 Jul 2026");
        StringBuilder body = new StringBuilder();
        body.append(publicHeader("home"));
        body.append("<section class='public-hero' id='home'><div class='hero-copy'>")
                .append("<p class='eyebrow'>National financial supervision portal</p>")
                .append("<h1>Welcome to Central Bank Management System</h1>")
                .append("<p>Monitor monetary policy, reserve guidance, currency markets, security notices, financial reports and public services from one professional portal.</p>")
                .append("<div class='hero-actions'><a class='btn' href='/about'>Learn More</a><a class='btn ghost' href='/login'>Secure Login</a></div>")
                .append("</div><div class='hero-stat'><span>Emergency Hotline</span><strong>16236</strong><small>Available for urgent banking support</small></div></section>");
        body.append("<main class='public-main'>");
        body.append("<section><div class='section-title'><p class='eyebrow'>Financial dashboard</p><h2>Key Monetary Updates</h2></div><div class='public-grid three'>")
                .append(infoCard("Policy Rate", "6.50%", "Current central bank policy rate for market guidance.", "/policy-rate"))
                .append(infoCard("Reserve Ratio", "10.00%", "Minimum reserve ratio for supervised commercial banks.", "/reserve-ratio"))
                .append(infoCard("Interbank Exchange Rate", "USD " + rateValue(usd.getReferenceRate()), exchangeStatus(usd) + " reference interbank spot exchange rate.", "/interbank-exchange-rate"))
                .append("</div></section>");
        body.append("<section class='public-grid two'>")
                .append(rateCard(usd))
                .append(rateCard(eur))
                .append("</section>");
        body.append("<section class='public-grid three'>")
                .append(publicPanel("ICT Security Alerts", "Cybersecurity Alerts", "Fraud prevention notices, suspicious link warnings and secure banking advice.", "/cipc", "Learn More"))
                .append(publicPanel("Latest Notices & Circulars", "Circulars and Official Announcements", "Regulatory updates, compliance notices and official public circulars.", "/notices", "View All"))
                .append(publicPanel("News & Announcements", "Latest News and Upcoming Events", "Policy briefings, public events and important announcements.", "/news", "Read More"))
                .append("</section>");
        body.append("<section><div class='section-title'><p class='eyebrow'>Economic indicators</p><h2>Macroeconomic Snapshot</h2></div><div class='indicator-strip'>")
                .append(metric("Inflation Rate", "7.4%"))
                .append(metric("GDP Growth Rate", "5.8%"))
                .append(metric("Foreign Exchange Reserve", "24.3B USD"))
                .append(metric("Policy Rate", "6.50%"))
                .append(metric("Last Updated", "18 Jul 2026"))
                .append("</div></section>");
        body.append("<section class='public-grid two'>")
                .append(publicPanel("Customer Interest Protection Center", "Hotline: 16236 | Email: cipc@centralbank.gov", "Submit complaints online, track service issues and view all public protection notices.", "/cipc", "Submit Complaint Online"))
                .append(publicPanel("FX Market Spot Exchange Rate", "Buying " + rateValue(usd.getBuyingRate()) + " | Selling " + rateValue(usd.getSellingRate()), exchangeStatus(usd) + ". Last updated: " + usd.getLastUpdated() + ".", "/interbank-exchange-rate", "Read More"))
                .append("</section>");
        body.append("<section class='public-grid three'>")
                .append(simpleMarketCard("Call Money Rate", "Current Rate", "8.25%", "Last updated: 18 Jul 2026"))
                .append(simpleMarketCard("Money Market Reference Rate", "DOMER", "7.85%", "Latest rates published today"))
                .append(simpleMarketCard("Money Market Reference Rate", "BOFR", "7.40%", "Last updated: 18 Jul 2026"))
                .append("</section>");
        body.append("<section><div class='section-title'><p class='eyebrow'>Quick services</p><h2>Popular Services</h2></div><div class='quick-services'>")
                .append(quickLink("Exchange Rates", "/interbank-exchange-rate"))
                .append(quickLink("Banking Regulations", "/notices"))
                .append(quickLink("Circulars", "/notices"))
                .append(quickLink("Reports", "/publications"))
                .append(quickLink("Complaint Submission", "/cipc"))
                .append(quickLink("Downloads", "/publications"))
                .append("</div></section>");
        body.append("<section class='public-grid two'>")
                .append(publicPanel("Publications & Reports", "Annual Report, Monetary Policy Report, Financial Stability Report, Economic Review", "Download and view official publications for research, policy review and academic use.", "/publications", "View All"))
                .append(publicPanel("Emergency Hotline", "16236", "For urgent banking fraud, digital payment interruption, consumer protection and public service support.", "/cipc", "Get Help"))
                .append("</section>");
        body.append("</main>");
        body.append(publicFooter());
        body.append("<script>document.querySelectorAll('.public-card,-link').forEach(function(el){el.addEventListener('mouseenter',function(){el.classList.add('lift')});el.addEventListener('mouseleave',function(){el.classList.remove('lift')});});</script>");
        return page("Central Bank Management System", body.toString(), "public");
    }

    public String publicPage(String page) {
        String title;
        String bodyText;
        String extra;
        switch (page) {
            case "about":
                title = "About Us";
                bodyText = "The Central Bank Management System supports supervision of commercial banks, monetary policy communication, reserve monitoring, public notices, security awareness and transparent reporting.";
                extra = detailList("Main Responsibilities", "Supervise commercial banks", "Maintain national reserves", "Publish monetary policy guidance", "Monitor financial stability", "Protect customer interests");
                break;
            case "policy-rate":
                title = "Policy Rate Details";
                bodyText = "The policy rate is the central bank's benchmark rate used to guide liquidity, lending behavior and monetary stability.";
                extra = detailList("Current Details", "Current value: 6.50%", "Used in loan and liquidity policy decisions", "Reviewed by the monetary policy team", "Commercial banks follow this rate for market expectations");
                break;
            case "reserve-ratio":
                title = "Reserve Ratio Details";
                bodyText = "The reserve ratio defines the minimum reserve a commercial bank should maintain against its current balance.";
                extra = detailList("Current Details", "Current value: 10.00%", "Low reserve creates warning alerts", "Supports liquidity discipline", "Connected to national reserve monitoring");
                break;
            case "interbank-exchange-rate":
                title = "Interbank Exchange Rate Details";
                bodyText = "The interbank exchange rate shows the reference spot buying and selling rates used by banks for currency settlement.";
                extra = detailList("FX Market Data", "USD buying: 117.20", "USD selling: 118.40", "EUR buying: 127.10", "EUR selling: 128.75", "Operating hours: 10:00 AM - 4:00 PM");
                break;
            case "cipc":
                title = "Customer Interest Protection Center";
                bodyText = "The CIPC receives complaints, shares fraud prevention advice and supports customers affected by banking service issues.";
                extra = detailList("Contact Options", "Hotline: 16236", "Email: cipc@centralbank.gov", "Submit Complaint Online", "View all protection notices");
                break;
            case "notices":
                title = "Notices & Circulars";
                bodyText = "Official circulars, notices and regulatory announcements for commercial banks and the public.";
                extra = noticeList("Circular 01/2026: Updated reserve reporting format", "Notice: Cybersecurity awareness for digital banking", "Announcement: New liquidity monitoring schedule");
                break;
            case "news":
                title = "News & Announcements";
                bodyText = "Latest news, upcoming events and central bank announcements.";
                extra = noticeList("Press release: Monetary policy update published", "Upcoming event: Financial stability seminar", "Announcement: Public complaint portal maintenance window");
                break;
            case "publications":
                title = "Publications & Reports";
                bodyText = "Annual report, monetary policy report, financial stability report and economic review documents.";
                extra = detailList("Available Publications", "Annual Report - View/Download", "Monetary Policy Report - View/Download", "Financial Stability Report - View/Download", "Economic Review - View/Download");
                break;
            case "search":
                title = "Search";
                bodyText = "Search results for public website content.";
                extra = detailList("Matching Sections", "Policy Rate Details", "Reserve Ratio Details", "Notices & Circulars", "Publications & Reports", "Customer Interest Protection Center");
                break;
            default:
                return homePage();
        }
        String body = publicHeader(page)
                + "<main class='public-main'><section class='detail-hero'><p class='eyebrow'>Central Bank Portal</p><h1>" + h(title) + "</h1><p>" + h(bodyText) + "</p></section>"
                + "<section class='public-card detail-card'>" + extra + "</section></main>"
                + publicFooter();
        return page(title, body, "public");
    }

    public String loginPage(String error) {
        String alert = error == null || error.isBlank() ? "" : "<div class='alert'>" + h(error) + "</div>";
        String body = publicHeader("login")
                + "<main class='login-shell login-modern'>"
                + "<section class='login-intro'>"
                + "<p class='eyebrow'>Secure portal</p>"
                + "<h1>Welcome to Central Bank Management System</h1>"
                + "<p>Select the authorized portal for secure access.</p>"
                + "</section>"
                + "<section class='login-panel'>"
                + alert
                + "<a class='portal-choice' href='/admin-login'><strong>Administrator</strong><span>Operational supervision portal</span></a>"
                + "<a class='portal-choice' href='/commercial-bank-login'><strong>Commercial Bank</strong><span>Bank operations portal</span></a>"
                + "</section>"
                + "</main>";
        return page("Login", body, "login");
    }

    public String credentialLoginPage(String title, String action, String userLabel, String sampleUser, String error) {
        String alert = error == null || error.isBlank() ? "" : "<div class='alert'>" + h(error) + "</div>";
        String passwordHint = "Governor".equals(title) ? "governor123"
                : "Administrator".equals(title) ? "admin123" : "bank123";
        String body = publicHeader("login")
                + "<main class='login-shell login-modern'>"
                + "<section class='login-intro'>"
                + "<p class='eyebrow'>Secure portal</p>"
                + "<h1>" + h(title) + " Login</h1>"
                + "</section>"
                + "<section class='login-panel'>"
                + alert
                + "<div class='portal-card'>"
                + "<h2>" + h(title) + "</h2>"
                + "<form class='form-grid' method='post' action='" + h(action) + "'>"
                + input(userLabel, "userId", sampleUser, "text")
                + input("Password", "password", passwordHint, "password")
                + "<button type='submit'>Login</button>"
                + "</form>"
                + "</div>"
                + "</section>"
                + "</main>";
        return page(title + " Login", body, "login");
    }

    public String governorLoginPage(String error) {
        String alert = error == null || error.isBlank() ? "" : "<div class='alert'>" + h(error) + "</div>";
        String body = "<main class='login-shell login-modern governor-login-page'>"
                + "<section class='login-intro'>"
                + "<p class='eyebrow'>Secure portal</p>"
                + "<h1>Governor Login</h1>"
                + "</section>"
                + "<section class='login-panel'>"
                + alert
                + "<div class='portal-card'>"
                + "<h2>Governor</h2>"
                + "<form class='form-grid' method='post' action='/governor-login'>"
                + input("Governor ID", "userId", "GOV001", "text")
                + input("Password", "password", "governor123", "password")
                + "<button type='submit'>Login</button>"
                + "</form>"
                + "</div>"
                + "</section>"
                + "</main>";
        return page("Governor Login", body, "login");
    }

    public String adminPage(CentralBankSystem system, Administrator administrator, String view, String message, String reportText) {
        String content;
        switch (view) {
            case "banks":
                content = adminBanks(system);
                break;
            case "loans":
                content = adminLoans(system);
                break;
            case "reserve":
                content = adminReserve(system);
                break;
            case "transactions":
                content = adminTransactions(system);
                break;
            case "reports":
                content = adminReports(reportText);
                break;
            case "settings":
                content = adminSettings(system);
                break;
            case "requests":
                content = adminRequests(system, administrator);
                break;
            case "notifications":
                content = notificationsPanel(system.notificationsFor(UserRole.ADMINISTRATOR, administrator.getAdministratorId()));
                break;
            default:
                content = adminDashboard(system, administrator);
        }
        return shell("Admin", adminNav(), message, content);
    }

    public String bankPage(CentralBankSystem system, CommercialBank bank, String view, String message) {
        if (bank == null) {
            return loginPage("Bank profile was not found.");
        }
        String content;
        switch (view) {
            case "loans":
                content = bankLoans(system, bank);
                break;
            case "transfer":
                content = bankTransfer(system, bank);
                break;
            case "transactions":
                content = bankTransactions(system, bank);
                break;
            case "customers":
                content = bankCustomers(system, bank);
                break;
            case "accounts":
                content = bankAccounts(system, bank);
                break;
            case "reports":
                content = bankReports(system, bank);
                break;
            case "notifications":
                content = notificationsPanel(system.notificationsFor(UserRole.COMMERCIAL_BANK, bank.getId()));
                break;
            case "profile":
                content = bankProfile(bank);
                break;
            default:
                content = bankDashboard(system, bank);
        }
        return shell(bank.getName(), bankNav(bank.getId()), message, content);
    }

    public String governorPage(CentralBankSystem system, String view, String message, String reportText) {
        String content;
        switch (view) {
            case "national-dashboard":
                content = governorDashboard(system);
                break;
            case "administrators":
            case "monetary-policy":
            case "interest-rate":
            case "money-supply":
            case "banks":
            case "licenses":
            case "fx":
            case "management":
                content = governorManagement(system);
                break;
            case "requests":
            case "pending":
            case "approved":
            case "rejected":
                content = governorRequestCenter(system);
                break;
            case "fraud":
            case "stability":
            case "national-reports":
            case "reports":
                content = governorReportsCenter(system, reportText);
                break;
            case "notifications":
                content = notificationsPanel(system.notificationsFor(UserRole.GOVERNOR, system.getGovernor().getGovernorId()));
                break;
            case "audit":
            case "login-logs":
            case "activity":
            case "logs":
                content = governorLogs(system);
                break;
            case "profile":
                content = governorProfile(system.getGovernor());
                break;
            default:
                content = governorDashboard(system);
        }
        return shell("Governor", governorNav(), message, content);
    }

    private String adminDashboard(CentralBankSystem system, Administrator administrator) {
        double totalBalance = system.getBanks().stream().mapToDouble(CommercialBank::getCurrentBalance).sum();
        double netWorth = system.getBanks().stream().mapToDouble(CommercialBank::getNetWorth).sum();
        long flagged = system.getTransactions().stream().filter(t -> t.getStatus().name().equals("FLAGGED")).count();
        long pendingRequests = system.requestsForAdministrator(administrator.getAdministratorId()).stream()
                .filter(r -> r.getStatus() == ApprovalStatus.PENDING).count();
        StringBuilder out = new StringBuilder();
        out.append("<section class='hero'><div><p class='eyebrow'>Central supervisor</p><h1>Administrator Dashboard</h1>")
                .append("<p>Manage routine operations and submit national-level changes to the Governor for approval.</p></div>")
                .append("<div class='reserve-box'><span>National Reserve</span><strong>")
                .append(NumberUtil.money(system.getReserve().getNationalReserve())).append("</strong><small>")
                .append(system.getReserve().isLow() ? "Low reserve warning" : "Reserve level normal")
                .append("</small></div></section>");
        out.append("<section class='cards'>")
                .append(card("Registered Banks", String.valueOf(system.getBanks().size()), "Commercial banks under supervision"))
                .append(card("Total Bank Balance", NumberUtil.money(totalBalance), "Liquidity in commercial banks"))
                .append(card("Combined Net Worth", NumberUtil.money(netWorth), "Assets minus liabilities"))
                .append(card("Fraud Alerts", String.valueOf(flagged), "Flagged transaction records"))
                .append(card("My Pending Requests", String.valueOf(pendingRequests), "Waiting for Governor review"))
                .append("</section>");
        out.append("<section class='split'><div class='panel'><h2>Recent Alerts</h2>").append(alertList(system.getRecentAlerts())).append("</div>")
                .append("<div class='panel'><h2>Audit Trail</h2>").append(alertList(system.getAuditTrail())).append("</div></section>");
        out.append("<section class='panel'><h2>Highest Net Worth Banks</h2>").append(bankTable(system.sortBanksByNetWorthDescending(), false)).append("</section>");
        return out.toString();
    }

    private String adminBanks(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Register Commercial Bank</h1>")
                .append("<form class='form-grid' method='post' action='/admin/bank/add'>")
                .append(input("Bank Name", "name", "", "text"))
                .append(input("License No", "license", "LC-", "text"))
                .append(select("Bank Type", "type", names(BankType.values()), "COMMERCIAL"))
                .append(input("Address", "address", "", "text"))
                .append(input("Capital", "capital", "1000000", "number"))
                .append(input("Credit Rating", "rating", "A", "text"))
                .append(input("Password", "password", "bank123", "password"))
                .append(input("Email", "email", "bank@example.com", "email"))
                .append(input("Supporting Information", "supportingInformation", "Branch and registration documents verified", "text"))
                .append("<button type='submit'>Submit to Governor</button></form>")
                .append("<p class='muted'>New banks are saved as Pending Approval and cannot log in until the Governor approves.</p></section>");
        out.append("<section class='panel'><h2>Commercial Banks</h2>").append(bankTable(system.getBanks(), true)).append("</section>");
        return out.toString();
    }

    private String adminLoans(CentralBankSystem system) {
        StringBuilder out = new StringBuilder("<section class='panel'><h1>Approve or Reject Loans</h1>");
        out.append("<p class='muted'>Risk score uses reserve ratio, debt ratio, credit rating, requested amount, and previous loans.</p>");
        out.append("<table><tr><th>ID</th><th>Bank</th><th>Type</th><th>Amount</th><th>Risk</th><th>Score</th><th>Status</th><th>Decision</th></tr>");
        for (Loan loan : system.getLoans()) {
            out.append("<tr><td>").append(h(loan.getLoanId())).append("</td><td>").append(h(loan.getBankId())).append("</td><td>")
                    .append(loan.getType()).append("</td><td>").append(NumberUtil.money(loan.getAmount())).append("</td><td>")
                    .append(badge(loan.getRiskLevel().name())).append("</td><td>").append(NumberUtil.money(loan.getRiskScore())).append("</td><td>")
                    .append(badge(loan.getStatus().name())).append("</td><td>");
            if (loan.getStatus() == LoanStatus.PENDING) {
                out.append("<form class='inline' method='post' action='/admin/loan/decide'>")
                        .append(hidden("loanId", loan.getLoanId()))
                        .append("<button name='decision' value='approve'>Approve</button>")
                        .append("<button class='secondary' name='decision' value='reject'>Reject</button></form>");
            } else {
                out.append(h(loan.getComments()));
            }
            out.append("</td></tr>");
        }
        out.append("</table></section>");
        return out.toString();
    }

    private String adminReserve(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='cards'>")
                .append(card("National Reserve", NumberUtil.money(system.getReserve().getNationalReserve()), "Funds available for liquidity supply"))
                .append(card("Minimum Required", NumberUtil.money(system.getReserve().getMinimumRequiredReserve()), "Warning threshold"))
                .append(card("Reserve Status", system.getReserve().isLow() ? "LOW" : "NORMAL", "Updated " + system.getReserve().getLastUpdated()))
                .append("</section>");
        out.append("<section class='split'><div class='panel'><h1>Money Supply Adjustment</h1>")
                .append("<form class='form-grid' method='post' action='/admin/supply'>")
                .append(bankSelect(system.activeBanks(), "bankId", "Target Bank"))
                .append(input("Amount", "amount", "100000", "number"))
                .append(select("Operation Type", "type", names(OperationType.values()), "INJECTION"))
                .append("<button type='submit'>Submit to Governor</button></form></div>");
        out.append("<div class='panel'><h1>Foreign Exchange Reserve Management</h1>")
                .append("<form class='form-grid' method='post' action='/admin/reserve/update'>")
                .append(input("National Reserve", "nationalReserve", String.valueOf(system.getReserve().getNationalReserve()), "number"))
                .append(input("Minimum Required Reserve", "minimumReserve", String.valueOf(system.getReserve().getMinimumRequiredReserve()), "number"))
                .append("<button type='submit'>Submit to Governor</button></form></div></section>");
        out.append("<section class='panel'><h2>Liquidity Operations</h2><table><tr><th>ID</th><th>Type</th><th>Bank</th><th>Amount</th><th>Date</th><th>Status</th></tr>");
        for (LiquidityOperation operation : system.getOperations()) {
            out.append("<tr><td>").append(h(operation.getOperationId())).append("</td><td>").append(operation.getOperationType())
                    .append("</td><td>").append(h(operation.getTargetBankId())).append("</td><td>").append(NumberUtil.money(operation.getAmount()))
                    .append("</td><td>").append(operation.getDate()).append("</td><td>").append(badge(operation.getStatus().name())).append("</td></tr>");
        }
        out.append("</table></section>");
        return out.toString();
    }

    private String adminTransactions(CentralBankSystem system) {
        return "<section class='panel'><h1>Monitor All Transactions</h1>" + transactionTable(system.getTransactions()) + "</section>";
    }

    private String adminReports(String reportText) {
        String selectedReport = reportText == null || reportText.isBlank() ? "Choose a report type and click Generate." : h(reportText);
        return "<section class='panel'><h1>Generate Reports</h1>"
                + "<form class='form-grid compact' method='get' action='/admin'>"
                + hidden("view", "reports")
                + select("Report Type", "type", adminReportNames(), "SUMMARY")
                + "<button type='submit'>Generate</button></form>"
                + "<pre class='report-box'>" + selectedReport + "</pre></section>";
    }

    private String adminSettings(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Submit Monetary Policy Request</h1>")
                .append("<form class='form-grid' method='post' action='/admin/policy'>")
                .append(input("Policy Name", "policyName", "Liquidity Stabilization Policy", "text"))
                .append(input("Interest Rate", "interestRate", "7.0", "number"))
                .append(input("Reserve Requirement %", "reserveRequirement", "10", "number"))
                .append(input("Description", "description", "Policy update for supervised banks", "text"))
                .append("<button type='submit'>Submit to Governor</button></form></section>");
        out.append("<section class='panel'><h2>Active Policies</h2><table><tr><th>ID</th><th>Name</th><th>Interest</th><th>Reserve Req.</th><th>Period</th><th>Description</th></tr>");
        for (MonetaryPolicy policy : system.getPolicies()) {
            out.append("<tr><td>").append(h(policy.getPolicyId())).append("</td><td>").append(h(policy.getPolicyName()))
                    .append("</td><td>").append(NumberUtil.percent(policy.getInterestRate())).append("</td><td>")
                    .append(NumberUtil.percent(policy.getReserveRequirement())).append("</td><td>")
                    .append(policy.getEffectiveFrom()).append(" to ").append(policy.getEffectiveTo()).append("</td><td>")
                    .append(h(policy.getDescription())).append("</td></tr>");
        }
        out.append("</table></section>");
        return out.toString();
    }

    private String adminRequests(CentralBankSystem system, Administrator administrator) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Submit Governor Approval Request</h1>")
                .append("<form class='form-grid' method='post' action='/admin/request/submit'>")
                .append(select("Request Type", "type", names(ApprovalType.values()), "NATIONAL_FINANCIAL_REPORT_APPROVAL"))
                .append(input("Description", "description", "Describe the operation requiring Governor approval", "text"))
                .append("<button type='submit'>Submit Request</button></form></section>");
        out.append("<section class='panel'><h2>Request History</h2>")
                .append(approvalRequestTable(system.requestsForAdministrator(administrator.getAdministratorId()), true, false))
                .append("</section>");
        return out.toString();
    }

    private String governorDashboard(CentralBankSystem system) {
        long pending = system.requestsByStatus(ApprovalStatus.PENDING).size();
        long approved = system.requestsByStatus(ApprovalStatus.APPROVED).size();
        long rejected = system.requestsByStatus(ApprovalStatus.REJECTED).size();
        long activeBanks = system.activeBanks().size();
        double totalBalance = system.getBanks().stream().mapToDouble(CommercialBank::getCurrentBalance).sum();
        double netWorth = system.getBanks().stream().mapToDouble(CommercialBank::getNetWorth).sum();
        long flagged = system.getTransactions().stream().filter(t -> t.getStatus() == TransactionStatus.FLAGGED).count();
        StringBuilder out = new StringBuilder();
        out.append("<section class='hero'><div><p class='eyebrow'>Governor overview</p><h1>Dashboard</h1>")
                .append("<p>National financial dashboard, approval workload, stability indicators, reserves and system statistics in one place.</p></div>")
                .append("<div class='reserve-box'><span>Reserve Status</span><strong>")
                .append(system.getReserve().isLow() ? "LOW" : "NORMAL").append("</strong><small>Updated ")
                .append(system.getReserve().getLastUpdated()).append("</small></div></section>");
        out.append("<section class='cards'>")
                .append(card("Active Commercial Banks", String.valueOf(activeBanks), "Approved institutions"))
                .append(card("Pending Requests", String.valueOf(pending), "Awaiting decision"))
                .append(card("Approved Requests", String.valueOf(approved), "Executed automatically"))
                .append(card("Rejected Requests", String.valueOf(rejected), "Cancelled actions"))
                .append(card("National Reserve", NumberUtil.money(system.getReserve().getNationalReserve()), "Foreign exchange reserve"))
                .append(card("Bank Liquidity", NumberUtil.money(totalBalance), "Commercial bank balances"))
                .append(card("Combined Net Worth", NumberUtil.money(netWorth), "National bank-sector net worth"))
                .append(card("Fraud Reports", String.valueOf(flagged), "Flagged transactions"))
                .append(card("Administrators", String.valueOf(system.getAdministrators().size()), "Managed only by Governor"))
                .append(card("Customers", String.valueOf(system.getCustomers().size()), "All customer records"))
                .append("</section>");
        out.append("<section class='split'><div class='panel'><h2>Pending Requests</h2>")
                .append(approvalRequestTable(system.requestsByStatus(ApprovalStatus.PENDING), false, true))
                .append("</div><div class='panel'><h2>Notifications</h2>")
                .append(notificationList(system.notificationsFor(UserRole.GOVERNOR, system.getGovernor().getGovernorId())))
                .append("</div></section>");
        return out.toString();
    }

    private String governorNationalDashboard(CentralBankSystem system) {
        double totalBalance = system.getBanks().stream().mapToDouble(CommercialBank::getCurrentBalance).sum();
        double netWorth = system.getBanks().stream().mapToDouble(CommercialBank::getNetWorth).sum();
        long flagged = system.getTransactions().stream().filter(t -> t.getStatus() == TransactionStatus.FLAGGED).count();
        return "<section class='hero'><div><p class='eyebrow'>National financial dashboard</p><h1>National Financial Dashboard</h1>"
                + "<p>Aggregated supervision view across commercial banks, reserves, policy, stability and fraud indicators.</p></div>"
                + "<div class='reserve-box'><span>Reserve Status</span><strong>"
                + (system.getReserve().isLow() ? "LOW" : "NORMAL") + "</strong><small>Updated "
                + system.getReserve().getLastUpdated() + "</small></div></section>"
                + "<section class='cards'>"
                + card("Bank Liquidity", NumberUtil.money(totalBalance), "Commercial bank balances")
                + card("Combined Net Worth", NumberUtil.money(netWorth), "National bank-sector net worth")
                + card("Fraud Reports", String.valueOf(flagged), "Flagged transactions")
                + card("Loan Queue", String.valueOf(system.getPendingLoanQueue().size()), "Pending loan decisions")
                + card("Policy Count", String.valueOf(system.getPolicies().size()), "Monetary policies")
                + card("National Reserve", NumberUtil.money(system.getReserve().getNationalReserve()), "Available reserve")
                + "</section>";
    }

    private String governorAdministrators(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Create Administrator</h1>")
                .append("<form class='form-grid' method='post' action='/governor/admin/create'>")
                .append(input("Administrator Name", "name", "", "text"))
                .append(input("Email", "email", "admin@centralbank.gov", "email"))
                .append(input("Password", "password", "admin123", "password"))
                .append("<button type='submit'>Create Administrator</button></form></section>");
        out.append("<section class='panel'><h2>Administrator Accounts</h2>")
                .append(administratorTable(system.getAdministrators()))
                .append("</section>");
        return out.toString();
    }

    private String governorRequests(String title, List<ApprovalRequest> requests) {
        return "<section class='panel'><h1>" + h(title) + "</h1>"
                + approvalRequestTable(requests, false, true) + "</section>";
    }

    private String governorManagement(CentralBankSystem system) {
        return "<section class='hero'><div><p class='eyebrow'>Governor controls</p><h1>Management</h1>"
                + "<p>Administrator Management, Interest Rate Management, Commercial Bank Management, licences, reserves and money supply controls.</p></div>"
                + "<div class='reserve-box'><span>Managed Administrators</span><strong>"
                + system.getAdministrators().size() + "</strong><small>Only Governor can create, edit, disable or delete</small></div></section>"
                + governorAdministrators(system)
                + governorPolicy(system)
                + governorReserve(system)
                + governorMoneySupply(system)
                + governorBanks(system);
    }

    private String governorRequestCenter(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='hero'><div><p class='eyebrow'>Governor decisions</p><h1>Request</h1>")
                .append("<p>Approval requests, pending requests, approved requests and rejected requests are merged here.</p></div>")
                .append("<div class='reserve-box'><span>Pending Requests</span><strong>")
                .append(system.requestsByStatus(ApprovalStatus.PENDING).size())
                .append("</strong><small>Review required</small></div></section>");
        out.append("<section class='cards'>")
                .append(card("All Requests", String.valueOf(system.getApprovalRequests().size()), "Complete request history"))
                .append(card("Pending", String.valueOf(system.requestsByStatus(ApprovalStatus.PENDING).size()), "Awaiting Governor review"))
                .append(card("Approved", String.valueOf(system.requestsByStatus(ApprovalStatus.APPROVED).size()), "Executed actions"))
                .append(card("Rejected", String.valueOf(system.requestsByStatus(ApprovalStatus.REJECTED).size()), "Cancelled actions"))
                .append("</section>");
        out.append("<section class='panel'><h2>Pending Requests</h2>")
                .append(approvalRequestTable(system.requestsByStatus(ApprovalStatus.PENDING), false, true))
                .append("</section>");
        out.append("<section class='split'><div class='panel'><h2>Approved Requests</h2>")
                .append(approvalRequestTable(system.requestsByStatus(ApprovalStatus.APPROVED), false, false))
                .append("</div><div class='panel'><h2>Rejected Requests</h2>")
                .append(approvalRequestTable(system.requestsByStatus(ApprovalStatus.REJECTED), false, false))
                .append("</div></section>");
        out.append("<section class='panel'><h2>All Approval Requests</h2>")
                .append(approvalRequestTable(system.getApprovalRequests(), false, true))
                .append("</section>");
        return out.toString();
    }

    private String governorPolicy(CentralBankSystem system) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Monetary Policy and Interest Rate Management</h1>")
                .append("<form class='form-grid' method='post' action='/governor/policy/add'>")
                .append(input("Policy Name", "policyName", "Policy Interest Rate Directive", "text"))
                .append(input("Interest Rate", "interestRate", "6.5", "number"))
                .append(input("Reserve Requirement %", "reserveRequirement", "10", "number"))
                .append(input("Description", "description", "Governor-approved national policy", "text"))
                .append("<button type='submit'>Save Policy</button></form></section>");
        out.append("<section class='panel'><h2>Active Policies</h2><table><tr><th>ID</th><th>Name</th><th>Interest</th><th>Reserve Req.</th><th>Period</th><th>Description</th></tr>");
        for (MonetaryPolicy policy : system.getPolicies()) {
            out.append("<tr><td>").append(h(policy.getPolicyId())).append("</td><td>").append(h(policy.getPolicyName()))
                    .append("</td><td>").append(NumberUtil.percent(policy.getInterestRate())).append("</td><td>")
                    .append(NumberUtil.percent(policy.getReserveRequirement())).append("</td><td>")
                    .append(policy.getEffectiveFrom()).append(" to ").append(policy.getEffectiveTo()).append("</td><td>")
                    .append(h(policy.getDescription())).append("</td></tr>");
        }
        out.append("</table></section>");
        return out.toString();
    }

    private String governorMoneySupply(CentralBankSystem system) {
        return "<section class='panel'><h1>Money Supply</h1>"
                + "<p class='muted'>Approved money supply requests execute automatically and are listed below.</p>"
                + "<table><tr><th>ID</th><th>Type</th><th>Bank</th><th>Amount</th><th>Date</th><th>Status</th></tr>"
                + liquidityRows(system.getOperations())
                + "</table></section>";
    }

    private String governorBanks(CentralBankSystem system) {
        return "<section class='panel'><h1>Commercial Bank Management and Licences</h1>"
                + bankTable(system.getBanks(), false)
                + "</section>";
    }

    private String governorReserve(CentralBankSystem system) {
        return "<section class='split'><div class='panel'><h1>Foreign Exchange Reserves</h1>"
                + "<section class='cards'>"
                + card("National Reserve", NumberUtil.money(system.getReserve().getNationalReserve()), "Current reserve")
                + card("Minimum Required", NumberUtil.money(system.getReserve().getMinimumRequiredReserve()), "Warning threshold")
                + card("Status", system.getReserve().isLow() ? "LOW" : "NORMAL", "Updated " + system.getReserve().getLastUpdated())
                + "</section></div><div class='panel'><h1>Manage Reserves</h1>"
                + "<form class='form-grid compact' method='post' action='/governor/reserve/update'>"
                + input("National Reserve", "nationalReserve", String.valueOf(system.getReserve().getNationalReserve()), "number")
                + input("Minimum Required Reserve", "minimumReserve", String.valueOf(system.getReserve().getMinimumRequiredReserve()), "number")
                + "<button type='submit'>Update Reserves</button></form></div></section>";
    }

    private String governorFraud(CentralBankSystem system) {
        List<Transaction> flagged = new ArrayList<>();
        for (Transaction transaction : system.getTransactions()) {
            if (transaction.getStatus() == TransactionStatus.FLAGGED) {
                flagged.add(transaction);
            }
        }
        return "<section class='panel'><h1>Fraud Reports</h1>" + transactionTable(flagged) + "</section>";
    }

    private String governorStability(CentralBankSystem system) {
        return "<section class='panel'><h1>Financial Stability Reports</h1>"
                + "<section class='cards'>"
                + card("Low Reserve Alert", system.getReserve().isLow() ? "YES" : "NO", "Reserve threshold monitoring")
                + card("Suspended Banks", String.valueOf(countBanks(system.getBanks(), BankStatus.SUSPENDED)), "Institutions under suspension")
                + card("Revoked Banks", String.valueOf(countBanks(system.getBanks(), BankStatus.REVOKED)), "Closed or revoked institutions")
                + card("Pending Bank Applications", String.valueOf(countBanks(system.getBanks(), BankStatus.PENDING_APPROVAL)), "Awaiting Governor approval")
                + "</section></section>";
    }

    private String governorReports(String reportText) {
        String selectedReport = reportText == null || reportText.isBlank() ? "Choose a national report type and click Generate." : h(reportText);
        return "<section class='panel'><h1>National Reports</h1>"
                + "<form class='form-grid compact' method='get' action='/governor'>"
                + hidden("view", "reports")
                + select("Report Type", "type", adminReportNames(), "SUMMARY")
                + "<button type='submit'>Generate</button></form>"
                + "<pre class='report-box'>" + selectedReport + "</pre></section>";
    }

    private String governorReportsCenter(CentralBankSystem system, String reportText) {
        return "<section class='hero'><div><p class='eyebrow'>Governor reporting</p><h1>Reports</h1>"
                + "<p>National reports, fraud reports and financial stability reports are grouped here.</p></div>"
                + "<div class='reserve-box'><span>Flagged Transactions</span><strong>"
                + system.getTransactions().stream().filter(t -> t.getStatus() == TransactionStatus.FLAGGED).count()
                + "</strong><small>Fraud review queue</small></div></section>"
                + governorReports(reportText)
                + governorFraud(system)
                + governorStability(system);
    }

    private String governorLogs(CentralBankSystem system) {
        return "<section class='hero'><div><p class='eyebrow'>System traceability</p><h1>Logs</h1>"
                + "<p>Audit logs, login logs and activity history are merged for Governor review.</p></div>"
                + "<div class='reserve-box'><span>Login Records</span><strong>"
                + system.getLoginLogs().size() + "</strong><small>Authentication events</small></div></section>"
                + "<section class='split'><div class='panel'><h2>Audit Logs</h2>"
                + alertList(system.getAuditTrail())
                + "</div><div class='panel'><h2>Login Logs</h2>"
                + alertList(system.getLoginLogs())
                + "</div></section>"
                + logPanel("Activity History", system.getActivityLogs());
    }

    private String governorStatistics(CentralBankSystem system) {
        return "<section class='panel'><h1>System Statistics</h1><section class='cards'>"
                + card("Administrators", String.valueOf(system.getAdministrators().size()), "Governor-managed accounts")
                + card("Commercial Banks", String.valueOf(system.getBanks().size()), "All bank records")
                + card("Customers", String.valueOf(system.getCustomers().size()), "All customer records")
                + card("Accounts", String.valueOf(system.getAccounts().size()), "All customer accounts")
                + card("Transactions", String.valueOf(system.getTransactions().size()), "Saved transaction records")
                + card("Approval Requests", String.valueOf(system.getApprovalRequests().size()), "Full request history")
                + "</section></section>";
    }

    private String governorProfile(Governor governor) {
        return "<section class='split'><div class='panel'><h1>Governor Profile</h1>"
                + "<form class='form-grid compact' method='post' action='/governor/profile/update'>"
                + input("Governor Name", "name", governor.getName(), "text")
                + input("Email", "email", governor.getEmail(), "email")
                + "<button type='submit'>Update Profile</button></form></div>"
                + "<div class='panel'><h1>Change Governor Password</h1>"
                + "<form class='form-grid compact' method='post' action='/governor/password'>"
                + input("New Password", "password", "", "password")
                + "<button type='submit'>Change Password</button></form></div></section>";
    }

    private String bankDashboard(CentralBankSystem system, CommercialBank bank) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='hero'><div><p class='eyebrow'>Commercial bank portal</p><h1>")
                .append(h(bank.getName())).append("</h1><p>Financial position, reserves, loans, transfers, transactions, credit rating and risk view.</p></div>")
                .append("<div class='reserve-box'><span>Financial Health</span><strong>")
                .append(h(bank.getFinancialHealthLabel())).append("</strong><small>Risk level: ")
                .append(bank.getRiskLevel()).append("</small></div></section>");
        out.append("<section class='cards'>")
                .append(card("Current Balance", NumberUtil.money(bank.getCurrentBalance()), "Operational liquidity"))
                .append(card("Reserve Balance", NumberUtil.money(bank.getReserveBalance()), "Held for regulatory requirement"))
                .append(card("Assets", NumberUtil.money(bank.getAssets()), "Total assets"))
                .append(card("Liabilities", NumberUtil.money(bank.getLiabilities()), "Outstanding obligations"))
                .append(card("Net Worth", NumberUtil.money(bank.getNetWorth()), "Assets minus liabilities"))
                .append(card("Outstanding Loans", NumberUtil.money(system.outstandingLoans(bank.getId())), "Pending/approved/disbursed"))
                .append(card("Credit Rating", h(bank.getCreditRating()), "Risk: " + bank.getRiskLevel()))
                .append(card("Transaction Volume", NumberUtil.money(system.transactionVolumeForBank(bank.getId())), "Transfer activity"))
                .append("</section>");
        out.append("<section class='split'><div class='panel'><h2>Recent Transactions</h2>")
                .append(transactionTable(system.transactionsForBank(bank.getId()))).append("</div>")
                .append("<div class='panel'><h2>Bank Customers</h2>").append(customerMiniTable(system.customersForBank(bank.getId())))
                .append("</div></section>");
        return out.toString();
    }

    private String bankLoans(CentralBankSystem system, CommercialBank bank) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Apply for Central Bank Loan</h1>")
                .append("<form class='form-grid compact' method='post' action='/bank/loan/apply'>")
                .append(hidden("bankId", bank.getId()))
                .append(select("Loan Type", "type", names(LoanType.values()), "SHORT_TERM"))
                .append(input("Amount", "amount", "250000", "number"))
                .append("<button type='submit'>Submit Loan Request</button></form></section>");
        out.append("<section class='panel'><h2>Loan History</h2><table><tr><th>ID</th><th>Type</th><th>Amount</th><th>Interest</th><th>Risk</th><th>Status</th><th>Comments</th></tr>");
        for (Loan loan : system.loansForBank(bank.getId())) {
            out.append("<tr><td>").append(h(loan.getLoanId())).append("</td><td>").append(loan.getType())
                    .append("</td><td>").append(NumberUtil.money(loan.getAmount())).append("</td><td>")
                    .append(NumberUtil.percent(loan.getInterestRate())).append("</td><td>")
                    .append(badge(loan.getRiskLevel().name())).append("</td><td>").append(badge(loan.getStatus().name()))
                    .append("</td><td>").append(h(loan.getComments())).append("</td></tr>");
        }
        out.append("</table></section>");
        return out.toString();
    }

    private String bankTransfer(CentralBankSystem system, CommercialBank bank) {
        return "<section class='panel'><h1>Transfer Funds</h1>"
                + "<form class='form-grid compact' method='post' action='/bank/transfer'>"
                + bankSelect(system.activeBanks(), "receiverId", "Receiver Bank")
                + input("Amount", "amount", "50000", "number")
                + "<button type='submit'>Transfer</button></form>"
                + "<p class='muted'>The fraud detector checks very large transfers, repeated failures, repeated same-party transfers, and negative balances.</p></section>";
    }

    private String bankTransactions(CentralBankSystem system, CommercialBank bank) {
        return "<section class='panel'><h1>Bank Transactions</h1>" + transactionTable(system.transactionsForBank(bank.getId())) + "</section>";
    }

    private String bankProfile(CommercialBank bank) {
        return "<section class='panel'><h1>Update Profile</h1>"
                + "<form class='form-grid compact' method='post' action='/bank/profile/update'>"
                + input("Address", "address", bank.getAddress(), "text")
                + input("Email", "email", bank.getContactEmail(), "email")
                + input("New Password", "password", "", "password")
                + "<button type='submit'>Update Profile</button></form></section>";
    }

    private String bankCustomers(CentralBankSystem system, CommercialBank bank) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='panel'><h1>Manage Customers</h1>")
                .append("<form class='form-grid' method='post' action='/bank/customer/add'>")
                .append(input("Customer Name", "name", "", "text"))
                .append(input("Email", "email", "customer@example.com", "email"))
                .append(input("Account Type", "accountType", "Savings", "text"))
                .append(input("Initial Deposit", "initialDeposit", "1000", "number"))
                .append("<button type='submit'>Create Customer</button></form></section>");
        out.append("<section class='panel'><h2>Customer Records</h2>")
                .append(customerFullTable(system.customersForBank(bank.getId())))
                .append("</section>");
        return out.toString();
    }

    private String bankAccounts(CentralBankSystem system, CommercialBank bank) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='split'><div class='panel'><h1>Deposit Money</h1>")
                .append("<form class='form-grid compact' method='post' action='/bank/deposit'>")
                .append(accountSelect(system.accountsForBank(bank.getId()), "accountId", "Account"))
                .append(input("Amount", "amount", "1000", "number"))
                .append("<button type='submit'>Deposit</button></form></div>");
        out.append("<div class='panel'><h1>Withdraw Money</h1>")
                .append("<form class='form-grid compact' method='post' action='/bank/withdraw'>")
                .append(accountSelect(system.accountsForBank(bank.getId()), "accountId", "Account"))
                .append(input("Amount", "amount", "500", "number"))
                .append("<button type='submit'>Withdraw</button></form></div></section>");
        out.append("<section class='panel'><h2>Accounts</h2>")
                .append(accountTable(system.accountsForBank(bank.getId())))
                .append("</section>");
        return out.toString();
    }

    private String bankReports(CentralBankSystem system, CommercialBank bank) {
        StringBuilder out = new StringBuilder();
        out.append("<section class='cards'>")
                .append(card("Customers", String.valueOf(system.customersForBank(bank.getId()).size()), "Own customer records"))
                .append(card("Deposits", NumberUtil.money(system.totalCustomerDeposits(bank.getId())), "Customer account balances"))
                .append(card("Loans", NumberUtil.money(system.outstandingLoans(bank.getId())), "Outstanding central bank loan exposure"))
                .append(card("Transactions", NumberUtil.money(system.transactionVolumeForBank(bank.getId())), "Own transfer activity"))
                .append("</section>");
        out.append("<section class='panel'><h1>Own Report</h1>")
                .append("<p class='muted'>This portal limits report data to the logged-in commercial bank.</p>")
                .append(transactionTable(system.transactionsForBank(bank.getId())))
                .append("</section>");
        return out.toString();
    }

    private String administratorTable(List<Administrator> administrators) {
        StringBuilder out = new StringBuilder("<table><tr><th>ID</th><th>Name</th><th>Email</th><th>Status</th><th>Created</th><th>Password Reset</th><th>Action</th></tr>");
        for (Administrator administrator : administrators) {
            out.append("<tr><td>").append(h(administrator.getAdministratorId())).append("</td><td>")
                    .append(h(administrator.getName())).append("</td><td>").append(h(administrator.getEmail()))
                    .append("</td><td>").append(badge(administrator.isEnabled() ? "ENABLED" : "DISABLED"))
                    .append("</td><td>").append(administrator.getCreatedOn()).append("</td><td>")
                    .append(administrator.getLastPasswordReset()).append("</td><td>")
                    .append("<details><summary>Manage</summary>")
                    .append("<form class='table-form' method='post' action='/governor/admin/edit'>")
                    .append(hidden("administratorId", administrator.getAdministratorId()))
                    .append(input("Name", "name", administrator.getName(), "text"))
                    .append(input("Email", "email", administrator.getEmail(), "email"))
                    .append("<button type='submit'>Save</button></form>")
                    .append("<form class='table-form' method='post' action='/governor/admin/password'>")
                    .append(hidden("administratorId", administrator.getAdministratorId()))
                    .append(input("New Password", "password", "", "password"))
                    .append("<button type='submit'>Reset Password</button></form>")
                    .append("<form class='inline' method='post' action='/governor/admin/toggle'>")
                    .append(hidden("administratorId", administrator.getAdministratorId()))
                    .append(hidden("enabled", administrator.isEnabled() ? "false" : "true"))
                    .append("<button class='secondary' type='submit'>")
                    .append(administrator.isEnabled() ? "Disable" : "Enable").append("</button></form>")
                    .append("<form class='inline' method='post' action='/governor/admin/delete'>")
                    .append(hidden("administratorId", administrator.getAdministratorId()))
                    .append("<button class='danger' type='submit'>Delete</button></form>")
                    .append("</details></td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String approvalRequestTable(List<ApprovalRequest> requests, boolean allowCancel, boolean allowReview) {
        if (requests.isEmpty()) {
            return "<p class='muted'>No requests found.</p>";
        }
        StringBuilder out = new StringBuilder("<table><tr><th>Request ID</th><th>Administrator ID</th><th>Administrator Name</th><th>Request Type</th><th>Description</th><th>Date</th><th>Time</th><th>Status</th><th>Governor Remarks</th><th>Governor ID</th><th>Approval Time</th><th>Action</th></tr>");
        for (ApprovalRequest request : requests) {
            out.append("<tr><td>").append(h(request.getRequestId())).append("</td><td>")
                    .append(h(request.getAdministratorId())).append("</td><td>")
                    .append(h(request.getAdministratorName())).append("</td><td>")
                    .append(h(request.getRequestType().name().replace('_', ' '))).append("</td><td>")
                    .append(h(request.getDescription())).append("</td><td>")
                    .append(request.getSubmittedAt().toLocalDate()).append("</td><td>")
                    .append(request.getSubmittedAt().toLocalTime().withNano(0)).append("</td><td>")
                    .append(badge(request.getStatus().name())).append("</td><td>")
                    .append(h(request.getGovernorRemarks())).append("</td><td>")
                    .append(h(request.getGovernorId())).append("</td><td>")
                    .append(request.getApprovalTime() == null ? "" : request.getApprovalTime().withNano(0)).append("</td><td>");
            if (allowReview && request.getStatus() == ApprovalStatus.PENDING) {
                out.append("<form class='table-form' method='post' action='/governor/request/review'>")
                        .append(hidden("requestId", request.getRequestId()))
                        .append(input("Remarks", "remarks", "Reviewed by Governor", "text"))
                        .append("<button name='decision' value='approve'>Approve</button>")
                        .append("<button class='secondary' name='decision' value='reject'>Reject</button></form>");
            } else if (allowCancel && request.getStatus() == ApprovalStatus.PENDING) {
                out.append("<form class='inline' method='post' action='/admin/request/cancel'>")
                        .append(hidden("requestId", request.getRequestId()))
                        .append("<button class='secondary' type='submit'>Cancel</button></form>");
            } else {
                out.append(h(request.getStatus().name().replace('_', ' ')));
            }
            out.append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String notificationsPanel(List<Notification> notifications) {
        return "<section class='panel'><h1>Notifications</h1>" + notificationList(notifications) + "</section>";
    }

    private String notificationList(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            return "<p class='muted'>No notifications yet.</p>";
        }
        StringBuilder out = new StringBuilder("<ul class='alert-list'>");
        for (Notification notification : notifications) {
            out.append("<li><strong>").append(notification.getCreatedAt().withNano(0)).append("</strong> - ")
                    .append(h(notification.getMessage())).append("</li>");
        }
        out.append("</ul>");
        return out.toString();
    }

    private String logPanel(String title, List<String> entries) {
        return "<section class='panel'><h1>" + h(title) + "</h1>" + alertList(entries) + "</section>";
    }

    private String liquidityRows(List<LiquidityOperation> operations) {
        StringBuilder out = new StringBuilder();
        for (LiquidityOperation operation : operations) {
            out.append("<tr><td>").append(h(operation.getOperationId())).append("</td><td>")
                    .append(operation.getOperationType()).append("</td><td>").append(h(operation.getTargetBankId()))
                    .append("</td><td>").append(NumberUtil.money(operation.getAmount())).append("</td><td>")
                    .append(operation.getDate()).append("</td><td>").append(badge(operation.getStatus().name()))
                    .append("</td></tr>");
        }
        return out.toString();
    }

    private int countBanks(List<CommercialBank> banks, BankStatus status) {
        int count = 0;
        for (CommercialBank bank : banks) {
            if (bank.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    private String bankTable(List<CommercialBank> banks, boolean editable) {
        StringBuilder out = new StringBuilder("<table><tr><th>ID</th><th>Name</th><th>Type</th><th>Status</th><th>Risk</th><th>Capital</th><th>Net Worth</th><th>Action</th></tr>");
        for (CommercialBank bank : banks) {
            out.append("<tr><td>").append(h(bank.getId())).append("</td><td>").append(h(bank.getName())).append("</td><td>")
                    .append(bank.getType()).append("</td><td>").append(badge(bank.getStatus().name())).append("</td><td>")
                    .append(badge(bank.getRiskLevel().name())).append("</td><td>").append(NumberUtil.money(bank.getCapital()))
                    .append("</td><td>").append(NumberUtil.money(bank.getNetWorth())).append("</td><td>");
            if (editable) {
                out.append("<details><summary>Edit</summary><form class='table-form' method='post' action='/admin/bank/edit'>")
                        .append(hidden("bankId", bank.getId()))
                        .append(input("Name", "name", bank.getName(), "text"))
                        .append(input("Address", "address", bank.getAddress(), "text"))
                        .append(input("Capital", "capital", String.valueOf(bank.getCapital()), "number"))
                        .append(input("Rating", "rating", bank.getCreditRating(), "text"))
                        .append(input("Email", "email", bank.getContactEmail(), "email"))
                        .append("<button type='submit'>Save</button></form>")
                        .append("<form class='table-form' method='post' action='/admin/bank/suspend'>")
                        .append(hidden("bankId", bank.getId()))
                        .append(input("Reason", "reason", "Risk or compliance concern", "text"))
                        .append("<button class='secondary'>Request Suspension</button></form>")
                        .append("<form class='table-form' method='post' action='/admin/bank/delete'>")
                        .append(hidden("bankId", bank.getId()))
                        .append(input("Reason", "reason", "Closure or licence revocation required", "text"))
                        .append("<button class='danger'>Request Closure</button></form>");
                if (bank.getStatus() == BankStatus.REJECTED) {
                    out.append("<form class='table-form' method='post' action='/admin/bank/resubmit'>")
                            .append(hidden("bankId", bank.getId()))
                            .append(input("Supporting Information", "supportingInformation", "Updated documents attached", "text"))
                            .append("<button type='submit'>Resubmit Registration</button></form>");
                }
                out.append("</details>");
            } else {
                out.append(h(bank.getFinancialHealthLabel()));
            }
            out.append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String transactionTable(List<Transaction> transactions) {
        StringBuilder out = new StringBuilder("<table><tr><th>ID</th><th>Sender</th><th>Receiver</th><th>Amount</th><th>Date</th><th>Status</th><th>Description</th></tr>");
        for (Transaction transaction : transactions) {
            out.append("<tr><td>").append(h(transaction.getTransactionId())).append("</td><td>")
                    .append(h(transaction.getSenderBankId())).append("</td><td>").append(h(transaction.getReceiverBankId()))
                    .append("</td><td>").append(NumberUtil.money(transaction.getAmount())).append("</td><td>")
                    .append(transaction.getDateTime().withNano(0)).append("</td><td>")
                    .append(badge(transaction.getStatus().name())).append("</td><td>")
                    .append(h(transaction.getDescription())).append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String customerMiniTable(List<Customer> customers) {
        StringBuilder out = new StringBuilder("<table><tr><th>Name</th><th>Status</th><th>Account</th></tr>");
        for (Customer customer : customers) {
            out.append("<tr><td>").append(h(customer.getName())).append("</td><td>")
                    .append(badge(customer.getStatus().name())).append("</td><td>")
                    .append(h(customer.getAccountId())).append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String customerFullTable(List<Customer> customers) {
        StringBuilder out = new StringBuilder("<table><tr><th>ID</th><th>Name</th><th>Email</th><th>Status</th><th>Account</th><th>Action</th></tr>");
        for (Customer customer : customers) {
            out.append("<tr><td>").append(h(customer.getCustomerId())).append("</td><td>")
                    .append(h(customer.getName())).append("</td><td>").append(h(customer.getEmail()))
                    .append("</td><td>").append(badge(customer.getStatus().name())).append("</td><td>")
                    .append(h(customer.getAccountId())).append("</td><td>")
                    .append("<details><summary>Edit</summary><form class='table-form' method='post' action='/bank/customer/edit'>")
                    .append(hidden("customerId", customer.getCustomerId()))
                    .append(input("Name", "name", customer.getName(), "text"))
                    .append(input("Email", "email", customer.getEmail(), "email"))
                    .append(select("Status", "status", names(CustomerStatus.values()), customer.getStatus().name()))
                    .append("<button type='submit'>Save</button></form></details>")
                    .append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String accountTable(List<Account> accounts) {
        StringBuilder out = new StringBuilder("<table><tr><th>Account</th><th>Customer</th><th>Type</th><th>Balance</th><th>Status</th></tr>");
        for (Account account : accounts) {
            out.append("<tr><td>").append(h(account.getAccountId())).append("</td><td>")
                    .append(h(account.getCustomerId())).append("</td><td>").append(h(account.getAccountType()))
                    .append("</td><td>").append(NumberUtil.money(account.getBalance())).append("</td><td>")
                    .append(badge(account.isActive() ? "ACTIVE" : "INACTIVE")).append("</td></tr>");
        }
        out.append("</table>");
        return out.toString();
    }

    private String publicHeader(String active) {
        return "<header class='site-header'><a class='site-logo' href='/'><img src='/assets/central-bank-logo.png' alt='Central Bank logo'></a>"
                + "<nav><a href='/'" + activeClass(active, "home") + ">Home</a><a href='/about'" + activeClass(active, "about") + ">About Us</a><a href='/login'" + activeClass(active, "login") + ">Login</a></nav>"
                + "<form class='search-bar' method='get' action='/search'><input name='q' placeholder='Search services, notices, reports'><button>Search</button></form></header>";
    }

    private String publicFooter() {
        return "<footer class='site-footer'><div><h3>Central Bank Management System</h3><p>Address: Central Bank Data Center, Dhaka</p><p>Phone: +880-2-000000 | Email: info@centralbank.gov</p></div>"
                + "<div><h4>Useful Links</h4><a href='/about'>About Us</a><a href='/notices'>Notices</a><a href='/publications'>Reports</a><a href='/cipc'>Privacy Policy</a></div>"
                + "<div><h4>Services</h4><a href='/interbank-exchange-rate'>Exchange Rates</a><a href='/reserve-ratio'>Reserve Ratio</a><a href='/policy-rate'>Policy Rate</a><a href='/login'>Secure Login</a></div>"
                + "<div><h4>Social</h4><a href='https://facebook.com'>Facebook</a><a href='https://linkedin.com'>LinkedIn</a><a href='https://youtube.com'>YouTube</a><p>Copyright 2026 Central Bank Management System.</p></div></footer>";
    }

    private String shell(String title, String nav, String message, String content) {
        String flash = message == null || message.isBlank() ? "" : "<div class='flash'>" + h(message) + "</div>";
        String body = "<div class='app-shell'><aside><div class='brand'><img src='/assets/central-bank-logo.png' alt='Central Bank logo'><span>Management System</span></div>"
                + nav + "<a class='logout' href='/logout'>Logout</a></aside><main class='content'>"
                + flash + content + "</main></div>";
        return page(title, body, "app");
    }

    private String adminNav() {
        return navLink("/admin", "Dashboard") + navLink("/admin?view=banks", "Banks")
                + navLink("/admin?view=loans", "Loans") + navLink("/admin?view=reserve", "Reserve")
                + navLink("/admin?view=transactions", "Transactions") + navLink("/admin?view=reports", "Reports")
                + navLink("/admin?view=settings", "Settings") + navLink("/admin?view=requests", "Approval Requests")
                + navLink("/admin?view=notifications", "Notifications");
    }

    private String governorNav() {
        return navLink("/governor", "Dashboard")
                + navLink("/governor?view=management", "Management")
                + navLink("/governor?view=requests", "Request")
                + navLink("/governor?view=reports", "Reports")
                + navLink("/governor?view=logs", "Logs")
                + navLink("/governor?view=profile", "Governor Profile");
    }

    private String bankNav(String bankId) {
        String root = "/bank";
        return navLink(root, "Dashboard") + navLink(root + "?view=loans", "Loans")
                + navLink(root + "?view=customers", "Customers") + navLink(root + "?view=accounts", "Accounts")
                + navLink(root + "?view=transfer", "Transfer") + navLink(root + "?view=transactions", "Transactions")
                + navLink(root + "?view=reports", "Reports") + navLink(root + "?view=notifications", "Notifications")
                + navLink(root + "?view=profile", "Profile");
    }

    private String navLink(String href, String label) {
        return "<a href='" + href + "'>" + label + "</a>";
    }

    private String activeClass(String active, String value) {
        return active.equals(value) ? " class='active'" : "";
    }

    private String page(String title, String body, String bodyClass) {
        return "<!doctype html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>" + h(title) + "</title><style>" + css() + "</style></head><body class='" + h(bodyClass) + "'>"
                + body + "</body></html>";
    }

    private String card(String label, String value, String hint) {
        return "<div class='card'><span>" + h(label) + "</span><strong>" + value + "</strong><small>" + h(hint) + "</small></div>";
    }

    private String infoCard(String title, String value, String text, String href) {
        return "<article class='public-card'><span>" + h(title) + "</span><strong>" + h(value) + "</strong><p>" + h(text) + "</p><a class='read-link' href='" + h(href) + "'>Read More</a></article>";
    }

    private String rateCard(String title, String buying, String selling, String updated) {
        return "<article class='public-card rate-card'><h3>" + h(title) + "</h3><div><span>Buying Rate</span><strong>" + h(buying) + "</strong></div><div><span>Selling Rate</span><strong>" + h(selling) + "</strong></div><small>Last Updated: " + h(updated) + "</small></article>";
    }

    private String rateCard(ExchangeRate rate) {
        return "<article class='public-card rate-card'><h3>" + h(rate.getCurrencyName()) + "</h3>"
                + "<div><span>Buying Rate</span><strong>" + rateValue(rate.getBuyingRate()) + "</strong></div>"
                + "<div><span>Selling Rate</span><strong>" + rateValue(rate.getSellingRate()) + "</strong></div>"
                + "<small>" + h(exchangeStatus(rate)) + " | Last Updated: " + h(rate.getLastUpdated()) + "</small>"
                + "</article>";
    }

    private String publicPanel(String title, String subtitle, String text, String href, String action) {
        return "<article class='public-card'><h3>" + h(title) + "</h3><strong>" + h(subtitle) + "</strong><p>" + h(text) + "</p><a class='read-link' href='" + h(href) + "'>" + h(action) + "</a></article>";
    }

    private String simpleMarketCard(String title, String label, String value, String updated) {
        return "<article class='public-card'><h3>" + h(title) + "</h3><span>" + h(label) + "</span><strong>" + h(value) + "</strong><p>" + h(updated) + "</p></article>";
    }

    private String metric(String label, String value) {
        return "<div><span>" + h(label) + "</span><strong>" + h(value) + "</strong></div>";
    }

    private List<ExchangeRate> defaultExchangeRates() {
        return List.of(
                new ExchangeRate("USD", "USD Exchange Rate", 117.80, 117.20, 118.40,
                        "18 Jul 2026", "Seeded central bank value", false),
                new ExchangeRate("EUR", "EUR Exchange Rate", 127.925, 127.10, 128.75,
                        "18 Jul 2026", "Seeded central bank value", false)
        );
    }

    private ExchangeRate exchangeRate(List<ExchangeRate> rates, String currencyCode, String currencyName,
                                      double referenceRate, double buyingRate, double sellingRate, String updated) {
        if (rates != null) {
            for (ExchangeRate rate : rates) {
                if (rate.getCurrencyCode().equalsIgnoreCase(currencyCode)) {
                    return rate;
                }
            }
        }
        return new ExchangeRate(currencyCode, currencyName, referenceRate, buyingRate, sellingRate,
                updated, "Seeded central bank value", false);
    }

    private String rateValue(double value) {
        return NumberUtil.money(value);
    }

    private String exchangeStatus(ExchangeRate rate) {
        return rate.isLive() ? "Live exchange rate" : "Cached exchange rate";
    }

    private String quickLink(String label, String href) {
        return "<a class='service-link' href='" + h(href) + "'>" + h(label) + "</a>";
    }

    private String detailList(String title, String... items) {
        StringBuilder out = new StringBuilder("<h2>").append(h(title)).append("</h2><ul class='detail-list'>");
        for (String item : items) {
            out.append("<li>").append(h(item)).append("</li>");
        }
        out.append("</ul>");
        return out.toString();
    }

    private String noticeList(String... items) {
        StringBuilder out = new StringBuilder("<div class='notice-list'>");
        for (String item : items) {
            out.append("<article><strong>").append(h(item)).append("</strong><p>Published for public information and supervised banking institutions.</p></article>");
        }
        out.append("</div>");
        return out.toString();
    }

    private String alertList(List<String> items) {
        if (items.isEmpty()) {
            return "<p class='muted'>No records yet.</p>";
        }
        StringBuilder out = new StringBuilder("<ul class='alert-list'>");
        for (String item : items) {
            out.append("<li>").append(h(item)).append("</li>");
        }
        out.append("</ul>");
        return out.toString();
    }

    private String input(String label, String name, String value, String type) {
        return "<label><span>" + h(label) + "</span><input type='" + h(type) + "' name='" + h(name)
                + "' value='" + h(value) + "' required></label>";
    }

    private String hidden(String name, String value) {
        return "<input type='hidden' name='" + h(name) + "' value='" + h(value) + "'>";
    }

    private String select(String label, String name, String[] options, String selected) {
        StringBuilder out = new StringBuilder("<label><span>").append(h(label)).append("</span><select name='")
                .append(h(name)).append("'>");
        for (String option : options) {
            out.append("<option value='").append(h(option)).append("'");
            if (option.equals(selected)) {
                out.append(" selected");
            }
            out.append(">").append(h(option.replace('_', ' '))).append("</option>");
        }
        out.append("</select></label>");
        return out.toString();
    }

    private String bankSelect(List<CommercialBank> banks, String name, String label) {
        StringBuilder out = new StringBuilder("<label><span>").append(h(label)).append("</span><select name='")
                .append(h(name)).append("'>");
        for (CommercialBank bank : banks) {
            out.append("<option value='").append(h(bank.getId())).append("'>").append(h(bank.getId()))
                    .append(" - ").append(h(bank.getName())).append("</option>");
        }
        out.append("</select></label>");
        return out.toString();
    }

    private String accountSelect(List<Account> accounts, String name, String label) {
        StringBuilder out = new StringBuilder("<label><span>").append(h(label)).append("</span><select name='")
                .append(h(name)).append("'>");
        for (Account account : accounts) {
            out.append("<option value='").append(h(account.getAccountId())).append("'>")
                    .append(h(account.getAccountId())).append(" - ")
                    .append(NumberUtil.money(account.getBalance())).append("</option>");
        }
        out.append("</select></label>");
        return out.toString();
    }

    private String badge(String value) {
        return "<span class='badge " + h(value.toLowerCase()) + "'>" + h(value.replace('_', ' ')) + "</span>";
    }

    private String[] names(Enum<?>[] values) {
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].name();
        }
        return names;
    }

    private String[] adminReportNames() {
        List<String> result = new ArrayList<>();
        for (ReportType type : ReportType.values()) {
            if (type != ReportType.CUSTOMER) {
                result.add(type.name());
            }
        }
        return result.toArray(new String[0]);
    }

    private String h(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String css() {
        return ""
                + ":root{--ink:#172033;"
                + "--muted:#627087;"
                + "--line:#d8dee8;--bg:#f5f7fb;"
                + "--panel:#fff;--nav:#10243e;"
                + "--blue:#1f6fbf;--blue2:#0b4f91;"
                + "--green:#1a7f55;"
                + "--red:#b42318;"
                + "--amber:#9a6700}"
                + "*{box-sizing:border-box}html{scroll-behavior:smooth}body{margin:0;"
                + "font-family:Segoe UI,Arial,sans-serif;"
                + "color:var(--ink);"
                + "background:var(--bg)}"
                + "a{color:inherit;"
                + "text-decoration:none}h1{font-size:30px;"
                + "margin:0 0 12px}h2{font-size:22px;margin:0 0 14px}h3{margin:0 0 10px}.muted{color:var(--muted)}"
                + ".site-header{position:sticky;"
                + "top:0;z-index:5;"
                + "background:#fff;"
                + "border-bottom:1px solid var(--line);"
                + "display:grid;"
                + "grid-template-columns:260px 1fr minmax(260px,420px);"
                + "gap:18px;"
                + "align-items:center;padding:14px 6vw;"
                + "box-shadow:0 8px 24px rgba(16,36,62,.06)}.site-logo{display:flex;align-items:center;"
                + "width:max-content;max-width:100%}.site-logo img{display:block;width:210px;max-width:100%;"
                + "height:auto;border-radius:8px;"
                + "background:var(--blue)}.site-header nav{display:flex;"
                + "gap:8px;"
                + "flex-wrap:wrap}.site-header nav a{padding:9px 12px;"
                + "border-radius:8px;"
                + "color:#314258;font-weight:600}.site-header nav a:hover,.site-header nav a.active{background:#e9f2ff;"
                + "color:#0b4f91}.search-bar{display:flex;"
                + "gap:8px}.search-bar input{min-width:0}.search-bar button{white-space:nowrap}"
                + ".public-hero{min-height:560px;"
                + "background:linear-gradient(135deg,rgba(16,36,62,.95),rgba(31,111,191,.86)),linear-gradient(90deg,#10243e,#1f6fbf);color:#fff;display:grid;grid-template-columns:1fr 310px;gap:28px;align-items:center;"
                + "padding:80px 8vw}.public-hero h1{font-size:52px;line-height:1.05}.public-hero p{font-size:19px;"
                + "max-width:760px;color:#e5eef8}.hero-actions{display:flex;gap:12px;margin-top:24px}.btn{display:inline-flex;"
                + "align-items:center;"
                + "justify-content:center;border-radius:8px;background:#fff;color:#0f3764;"
                + "padding:12px 18px;font-weight:800}.btn.ghost{background:transparent;"
                + "color:#fff;border:1px solid rgba(255,255,255,.55)}.hero-stat{background:rgba(255,255,255,.12);border:1px solid rgba(255,255,255,.28);border-radius:12px;"
                + "padding:24px;"
                + "backdrop-filter:blur(8px)}.hero-stat span,.hero-stat small{display:block;"
                + "color:#dce8f8}.hero-stat strong{display:block;"
                + "font-size:42px;margin:8px 0}.public-main{padding:34px 6vw 50px}.public-main section{margin-bottom:28px}.section-title{display:flex;justify-content:space-between;align-items:end;margin-bottom:14px}.eyebrow{text-transform:uppercase;letter-spacing:.08em;font-weight:800;font-size:12px;color:#4b82bd}.public-grid{display:grid;gap:18px}.public-grid.three{grid-template-columns:repeat(3,1fr)}.public-grid.two{grid-template-columns:repeat(2,1fr)}.public-card{background:#fff;border:1px solid var(--line);border-radius:12px;padding:22px;box-shadow:0 12px 30px rgba(16,36,62,.07);transition:transform .18s ease,box-shadow .18s ease,border-color .18s ease}.public-card:hover,.public-card.lift{transform:translateY(-4px);box-shadow:0 18px 36px rgba(16,36,62,.11);border-color:#b8d3f1}.public-card span{display:block;color:var(--muted);font-size:13px}.public-card strong{display:block;font-size:25px;margin:8px 0;color:#10243e}.public-card p{color:var(--muted);line-height:1.55}.read-link{display:inline-flex;margin-top:8px;color:var(--blue);font-weight:800}.rate-card{display:grid;grid-template-columns:1fr 1fr;gap:12px}.rate-card h3,.rate-card small{grid-column:1/-1}.indicator-strip{display:grid;grid-template-columns:repeat(5,1fr);gap:12px}.indicator-strip div,-link{background:#fff;border:1px solid var(--line);border-radius:12px;padding:18px;box-shadow:0 10px 24px rgba(16,36,62,.05)}.indicator-strip span{display:block;color:var(--muted);font-size:13px}.indicator-strip strong{font-size:22px;color:#10243e}.quick-services{display:grid;grid-template-columns:repeat(6,1fr);gap:12px}-link{font-weight:800;color:#143b64;text-align:center;transition:transform .18s ease,background .18s ease}-link:hover,-link.lift{transform:translateY(-3px);background:#eef6ff}.detail-hero{background:linear-gradient(135deg,#fff,#eef5ff);border:1px solid var(--line);border-radius:12px;padding:34px}.detail-hero h1{font-size:40px}.detail-hero p{color:var(--muted);max-width:850px;line-height:1.6}.detail-card{max-width:950px}.detail-list{line-height:1.9;color:#34465d}.notice-list{display:grid;gap:12px}.notice-list article{border:1px solid var(--line);border-radius:10px;padding:16px;background:#f8fbff}"
                + ".site-footer{background:#10243e;"
                + "color:#e6eef8;display:grid;"
                + "grid-template-columns:2fr 1fr 1fr 1fr;gap:24px;padding:34px 6vw}.site-footer a{display:block;"
                + "color:#c8d8e8;margin:7px 0}.site-footer p{color:#c8d8e8}.login-shell{min-height:calc(100vh - 67px);display:grid;"
                + "grid-template-columns:1fr 460px}.login-modern{background:#f7f9fc}.login-intro{background:linear-gradient(135deg,#10243e,#195c9e);color:#fff;padding:8vw;display:flex;flex-direction:column;"
                + "justify-content:center}.login-intro h1{font-size:48px;"
                + "line-height:1.05}.login-intro p{font-size:19px;max-width:640px;color:#dce8f8}.login-panel{padding:42px;background:#f7f9fc;display:flex;flex-direction:column;"
                + "justify-content:center;gap:20px}.portal-card,.panel,.card,.reserve-box{background:var(--panel);border:1px solid var(--line);border-radius:8px;box-shadow:0 10px 24px rgba(16,36,62,.06)}.portal-card{padding:24px}"
                + ".app-shell{display:grid;grid-template-columns:240px 1fr;min-height:100vh}aside{background:var(--nav);color:#fff;padding:24px 18px;position:sticky;top:0;height:100vh}.brand{font-size:23px;font-weight:800;margin-bottom:26px}.brand img{display:block;width:180px;max-width:100%;height:auto;margin-bottom:10px;border-radius:8px;background:var(--blue)}.brand span{font-size:13px;color:#b9c8da}aside a{display:block;padding:11px 12px;margin:4px 0;border-radius:6px;color:#e6eef8}aside a:hover,.logout{background:rgba(255,255,255,.10)}.content{padding:28px;max-width:1500px;width:100%}.flash,.alert{padding:12px 14px;border-radius:6px;background:#e7f3ff;border:1px solid #bfd9f3;margin-bottom:18px}.alert{background:#fff0f0;border-color:#ffd0d0}"
                + ".hero{display:grid;grid-template-columns:1fr 280px;gap:18px;margin-bottom:18px;background:linear-gradient(135deg,#ffffff,#eef5ff);border:1px solid var(--line);border-radius:8px;padding:24px}.hero p{color:var(--muted);max-width:720px}.reserve-box{padding:20px;display:flex;flex-direction:column;gap:8px}.reserve-box span,.card span{color:var(--muted);font-size:13px}.reserve-box strong,.card strong{font-size:26px}.reserve-box small,.card small{color:var(--muted)}"
                + ".cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:14px;margin-bottom:18px}.card{padding:18px;display:flex;flex-direction:column;gap:8px;min-height:118px}.split{display:grid;grid-template-columns:1fr 1fr;gap:18px;margin-bottom:18px}.panel{padding:20px;margin-bottom:18px;overflow:auto}"
                + "table{width:100%;border-collapse:collapse;font-size:14px}th,td{padding:11px 10px;border-bottom:1px solid var(--line);text-align:left;vertical-align:top}th{font-size:12px;text-transform:uppercase;color:#60708a;background:#f3f6fa}tr:hover td{background:#fafcff}"
                + "form{margin:0}.form-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(190px,1fr));gap:14px;align-items:end}.form-grid.compact{max-width:780px}.table-form{display:grid;grid-template-columns:1fr;gap:8px;margin-top:10px}.inline{display:inline-flex;gap:8px;flex-wrap:wrap}label span{display:block;font-size:12px;color:var(--muted);margin-bottom:5px}input,select{width:100%;padding:10px;border:1px solid #cbd5e1;border-radius:6px;background:#fff;color:var(--ink)}button{border:0;border-radius:6px;background:var(--blue);color:#fff;padding:10px 14px;font-weight:700;cursor:pointer}button:hover{background:var(--blue2)}button.secondary{background:#475569}button.danger{background:var(--red)}details summary{cursor:pointer;color:var(--blue);font-weight:700}"
                + ".badge{display:inline-block;border-radius:999px;padding:5px 9px;font-size:12px;font-weight:700;background:#eef2f7;color:#40506a}.badge.low,.badge.active,.badge.success,.badge.completed,.badge.disbursed,.badge.approved{background:#e8f7ef;color:var(--green)}.badge.medium,.badge.pending{background:#fff7df;color:var(--amber)}.badge.high,.badge.critical,.badge.suspended,.badge.failed,.badge.flagged,.badge.rejected{background:#fff0ed;color:var(--red)}"
                + ".badge.pending_approval{background:#fff7df;color:var(--amber)}.badge.enabled{background:#e8f7ef;color:var(--green)}.badge.disabled,.badge.revoked{background:#fff0ed;color:var(--red)}.portal-choice{display:block;background:#fff;border:1px solid var(--line);border-radius:8px;padding:28px;box-shadow:0 10px 24px rgba(16,36,62,.06);transition:transform .18s ease,border-color .18s ease}.portal-choice:hover{transform:translateY(-3px);border-color:#9fc5f0}.portal-choice strong{display:block;font-size:26px;color:#10243e}.portal-choice span{display:block;margin-top:8px;color:var(--muted)}aside{overflow:auto}"
                + ".governor-login-page{min-height:100vh;height:100vh}.governor-login-page .login-intro,.governor-login-page .login-panel{min-height:100vh}.governor-login-only{min-height:100vh;display:grid;place-items:center;padding:24px;background:#f5f7fb}.governor-login-only .portal-card{width:min(430px,100%)}"
                + ".alert-list{margin:0;padding-left:18px}.alert-list li{margin:8px 0}.report-box{white-space:pre-wrap;background:#0f172a;color:#e6edf8;padding:18px;border-radius:8px;min-height:260px;overflow:auto}"
                + "@media(max-width:1050px){.site-header{grid-template-columns:1fr}.public-hero,.public-grid.three,.public-grid.two,.indicator-strip,.quick-services,.site-footer{grid-template-columns:1fr 1fr}.login-shell,.app-shell,.hero,.split{grid-template-columns:1fr}aside{height:auto;position:relative}.content{padding:18px}}"
                + "@media(max-width:700px){.public-hero,.public-grid.three,.public-grid.two,.indicator-strip,.quick-services,.site-footer{grid-template-columns:1fr}.public-hero{padding:48px 24px}.public-hero h1,.login-intro h1{font-size:36px}.public-main{padding:24px}.site-header{padding:12px 18px}.login-intro{padding:40px 24px}.login-panel{padding:24px}.rate-card{grid-template-columns:1fr}}";
    }
}
