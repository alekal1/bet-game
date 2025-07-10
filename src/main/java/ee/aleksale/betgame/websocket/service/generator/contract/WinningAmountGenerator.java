package ee.aleksale.betgame.websocket.service.generator.contract;

import ee.aleksale.betgame.websocket.model.PlayerBet;

public interface WinningAmountGenerator {

    double generateWinningAmount(PlayerBet playerBet);
}
