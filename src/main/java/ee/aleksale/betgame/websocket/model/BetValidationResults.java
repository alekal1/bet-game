package ee.aleksale.betgame.websocket.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BetValidationResults {
    private final boolean isValid;
    private final String errorMessage;

    public static BetValidationResults success() {
        return new BetValidationResults(true, null);
    }

    public static BetValidationResults error(String message) {
        return new BetValidationResults(false, message);
    }
}
