package central_banking_system;


import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FraudDetector {
    private static final double LARGE_TRANSFER_LIMIT = 500000;

    public List<String> detect(Transaction transaction, List<Transaction> history, CommercialBank sender) {
        List<String> warnings = new ArrayList<>();
        if (transaction.getAmount() <= 0) {
            warnings.add("Suspicious transfer: amount is zero or negative");
        }
        if (transaction.getAmount() >= LARGE_TRANSFER_LIMIT) {
            warnings.add("Very large transfer");
        }
        if (sender != null && sender.getCurrentBalance() < 0) {
            warnings.add("Negative bank balance after transfer");
        }
        if (countRecentFailures(transaction.getSenderBankId(), history) >= 2) {
            warnings.add("Repeated failed transfers from same bank");
        }
        if (countSimilarRecentTransfers(transaction, history) >= 2) {
            warnings.add("Possible structuring: repeated transfers to same receiver");
        }
        return warnings;
    }

    private int countRecentFailures(String bankId, List<Transaction> history) {
        Queue<Transaction> recent = recentTransactions(history, 10);
        int count = 0;
        while (!recent.isEmpty()) {
            Transaction transaction = recent.poll();
            if (transaction.getSenderBankId().equals(bankId) && transaction.getStatus() == TransactionStatus.FAILED) {
                count++;
            }
        }
        return count;
    }

    private int countSimilarRecentTransfers(Transaction current, List<Transaction> history) {
        Queue<Transaction> recent = recentTransactions(history, 15);
        int count = 0;
        while (!recent.isEmpty()) {
            Transaction transaction = recent.poll();
            boolean sameParties = transaction.getSenderBankId().equals(current.getSenderBankId())
                    && transaction.getReceiverBankId().equals(current.getReceiverBankId());
            boolean sameDay = transaction.getDateTime().isAfter(LocalDateTime.now().minusDays(1));
            if (sameParties && sameDay && transaction.getAmount() >= 100000) {
                count++;
            }
        }
        return count;
    }

    private Queue<Transaction> recentTransactions(List<Transaction> history, int limit) {
        Queue<Transaction> queue = new ArrayDeque<>();
        int start = Math.max(0, history.size() - limit);
        for (int i = start; i < history.size(); i++) {
            queue.offer(history.get(i));
        }
        return queue;
    }
}
