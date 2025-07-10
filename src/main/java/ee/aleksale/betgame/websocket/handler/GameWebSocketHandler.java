package ee.aleksale.betgame.websocket.handler;

import ee.aleksale.betgame.common.utils.JsonUtils;
import ee.aleksale.betgame.websocket.interceptor.AuthHandshakeInterceptor;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.registry.RoundBetRegistry;
import ee.aleksale.betgame.websocket.registry.SessionRegistry;
import ee.aleksale.betgame.websocket.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameService gameService;
    private final SessionRegistry sessionRegistry;
    private final RoundBetRegistry roundBetRegistry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionRegistry.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        sessionRegistry.remove(session);
        roundBetRegistry.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        var playerBet = JsonUtils.readValue(message.getPayload(), PlayerBet.class);
        playerBet.setUsername(
                (String) session.getAttributes()
                        .get(AuthHandshakeInterceptor.USERNAME_ATTRIBUTE));

        gameService.handleMessage(session, playerBet);
    }
}
