package ee.aleksale.betgame.websocket.interceptor;

import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.common.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    public static final String USERNAME_ATTRIBUTE = "username";

    private final PlayerRepository playerRepository;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest httpRequest,
            ServerHttpResponse httpResponse,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)  {

        if (httpRequest instanceof ServletServerHttpRequest serverHttpRequest) {
            var token = TokenUtil.getToken(serverHttpRequest.getServletRequest());

            var isValid = isAuthenticatedRequest(token);

            if (isValid) {
                httpResponse.setStatusCode(HttpStatus.OK);
                attributes.put(USERNAME_ATTRIBUTE, TokenUtil.getUsername(token));
                return true;
            }

            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }

    private boolean isAuthenticatedRequest(String token) {
        if (token == null) {
            return false;
        }

        var playerId = TokenUtil.getId(token);
        var playerUsername = TokenUtil.getUsername(token);

        return playerRepository.existsByIdAndUsername(playerId, playerUsername);
    }
}
