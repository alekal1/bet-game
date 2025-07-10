package ee.aleksale.betgame.websocket.service.validator;

import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.service.managers.RoundStateManager;
import ee.aleksale.betgame.websocket.stub.WebSocketSessionStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PlayerBetValidatorTest {

    private PlayerRepository playerRepository;
    private RoundBetRegistry roundBetRegistry;
    private RoundStateManager roundStateManager;

    private PlayerBetValidator validator;

    @BeforeEach
    void init() {
        playerRepository = mock(PlayerRepository.class);
        roundBetRegistry = mock(RoundBetRegistry.class);
        roundStateManager = mock(RoundStateManager.class);

        validator = new PlayerBetValidator(
                playerRepository,
                roundBetRegistry,
                roundStateManager
        );
    }

    @Test
    void isValidBet_roundIsNotRunning() {
        var playerBet = getPlayerBet();

        doReturn(new AtomicBoolean(false))
                .when(roundStateManager)
                .getIsRoundRunning();

        var result = validator.isValidBet(playerBet);

        assertFalse(result.isValid());
    }

    @Test
    void isValidBet_alreadyBet() {
        var playerBet = getPlayerBet();

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Map.of(new WebSocketSessionStub(), playerBet))
                .when(roundBetRegistry)
                .getAll();

        var result = validator.isValidBet(playerBet);

        assertFalse(result.isValid());
    }

    @Test
    void isValidBet_totalAmountExceeded() {
        var playerBet = getPlayerBet();
        var playerEntity = new PlayerEntity();
        playerEntity.setTotalAmount(0.0);

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Map.of(new WebSocketSessionStub(), new PlayerBet()))
                .when(roundBetRegistry)
                .getAll();

        doReturn(Optional.of(playerEntity))
                .when(playerRepository)
                .findByUsername(playerBet.getUsername());

        var result = validator.isValidBet(playerBet);

        assertFalse(result.isValid());
    }

    @Test
    void isValidBet_invalidNumber() {
        var playerBet = getPlayerBet();
        playerBet.setNumber(11);

        var playerEntity = new PlayerEntity();
        playerEntity.setTotalAmount(110.0);

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Map.of(new WebSocketSessionStub(), new PlayerBet()))
                .when(roundBetRegistry)
                .getAll();

        doReturn(Optional.of(playerEntity))
                .when(playerRepository)
                .findByUsername(playerBet.getUsername());

        var result = validator.isValidBet(playerBet);

        assertFalse(result.isValid());
    }

    @Test
    void isValidBet() {
        var playerBet = getPlayerBet();

        var playerEntity = new PlayerEntity();
        playerEntity.setTotalAmount(110.0);

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Map.of(new WebSocketSessionStub(), new PlayerBet()))
                .when(roundBetRegistry)
                .getAll();

        doReturn(Optional.of(playerEntity))
                .when(playerRepository)
                .findByUsername(playerBet.getUsername());

        var result = validator.isValidBet(playerBet);

        assertTrue(result.isValid());
    }

    private PlayerBet getPlayerBet() {
        var bet = new PlayerBet();
        bet.setUsername("anyUsername");
        bet.setAmount(10.0);
        bet.setNumber(1);
        return bet;
    }
}
