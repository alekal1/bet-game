package ee.aleksale.betgame.unit.websocket.service.generator;

import ee.aleksale.betgame.websocket.config.GameProperties;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.service.generator.MultiplierWinningAmountGenerator;
import ee.aleksale.betgame.websocket.service.generator.contract.WinningAmountGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MultiplierWinningAmountGeneratorTest {

    private GameProperties gameProperties;

    private WinningAmountGenerator winningAmountGenerator;

    @BeforeEach
    void init() {
        gameProperties = mock(GameProperties.class);

        winningAmountGenerator = new MultiplierWinningAmountGenerator(
                gameProperties
        );
    }

    private static Stream<Arguments> provideMultiplierConfigArguments() {
        return Stream.of(
                Arguments.of(0.01),
                Arguments.of(1.0),
                Arguments.of(1000.0),
                Arguments.of(3.14)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMultiplierConfigArguments")
    void generateWinningAmount(double multiplier) {
        doReturn(multiplier)
                .when(gameProperties)
                .getWinMultiplier();

        var playerBet = new PlayerBet();
        playerBet.setAmount(10.0);

        var result = winningAmountGenerator.generateWinningAmount(playerBet);

        assertEquals(playerBet.getAmount(), result / multiplier);
    }

}
