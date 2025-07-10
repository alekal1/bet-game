package ee.aleksale.betgame.websocket.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("game.properties")
public class GameProperties {
    private double winMultiplier;
    private int roundDurationInSeconds;
}
