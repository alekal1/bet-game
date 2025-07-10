package ee.aleksale.betgame.websocket.registry;

import ee.aleksale.betgame.websocket.registry.contract.SetRegistry;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry implements SetRegistry<WebSocketSession> {

    @ThreadSafe
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void add(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void remove(WebSocketSession session) {
        sessions.remove(session);
    }

    @Override
    public Set<WebSocketSession> getAll() {
        return Collections.unmodifiableSet(sessions);
    }

    @Override
    public void clear() {
        sessions.clear();
    }
}
