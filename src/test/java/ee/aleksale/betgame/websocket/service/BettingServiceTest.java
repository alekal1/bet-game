package ee.aleksale.betgame.websocket.service;

import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.stub.WebSocketSessionStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BettingServiceTest {

    private RoundBetRegistry roundBetRegistry;

    private BettingService bettingService;

    @BeforeEach
    void init() {
        roundBetRegistry = mock(RoundBetRegistry.class);

        bettingService = new BettingService(
                mock(PlayerRepository.class),
                roundBetRegistry
        );
    }

    @Test
    void getWinBets_oneWinner() {
        var winningNumber = 1;

        var playerBet1 = getPlayerBet(winningNumber);
        var playerBet2 = getPlayerBet(winningNumber + 1);

        doReturn(Map.of(
                new WebSocketSessionStub(), playerBet1,
                new WebSocketSessionStub(), playerBet2))
                .when(roundBetRegistry)
                .getAll();

        var winBets = bettingService.getWinBets(winningNumber);

        assertFalse(winBets.isEmpty());
        assertEquals(1, winBets.size());
    }

    @Test
    void getWinBets_noWinners() {
        var winningNumber = 1;
        var playerBet = getPlayerBet(winningNumber + 1);

        doReturn(Map.of(new WebSocketSessionStub(), playerBet))
                .when(roundBetRegistry)
                .getAll();

        var winBets = bettingService.getWinBets(winningNumber);

        assertTrue(winBets.isEmpty());
    }

    @Test
    void getLoseBets_oneLooser() {
        var winningNumber = 1;

        var playerBet1 = getPlayerBet(winningNumber);
        var playerBet2 = getPlayerBet(winningNumber + 1);

        doReturn(Map.of(
                new WebSocketSessionStub(), playerBet1,
                new WebSocketSessionStub(), playerBet2))
                .when(roundBetRegistry)
                .getAll();

        var winBets = bettingService.getLoseBets(winningNumber);

        assertFalse(winBets.isEmpty());
        assertEquals(1, winBets.size());
    }

    @Test
    void getLoseBets_noLosers() {
        var winningNumber = 1;
        var playerBet = getPlayerBet(winningNumber);

        doReturn(Map.of(new WebSocketSessionStub(), playerBet))
                .when(roundBetRegistry)
                .getAll();

        var winBets = bettingService.getLoseBets(winningNumber);

        assertTrue(winBets.isEmpty());
    }

    private PlayerBet getPlayerBet(int number) {
        var bet = new PlayerBet();
        bet.setUsername("anyUsername");
        bet.setAmount(10.0);
        bet.setNumber(number);
        return bet;
    }

}
