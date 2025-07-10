package ee.aleksale.betgame.common.exception.contract;

import lombok.Getter;

public abstract class GameException extends IllegalArgumentException {
    @Getter
    final String message;

    public GameException(String message) {
        this.message = message;
    }
}
