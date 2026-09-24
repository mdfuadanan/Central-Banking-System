# Central Banking System

A Java-based central banking simulator for **Design Project 1**. The application models central-bank supervision, commercial-bank operations, monetary policy, liquidity management, customer accounts, interbank transfers, approval workflows, risk assessment, fraud detection, reporting, and audit history.

It runs as a lightweight web application using Java's built-in `com.sun.net.httpserver.HttpServer`. Application data is stored in human-readable CSV files, so no external database or runtime framework is required.

## Features

- **Public portal** with monetary indicators, reserve guidance, notices, publications, CIPC information, and USD/EUR exchange rates.
- **Governor portal** for administrator management, bank licensing decisions, monetary policy, reserve management, approval requests, reports, fraud review, and logs.
- **Administrator portal** for commercial-bank supervision, bank applications, loan decisions, liquidity requests, reserve and policy requests, transactions, and reports.
- **Commercial-bank portal** for central-bank loan applications, interbank transfers, customer and account management, deposits, withdrawals, profiles, notifications, and bank-specific reports.
- **Two-stage approval workflow** in which administrator requests are reviewed by the Governor before sensitive actions are executed.
- **Risk assessment** based on reserve ratio, debt-to-asset ratio, credit rating, previous loans, requested amount, and outstanding exposure.
- **Fraud and AML checks** for large transfers, repeated failures, repeated transfers between the same parties, invalid amounts, and negative balances.
- **CSV persistence** with automatic data-directory creation and seed data on first launch.
- **Live exchange-rate retrieval** through ExchangeRate-API, with cached CSV fallback after a failed request.
- **Text reports and audit trails** for banks, loans, transactions, reserves, fraud, investment risk, performance, and system summaries.
- **End-to-end smoke test** covering persistence, HTTP pages, authentication, RBAC, approvals, banking operations, and reports.

## Technology

- Java 25 source level (JDK 17+ is recommended by the project documentation; JDK 25 is required by the Maven compiler configuration).
- Built-in `HttpServer` and Java HTTP Client.
- Server-side HTML/CSS rendering in `PageRenderer`.
- Maven or the included Windows batch scripts.
- CSV flat-file storage under `data/`.
- No third-party runtime framework or external database.

## Repository structure

```text
.
├── src/main/java/central_banking_system/
│   ├── CentralBankingSystemApp.java  # Application entry point and port selection
│   ├── AppRouter.java                # HTTP routes, forms, authentication, and RBAC
│   ├── PageRenderer.java             # Public and authenticated HTML/CSS pages
│   ├── CentralBankSystem.java        # Core domain operations and workflows
│   ├── FileManager.java              # CSV loading, saving, initialization, and seed data
│   ├── RiskAssessment.java           # Bank and loan risk scoring
│   ├── FraudDetector.java            # Transfer anomaly and AML checks
│   ├── ExchangeRateService.java      # Live rates, caching, and offline fallback
│   ├── ReportService.java             # Text report generation
│   ├── LoginManager.java              # Governor, administrator, and bank login checks
│   ├── SessionManager.java            # In-memory HTTP session tokens
│   ├── SystemSmokeTest.java           # Integration and HTTP smoke tests
│   └── ...                            # Domain models and status/type enums
├── src/main/resources/assets/         # Web assets, including the bank logo
├── data/                               # Runtime CSV data and audit logs
├── build/                              # Output from compile.bat and isolated test data
├── target/                             # Maven build output
├── Drawio Files/                       # Activity, class, sequence, and use-case diagrams
├── Screenshots/                        # UI and diagram screenshots
├── Project Reports/                    # Project proposal and final report
├── Presentations/                      # Proposal and final presentations
├── pom.xml                             # Maven configuration
├── compile.bat                         # Windows compilation script
├── run.bat                             # Windows compile-and-run script
└── test.bat                            # Windows smoke-test script
```

## Getting started

### Prerequisites

- JDK 25 for the configured Maven build. The batch/manual compiler path requires a JDK that supports the source syntax used by the project.
- Windows for the `.bat` scripts, or a shell with `javac` and `java` on `PATH` for manual commands.
- Internet access is optional. Without it, the exchange-rate service uses the cached values in `data/exchange_rates.csv`.

### Windows

```bat
compile.bat
run.bat
```

`run.bat` recompiles the project and starts the server. The application tries ports `8080` through `8090` and prints the selected URL in the console.

### Manual compilation and execution

```bash
mkdir -p build
javac -d build src/main/java/central_banking_system/*.java
# Copy the logo/resource directory when using the manual path:
cp -R src/main/resources/* build/  # On Windows, use xcopy /E /I /Y src\main\resources build
java -cp build central_banking_system.CentralBankingSystemApp
```

### Maven

```bash
mvn clean compile
java -cp target/classes central_banking_system.CentralBankingSystemApp
```

The Maven project is configured with `central_banking_system.CentralBankingSystemApp` as its main class, but it does not declare the Maven Exec plugin; use the Java command above to launch after compiling.

## Web portals

Once the server is running, open:

- Public portal: <http://localhost:8080/>
- Login selection: <http://localhost:8080/login>
- Administrator: <http://localhost:8080/admin-login>
- Commercial bank: <http://localhost:8080/commercial-bank-login>
- Governor: <http://localhost:8080/governor-login>

If port 8080 is unavailable, use the port printed by the application.

### Seed credentials

These credentials are development/demo data initialized by `FileManager`; change them before any real deployment.

| Role | ID | Password |
|---|---|---|
| Governor | `GOV001` | `governor123` |
| Administrator | `admin` | `admin123` |
| Delta Commercial Bank | `B001` | `bank123` |
| Padma Islamic Bank | `B002` | `bank123` |
| Meghna Development Bank | `B003` | `bank123` |

The seeded Meghna bank is suspended and cannot log in until its status is changed through the application workflow.

## Data persistence

On startup, `FileManager` creates `data/` and initializes missing CSV files with sample records. The files represent:

- Banks, administrators, governor, customers, and accounts.
- Loans, interbank transactions, liquidity operations, and reserves.
- Monetary policies and exchange rates.
- Approval requests and notifications.
- Audit, login, and activity logs.

The application writes changes directly to these files. Keep backups of `data/` when experimenting with stateful workflows. Smoke tests use the isolated `build/test-data/` directory instead of the normal runtime data directory.

## Testing

Run the smoke suite on Windows with:

```bat
test.bat
```

Or run it manually after compilation:

```bash
java -cp build central_banking_system.SystemSmokeTest
```

The test starts an ephemeral HTTP server and verifies the public homepage, login pages, role boundaries, session cookies, administrator and bank portals, Governor approval of a new bank, banking operations, persistence initialization, and report generation.

## Design documentation

- [Final project report](Project%20Reports/Final_Report_Design_Project_1.pdf)
- [Project proposal](Project%20Reports/Project_Proposal.docx)
- [Activity diagram](Drawio%20Files/Activity_Diagram.drawio)
- [Class diagram](Drawio%20Files/Class_Diagram.drawio)
- [Sequence diagram](Drawio%20Files/Sequence%20Diagram.drawio)
- [Use-case diagram](Drawio%20Files/Use%20Case%20Diagram.drawio)
- [Screenshots](Screenshots/)
- [Presentations](Presentations/)

## Security and scope notice

This is an educational simulator, not production banking software. Passwords are stored in the CSV data files, sessions are held in memory, and the built-in HTTP server is intended for local demonstration. Do not expose the application to an untrusted network or use real customer information.

## Project context

This repository is a group project for the **Design Project 1** course. It is primarily Java, with Windows batch files for convenience and supporting diagrams, reports, presentations, screenshots, and web assets.
