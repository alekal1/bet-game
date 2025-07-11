package ee.aleksale.betgame.unit.websocket.stub;

import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSocketSessionStub implements WebSocketSession {

    List<WebSocketMessage<?>> messages = new ArrayList<>();
    boolean isOpen = false;

    @Override
    public String getId() {
        return "";
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Principal getPrincipal() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public String getAcceptedProtocol() {
        return "";
    }

    @Override
    public void setTextMessageSizeLimit(int messageSizeLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTextMessageSizeLimit() {
        return 0;
    }

    @Override
    public void setBinaryMessageSizeLimit(int messageSizeLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return 0;
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return List.of();
    }

    @Override
    public void sendMessage(WebSocketMessage<?> message) throws IOException {
        messages.add(message);
    }

    public List<WebSocketMessage<?>> getMessages() {
        return messages;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    public void open() {
        isOpen = true;
    }

    @Override
    public void close(CloseStatus status) {
        isOpen = false;
    }
}
