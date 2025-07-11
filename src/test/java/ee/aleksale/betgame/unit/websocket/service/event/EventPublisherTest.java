package ee.aleksale.betgame.unit.websocket.service.event;

import ee.aleksale.betgame.websocket.model.GameMessage;
import ee.aleksale.betgame.websocket.registry.SessionRegistry;
import ee.aleksale.betgame.unit.websocket.stub.WebSocketSessionStub;
import ee.aleksale.betgame.websocket.service.event.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    private SessionRegistry sessionRegistry;

    private EventPublisher eventPublisher;

    @BeforeEach
    void init() {
        sessionRegistry = mock(SessionRegistry.class);

        eventPublisher = new EventPublisher(sessionRegistry);
    }

    @Test
    void publishToSession_closedSession() {
        var session = new WebSocketSessionStub();
        session.close();

        eventPublisher.publishToSession(session, new GameMessage.LoseMessage());

        assertTrue(session.getMessages().isEmpty());
    }

    @Test
    void publishToSession_openSession() {
        var session = new WebSocketSessionStub();
        session.open();

        eventPublisher.publishToSession(session, new GameMessage.LoseMessage());

        assertFalse(session.getMessages().isEmpty());
    }

    @Test
    void publishToAllSession_oneIsClosed() {
        var openSession = new WebSocketSessionStub();
        openSession.open();

        var closedSession = new WebSocketSessionStub();
        closedSession.close();

        doReturn(Set.of(openSession, closedSession))
                .when(sessionRegistry)
                .getAll();

        eventPublisher.publishToAllSessions(new GameMessage.LoseMessage());

        assertFalse(openSession.getMessages().isEmpty());
        assertTrue(closedSession.getMessages().isEmpty());
    }
}
