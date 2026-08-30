package central_banking_system;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.StringJoiner;

public class CentralBankSystem {
    private final FileManager fileManager;
    private final RiskAssessment riskAssessment = new RiskAssessment();
    private final FraudDetector fraudDetector = new FraudDetector();
    private final ReportService reportService = new ReportService();
    private final ExchangeRateService exchangeRateService;
    private final CentralBank centralBank = new CentralBank("CB001", "National Central Bank",
            "Project Governor", "Central Bank Data Center", LocalDate.of(1971, 12, 16));

    private final Governor governor;
    private final ArrayList<Administrator> administrators;
    private final ArrayList<CommercialBank> banks;
    private final ArrayList<Customer> customers;
    private final ArrayList<Account> accounts;
    private final ArrayList<Loan> loans;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<LiquidityOperation> operations;
    private final ArrayList<MonetaryPolicy> policies;
    private final ArrayList<ApprovalRequest> approvalRequests;
    private final ArrayList<Notification> notifications;
    private final LinkedList<String> auditTrail = new LinkedList<>();
    private final LinkedList<String> loginLogs = new LinkedList<>();
    private final LinkedList<String> activityLogs = new LinkedList<>();
    private final Queue<Loan> pendingLoanQueue = new LinkedList<>();
    private final Stack<String> recentAlerts = new Stack<>();
    private final String[] reportCategories = {"Bank", "Customer", "Transaction", "Reserve", "Fraud", "Loan", "Investment Risk", "Performance"};
    private Reserve reserve;

    public CentralBankSystem(FileManager fileManager) {
        this.fileManager = fileManager;
        this.fileManager.initialize();
        this.exchangeRateService = new ExchangeRateService(fileManager);
        this.governor = fileManager.loadGovernor();
        this.administrators = new ArrayList<>(fileManager.loadAdministrators());
        this.banks = new ArrayList<>(fileManager.loadBanks());
        this.customers = new ArrayList<>(fileManager.loadCustomers());
        this.accounts = new ArrayList<>(fileManager.loadAccounts());
        this.loans = new ArrayList<>(fileManager.loadLoans());
        this.transactions = new ArrayList<>(fileManager.loadTransactions());
        this.reserve = fileManager.loadReserve();
        this.operations = new ArrayList<>(fileManager.loadOperations());
        this.policies = new ArrayList<>(fileManager.loadPolicies());
        this.approvalRequests = new ArrayList<>(fileManager.loadApprovalRequests());
        this.notifications = new ArrayList<>(fileManager.loadNotifications());
        this.auditTrail.addAll(fileManager.loadLog("audit_logs.csv"));
        this.loginLogs.addAll(fileManager.loadLog("login_logs.csv"));
        this.activityLogs.addAll(fileManager.loadLog("activity_logs.csv"));
        rebuildPendingLoanQueue();
        audit("System loaded from file storage");
    }

    public CommercialBank registerBank(String name, String license, BankType type, String address,
                                       double capital, String rating, String password, String email) {
        CommercialBank bank = buildBank(name, license, type, address, capital, rating, password, email, BankStatus.ACTIVE);
        centralBank.registerBank(banks, bank);
        saveBanks();
        audit("Governor registered active bank " + bank.getName());
        return bank;
    }

    public CommercialBank registerBankApplication(String administratorId, String name, String license, BankType type,
                                                  String address, double capital, String rating, String password,
                                                  String email, String supportingInformation) {
        CommercialBank bank = buildBank(name, license, type, address, capital, rating, password, email,
                BankStatus.PENDING_APPROVAL);
        centralBank.registerBank(banks, bank);
        saveBanks();

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("bankId", bank.getId());
        payload.put("supportingInformation", supportingInformation);
        submitApprovalRequest(administratorId, ApprovalType.COMMERCIAL_BANK_REGISTRATION_APPROVAL,
                "Registration approval requested for " + bank.getName() + ". " + supportingInformation,
                bank.getId(), encodePayload(payload));
        audit("Registered pending bank application " + bank.getName());
        return bank;
    }

    private CommercialBank buildBank(String name, String license, BankType type, String address,
                                     double capital, String rating, String password, String email, BankStatus status) {
        return new CommercialBank(IdGenerator.next("B"), clean(name), clean(license), type, clean(address), capital,
                status, RiskLevel.MEDIUM, LocalDate.now(), capital * 0.30, capital * 0.10, capital,
                capital * 0.30, clean(rating), clean(password), clean(email));
    }

    public void editBank(String bankId, String name, String address, double capital, String rating, String email) {
        CommercialBank bank = findBank(bankId);
        if (bank == null) {
            return;
        }
        bank.setName(clean(name));
        bank.setAddress(clean(address));
        bank.setCapital(capital);
        bank.setCreditRating(clean(rating));
        bank.setContactEmail(clean(email));
        bank.setAssets(Math.max(bank.getAssets(), capital));
        bank.recalculateNetWorth();
        saveBanks();
        audit("Edited bank " + bank.getName());
    }

    public void requestBankSuspension(String administratorId, String bankId, String reason) {
        CommercialBank bank = findBank(bankId);
        if (bank == null) {
            return;
        }
        submitApprovalRequest(administratorId, ApprovalType.COMMERCIAL_BANK_SUSPENSION,
                "Suspension requested for " + bank.getName() + ": " + reason, bankId, encodePayload(Map.of("bankId", bankId)));
    }

    public void requestBankClosure(String administratorId, String bankId, String reason) {
        CommercialBank bank = findBank(bankId);
        if (bank == null) {
            return;
        }
        submitApprovalRequest(administratorId, ApprovalType.COMMERCIAL_BANK_CLOSURE,
                "Closure requested for " + bank.getName() + ": " + reason, bankId, encodePayload(Map.of("bankId", bankId)));
    }

    public void resubmitBankRegistration(String administratorId, String bankId, String supportingInformation) {
        CommercialBank bank = findBank(bankId);
        if (bank == null || bank.getStatus() == BankStatus.ACTIVE) {
            return;
        }
        bank.setStatus(BankStatus.PENDING_APPROVAL);
        saveBanks();

        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("bankId", bankId);
        payload.put("supportingInformation", supportingInformation);
        submitApprovalRequest(administratorId, ApprovalType.COMMERCIAL_BANK_REGISTRATION_APPROVAL,
                "Registration resubmission requested for " + bank.getName() + ". " + supportingInformation,
                bankId, encodePayload(payload));
        audit("Resubmitted bank registration " + bank.getName());
    }

    public void deleteBank(String bankId) {
        CommercialBank bank = findBank(bankId);
        if (bank != null) {
            bank.setStatus(BankStatus.REVOKED);
            saveBanks();
            audit("Revoked bank " + bankId);
        }
    }

    public void suspendBank(String bankId) {
        CommercialBank bank = findBank(bankId);
        if (bank != null) {
            bank.setStatus(BankStatus.SUSPENDED);
            saveBanks();
            audit("Suspended bank " + bank.getName());
        }
    }

    public Loan applyLoan(String bankId, LoanType type, double amount) {
        CommercialBank bank = findBank(bankId);
        if (bank == null || bank.getStatus() != BankStatus.ACTIVE) {
            return null;
        }
        RiskResult result = riskAssessment.evaluate(bank, loans, amount);
        Loan loan = new Loan(IdGenerator.next("L"), bankId, type, amount, 0, LocalDate.now(),
                type == LoanType.SHORT_TERM ? LocalDate.now().plusMonths(12) : LocalDate.now().plusYears(3),
                LoanStatus.PENDING, result.getScore(), result.getLevel(), result.getExplanation());
        bank.setRiskLevel(result.getLevel());
        loans.add(loan);
        pendingLoanQueue.offer(loan);
        saveBanks();
        saveLoans();
        audit("Loan requested by " + bank.getName() + " for " + amount);
        return loan;
    }

    public void decideLoan(String loanId, boolean approve) {
        Loan loan = findLoan(loanId);
        if (loan == null || loan.getStatus() != LoanStatus.PENDING) {
            return;
        }
        CommercialBank bank = findBank(loan.getBankId());
        if (approve && bank != null && loan.getRiskLevel() != RiskLevel.CRITICAL) {
            double rate = loan.getRiskLevel() == RiskLevel.LOW ? 6.5
                    : loan.getRiskLevel() == RiskLevel.MEDIUM ? 8.0 : 10.5;
            loan.approve(rate, "Approved after risk score " + loan.getRiskScore());
            loan.disburse();
            bank.setCurrentBalance(bank.getCurrentBalance() + loan.getAmount());
            bank.setLiabilities(bank.getLiabilities() + loan.getAmount());
            bank.setAssets(bank.getAssets() + loan.getAmount());
            bank.recalculateNetWorth();
            reserve.supplyFunds(loan.getAmount());
            alertIfLowReserve();
            audit("Approved and disbursed loan " + loanId);
        } else {
            loan.reject("Rejected because risk is not acceptable");
            audit("Rejected loan " + loanId);
        }
        rebuildPendingLoanQueue();
        saveBanks();
        saveLoans();
        saveReserve();
    }

    public void requestMoneySupply(String administratorId, String bankId, double amount, OperationType type) {
        CommercialBank bank = findBank(bankId);
        if (bank == null) {
            return;
        }
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("bankId", bankId);
        payload.put("amount", String.valueOf(amount));
        payload.put("type", type.name());
        submitApprovalRequest(administratorId, ApprovalType.MONEY_SUPPLY_ADJUSTMENT,
                "Money supply adjustment for " + bank.getName() + " amount " + NumberUtil.money(amount),
                bankId, encodePayload(payload));
    }

    public LiquidityOperation supplyMoney(String bankId, double amount, OperationType type) {
        return executeSupplyMoney(bankId, amount, type);
    }

    private LiquidityOperation executeSupplyMoney(String bankId, double amount, OperationType type) {
        CommercialBank bank = findBank(bankId);
        if (bank == null || amount <= 0 || reserve.getNationalReserve() < amount || bank.getStatus() != BankStatus.ACTIVE) {
            pushAlert("Liquidity request rejected for " + bankId);
            return null;
        }
        reserve.supplyFunds(amount);
        LiquidityOperation operation = new LiquidityOperation(IdGenerator.next("OP"), type, amount, bankId,
                LocalDate.now(), OperationStatus.COMPLETED);
        operations.add(operation);
        centralBank.injectMoney(operation.getOperationId(), bank, amount);
        alertIfLowReserve();
        saveBanks();
        saveReserve();
        fileManager.saveOperations(operations);
        audit("Supplied " + amount + " to " + bank.getName());
        return operation;
    }

    public Transaction transferFunds(String senderId, String receiverId, double amount) {
        CommercialBank sender = findBank(senderId);
        CommercialBank receiver = findBank(receiverId);
        TransactionStatus status = TransactionStatus.SUCCESS;
        String description = "Transfer completed";
        if (sender == null || receiver == null || senderId.equals(receiverId) || amount <= 0
                || sender.getStatus() != BankStatus.ACTIVE || receiver.getStatus() != BankStatus.ACTIVE) {
            status = TransactionStatus.FAILED;
            description = "Invalid transfer request";
        } else if (sender.getCurrentBalance() < amount) {
            status = TransactionStatus.FAILED;
            description = "Sender had insufficient balance";
        }

        Transaction transaction = new Transaction(IdGenerator.next("T"), senderId, receiverId, amount,
                LocalDateTime.now(), status, description);
        List<String> warnings = fraudDetector.detect(transaction, transactions, sender);
        if (!warnings.isEmpty()) {
            transaction.setStatus(status == TransactionStatus.FAILED ? TransactionStatus.FAILED : TransactionStatus.FLAGGED);
            transaction.setDescription(description + "; " + String.join(", ", warnings));
            pushAlert(transaction.getTransactionId() + ": " + String.join(", ", warnings));
        }

        if (transaction.getStatus() != TransactionStatus.FAILED && sender != null && receiver != null) {
            sender.setCurrentBalance(sender.getCurrentBalance() - amount);
            receiver.setCurrentBalance(receiver.getCurrentBalance() + amount);
            sender.recalculateNetWorth();
            receiver.recalculateNetWorth();
            saveBanks();
        }
        transactions.add(transaction);
        fileManager.saveTransactions(transactions);
        audit("Recorded transaction " + transaction.getTransactionId());
        return transaction;
    }

    public Customer addCustomer(String bankId, String name, String email, String accountType, double initialDeposit) {
        CommercialBank bank = findBank(bankId);
        if (bank == null || bank.getStatus() != BankStatus.ACTIVE) {
            return null;
        }
        String customerId = IdGenerator.next("C");
        String accountId = IdGenerator.next("A");
        Customer customer = new Customer(customerId, bankId, clean(name), clean(email), CustomerStatus.ACTIVE, accountId);
        Account account = new Account(accountId, customerId, bankId, clean(accountType), Math.max(0, initialDeposit), true);
        customers.add(customer);
        accounts.add(account);
        if (initialDeposit > 0) {
            bank.setCurrentBalance(bank.getCurrentBalance() + initialDeposit);
            bank.setAssets(bank.getAssets() + initialDeposit);
            bank.recalculateNetWorth();
            transactions.add(new Transaction(IdGenerator.next("T"), "CUSTOMER", bankId, initialDeposit,
                    LocalDateTime.now(), TransactionStatus.SUCCESS, "Initial customer deposit for " + customerId));
        }
        saveBanks();
        fileManager.saveCustomers(customers);
        fileManager.saveAccounts(accounts);
        fileManager.saveTransactions(transactions);
        audit("Added customer " + customer.getName() + " for " + bank.getName());
        return customer;
    }

    public void editCustomer(String bankId, String customerId, String name, String email, CustomerStatus status) {
        Customer customer = findCustomer(customerId);
        if (customer == null || !customer.getBankId().equals(bankId)) {
            return;
        }
        customer.setName(clean(name));
        customer.setEmail(clean(email));
        customer.setStatus(status);
        fileManager.saveCustomers(customers);
        audit("Edited customer " + customerId + " for bank " + bankId);
    }

    public void deposit(String bankId, String accountId, double amount) {
        Account account = findAccount(accountId);
        CommercialBank bank = findBank(bankId);
        if (account == null || bank == null || !account.getBankId().equals(bankId) || amount <= 0) {
            return;
        }
        account.setBalance(account.getBalance() + amount);
        bank.setCurrentBalance(bank.getCurrentBalance() + amount);
        bank.setAssets(bank.getAssets() + amount);
        bank.recalculateNetWorth();
        transactions.add(new Transaction(IdGenerator.next("T"), "CUSTOMER", bankId, amount, LocalDateTime.now(),
                TransactionStatus.SUCCESS, "Customer deposit to " + accountId));
        saveBanks();
        fileManager.saveAccounts(accounts);
        fileManager.saveTransactions(transactions);
        audit("Deposited " + amount + " to account " + accountId);
    }

    public void withdraw(String bankId, String accountId, double amount) {
        Account account = findAccount(accountId);
        CommercialBank bank = findBank(bankId);
        if (account == null || bank == null || !account.getBankId().equals(bankId) || amount <= 0
                || account.getBalance() < amount || bank.getCurrentBalance() < amount) {
            return;
        }
        account.setBalance(account.getBalance() - amount);
        bank.setCurrentBalance(bank.getCurrentBalance() - amount);
        bank.setAssets(Math.max(0, bank.getAssets() - amount));
        bank.recalculateNetWorth();
        transactions.add(new Transaction(IdGenerator.next("T"), bankId, "CUSTOMER", amount, LocalDateTime.now(),
                TransactionStatus.SUCCESS, "Customer withdrawal from " + accountId));
        saveBanks();
        fileManager.saveAccounts(accounts);
        fileManager.saveTransactions(transactions);
        audit("Withdrew " + amount + " from account " + accountId);
    }

    public void updateBankProfile(String bankId, String address, String email, String password) {
        CommercialBank bank = findBank(bankId);
        if (bank != null) {
            bank.setAddress(clean(address));
            bank.setContactEmail(clean(email));
            if (password != null && !password.isBlank()) {
                bank.setPassword(password);
            }
            saveBanks();
            audit("Updated profile for " + bank.getName());
        }
    }

    public void requestReserveUpdate(String administratorId, double nationalReserve, double minimumReserve) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("nationalReserve", String.valueOf(nationalReserve));
        payload.put("minimumReserve", String.valueOf(minimumReserve));
        submitApprovalRequest(administratorId, ApprovalType.FOREIGN_EXCHANGE_RESERVE_MANAGEMENT,
                "Foreign exchange reserve management update requested. National reserve "
                        + NumberUtil.money(nationalReserve) + ", minimum " + NumberUtil.money(minimumReserve),
                "", encodePayload(payload));
    }

    public void updateReserve(double nationalReserve, double minimumReserve) {
        reserve.setNationalReserve(nationalReserve);
        reserve.setMinimumRequiredReserve(minimumReserve);
        alertIfLowReserve();
        saveReserve();
        audit("Updated reserve settings");
    }

    public void requestPolicyUpdate(String administratorId, String policyName, double interestRate,
                                    double reserveRequirement, String description) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("policyName", policyName);
        payload.put("interestRate", String.valueOf(interestRate));
        payload.put("reserveRequirement", String.valueOf(reserveRequirement));
        payload.put("description", description);
        ApprovalType type = policyName.toLowerCase().contains("rate")
                ? ApprovalType.POLICY_INTEREST_RATE_CHANGE : ApprovalType.MAJOR_MONETARY_POLICY_CHANGE;
        submitApprovalRequest(administratorId, type,
                "Policy change requested: " + policyName + " (" + description + ")", "",
                encodePayload(payload));
    }

    public void updatePolicy(String policyName, double interestRate, double reserveRequirement, String description) {
        MonetaryPolicy policy = new MonetaryPolicy(IdGenerator.next("P"), clean(policyName), interestRate,
                reserveRequirement, LocalDate.now(), LocalDate.now().plusYears(1), clean(description));
        policies.add(policy);
        fileManager.savePolicies(policies);
        audit("Added monetary policy " + policyName);
    }

    public ApprovalRequest submitGeneralApprovalRequest(String administratorId, ApprovalType type, String description) {
        return submitApprovalRequest(administratorId, type, description, "", "");
    }

    public ApprovalRequest submitApprovalRequest(String administratorId, ApprovalType type,
                                                 String description, String targetId, String payload) {
        Administrator administrator = findAdministrator(administratorId);
        String adminName = administrator == null ? "Unknown Administrator" : administrator.getName();
        ApprovalRequest request = new ApprovalRequest(IdGenerator.next("REQ"), administratorId, adminName, type,
                clean(description), LocalDateTime.now(), ApprovalStatus.PENDING, "", "", null,
                clean(targetId), payload == null ? "" : payload);
        approvalRequests.add(request);
        fileManager.saveApprovalRequests(approvalRequests);
        notifyUser(UserRole.GOVERNOR, governor.getGovernorId(), governorNotificationMessage(request));
        audit("Approval request submitted " + request.getRequestId() + " by " + adminName);
        activity("Administrator " + adminName + " submitted " + type.name().replace('_', ' '));
        return request;
    }

    public boolean cancelPendingRequest(String administratorId, String requestId) {
        ApprovalRequest request = findApprovalRequest(requestId);
        if (request == null || request.getStatus() != ApprovalStatus.PENDING
                || !request.getAdministratorId().equalsIgnoreCase(administratorId)) {
            return false;
        }
        request.setStatus(ApprovalStatus.REJECTED);
        request.setGovernorRemarks("Cancelled by Administrator before Governor review.");
        request.setApprovalTime(LocalDateTime.now());
        fileManager.saveApprovalRequests(approvalRequests);
        notifyUser(UserRole.GOVERNOR, governor.getGovernorId(), "Approval request " + requestId + " was cancelled by Administrator.");
        audit("Cancelled approval request " + requestId);
        return true;
    }

    public boolean reviewApprovalRequest(String requestId, boolean approve, String remarks, String governorId) {
        ApprovalRequest request = findApprovalRequest(requestId);
        if (request == null || request.getStatus() != ApprovalStatus.PENDING) {
            return false;
        }
        request.setGovernorId(governorId);
        request.setGovernorRemarks(clean(remarks));
        request.setApprovalTime(LocalDateTime.now());
        if (approve) {
            executeApprovedRequest(request);
            request.setStatus(ApprovalStatus.APPROVED);
            notifyUser(UserRole.ADMINISTRATOR, request.getAdministratorId(),
                    "Request " + requestId + " approved by Governor. Remarks: " + clean(remarks));
            audit("Governor approved request " + requestId);
        } else {
            request.setStatus(ApprovalStatus.REJECTED);
            if (request.getRequestType() == ApprovalType.COMMERCIAL_BANK_REGISTRATION_APPROVAL) {
                CommercialBank bank = findBank(request.getTargetId());
                if (bank != null && bank.getStatus() == BankStatus.PENDING_APPROVAL) {
                    bank.setStatus(BankStatus.REJECTED);
                    saveBanks();
                }
            }
            notifyUser(UserRole.ADMINISTRATOR, request.getAdministratorId(),
                    "Request " + requestId + " rejected by Governor. Remarks: " + clean(remarks));
            audit("Governor rejected request " + requestId);
        }
        fileManager.saveApprovalRequests(approvalRequests);
        activity("Governor reviewed " + request.getRequestType().name().replace('_', ' ') + " with status " + request.getStatus());
        return true;
    }

    private void executeApprovedRequest(ApprovalRequest request) {
        Map<String, String> payload = UrlUtil.parseQuery(request.getPayload());
        switch (request.getRequestType()) {
            case COMMERCIAL_BANK_REGISTRATION_APPROVAL:
            case COMMERCIAL_BANK_LICENCE_APPROVAL:
                activateBank(request.getTargetId());
                break;
            case COMMERCIAL_BANK_LICENCE_REVOCATION:
            case COMMERCIAL_BANK_CLOSURE:
                deleteBank(request.getTargetId());
                break;
            case COMMERCIAL_BANK_SUSPENSION:
                suspendBank(request.getTargetId());
                break;
            case MONEY_SUPPLY_ADJUSTMENT:
                executeSupplyMoney(payload.get("bankId"), NumberUtil.parseDouble(payload.get("amount"), 0),
                        OperationType.valueOf(payload.getOrDefault("type", OperationType.INJECTION.name())));
                break;
            case FOREIGN_EXCHANGE_RESERVE_MANAGEMENT:
                updateReserve(NumberUtil.parseDouble(payload.get("nationalReserve"), reserve.getNationalReserve()),
                        NumberUtil.parseDouble(payload.get("minimumReserve"), reserve.getMinimumRequiredReserve()));
                break;
            case MAJOR_MONETARY_POLICY_CHANGE:
            case POLICY_INTEREST_RATE_CHANGE:
            case NATIONAL_BANKING_REGULATION:
                updatePolicy(payload.getOrDefault("policyName", request.getRequestType().name().replace('_', ' ')),
                        NumberUtil.parseDouble(payload.get("interestRate"), 0),
                        NumberUtil.parseDouble(payload.get("reserveRequirement"), 0),
                        payload.getOrDefault("description", request.getDescription()));
                break;
            case NATIONAL_FINANCIAL_REPORT_APPROVAL:
            case FRAUD_INVESTIGATION_DECISION:
            case FINANCIAL_STABILITY_DECISION:
                activity("Executed approved decision: " + request.getDescription());
                break;
            default:
                activity("Approved request recorded: " + request.getDescription());
        }
    }

    private void activateBank(String bankId) {
        CommercialBank bank = findBank(bankId);
        if (bank != null) {
            bank.setStatus(BankStatus.ACTIVE);
            bank.setActivatedOn(LocalDate.now());
            saveBanks();
            notifyUser(UserRole.COMMERCIAL_BANK, bank.getId(), "Commercial bank licence approved. Portal login is now active.");
            audit("Activated commercial bank " + bank.getName());
        }
    }

    public Administrator createAdministrator(String name, String password, String email) {
        Administrator administrator = new Administrator(IdGenerator.next("ADM"), clean(name), clean(password), clean(email),
                true, LocalDate.now(), LocalDate.now());
        administrators.add(administrator);
        fileManager.saveAdministrators(administrators);
        notifyUser(UserRole.ADMINISTRATOR, administrator.getAdministratorId(), "Administrator account created by Governor.");
        audit("Governor created Administrator " + administrator.getName());
        return administrator;
    }

    public void editAdministrator(String administratorId, String name, String email) {
        Administrator administrator = findAdministrator(administratorId);
        if (administrator == null) {
            return;
        }
        administrator.setName(clean(name));
        administrator.setEmail(clean(email));
        fileManager.saveAdministrators(administrators);
        audit("Governor edited Administrator " + administratorId);
    }

    public void changeAdministratorPassword(String administratorId, String password, boolean resetNotice) {
        Administrator administrator = findAdministrator(administratorId);
        if (administrator == null || password == null || password.isBlank()) {
            return;
        }
        administrator.setPassword(password);
        fileManager.saveAdministrators(administrators);
        notifyUser(UserRole.ADMINISTRATOR, administratorId,
                resetNotice ? "Password reset by Governor." : "Password changed by Governor.");
        audit("Governor changed password for Administrator " + administratorId);
    }

    public void setAdministratorEnabled(String administratorId, boolean enabled) {
        Administrator administrator = findAdministrator(administratorId);
        if (administrator == null) {
            return;
        }
        administrator.setEnabled(enabled);
        fileManager.saveAdministrators(administrators);
        notifyUser(UserRole.ADMINISTRATOR, administratorId,
                enabled ? "Administrator account enabled by Governor." : "Administrator account disabled by Governor.");
        audit((enabled ? "Enabled" : "Disabled") + " Administrator " + administratorId);
    }

    public void deleteAdministrator(String administratorId) {
        if (administrators.size() <= 1) {
            return;
        }
        administrators.removeIf(admin -> admin.getAdministratorId().equalsIgnoreCase(administratorId));
        fileManager.saveAdministrators(administrators);
        audit("Governor deleted Administrator " + administratorId);
    }

    public void updateGovernorProfile(String name, String email) {
        governor.setName(clean(name));
        governor.setEmail(clean(email));
        fileManager.saveGovernor(governor);
        audit("Governor profile updated");
    }

    public void changeGovernorPassword(String password) {
        if (password == null || password.isBlank()) {
            return;
        }
        governor.setPassword(password);
        fileManager.saveGovernor(governor);
        audit("Governor password changed");
    }

    public void notifyUser(UserRole role, String recipientId, String message) {
        notifications.add(new Notification(IdGenerator.next("N"), role, recipientId, clean(message), LocalDateTime.now(), false));
        fileManager.saveNotifications(notifications);
    }

    public String generateReport(ReportType type) {
        return reportService.generate(type, banks, customers, accounts, loans, transactions, reserve, getRecentAlerts());
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRateService.currentRates();
    }

    public CommercialBank findBank(String bankId) {
        for (CommercialBank bank : banks) {
            if (bank.getId().equalsIgnoreCase(bankId)) {
                return bank;
            }
        }
        return null;
    }

    public Administrator findAdministrator(String administratorId) {
        for (Administrator administrator : administrators) {
            if (administrator.getAdministratorId().equalsIgnoreCase(administratorId)) {
                return administrator;
            }
        }
        return null;
    }

    public ApprovalRequest findApprovalRequest(String requestId) {
        for (ApprovalRequest request : approvalRequests) {
            if (request.getRequestId().equalsIgnoreCase(requestId)) {
                return request;
            }
        }
        return null;
    }

    public Loan findLoan(String loanId) {
        for (Loan loan : loans) {
            if (loan.getLoanId().equalsIgnoreCase(loanId)) {
                return loan;
            }
        }
        return null;
    }

    public Customer findCustomer(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public Account findAccount(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equalsIgnoreCase(accountId)) {
                return account;
            }
        }
        return null;
    }

    public List<CommercialBank> searchBanks(String keyword) {
        List<CommercialBank> result = new ArrayList<>();
        String search = keyword == null ? "" : keyword.toLowerCase();
        for (CommercialBank bank : banks) {
            if (bank.getName().toLowerCase().contains(search)
                    || bank.getId().toLowerCase().contains(search)
                    || bank.getLicenseNo().toLowerCase().contains(search)) {
                result.add(bank);
            }
        }
        return result;
    }

    public List<CommercialBank> sortBanksByNetWorthDescending() {
        CommercialBank[] array = banks.toArray(new CommercialBank[0]);
        for (int i = 0; i < array.length - 1; i++) {
            int best = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j].getNetWorth() > array[best].getNetWorth()) {
                    best = j;
                }
            }
            CommercialBank temp = array[i];
            array[i] = array[best];
            array[best] = temp;
        }
        return new ArrayList<>(List.of(array));
    }

    public int totalActiveCustomers(String bankId) {
        int count = 0;
        for (Customer customer : customers) {
            if (customer.getBankId().equals(bankId) && customer.getStatus() == CustomerStatus.ACTIVE) {
                count++;
            }
        }
        return count;
    }

    public int totalDormantCustomers(String bankId) {
        int count = 0;
        for (Customer customer : customers) {
            if (customer.getBankId().equals(bankId) && customer.getStatus() == CustomerStatus.DORMANT) {
                count++;
            }
        }
        return count;
    }

    public double totalCustomerDeposits(String bankId) {
        double total = 0;
        for (Account account : accounts) {
            if (account.getBankId().equals(bankId)) {
                total += account.getBalance();
            }
        }
        return total;
    }

    public double outstandingLoans(String bankId) {
        double total = 0;
        for (Loan loan : loans) {
            if (loan.getBankId().equals(bankId)
                    && (loan.getStatus() == LoanStatus.PENDING || loan.getStatus() == LoanStatus.APPROVED
                    || loan.getStatus() == LoanStatus.DISBURSED)) {
                total += loan.getAmount();
            }
        }
        return total;
    }

    public double transactionVolumeForBank(String bankId) {
        double total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getSenderBankId().equals(bankId) || transaction.getReceiverBankId().equals(bankId)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    public List<Transaction> transactionsForBank(String bankId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getSenderBankId().equals(bankId) || transaction.getReceiverBankId().equals(bankId)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public List<Customer> customersForBank(String bankId) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getBankId().equals(bankId)) {
                result.add(customer);
            }
        }
        return result;
    }

    public List<Account> accountsForBank(String bankId) {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getBankId().equals(bankId)) {
                result.add(account);
            }
        }
        return result;
    }

    public List<Loan> loansForBank(String bankId) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBankId().equals(bankId)) {
                result.add(loan);
            }
        }
        return result;
    }

    public List<CommercialBank> activeBanks() {
        List<CommercialBank> active = new ArrayList<>();
        for (CommercialBank bank : banks) {
            if (bank.getStatus() == BankStatus.ACTIVE) {
                active.add(bank);
            }
        }
        return active;
    }

    public List<ApprovalRequest> requestsByStatus(ApprovalStatus status) {
        List<ApprovalRequest> result = new ArrayList<>();
        for (ApprovalRequest request : approvalRequests) {
            if (request.getStatus() == status) {
                result.add(request);
            }
        }
        return result;
    }

    public List<ApprovalRequest> requestsForAdministrator(String administratorId) {
        List<ApprovalRequest> result = new ArrayList<>();
        for (ApprovalRequest request : approvalRequests) {
            if (request.getAdministratorId().equalsIgnoreCase(administratorId)) {
                result.add(request);
            }
        }
        return result;
    }

    public List<Notification> notificationsFor(UserRole role, String recipientId) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientRole() == role && notification.getRecipientId().equalsIgnoreCase(recipientId)) {
                result.add(notification);
            }
        }
        return result;
    }

    public List<String> getRecentAlerts() {
        List<String> alerts = new ArrayList<>();
        for (int i = recentAlerts.size() - 1; i >= 0; i--) {
            alerts.add(recentAlerts.get(i));
        }
        return alerts;
    }

    public void recordLogin(UserRole role, String userId, boolean success, String details) {
        loginLogs.addFirst(LocalDateTime.now().withNano(0) + " - " + role + " - " + userId + " - "
                + (success ? "SUCCESS" : "FAILED") + " - " + clean(details));
        trim(loginLogs, 200);
        fileManager.saveLog("login_logs.csv", loginLogs);
    }

    public void saveAll() {
        saveBanks();
        fileManager.saveCustomers(customers);
        fileManager.saveAccounts(accounts);
        saveLoans();
        fileManager.saveTransactions(transactions);
        saveReserve();
        fileManager.saveOperations(operations);
        fileManager.savePolicies(policies);
        fileManager.saveAdministrators(administrators);
        fileManager.saveGovernor(governor);
        fileManager.saveApprovalRequests(approvalRequests);
        fileManager.saveNotifications(notifications);
        fileManager.saveLog("audit_logs.csv", auditTrail);
        fileManager.saveLog("login_logs.csv", loginLogs);
        fileManager.saveLog("activity_logs.csv", activityLogs);
    }

    private void rebuildPendingLoanQueue() {
        pendingLoanQueue.clear();
        for (Loan loan : loans) {
            if (loan.getStatus() == LoanStatus.PENDING) {
                pendingLoanQueue.offer(loan);
            }
        }
    }

    private void alertIfLowReserve() {
        if (reserve.isLow()) {
            pushAlert("National reserve is below the minimum required level");
        }
    }

    private void pushAlert(String alert) {
        recentAlerts.push(LocalDateTime.now().withNano(0) + " - " + alert);
        while (recentAlerts.size() > 8) {
            recentAlerts.remove(0);
        }
    }

    private void audit(String event) {
        auditTrail.addFirst(LocalDateTime.now().withNano(0) + " - " + event);
        trim(auditTrail, 200);
        fileManager.saveLog("audit_logs.csv", auditTrail);
    }

    private void activity(String event) {
        activityLogs.addFirst(LocalDateTime.now().withNano(0) + " - " + event);
        trim(activityLogs, 200);
        fileManager.saveLog("activity_logs.csv", activityLogs);
    }

    private void trim(LinkedList<String> entries, int max) {
        while (entries.size() > max) {
            entries.removeLast();
        }
    }

    private void saveBanks() {
        fileManager.saveBanks(banks);
    }

    private void saveLoans() {
        fileManager.saveLoans(loans);
    }

    private void saveReserve() {
        fileManager.saveReserve(reserve);
    }

    private String encodePayload(Map<String, String> values) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            joiner.add(encode(entry.getKey()) + "=" + encode(entry.getValue()));
        }
        return joiner.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private String governorNotificationMessage(ApprovalRequest request) {
        String prefix = request.getRequestType() == ApprovalType.COMMERCIAL_BANK_SUSPENSION
                || request.getRequestType() == ApprovalType.COMMERCIAL_BANK_CLOSURE
                || request.getRequestType() == ApprovalType.FRAUD_INVESTIGATION_DECISION
                || request.getRequestType() == ApprovalType.FINANCIAL_STABILITY_DECISION ? "Urgent approval request submitted: " : "New approval request submitted: ";
        return prefix + request.getRequestId() + " - " + request.getRequestType().name().replace('_', ' ');
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public CentralBank getCentralBank() {
        return centralBank;
    }

    public Governor getGovernor() {
        return governor;
    }

    public List<Administrator> getAdministrators() {
        return administrators;
    }

    public List<CommercialBank> getBanks() {
        return banks;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<LiquidityOperation> getOperations() {
        return operations;
    }

    public List<MonetaryPolicy> getPolicies() {
        return policies;
    }

    public List<ApprovalRequest> getApprovalRequests() {
        return approvalRequests;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public Queue<Loan> getPendingLoanQueue() {
        return pendingLoanQueue;
    }

    public List<String> getAuditTrail() {
        return auditTrail;
    }

    public List<String> getLoginLogs() {
        return loginLogs;
    }

    public List<String> getActivityLogs() {
        return activityLogs;
    }

    public String[] getReportCategories() {
        return reportCategories;
    }

    public Reserve getReserve() {
        return reserve;
    }
}
