package ee.aleksale.betgame.unit.websocket.service.validator;

import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.service.managers.RoundStateManager;
import ee.aleksale.betgame.unit.websocket.stub.WebSocketSessionStub;
import ee.aleksale.betgame.websocket.service.validator.PlayerBetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    private static Stream<Arguments> notValidBetsArguments() {
        return Stream.of(
                Arguments.of(new PlayerBet("", -0, 10)),
                Arguments.of(new PlayerBet("", -1, 10)),
                Arguments.of(new PlayerBet("", 11, 10)),
                Arguments.of(new PlayerBet("", 1, -0)),
                Arguments.of(new PlayerBet("", 1, -1))
        );
    }

    @ParameterizedTest
    @MethodSource("notValidBetsArguments")
    void notValidBets(PlayerBet playerBet) {
        var playerEntity = new PlayerEntity();
        playerEntity.setTotalAmount(0.0);
        playerEntity.setUsername("");

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Optional.of(playerEntity))
                .when(playerRepository)
                .findByUsername(playerEntity.getUsername());

        var result = validator.isValidBet(playerBet);

        assertFalse(result.isValid());
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
    void isValidBet_notValidAmount() {
        var playerBet = getPlayerBet();
        playerBet.setAmount(-1);

        doReturn(new AtomicBoolean(true))
                .when(roundStateManager)
                .getIsRoundRunning();

        doReturn(Map.of(new WebSocketSessionStub(), new PlayerBet()))
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
