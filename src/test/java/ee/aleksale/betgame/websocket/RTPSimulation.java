package ee.aleksale.betgame.websocket;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RTPSimulation {
    private final Logger log = LoggerFactory.getLogger(RTPSimulation.class);

    private static final int TOTAL_ROUNDS = Integer.parseInt(System.getProperty("totalRounds"));
    private static final int THREAD_COUNT = 24;

    private static final double WIN_MULTIPLIER = Double.parseDouble(System.getProperty("winMultiplier"));
    private static final double PLAYERS_BET = Double.parseDouble(System.getProperty("playersBet"));

    @Test
    void simulation() throws Exception {

        var roundsPerThread = TOTAL_ROUNDS / THREAD_COUNT;
        var spentTotal = new AtomicReference<>(0.0);
        var wonTotal = new AtomicReference<>(0.0);

        List<Callable<Void>> batchRounds = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            batchRounds.add(getBatchRounds(roundsPerThread, spentTotal, wonTotal));
        }

        var executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.invokeAll(batchRounds);
        executorService.shutdown();

        double rtp = ((double) wonTotal.get() / spentTotal.get()) * 100;
        printResults(spentTotal.get(), wonTotal.get(), rtp);
    }

    private void printResults(double spentTotal, double wonTotal, double rtp) {
        log.info("Rounds total: " + TOTAL_ROUNDS);
        log.info("Win multiplier: " + WIN_MULTIPLIER);
        log.info("Player's bet: " + PLAYERS_BET);
        log.info("Spent total: " + spentTotal);
        log.info("Won total: " + wonTotal);
        log.info("RTP: " + rtp);
    }

    private Callable<Void> getBatchRounds(int roundsInBatch, AtomicReference<Double> spentTotal, AtomicReference<Double> wonTotal) {
        return () -> {
            double batchSpent = 0.0;
            double batchWon = 0.0;

            for (int i = 0; i < roundsInBatch; i++) {
                var winningNumber = ThreadLocalRandom.current().nextInt(11);
                int playerBetNumber = ThreadLocalRandom.current().nextInt(11);

                batchSpent += PLAYERS_BET;

                if (playerBetNumber == winningNumber) {
                    batchWon += PLAYERS_BET * WIN_MULTIPLIER;
                }
            }

            double finalBatchSpent = batchSpent;
            double finalBatchWon = batchWon;

            spentTotal.updateAndGet(value -> value + finalBatchSpent);
            wonTotal.updateAndGet(value -> value + finalBatchWon);
            return null;
        };
    }
}
