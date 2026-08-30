package central_banking_system;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CentralBankingSystemApp {
    public static void main(String[] args) throws IOException {
        Path dataDirectory = Paths.get("data").toAbsolutePath().normalize();
        CentralBankSystem system = new CentralBankSystem(new FileManager(dataDirectory));
        HttpServer server = createServer(system);
        int port = server.getAddress().getPort();
        server.start();
        System.out.println("Central Banking System is online.");
        System.out.println("Open: http://localhost:" + port);
        System.out.println("Governer: http://localhost:" + port + "/governor-login");
        System.out.println("Governor login: GOV001 / governor123");
        System.out.println("Administrator login: admin / admin123");
        System.out.println("Commercial Bank login: B001 / bank123");
    }

    private static HttpServer createServer(CentralBankSystem system) throws IOException {
        IOException last = null;
        for (int port = 8080; port <= 8090; port++) {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                server.createContext("/", new AppRouter(system));
                server.setExecutor(null);
                return server;
            } catch (IOException ex) {
                last = ex;
            }
        }
        throw last == null ? new IOException("No available port") : last;
    }
}
