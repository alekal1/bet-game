package ee.aleksale.betgame.websocket.service.generator;

import ee.aleksale.betgame.websocket.config.GameProperties;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.service.generator.contract.WinningAmountGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultiplierWinningAmountGenerator implements WinningAmountGenerator {

    private final GameProperties gameProperties;

    @Override
    public double generateWinningAmount(PlayerBet playerBet) {
        return playerBet.getAmount() * gameProperties.getWinMultiplier();
    }
}
