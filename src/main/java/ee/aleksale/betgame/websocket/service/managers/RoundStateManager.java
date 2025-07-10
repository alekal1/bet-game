package ee.aleksale.betgame.websocket.service.managers;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class RoundStateManager {

    @Getter
    private final AtomicBoolean isRoundRunning = new AtomicBoolean(false);

    public void startNewRound() {
        isRoundRunning.set(true);
    }

    public void endCurrentRound() {
        isRoundRunning.set(false);
    }
}
