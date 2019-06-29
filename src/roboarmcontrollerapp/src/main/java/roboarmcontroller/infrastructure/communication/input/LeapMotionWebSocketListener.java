package roboarmcontroller.infrastructure.communication.input;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roboarmcontroller.infrastructure.communication.parser.DataParser;

import javax.annotation.PostConstruct;
import java.net.URI;

@Component
public class LeapMotionWebSocketListener extends WebSocketClient {
    private final Logger log = LoggerFactory.getLogger(LeapMotionWebSocketListener.class);

    private DataParser dataParser;

    @Autowired
    public LeapMotionWebSocketListener(@Value("${handsensor.websocket.uri.listen}") URI uri, DataParser dataParser) {
        super(uri);
        this.dataParser = dataParser;
    }

    @PostConstruct
    public void startListener() {
        log.info("started listening at: {}", this.getURI());
        connect();
    }

    public void onOpen(ServerHandshake serverHandshake) {
        log.info("WebSocket Opened");
        this.send("{\"focused\": true}");
    }

    /*
      This method gets called every time the web socket receives a message
    */
    public void onMessage(String json) {
        log.debug("Message Received {}", json);
        dataParser.publish(json);
    }

    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket Closed. Code={}, Reason={}, Remote={}", code, reason, remote);
    }

    public void onError(Exception e) {
        log.info("WebSocket Error", e);
    }
}
