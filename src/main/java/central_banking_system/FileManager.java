package central_banking_system;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final Path dataDirectory;

    public FileManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void initialize() {
        try {
            Files.createDirectories(dataDirectory);
            createIfMissing("banks.csv", sampleBanks());
            createIfMissing("customers.csv", sampleCustomers());
            createIfMissing("accounts.csv", sampleAccounts());
            createIfMissing("loans.csv", sampleLoans());
            createIfMissing("transactions.csv", sampleTransactions());
            createIfMissing("reserve.csv", List.of(CsvUtil.join("12500000", "3000000", LocalDate.now().toString())));
            createIfMissing("operations.csv", sampleOperations());
            createIfMissing("policies.csv", samplePolicies());
            createIfMissing("exchange_rates.csv", sampleExchangeRates());
            createIfMissing("governor.csv", sampleGovernor());
            createIfMissing("administrators.csv", sampleAdministrators());
            createIfMissing("approval_requests.csv", new ArrayList<>());
            createIfMissing("notifications.csv", new ArrayList<>());
            createIfMissing("audit_logs.csv", new ArrayList<>());
            createIfMissing("login_logs.csv", new ArrayList<>());
            createIfMissing("activity_logs.csv", new ArrayList<>());
        } catch (IOException ex) {
            throw new IllegalStateException("Could not initialize data folder: " + dataDirectory, ex);
        }
    }

    public List<CommercialBank> loadBanks() {
        List<CommercialBank> banks = new ArrayList<>();
        for (String line : readLines("banks.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length < 16) {
                continue;
            }
            banks.add(new CommercialBank(p[0], p[1], p[2], BankType.valueOf(p[3]), p[4],
                    toDouble(p[5]), BankStatus.valueOf(p[6]), RiskLevel.valueOf(p[7]),
                    LocalDate.parse(p[8]), toDouble(p[9]), toDouble(p[10]), toDouble(p[11]),
                    toDouble(p[12]), p[13], p[14], p[15]));
        }
        return banks;
    }

    public void saveBanks(List<CommercialBank> banks) {
        List<String> lines = new ArrayList<>();
        for (CommercialBank bank : banks) {
            lines.add(CsvUtil.join(bank.getId(), bank.getName(), bank.getLicenseNo(), bank.getType().name(),
                    bank.getAddress(), String.valueOf(bank.getCapital()), bank.getStatus().name(),
                    bank.getRiskLevel().name(), bank.getActivatedOn().toString(),
                    String.valueOf(bank.getCurrentBalance()), String.valueOf(bank.getReserveBalance()),
                    String.valueOf(bank.getAssets()), String.valueOf(bank.getLiabilities()),
                    bank.getCreditRating(), bank.getPassword(), bank.getContactEmail()));
        }
        writeLines("banks.csv", lines);
    }

    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (String line : readLines("customers.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 6) {
                customers.add(new Customer(p[0], p[1], p[2], p[3], CustomerStatus.valueOf(p[4]), p[5]));
            }
        }
        return customers;
    }

    public void saveCustomers(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            lines.add(CsvUtil.join(customer.getCustomerId(), customer.getBankId(), customer.getName(),
                    customer.getEmail(), customer.getStatus().name(), customer.getAccountId()));
        }
        writeLines("customers.csv", lines);
    }

    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (String line : readLines("accounts.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 6) {
                accounts.add(new Account(p[0], p[1], p[2], p[3], toDouble(p[4]), Boolean.parseBoolean(p[5])));
            }
        }
        return accounts;
    }

    public void saveAccounts(List<Account> accounts) {
        List<String> lines = new ArrayList<>();
        for (Account account : accounts) {
            lines.add(CsvUtil.join(account.getAccountId(), account.getCustomerId(), account.getBankId(),
                    account.getAccountType(), String.valueOf(account.getBalance()), String.valueOf(account.isActive())));
        }
        writeLines("accounts.csv", lines);
    }

    public List<Loan> loadLoans() {
        List<Loan> loans = new ArrayList<>();
        for (String line : readLines("loans.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 11) {
                loans.add(new Loan(p[0], p[1], LoanType.valueOf(p[2]), toDouble(p[3]), toDouble(p[4]),
                        LocalDate.parse(p[5]), LocalDate.parse(p[6]), LoanStatus.valueOf(p[7]),
                        toDouble(p[8]), RiskLevel.valueOf(p[9]), p[10]));
            }
        }
        return loans;
    }

    public void saveLoans(List<Loan> loans) {
        List<String> lines = new ArrayList<>();
        for (Loan loan : loans) {
            lines.add(CsvUtil.join(loan.getLoanId(), loan.getBankId(), loan.getType().name(),
                    String.valueOf(loan.getAmount()), String.valueOf(loan.getInterestRate()),
                    loan.getStartDate().toString(), loan.getEndDate().toString(), loan.getStatus().name(),
                    String.valueOf(loan.getRiskScore()), loan.getRiskLevel().name(), loan.getComments()));
        }
        writeLines("loans.csv", lines);
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : readLines("transactions.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 7) {
                transactions.add(new Transaction(p[0], p[1], p[2], toDouble(p[3]), LocalDateTime.parse(p[4]),
                        TransactionStatus.valueOf(p[5]), p[6]));
            }
        }
        return transactions;
    }

    public void saveTransactions(List<Transaction> transactions) {
        List<String> lines = new ArrayList<>();
        for (Transaction transaction : transactions) {
            lines.add(CsvUtil.join(transaction.getTransactionId(), transaction.getSenderBankId(),
                    transaction.getReceiverBankId(), String.valueOf(transaction.getAmount()),
                    transaction.getDateTime().toString(), transaction.getStatus().name(),
                    transaction.getDescription()));
        }
        writeLines("transactions.csv", lines);
    }

    public Reserve loadReserve() {
        List<String> lines = readLines("reserve.csv");
        if (lines.isEmpty()) {
            return new Reserve(12500000, 3000000, LocalDate.now());
        }
        String[] p = CsvUtil.split(lines.get(0));
        return new Reserve(toDouble(p[0]), toDouble(p[1]), LocalDate.parse(p[2]));
    }

    public void saveReserve(Reserve reserve) {
        writeLines("reserve.csv", List.of(CsvUtil.join(String.valueOf(reserve.getNationalReserve()),
                String.valueOf(reserve.getMinimumRequiredReserve()), reserve.getLastUpdated().toString())));
    }

    public List<LiquidityOperation> loadOperations() {
        List<LiquidityOperation> operations = new ArrayList<>();
        for (String line : readLines("operations.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 6) {
                operations.add(new LiquidityOperation(p[0], OperationType.valueOf(p[1]), toDouble(p[2]), p[3],
                        LocalDate.parse(p[4]), OperationStatus.valueOf(p[5])));
            }
        }
        return operations;
    }

    public void saveOperations(List<LiquidityOperation> operations) {
        List<String> lines = new ArrayList<>();
        for (LiquidityOperation operation : operations) {
            lines.add(CsvUtil.join(operation.getOperationId(), operation.getOperationType().name(),
                    String.valueOf(operation.getAmount()), operation.getTargetBankId(),
                    operation.getDate().toString(), operation.getStatus().name()));
        }
        writeLines("operations.csv", lines);
    }

    public List<MonetaryPolicy> loadPolicies() {
        List<MonetaryPolicy> policies = new ArrayList<>();
        for (String line : readLines("policies.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 7) {
                policies.add(new MonetaryPolicy(p[0], p[1], toDouble(p[2]), toDouble(p[3]),
                        LocalDate.parse(p[4]), LocalDate.parse(p[5]), p[6]));
            }
        }
        return policies;
    }

    public void savePolicies(List<MonetaryPolicy> policies) {
        List<String> lines = new ArrayList<>();
        for (MonetaryPolicy policy : policies) {
            lines.add(CsvUtil.join(policy.getPolicyId(), policy.getPolicyName(), String.valueOf(policy.getInterestRate()),
                    String.valueOf(policy.getReserveRequirement()), policy.getEffectiveFrom().toString(),
                    policy.getEffectiveTo().toString(), policy.getDescription()));
        }
        writeLines("policies.csv", lines);
    }

    public List<ExchangeRate> loadExchangeRates() {
        List<ExchangeRate> rates = new ArrayList<>();
        for (String line : readLines("exchange_rates.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 7) {
                rates.add(new ExchangeRate(p[0], p[1], toDouble(p[2]), toDouble(p[3]), toDouble(p[4]),
                        p[5], p[6], false));
            }
        }
        if (rates.isEmpty()) {
            for (String line : sampleExchangeRates()) {
                String[] p = CsvUtil.split(line);
                rates.add(new ExchangeRate(p[0], p[1], toDouble(p[2]), toDouble(p[3]), toDouble(p[4]),
                        p[5], p[6], false));
            }
        }
        return rates;
    }

    public void saveExchangeRates(List<ExchangeRate> rates) {
        List<String> lines = new ArrayList<>();
        for (ExchangeRate rate : rates) {
            lines.add(CsvUtil.join(rate.getCurrencyCode(), rate.getCurrencyName(),
                    String.valueOf(rate.getReferenceRate()), String.valueOf(rate.getBuyingRate()),
                    String.valueOf(rate.getSellingRate()), rate.getLastUpdated(), rate.getSource()));
        }
        writeLines("exchange_rates.csv", lines);
    }

    public Governor loadGovernor() {
        List<String> lines = readLines("governor.csv");
        if (lines.isEmpty()) {
            return new Governor("GOV001", "Project Governor", "governor123", "governor@centralbank.gov");
        }
        String[] p = CsvUtil.split(lines.get(0));
        return new Governor(p[0], p[1], p[2], p.length > 3 ? p[3] : "governor@centralbank.gov");
    }

    public void saveGovernor(Governor governor) {
        writeLines("governor.csv", List.of(CsvUtil.join(governor.getGovernorId(), governor.getName(),
                governor.getPassword(), governor.getEmail())));
    }

    public List<Administrator> loadAdministrators() {
        List<Administrator> administrators = new ArrayList<>();
        for (String line : readLines("administrators.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 7) {
                administrators.add(new Administrator(p[0], p[1], p[2], p[3], Boolean.parseBoolean(p[4]),
                        LocalDate.parse(p[5]), LocalDate.parse(p[6])));
            }
        }
        return administrators;
    }

    public void saveAdministrators(List<Administrator> administrators) {
        List<String> lines = new ArrayList<>();
        for (Administrator administrator : administrators) {
            lines.add(CsvUtil.join(administrator.getAdministratorId(), administrator.getName(),
                    administrator.getPassword(), administrator.getEmail(), String.valueOf(administrator.isEnabled()),
                    administrator.getCreatedOn().toString(), administrator.getLastPasswordReset().toString()));
        }
        writeLines("administrators.csv", lines);
    }

    public List<ApprovalRequest> loadApprovalRequests() {
        List<ApprovalRequest> requests = new ArrayList<>();
        for (String line : readLines("approval_requests.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 12) {
                LocalDateTime approvalTime = p[9].isBlank() ? null : LocalDateTime.parse(p[9]);
                requests.add(new ApprovalRequest(p[0], p[1], p[2], ApprovalType.valueOf(p[3]), p[4],
                        LocalDateTime.parse(p[5]), ApprovalStatus.valueOf(p[6]), p[7], p[8],
                        approvalTime, p[10], p[11]));
            }
        }
        return requests;
    }

    public void saveApprovalRequests(List<ApprovalRequest> requests) {
        List<String> lines = new ArrayList<>();
        for (ApprovalRequest request : requests) {
            lines.add(CsvUtil.join(request.getRequestId(), request.getAdministratorId(),
                    request.getAdministratorName(), request.getRequestType().name(), request.getDescription(),
                    request.getSubmittedAt().toString(), request.getStatus().name(), request.getGovernorRemarks(),
                    request.getGovernorId(), request.getApprovalTime() == null ? "" : request.getApprovalTime().toString(),
                    request.getTargetId(), request.getPayload()));
        }
        writeLines("approval_requests.csv", lines);
    }

    public List<Notification> loadNotifications() {
        List<Notification> notifications = new ArrayList<>();
        for (String line : readLines("notifications.csv")) {
            String[] p = CsvUtil.split(line);
            if (p.length >= 6) {
                notifications.add(new Notification(p[0], UserRole.valueOf(p[1]), p[2], p[3],
                        LocalDateTime.parse(p[4]), Boolean.parseBoolean(p[5])));
            }
        }
        return notifications;
    }

    public void saveNotifications(List<Notification> notifications) {
        List<String> lines = new ArrayList<>();
        for (Notification notification : notifications) {
            lines.add(CsvUtil.join(notification.getNotificationId(), notification.getRecipientRole().name(),
                    notification.getRecipientId(), notification.getMessage(), notification.getCreatedAt().toString(),
                    String.valueOf(notification.isRead())));
        }
        writeLines("notifications.csv", lines);
    }

    public List<String> loadLog(String fileName) {
        return readLines(fileName);
    }

    public void saveLog(String fileName, List<String> entries) {
        writeLines(fileName, entries);
    }

    private void createIfMissing(String fileName, List<String> lines) throws IOException {
        Path file = dataDirectory.resolve(fileName);
        if (!Files.exists(file)) {
            Files.write(file, lines, StandardCharsets.UTF_8);
        }
    }

    private List<String> readLines(String fileName) {
        Path file = dataDirectory.resolve(fileName);
        try {
            if (!Files.exists(file)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read " + file, ex);
        }
    }

    private void writeLines(String fileName, List<String> lines) {
        try {
            Files.write(dataDirectory.resolve(fileName), lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not write " + fileName, ex);
        }
    }

    private double toDouble(String value) {
        return Double.parseDouble(value);
    }

    private List<String> sampleBanks() {
        return List.of(
                CsvUtil.join("B001", "Delta Commercial Bank", "LC-1001", "COMMERCIAL", "Dhaka", "5000000",
                        "ACTIVE", "LOW", "2020-01-15", "1800000", "320000", "6200000", "2300000", "A", "bank123", "delta@example.com"),
                CsvUtil.join("B002", "Padma Islamic Bank", "LC-1002", "ISLAMIC", "Chattogram", "4200000",
                        "ACTIVE", "MEDIUM", "2021-03-20", "1250000", "140000", "4800000", "2650000", "B", "bank123", "padma@example.com"),
                CsvUtil.join("B003", "Meghna Development Bank", "LC-1003", "DEVELOPMENT", "Sylhet", "3600000",
                        "SUSPENDED", "HIGH", "2019-06-01", "650000", "25000", "3300000", "3900000", "C", "bank123", "meghna@example.com")
        );
    }

    private List<String> sampleCustomers() {
        return List.of(
                CsvUtil.join("C001", "B001", "Rahim Uddin", "rahim@example.com", "ACTIVE", "A001"),
                CsvUtil.join("C002", "B001", "Farzana Akter", "farzana@example.com", "DORMANT", "A002"),
                CsvUtil.join("C003", "B002", "Kamal Hossain", "kamal@example.com", "ACTIVE", "A003"),
                CsvUtil.join("C004", "B003", "Nusrat Jahan", "nusrat@example.com", "ACTIVE", "A004")
        );
    }

    private List<String> sampleAccounts() {
        return List.of(
                CsvUtil.join("A001", "C001", "B001", "Savings", "125000", "true"),
                CsvUtil.join("A002", "C002", "B001", "Current", "24000", "false"),
                CsvUtil.join("A003", "C003", "B002", "Savings", "76000", "true"),
                CsvUtil.join("A004", "C004", "B003", "Current", "18000", "true")
        );
    }

    private List<String> sampleLoans() {
        return List.of(
                CsvUtil.join("L001", "B002", "SHORT_TERM", "450000", "7.5", LocalDate.now().minusMonths(3).toString(),
                        LocalDate.now().plusMonths(9).toString(), "DISBURSED", "52", "MEDIUM", "Working capital support"),
                CsvUtil.join("L002", "B003", "LONG_TERM", "900000", "0", LocalDate.now().toString(),
                        LocalDate.now().plusYears(2).toString(), "PENDING", "77", "HIGH", "Pending risk committee review")
        );
    }

    private List<String> sampleTransactions() {
        return List.of(
                CsvUtil.join("T001", "B001", "B002", "150000", LocalDateTime.now().minusDays(2).toString(), "SUCCESS", "Inter-bank settlement"),
                CsvUtil.join("T002", "B003", "B001", "900000", LocalDateTime.now().minusDays(1).toString(), "FLAGGED", "Very large transfer"),
                CsvUtil.join("T003", "B003", "B002", "120000", LocalDateTime.now().minusHours(8).toString(), "FAILED", "Sender had insufficient balance")
        );
    }

    private List<String> sampleOperations() {
        return List.of(
                CsvUtil.join("OP001", "INJECTION", "300000", "B002", LocalDate.now().minusDays(10).toString(), "COMPLETED")
        );
    }

    private List<String> samplePolicies() {
        return List.of(
                CsvUtil.join("P001", "Standard Reserve Policy", "6.5", "10",
                        LocalDate.now().minusMonths(1).toString(), LocalDate.now().plusMonths(11).toString(),
                        "Commercial banks must hold at least ten percent reserve against current balance")
        );
    }

    private List<String> sampleExchangeRates() {
        return List.of(
                CsvUtil.join("USD", "USD Exchange Rate", "117.80", "117.20", "118.40",
                        "18 Jul 2026", "Seeded central bank value"),
                CsvUtil.join("EUR", "EUR Exchange Rate", "127.925", "127.10", "128.75",
                        "18 Jul 2026", "Seeded central bank value")
        );
    }

    private List<String> sampleGovernor() {
        return List.of(CsvUtil.join("GOV001", "Project Governor", "governor123", "governor@centralbank.gov"));
    }

    private List<String> sampleAdministrators() {
        LocalDate today = LocalDate.now();
        return List.of(CsvUtil.join("admin", "Central Bank Administrator", "admin123",
                "admin@centralbank.gov", "true", today.toString(), today.toString()));
    }
}
