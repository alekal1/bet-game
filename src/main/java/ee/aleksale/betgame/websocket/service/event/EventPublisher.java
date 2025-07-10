package ee.aleksale.betgame.websocket.service.event;

import ee.aleksale.betgame.common.utils.JsonUtils;
import ee.aleksale.betgame.websocket.model.GameMessage;
import ee.aleksale.betgame.websocket.registry.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final SessionRegistry sessionRegistry;

    public void publishToSession(WebSocketSession session, GameMessage message) {
        publishToSession(session, message.toString());
    }

    public void publishToAllSessions(GameMessage message) {
        sessionRegistry.getAll().forEach(session -> publishToSession(session, message.toString()));
    }

    private void publishToSession(WebSocketSession session, String payload) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(payload));
            }
        } catch (IOException e) {
            log.error("Failed to send message to session {}", session.getId(), e);
        }
    }
}
