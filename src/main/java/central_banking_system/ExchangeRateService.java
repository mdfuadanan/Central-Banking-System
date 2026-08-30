package central_banking_system;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExchangeRateService {
    private static final String API_BASE = "https://open.er-api.com/v6/latest/";
    private static final double DISPLAY_SPREAD = 0.005;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration RETRY_AFTER_FAILURE = Duration.ofMinutes(5);

    private final FileManager fileManager;
    private final HttpClient httpClient;
    private LocalDateTime lastFailedAttempt;
    private List<ExchangeRate> memoryCache;

    public ExchangeRateService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
    }

    public List<ExchangeRate> currentRates() {
        List<ExchangeRate> cached = memoryCache == null ? fileManager.loadExchangeRates() : memoryCache;
        if (shouldSkipLiveAttempt()) {
            return cachedAsOffline(cached);
        }

        try {
            List<ExchangeRate> liveRates = fetchLiveRates();
            fileManager.saveExchangeRates(liveRates);
            memoryCache = liveRates;
            lastFailedAttempt = null;
            return liveRates;
        } catch (RuntimeException ex) {
            lastFailedAttempt = LocalDateTime.now();
            memoryCache = cached;
            return cachedAsOffline(cached);
        }
    }

    private boolean shouldSkipLiveAttempt() {
        return lastFailedAttempt != null
                && LocalDateTime.now().isBefore(lastFailedAttempt.plus(RETRY_AFTER_FAILURE));
    }

    private List<ExchangeRate> fetchLiveRates() {
        List<ExchangeRate> rates = new ArrayList<>();
        rates.add(fetchRate("USD", "USD Exchange Rate"));
        rates.add(fetchRate("EUR", "EUR Exchange Rate"));
        return rates;
    }

    private ExchangeRate fetchRate(String currencyCode, String currencyName) {
        String json = fetchJson(API_BASE + currencyCode);
        double reference = parseBdtRate(json);
        String updated = parseUpdatedTime(json);
        double buying = reference * (1 - DISPLAY_SPREAD);
        double selling = reference * (1 + DISPLAY_SPREAD);
        return new ExchangeRate(currencyCode, currencyName, reference, buying, selling, updated,
                "ExchangeRate-API Open Access", true);
    }

    private String fetchJson(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(REQUEST_TIMEOUT)
                    .header("User-Agent", "Central-Banking-System-Version-5")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("Exchange rate API returned HTTP " + response.statusCode());
            }
            return response.body();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not fetch exchange rates", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Exchange rate fetch was interrupted", ex);
        }
    }

    private double parseBdtRate(String json) {
        Matcher matcher = Pattern.compile("\"BDT\"\\s*:\\s*([0-9]+(?:\\.[0-9]+)?)").matcher(json);
        if (!matcher.find()) {
            throw new IllegalStateException("BDT rate not found in exchange rate response");
        }
        return Double.parseDouble(matcher.group(1));
    }

    private String parseUpdatedTime(String json) {
        Matcher matcher = Pattern.compile("\"time_last_update_utc\"\\s*:\\s*\"([^\"]+)\"").matcher(json);
        if (matcher.find()) {
            return matcher.group(1).replace("+0000", "UTC");
        }
        Matcher unixMatcher = Pattern.compile("\"time_last_update_unix\"\\s*:\\s*([0-9]+)").matcher(json);
        if (unixMatcher.find()) {
            return "Unix time " + unixMatcher.group(1);
        }
        return LocalDateTime.now().withNano(0) + " UTC";
    }

    private List<ExchangeRate> cachedAsOffline(List<ExchangeRate> cached) {
        List<ExchangeRate> offline = new ArrayList<>();
        for (ExchangeRate rate : cached) {
            offline.add(rate.withLive(false));
        }
        return offline;
    }
}
