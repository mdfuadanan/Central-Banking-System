package central_banking_system;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class AppRouter implements HttpHandler {
    private static final String LOGO_RESOURCE = "/assets/central-bank-logo.png";
    private static final String SESSION_COOKIE = "CBS5SESSION";

    private final CentralBankSystem system;
    private final PageRenderer renderer = new PageRenderer();
    private final LoginManager loginManager = new LoginManager();
    private final SessionManager sessionManager = new SessionManager();

    public AppRouter(CentralBankSystem system) {
        this.system = system;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            if ("POST".equalsIgnoreCase(method)) {
                handlePost(exchange, path, readForm(exchange));
            } else {
                handleGet(exchange, path, UrlUtil.parseQuery(exchange.getRequestURI().getRawQuery()));
            }
        } catch (RuntimeException ex) {
            sendHtml(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR,
                    renderer.loginPage("Application error: " + ex.getMessage()));
        }
    }

    private void handleGet(HttpExchange exchange, String path, Map<String, String> query) throws IOException {
        if (LOGO_RESOURCE.equals(path)) {
            sendResource(exchange, "image/png", LOGO_RESOURCE);
            return;
        }
        if ("/".equals(path) || "/home".equals(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK, renderer.homePage(system.getExchangeRates()));
            return;
        }
        if (List.of("/about", "/policy-rate", "/reserve-ratio", "/interbank-exchange-rate",
                "/cipc", "/notices", "/news", "/publications", "/search").contains(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK, renderer.publicPage(path.substring(1)));
            return;
        }
        if ("/login".equals(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK, renderer.loginPage(query.get("error")));
            return;
        }
        if ("/admin-login".equals(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.credentialLoginPage("Administrator", "/admin-login", "Administrator ID", "admin", query.get("error")));
            return;
        }
        if ("/commercial-bank-login".equals(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.credentialLoginPage("Commercial Bank", "/commercial-bank-login", "Bank ID", "B001", query.get("error")));
            return;
        }
        if ("/governor-login".equals(path)) {
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.governorLoginPage(query.get("error")));
            return;
        }
        if ("/logout".equals(path)) {
            logout(exchange);
            return;
        }
        if ("/admin".equals(path)) {
            UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
            if (session == null) {
                return;
            }
            String view = query.getOrDefault("view", "dashboard");
            String report = reportText(view, query);
            Administrator administrator = system.findAdministrator(session.getUserId());
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.adminPage(system, administrator, view, query.get("msg"), report));
            return;
        }
        if ("/bank".equals(path)) {
            UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
            if (session == null) {
                return;
            }
            CommercialBank bank = system.findBank(session.getUserId());
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.bankPage(system, bank, query.getOrDefault("view", "dashboard"), query.get("msg")));
            return;
        }
        if ("/governor".equals(path)) {
            UserSession session = requireRole(exchange, UserRole.GOVERNOR);
            if (session == null) {
                return;
            }
            String view = query.getOrDefault("view", "dashboard");
            String report = reportText(view, query);
            sendHtml(exchange, HttpURLConnection.HTTP_OK,
                    renderer.governorPage(system, view, query.get("msg"), report));
            return;
        }
        sendHtml(exchange, HttpURLConnection.HTTP_NOT_FOUND, renderer.publicPage("home"));
    }

    private void handlePost(HttpExchange exchange, String path, Map<String, String> form) throws IOException {
        switch (path) {
            case "/admin-login":
                loginAdministrator(exchange, form);
                return;
            case "/commercial-bank-login":
                loginCommercialBank(exchange, form);
                return;
            case "/governor-login":
                loginGovernor(exchange, form);
                return;
            case "/admin/bank/add":
                adminBankAdd(exchange, form);
                return;
            case "/admin/bank/edit":
                adminBankEdit(exchange, form);
                return;
            case "/admin/bank/suspend":
                adminBankSuspension(exchange, form);
                return;
            case "/admin/bank/delete":
                adminBankClosure(exchange, form);
                return;
            case "/admin/bank/resubmit":
                adminBankResubmit(exchange, form);
                return;
            case "/admin/loan/decide":
                adminLoanDecide(exchange, form);
                return;
            case "/admin/supply":
                adminMoneySupply(exchange, form);
                return;
            case "/admin/reserve/update":
                adminReserveUpdate(exchange, form);
                return;
            case "/admin/policy":
                adminPolicy(exchange, form);
                return;
            case "/admin/request/submit":
                adminGeneralRequest(exchange, form);
                return;
            case "/admin/request/cancel":
                adminCancelRequest(exchange, form);
                return;
            case "/bank/loan/apply":
                bankLoanApply(exchange, form);
                return;
            case "/bank/transfer":
                bankTransfer(exchange, form);
                return;
            case "/bank/profile/update":
                bankProfileUpdate(exchange, form);
                return;
            case "/bank/customer/add":
                bankCustomerAdd(exchange, form);
                return;
            case "/bank/customer/edit":
                bankCustomerEdit(exchange, form);
                return;
            case "/bank/deposit":
                bankDeposit(exchange, form);
                return;
            case "/bank/withdraw":
                bankWithdraw(exchange, form);
                return;
            case "/governor/request/review":
                governorReviewRequest(exchange, form);
                return;
            case "/governor/admin/create":
                governorAdminCreate(exchange, form);
                return;
            case "/governor/admin/edit":
                governorAdminEdit(exchange, form);
                return;
            case "/governor/admin/password":
                governorAdminPassword(exchange, form);
                return;
            case "/governor/admin/toggle":
                governorAdminToggle(exchange, form);
                return;
            case "/governor/admin/delete":
                governorAdminDelete(exchange, form);
                return;
            case "/governor/profile/update":
                governorProfileUpdate(exchange, form);
                return;
            case "/governor/password":
                governorPassword(exchange, form);
                return;
            case "/governor/reserve/update":
                governorReserveUpdate(exchange, form);
                return;
            case "/governor/policy/add":
                governorPolicyAdd(exchange, form);
                return;
            default:
                redirect(exchange, "/");
        }
    }

    private void loginAdministrator(HttpExchange exchange, Map<String, String> form) throws IOException {
        LoginManager.LoginResult result = loginManager.loginAdministrator(form.get("userId"), form.get("password"),
                system.getAdministrators());
        system.recordLogin(UserRole.ADMINISTRATOR, form.getOrDefault("userId", ""), result.isSuccess(), result.getMessage());
        if (!result.isSuccess()) {
            redirect(exchange, "/admin-login?error=" + enc(result.getMessage()));
            return;
        }
        loginSuccess(exchange, result, "/admin?msg=Welcome " + enc(result.getDisplayName()));
    }

    private void loginCommercialBank(HttpExchange exchange, Map<String, String> form) throws IOException {
        LoginManager.LoginResult result = loginManager.loginCommercialBank(form.get("userId"), form.get("password"),
                system.getBanks());
        system.recordLogin(UserRole.COMMERCIAL_BANK, form.getOrDefault("userId", ""), result.isSuccess(), result.getMessage());
        if (!result.isSuccess()) {
            redirect(exchange, "/commercial-bank-login?error=" + enc(result.getMessage()));
            return;
        }
        loginSuccess(exchange, result, "/bank?msg=Welcome " + enc(result.getDisplayName()));
    }

    private void loginGovernor(HttpExchange exchange, Map<String, String> form) throws IOException {
        LoginManager.LoginResult result = loginManager.loginGovernor(form.get("userId"), form.get("password"),
                system.getGovernor());
        system.recordLogin(UserRole.GOVERNOR, form.getOrDefault("userId", ""), result.isSuccess(), result.getMessage());
        if (!result.isSuccess()) {
            redirect(exchange, "/governor-login?error=" + enc(result.getMessage()));
            return;
        }
        loginSuccess(exchange, result, "/governor?msg=Welcome " + enc(result.getDisplayName()));
    }

    private void loginSuccess(HttpExchange exchange, LoginManager.LoginResult result, String redirectTo) throws IOException {
        UserSession session = sessionManager.create(result.getRole(), result.getUserId(), result.getDisplayName());
        setSessionCookie(exchange, session);
        redirect(exchange, redirectTo);
    }

    private void adminBankAdd(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.registerBankApplication(session.getUserId(), form.get("name"), form.get("license"),
                BankType.valueOf(form.get("type")), form.get("address"), money(form.get("capital")),
                form.get("rating"), form.get("password"), form.get("email"), form.get("supportingInformation"));
        redirect(exchange, "/admin?view=banks&msg=Bank application submitted to Governor");
    }

    private void adminBankEdit(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.ADMINISTRATOR) == null) {
            return;
        }
        system.editBank(form.get("bankId"), form.get("name"), form.get("address"),
                money(form.get("capital")), form.get("rating"), form.get("email"));
        redirect(exchange, "/admin?view=banks&msg=Bank updated");
    }

    private void adminBankSuspension(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.requestBankSuspension(session.getUserId(), form.get("bankId"), form.get("reason"));
        redirect(exchange, "/admin?view=banks&msg=Suspension request sent to Governor");
    }

    private void adminBankClosure(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.requestBankClosure(session.getUserId(), form.get("bankId"), form.get("reason"));
        redirect(exchange, "/admin?view=banks&msg=Closure request sent to Governor");
    }

    private void adminBankResubmit(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.resubmitBankRegistration(session.getUserId(), form.get("bankId"), form.get("supportingInformation"));
        redirect(exchange, "/admin?view=banks&msg=Bank registration resubmitted to Governor");
    }

    private void adminLoanDecide(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.ADMINISTRATOR) == null) {
            return;
        }
        system.decideLoan(form.get("loanId"), "approve".equals(form.get("decision")));
        redirect(exchange, "/admin?view=loans&msg=Loan decision saved");
    }

    private void adminMoneySupply(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.requestMoneySupply(session.getUserId(), form.get("bankId"), money(form.get("amount")),
                OperationType.valueOf(form.get("type")));
        redirect(exchange, "/admin?view=reserve&msg=Money supply request sent to Governor");
    }

    private void adminReserveUpdate(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.requestReserveUpdate(session.getUserId(), money(form.get("nationalReserve")), money(form.get("minimumReserve")));
        redirect(exchange, "/admin?view=reserve&msg=Reserve management request sent to Governor");
    }

    private void adminPolicy(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.requestPolicyUpdate(session.getUserId(), form.get("policyName"), money(form.get("interestRate")),
                money(form.get("reserveRequirement")), form.get("description"));
        redirect(exchange, "/admin?view=settings&msg=Policy request sent to Governor");
    }

    private void adminGeneralRequest(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.submitGeneralApprovalRequest(session.getUserId(), ApprovalType.valueOf(form.get("type")), form.get("description"));
        redirect(exchange, "/admin?view=requests&msg=Approval request submitted");
    }

    private void adminCancelRequest(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.ADMINISTRATOR);
        if (session == null) {
            return;
        }
        system.cancelPendingRequest(session.getUserId(), form.get("requestId"));
        redirect(exchange, "/admin?view=requests&msg=Request cancelled");
    }

    private void bankLoanApply(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.applyLoan(session.getUserId(), LoanType.valueOf(form.get("type")), money(form.get("amount")));
        redirect(exchange, "/bank?view=loans&msg=Loan request submitted");
    }

    private void bankTransfer(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.transferFunds(session.getUserId(), form.get("receiverId"), money(form.get("amount")));
        redirect(exchange, "/bank?view=transactions&msg=Transfer recorded");
    }

    private void bankProfileUpdate(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.updateBankProfile(session.getUserId(), form.get("address"), form.get("email"), form.get("password"));
        redirect(exchange, "/bank?view=profile&msg=Profile updated");
    }

    private void bankCustomerAdd(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.addCustomer(session.getUserId(), form.get("name"), form.get("email"), form.get("accountType"),
                money(form.get("initialDeposit")));
        redirect(exchange, "/bank?view=customers&msg=Customer created");
    }

    private void bankCustomerEdit(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.editCustomer(session.getUserId(), form.get("customerId"), form.get("name"), form.get("email"),
                CustomerStatus.valueOf(form.get("status")));
        redirect(exchange, "/bank?view=customers&msg=Customer updated");
    }

    private void bankDeposit(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.deposit(session.getUserId(), form.get("accountId"), money(form.get("amount")));
        redirect(exchange, "/bank?view=accounts&msg=Deposit recorded");
    }

    private void bankWithdraw(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.COMMERCIAL_BANK);
        if (session == null) {
            return;
        }
        system.withdraw(session.getUserId(), form.get("accountId"), money(form.get("amount")));
        redirect(exchange, "/bank?view=accounts&msg=Withdrawal recorded");
    }

    private void governorReviewRequest(HttpExchange exchange, Map<String, String> form) throws IOException {
        UserSession session = requireRole(exchange, UserRole.GOVERNOR);
        if (session == null) {
            return;
        }
        system.reviewApprovalRequest(form.get("requestId"), "approve".equals(form.get("decision")),
                form.get("remarks"), session.getUserId());
        redirect(exchange, "/governor?view=requests&msg=Request reviewed");
    }

    private void governorAdminCreate(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.createAdministrator(form.get("name"), form.get("password"), form.get("email"));
        redirect(exchange, "/governor?view=administrators&msg=Administrator created");
    }

    private void governorAdminEdit(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.editAdministrator(form.get("administratorId"), form.get("name"), form.get("email"));
        redirect(exchange, "/governor?view=administrators&msg=Administrator updated");
    }

    private void governorAdminPassword(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.changeAdministratorPassword(form.get("administratorId"), form.get("password"), true);
        redirect(exchange, "/governor?view=administrators&msg=Administrator password reset");
    }

    private void governorAdminToggle(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.setAdministratorEnabled(form.get("administratorId"), Boolean.parseBoolean(form.get("enabled")));
        redirect(exchange, "/governor?view=administrators&msg=Administrator status updated");
    }

    private void governorAdminDelete(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.deleteAdministrator(form.get("administratorId"));
        redirect(exchange, "/governor?view=administrators&msg=Administrator deleted");
    }

    private void governorProfileUpdate(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.updateGovernorProfile(form.get("name"), form.get("email"));
        redirect(exchange, "/governor?view=profile&msg=Governor profile updated");
    }

    private void governorPassword(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.changeGovernorPassword(form.get("password"));
        redirect(exchange, "/governor?view=profile&msg=Governor password changed");
    }

    private void governorReserveUpdate(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.updateReserve(money(form.get("nationalReserve")), money(form.get("minimumReserve")));
        redirect(exchange, "/governor?view=fx&msg=Foreign exchange reserves updated");
    }

    private void governorPolicyAdd(HttpExchange exchange, Map<String, String> form) throws IOException {
        if (requireRole(exchange, UserRole.GOVERNOR) == null) {
            return;
        }
        system.updatePolicy(form.get("policyName"), money(form.get("interestRate")),
                money(form.get("reserveRequirement")), form.get("description"));
        redirect(exchange, "/governor?view=monetary-policy&msg=Policy saved");
    }

    private UserSession requireRole(HttpExchange exchange, UserRole requiredRole) throws IOException {
        UserSession session = sessionManager.find(readSessionCookie(exchange));
        if (session == null || session.getRole() != requiredRole || !isStillAuthorized(session)) {
            if (session != null) {
                sessionManager.destroy(session.getToken());
                clearSessionCookie(exchange);
            }
            redirect(exchange, loginPath(requiredRole));
            return null;
        }
        return session;
    }

    private boolean isStillAuthorized(UserSession session) {
        if (session.getRole() == UserRole.ADMINISTRATOR) {
            Administrator administrator = system.findAdministrator(session.getUserId());
            return administrator != null && administrator.isEnabled();
        }
        if (session.getRole() == UserRole.COMMERCIAL_BANK) {
            CommercialBank bank = system.findBank(session.getUserId());
            return bank != null && bank.getStatus() == BankStatus.ACTIVE;
        }
        return session.getRole() == UserRole.GOVERNOR
                && system.getGovernor().getGovernorId().equalsIgnoreCase(session.getUserId());
    }

    private String loginPath(UserRole role) {
        if (role == UserRole.GOVERNOR) {
            return "/governor-login?error=Governor login required";
        }
        if (role == UserRole.ADMINISTRATOR) {
            return "/admin-login?error=Administrator login required";
        }
        return "/commercial-bank-login?error=Commercial Bank login required";
    }

    private String reportText(String view, Map<String, String> query) {
        if (("reports".equals(view) || "national-reports".equals(view)) && query.containsKey("type")) {
            ReportType reportType = ReportType.valueOf(query.get("type"));
            if (reportType != ReportType.CUSTOMER) {
                return system.generateReport(reportType);
            }
        }
        return "";
    }

    private Map<String, String> readForm(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return UrlUtil.parseQuery(body);
        }
    }

    private void sendHtml(HttpExchange exchange, int statusCode, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location.replace(" ", "%20"));
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_SEE_OTHER, -1);
        exchange.close();
    }

    private String readSessionCookie(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies == null) {
            return "";
        }
        for (String header : cookies) {
            String[] parts = header.split(";");
            for (String part : parts) {
                String[] pair = part.trim().split("=", 2);
                if (pair.length == 2 && SESSION_COOKIE.equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return "";
    }

    private void setSessionCookie(HttpExchange exchange, UserSession session) {
        exchange.getResponseHeaders().add("Set-Cookie",
                SESSION_COOKIE + "=" + session.getToken() + "; Path=/; HttpOnly; SameSite=Lax");
    }

    private void clearSessionCookie(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Set-Cookie",
                SESSION_COOKIE + "=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
    }

    private void logout(HttpExchange exchange) throws IOException {
        String token = readSessionCookie(exchange);
        sessionManager.destroy(token);
        clearSessionCookie(exchange);
        redirect(exchange, "/login?error=Logged out");
    }

    private String enc(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private double money(String value) {
        return NumberUtil.parseDouble(value, 0);
    }

    private void sendResource(HttpExchange exchange, String contentType, String resourcePath) throws IOException {
        try (InputStream in = AppRouter.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                sendHtml(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Not found");
                return;
            }
            byte[] bytes = in.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Cache-Control", "public, max-age=86400");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        }
    }
}
