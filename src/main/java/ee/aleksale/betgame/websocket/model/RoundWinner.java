package ee.aleksale.betgame.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundWinner {
    private String username;
    private double winAmount;

    @Override
    public String toString() {
        return String.format("%s - %.2f", username, winAmount);
    }
}
