package roboarmcontroller.communication;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class LeapMotionHandler extends WebSocketClient {
    private final Logger log = LoggerFactory.getLogger(LeapMotionHandler.class);


    public LeapMotionHandler(URI uri){
        super(uri);
    }

    public void onOpen(ServerHandshake serverHandshake) {
        log.info("WebSocket Opened");

        this.send("{\"focused\": true}");
    }

    public void onMessage(String s) {
        log.info("Message Received {}", s);
    }

    public void onClose(int i, String s, boolean b) {
        log.info("WebSocket Closed");
    }

    public void onError(Exception e) {
        log.info("WebSocket Error");
    }
}
