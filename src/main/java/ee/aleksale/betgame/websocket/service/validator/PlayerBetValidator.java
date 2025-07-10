package ee.aleksale.betgame.websocket.service.validator;

import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.websocket.config.GameProperties;
import ee.aleksale.betgame.websocket.model.BetValidationResults;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.service.managers.RoundStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerBetValidator {

    private final PlayerRepository playerRepository;
    private final RoundBetRegistry roundBetRegistry;
    private final RoundStateManager roundStateManager;

    public BetValidationResults isValidBet(PlayerBet playerBet) {
        if (!roundStateManager.getIsRoundRunning().get()) {
            return BetValidationResults.error("| Round is not running |");
        }

        if (hasPlayerAlreadyBet(playerBet)) {
            return BetValidationResults.error("| You have already bet! |");
        }

        if (!hasValidAmount(playerBet)) {
            return BetValidationResults.error("| Bet amount exceeding your total amount |");
        }

        if (!isValidNumber(playerBet.getNumber())) {
            return BetValidationResults.error("| You can place to a number in range from 0 to 10 |");
        }

        return BetValidationResults.success();
    }

    private boolean hasPlayerAlreadyBet(PlayerBet playerBet) {
        return roundBetRegistry.getAll().values().stream()
                .anyMatch(bet -> playerBet.getUsername().equals(bet.getUsername()));
    }

    private boolean hasValidAmount(PlayerBet playerBet) {
        var player = playerRepository.findByUsername(playerBet.getUsername()).orElseThrow();

        return player.getTotalAmount() >= playerBet.getAmount();
    }

    private boolean isValidNumber(int number) {
        return number >= 0 && number <= 10;
    }
}
