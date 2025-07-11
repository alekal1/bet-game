package ee.aleksale.betgame.websocket.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebSocketWarnings {

    public static final String ROUND_IS_RUNNING = "| Round is not running |";
    public static final String ALREADY_BET = "| You have already bet! |";
    public static final String BET_AMOUNT_EXCEEDED = "| Bet amount exceeding your total amount |";
    public static final String NOT_VALID_NUMBER = "| You can place to a number in range from 0 to 10 |";
}
