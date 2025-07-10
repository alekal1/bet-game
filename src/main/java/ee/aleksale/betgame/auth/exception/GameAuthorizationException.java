package ee.aleksale.betgame.auth.exception;

import ee.aleksale.betgame.common.exception.contract.GameException;

public class GameAuthorizationException extends GameException {

    public GameAuthorizationException(String message) {
        super(message);
    }
}
