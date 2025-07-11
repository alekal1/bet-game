package ee.aleksale.betgame.integration;


import ee.aleksale.betgame.TestDatabase;
import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.common.utils.JsonUtils;
import ee.aleksale.betgame.websocket.model.GameMessage;
import ee.aleksale.betgame.websocket.model.PlayerBet;
import ee.aleksale.betgame.websocket.utils.WebSocketWarnings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebSocketIntegrationTest extends TestDatabase {

    private static final double PLAYERS_BET = 10.0;
    private static final int PLAYERS_NUMBER = 10;

    private static final int WAITING_TIMEOUT = 11;

    @LocalServerPort
    private int port;

    private static WebSocketSession session;
    private static BlockingQueue<String> messages;
    private static String token;

    @BeforeAll
    static void init() {
        messages = new LinkedBlockingDeque<>();
    }

    @AfterAll
    static void closeSession() throws Exception {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Test
    @Order(1)
    void authorize() {
        assertNull(token);

        var template = new RestTemplate();
        var httpEntity = getHttpEntity();

        var response = template.postForEntity(
                getRestUrl(),
                httpEntity,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        token = response.getBody();
    }

    @Test
    @Order(2)
    void connectToSession() throws Exception {
        assertNull(session);
        assertNotNull(token);

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        session = new StandardWebSocketClient()
                .execute(new TextWebSocketHandler() {
                    @Override
                    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
                        var offered = messages.offer(message.getPayload());
                        assertTrue(offered);
                    }
                }, headers, URI.create(getWebSocketUrl())).get();
    }


    @Test
    @Order(3)
    void roundStarted() throws Exception {
        assertNotNull(session);

        var roundStartedMessage = waitForMessage(new GameMessage.RoundStartMessage(10).toString());

        assertNotNull(roundStartedMessage);
    }

    @Test
    @Order(4)
    void placeWrongBet() throws Exception {
        assertNotNull(session);

        var playerBet = getPlayerBet();
        playerBet.setNumber(11);

        session.sendMessage(new TextMessage(JsonUtils.writeValuesAsString(playerBet)));

        var notValidNumberMessage = waitForMessage(new GameMessage.WarningMessage(WebSocketWarnings.NOT_VALID_NUMBER).toString());

        assertNotNull(notValidNumberMessage);
    }

    @Test
    @Order(5)
    void placeValidBet() throws Exception {
        assertNotNull(session);

        var playerBet = getPlayerBet();

        session.sendMessage(new TextMessage(JsonUtils.writeValuesAsString(playerBet)));

        var notValidNumberMessage = waitForMessage(new GameMessage.BetAcceptedMessage(playerBet).toString());

        assertNotNull(notValidNumberMessage);
    }


    @Test
    @Order(6)
    void endRound() throws Exception {
        assertNotNull(session);

        var roundEndMessage = waitForMessage("| Round has ended!");

        assertNotNull(roundEndMessage);
    }

    public String waitForMessage(String gameMessage) throws InterruptedException {
        long waitingStop = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(WAITING_TIMEOUT);
        while (System.currentTimeMillis() < waitingStop) {
            var message = messages.poll(1, TimeUnit.SECONDS);
            if (message != null && message.contains(gameMessage)) {
                    return message;
            }
        }
        return null;
    }

    private HttpEntity<AuthRequest> getHttpEntity() {
        var request = getAuthRequest();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, headers);
    }

    private AuthRequest getAuthRequest() {
        var request = new AuthRequest();
        request.setUsername(RandomStringUtils.random(5));
        return request;
    }

    private String getRestUrl() {
        return String.format("http://localhost:%d/v1/auth", port);
    }

    private String getWebSocketUrl() {
        return String.format("ws://localhost:%d/ws/bet-game", port);
    }

    private PlayerBet getPlayerBet() {
        var playerBet = new PlayerBet();
        playerBet.setNumber(PLAYERS_NUMBER);
        playerBet.setAmount(PLAYERS_BET);
        return playerBet;
    }
}