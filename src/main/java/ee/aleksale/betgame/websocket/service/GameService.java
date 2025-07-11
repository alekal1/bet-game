package ee.aleksale.betgame.websocket.service;

import ee.aleksale.betgame.websocket.config.GameProperties;
import ee.aleksale.betgame.websocket.model.GameMessage;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.model.RoundWinner;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.service.event.EventPublisher;
import ee.aleksale.betgame.websocket.service.generator.contract.WinningAmountGenerator;
import ee.aleksale.betgame.websocket.service.generator.contract.WinningNumberGenerator;
import ee.aleksale.betgame.websocket.service.managers.RoundStateManager;
import ee.aleksale.betgame.websocket.service.scheduler.GameScheduler;
import ee.aleksale.betgame.websocket.service.validator.PlayerBetValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final RoundBetRegistry roundBetRegistry;
    private final RoundStateManager roundStateManager;

    private final EventPublisher eventPublisher;
    private final GameScheduler gameScheduler;

    private final PlayerBetValidator playerBetValidator;
    private final BettingService bettingService;

    private final GameProperties gameProperties;

    private final WinningNumberGenerator winningNumberGenerator;
    private final WinningAmountGenerator winningAmountGenerator;

    @PostConstruct
    public void startTheGame() {
        startRound();
    }

    public void startRound() {
        if (!roundStateManager.getIsRoundRunning().get()) {
            roundStateManager.startNewRound();

            eventPublisher.publishToAllSessions(
                    new GameMessage.RoundStartMessage(gameProperties.getRoundDurationInSeconds()));

            gameScheduler.scheduleTask(this::endRound, gameProperties.getRoundDurationInSeconds());
        }
    }

    private void endRound() {
        if (roundStateManager.getIsRoundRunning().get()) {
            roundStateManager.endCurrentRound();

            int winningNumber = winningNumberGenerator.generateWinningNumber();
            eventPublisher.publishToAllSessions(new GameMessage.RoundEndMessage(winningNumber));

            var winnersList = notifyWinnersAndGetAll(winningNumber);
            notifyLoosers(winningNumber);

            eventPublisher.publishToAllSessions(new GameMessage.WinnersMessage(winnersList));

            roundBetRegistry.clear();
            gameScheduler.scheduleTask(this::startRound, 1L);
        }
    }

    public void handleMessage(WebSocketSession session, PlayerBet playerBet) {
        var betValidationResult = playerBetValidator.isValidBet(playerBet);

        if (!betValidationResult.isValid()) {
            eventPublisher.publishToSession(session, new GameMessage.WarningMessage(betValidationResult.getErrorMessage()));
            return;
        }

        bettingService.removeAmountFromPlayer(playerBet);
        roundBetRegistry.add(session, playerBet);

        eventPublisher.publishToSession(session, new GameMessage.BetAcceptedMessage(playerBet));
    }

    private List<RoundWinner> notifyWinnersAndGetAll(int winningNumber) {
        var winnersList = new ArrayList<RoundWinner>();

        bettingService.getWinBets(winningNumber)
                .forEach(winner -> {
                    double winningAmount = winningAmountGenerator.generateWinningAmount(winner);

                    eventPublisher.publishToSession(
                            roundBetRegistry.getKey(winner),
                            new GameMessage.WinMessage(winningAmount));

                    bettingService.addAmountToPlayer(winner.getUsername(), winningAmount);
                    winnersList.add(new RoundWinner(winner.getUsername(), winningAmount));
                });
        return winnersList;
    }

    private void notifyLoosers(int winningNumber) {
        bettingService.getLoseBets(winningNumber)
                .forEach(looser -> eventPublisher.publishToSession(
                        roundBetRegistry.getKey(looser),
                        new GameMessage.LoseMessage()
                ));
    }
}
