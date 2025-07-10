package ee.aleksale.betgame.common.exception;

import ee.aleksale.betgame.common.exception.contract.GameException;

public class GameSystemException extends GameException {

    public GameSystemException(String message) {
        super(message);
    }
}
