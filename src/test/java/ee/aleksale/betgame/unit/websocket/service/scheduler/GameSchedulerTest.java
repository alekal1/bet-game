package ee.aleksale.betgame.unit.websocket.service.scheduler;

import ee.aleksale.betgame.websocket.service.scheduler.GameScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSchedulerTest {

    private static final Integer DELAY_IN_SECONDS = 2;

    private GameScheduler gameScheduler;

    @BeforeEach
    void init() {
        gameScheduler = new GameScheduler();
    }

    @Test
    void scheduleTask() throws Exception {
        long startTime = System.currentTimeMillis();

        var schedule = gameScheduler.scheduleTask(
                System.out::println,
                DELAY_IN_SECONDS);
        schedule.get();

        long elapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime);

        assertTrue(elapsed <= DELAY_IN_SECONDS);
    }
}
