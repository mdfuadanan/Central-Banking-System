package central_banking_system;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SystemSmokeTest {
    public static void main(String[] args) throws Exception {
        Path dataDirectory = Path.of("build", "test-data").toAbsolutePath().normalize();
        deleteDirectory(dataDirectory);
        CentralBankSystem system = new CentralBankSystem(new FileManager(dataDirectory));

        require(system.getBanks().size() >= 3, "sample banks should load");
        Loan loan = system.applyLoan("B001", LoanType.SHORT_TERM, 250000);
        require(loan != null, "loan request should be created");
        Transaction transaction = system.transferFunds("B001", "B002", 50000);
        require(transaction != null, "transaction should be recorded");
        String report = system.generateReport(ReportType.SUMMARY);
        require(report.contains("System Summary"), "summary report should be generated");

        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", new AppRouter(system));
        server.start();
        try {
            runWebSmoke(system, "http://localhost:" + server.getAddress().getPort());
        } finally {
            server.stop(0);
        }

        System.out.println("Smoke test passed: compile, homepage, RBAC, logins, approvals, banking operations and reports are working.");
    }

    private static void runWebSmoke(CentralBankSystem system, String baseUrl) throws Exception {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

        HttpResponse<String> home = get(client, baseUrl + "/", "");
        require(home.statusCode() == 200, "homepage should load");
        require(home.body().contains("Welcome to Central Bank Management System"), "homepage content should be preserved");
        require(home.body().contains("USD Exchange Rate") && home.body().contains("EUR Exchange Rate"),
                "homepage should show currency exchange rates");
        require((home.body().contains("Live exchange rate") || home.body().contains("Cached exchange rate"))
                        && home.body().contains("Last Updated:"),
                "homepage should show live/cached exchange rate status and last updated date");

        HttpResponse<String> login = get(client, baseUrl + "/login", "");
        require(login.statusCode() == 200, "login selection page should load");
        require(login.body().contains("Administrator"), "login selection should include Administrator");
        require(login.body().contains("Commercial Bank"), "login selection should include Commercial Bank");
        require(!login.body().contains("Governor"), "login selection should hide Governor");

        HttpResponse<String> adminLogin = get(client, baseUrl + "/admin-login", "");
        require(!adminLogin.body().contains("Role-based access is enforced for every protected page."),
                "Administrator login should not show removed RBAC sentence");

        HttpResponse<String> commercialBankLogin = get(client, baseUrl + "/commercial-bank-login", "");
        require(!commercialBankLogin.body().contains("Role-based access is enforced for every protected page."),
                "Commercial Bank login should not show removed RBAC sentence");

        HttpResponse<String> governorLogin = get(client, baseUrl + "/governor-login", "");
        require(governorLogin.body().contains("Governor ID") && governorLogin.body().contains("Password"),
                "Governor login should contain credential fields");
        require(!governorLogin.body().contains("<header class='site-header'"),
                "Governor login should not show the public header");
        require(!governorLogin.body().contains("Role-based access is enforced for every protected page."),
                "Governor login should not show removed RBAC sentence");

        HttpResponse<String> unauthorized = get(client, baseUrl + "/governor", "");
        require(unauthorized.statusCode() == 303 && location(unauthorized).contains("/governor-login"),
                "Governor dashboard should require Governor session");

        String adminCookie = login(client, baseUrl + "/admin-login", "admin", "admin123");
        HttpResponse<String> adminDashboard = get(client, baseUrl + "/admin", adminCookie);
        require(adminDashboard.statusCode() == 200 && adminDashboard.body().contains("Administrator Dashboard"),
                "Administrator dashboard should load after login");

        String bankCookie = login(client, baseUrl + "/commercial-bank-login", "B001", "bank123");
        HttpResponse<String> bankDashboard = get(client, baseUrl + "/bank", bankCookie);
        require(bankDashboard.statusCode() == 200 && bankDashboard.body().contains("Commercial bank portal"),
                "Commercial Bank dashboard should load after login");

        HttpResponse<String> bankToGovernor = get(client, baseUrl + "/governor", bankCookie);
        require(bankToGovernor.statusCode() == 303 && location(bankToGovernor).contains("/governor-login"),
                "Commercial Bank must not access Governor pages");

        String governorCookie = login(client, baseUrl + "/governor-login", "GOV001", "governor123");
        HttpResponse<String> governorDashboard = get(client, baseUrl + "/governor", governorCookie);
        require(governorDashboard.statusCode() == 200 && governorDashboard.body().contains("<h1>Dashboard</h1>"),
                "Governor dashboard should load after login");
        require(governorDashboard.body().contains(">Management</a>")
                        && governorDashboard.body().contains(">Request</a>")
                        && governorDashboard.body().contains(">Reports</a>")
                        && governorDashboard.body().contains(">Logs</a>")
                        && !governorDashboard.body().contains(">National Financial Dashboard</a>")
                        && !governorDashboard.body().contains(">Pending Requests</a>"),
                "Governor side panel should use merged categories");

        HttpResponse<String> management = get(client, baseUrl + "/governor?view=management", governorCookie);
        require(management.body().contains("<h1>Management</h1>")
                        && management.body().contains("Create Administrator")
                        && management.body().contains("Interest Rate Management")
                        && management.body().contains("Commercial Bank Management"),
                "Governor Management page should merge admin, interest rate and commercial bank management");

        HttpResponse<String> requests = get(client, baseUrl + "/governor?view=requests", governorCookie);
        require(requests.body().contains("<h1>Request</h1>")
                        && requests.body().contains("Pending Requests")
                        && requests.body().contains("Approved Requests")
                        && requests.body().contains("Rejected Requests"),
                "Governor Requests page should merge all request states");

        HttpResponse<String> reports = get(client, baseUrl + "/governor?view=reports", governorCookie);
        require(reports.body().contains("<h1>Reports</h1>")
                        && reports.body().contains("National Reports")
                        && reports.body().contains("Fraud Reports"),
                "Governor Reports page should merge national and fraud reports");

        HttpResponse<String> logs = get(client, baseUrl + "/governor?view=logs", governorCookie);
        require(logs.body().contains("<h1>Logs</h1>")
                        && logs.body().contains("Audit Logs")
                        && logs.body().contains("Login Logs"),
                "Governor Logs page should merge audit and login logs");

        Map<String, String> bankApplication = new LinkedHashMap<>();
        bankApplication.put("name", "Smoke Test Bank");
        bankApplication.put("license", "LC-SMOKE");
        bankApplication.put("type", "COMMERCIAL");
        bankApplication.put("address", "Dhaka");
        bankApplication.put("capital", "1500000");
        bankApplication.put("rating", "A");
        bankApplication.put("password", "smoke123");
        bankApplication.put("email", "smoke@example.com");
        bankApplication.put("supportingInformation", "Smoke test registration documents");
        post(client, baseUrl + "/admin/bank/add", adminCookie, bankApplication);

        ApprovalRequest pending = latestPendingRegistration(system);
        CommercialBank newBank = system.findBank(pending.getTargetId());
        require(newBank != null && newBank.getStatus() == BankStatus.PENDING_APPROVAL,
                "new bank should wait for Governor approval");

        Map<String, String> review = new LinkedHashMap<>();
        review.put("requestId", pending.getRequestId());
        review.put("remarks", "Approved by smoke test");
        review.put("decision", "approve");
        post(client, baseUrl + "/governor/request/review", governorCookie, review);
        require(newBank.getStatus() == BankStatus.ACTIVE, "Governor approval should activate bank");

        String newBankCookie = login(client, baseUrl + "/commercial-bank-login", newBank.getId(), "smoke123");
        HttpResponse<String> newBankDashboard = get(client, baseUrl + "/bank", newBankCookie);
        require(newBankDashboard.statusCode() == 200 && newBankDashboard.body().contains("Smoke Test Bank"),
                "approved commercial bank should be able to log in");
    }

    private static ApprovalRequest latestPendingRegistration(CentralBankSystem system) {
        List<ApprovalRequest> requests = system.requestsByStatus(ApprovalStatus.PENDING);
        for (int i = requests.size() - 1; i >= 0; i--) {
            ApprovalRequest request = requests.get(i);
            if (request.getRequestType() == ApprovalType.COMMERCIAL_BANK_REGISTRATION_APPROVAL) {
                return request;
            }
        }
        throw new IllegalStateException("pending commercial bank registration request should exist");
    }

    private static String login(HttpClient client, String url, String userId, String password) throws Exception {
        HttpResponse<String> response = post(client, url, "", Map.of("userId", userId, "password", password));
        require(response.statusCode() == 303, "login should redirect on success: " + url);
        Optional<String> cookie = response.headers().firstValue("Set-Cookie");
        require(cookie.isPresent(), "login should issue a session cookie");
        return cookie.get().split(";", 2)[0];
    }

    private static HttpResponse<String> get(HttpClient client, String url, String cookie) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url)).GET();
        if (cookie != null && !cookie.isBlank()) {
            builder.header("Cookie", cookie);
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> post(HttpClient client, String url, String cookie,
                                             Map<String, String> values) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form(values)));
        if (cookie != null && !cookie.isBlank()) {
            builder.header("Cookie", cookie);
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static String form(Map<String, String> values) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (!body.isEmpty()) {
                body.append("&");
            }
            body.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            body.append("=");
            body.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return body.toString();
    }

    private static String location(HttpResponse<String> response) {
        return response.headers().firstValue("Location").orElse("");
    }

    private static void deleteDirectory(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        List<Path> paths;
        try (var stream = Files.walk(directory)) {
            paths = stream.sorted((a, b) -> b.compareTo(a)).toList();
        }
        for (Path path : paths) {
            Files.deleteIfExists(path);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
