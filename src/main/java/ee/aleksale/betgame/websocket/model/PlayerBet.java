package ee.aleksale.betgame.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBet {
    @JsonIgnore
    private String username;

    private int number;
    private double amount;
}
