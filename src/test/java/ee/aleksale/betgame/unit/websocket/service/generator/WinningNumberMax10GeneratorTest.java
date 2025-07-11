package ee.aleksale.betgame.unit.websocket.service.generator;

import ee.aleksale.betgame.websocket.service.generator.WinningNumberMax10Generator;
import ee.aleksale.betgame.websocket.service.generator.contract.WinningNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WinningNumberMax10GeneratorTest {

    WinningNumberGenerator winningNumberGenerator;

    @BeforeEach
    void init() {
        winningNumberGenerator = new WinningNumberMax10Generator();
    }

    @RepeatedTest(10)
    void generateWinningNumber() {
        var randomNumber = winningNumberGenerator.generateWinningNumber();
        assertTrue(randomNumber <= 10);
        assertTrue(randomNumber >= 0);
    }
}
