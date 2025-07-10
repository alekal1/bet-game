package ee.aleksale.betgame.websocket.service.generator;

import ee.aleksale.betgame.websocket.service.generator.contract.WinningNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class WinningNumberMax10Generator implements WinningNumberGenerator {

    @Override
    public int generateWinningNumber() {
        return ThreadLocalRandom.current().nextInt(11);
    }
}
