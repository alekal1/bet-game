package ee.aleksale.betgame.websocket.registry;

import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.contract.KeyValueRegistry;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoundBetRegistry implements KeyValueRegistry<WebSocketSession, PlayerBet> {

    @ThreadSafe
    private final Map<WebSocketSession, PlayerBet> currentRoundBets = new ConcurrentHashMap<>();

    @Override
    public void add(WebSocketSession key, PlayerBet value) {
        currentRoundBets.put(key, value);
    }

    @Override
    public WebSocketSession getKey(PlayerBet value) {
        return currentRoundBets.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue().getUsername(), value.getUsername()))
                .map(Map.Entry::getKey)
                .findAny().orElseThrow();
    }

    @Override
    public void remove(WebSocketSession key) {
        currentRoundBets.remove(key);
    }

    @Override
    public Map<WebSocketSession, PlayerBet> getAll() {
        return currentRoundBets;
    }

    @Override
    public void clear() {
        currentRoundBets.clear();
    }
}
