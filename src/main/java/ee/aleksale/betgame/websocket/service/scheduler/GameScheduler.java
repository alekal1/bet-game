package ee.aleksale.betgame.websocket.service.scheduler;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class GameScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ScheduledFuture<?> scheduleTask(Runnable runnable, long delay) {
        return scheduler.schedule(
                runnable,
                delay,
                TimeUnit.SECONDS
        );
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
