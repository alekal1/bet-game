package ee.aleksale.betgame.websocket.model;

import java.util.List;
import java.util.stream.Collectors;

public interface GameMessage {

    record RoundStartMessage(int duration) implements GameMessage {
        @Override
        public String toString() {
            return String.format("| New round started | Players now have %d seconds to place their bets |", duration);
        }
    }

    record RoundEndMessage(int winningNumber) implements GameMessage {
        @Override
        public String toString() {
            return String.format("| Round has ended! Winning number was %d|", winningNumber);
        }
    }

    record WinMessage(double amount) implements GameMessage {
        @Override
        public String toString() {
            return String.format("| Congratulations! You won %.2f |", amount);
        }
    }

    record LoseMessage() implements GameMessage {
        @Override
        public String toString() {
            return "| You lose. Better luck next time! |";
        }
    }

    record WinnersMessage(List<RoundWinner> winners) implements GameMessage {
        @Override
        public String toString() {
            if (winners.isEmpty()) {
                return "| No winners this round |";
            }
            return "| Winners: " + winners.stream()
                    .map(RoundWinner::toString)
                    .collect(Collectors.joining("; "));
        }
    }

    record BetAcceptedMessage(PlayerBet bet) implements GameMessage {
        @Override
        public String toString() {
            return String.format("| Bet accepted: number %d, amount %.2f |", bet.getNumber(), bet.getAmount());
        }
    }

    record WarningMessage(String message) implements GameMessage {
        @Override
        public String toString() {
            return message;
        }
    }
}


