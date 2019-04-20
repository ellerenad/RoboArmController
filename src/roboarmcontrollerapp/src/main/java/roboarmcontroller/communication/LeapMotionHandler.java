package roboarmcontroller.communication;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class LeapMotionHandler extends WebSocketClient {
    private final Logger log = LoggerFactory.getLogger(LeapMotionHandler.class);

    private DataPublisher dataPublisher = new DataPublisher();

    public LeapMotionHandler(URI uri){
        super(uri);
    }

    public void startListener() {
        connect();
    }


    public void onOpen(ServerHandshake serverHandshake) {
        log.info("WebSocket Opened");

        this.send("{\"focused\": true}");
    }

    /*
        This method gets called evertime the websocket receives a message
     */
    public void onMessage(String json) {
        log.debug("Message Received {}", json);
        dataPublisher.publish(json);
    }

    public void onClose(int i, String s, boolean b) {
        log.info("WebSocket Closed");
    }

    public void onError(Exception e) {
        log.info("WebSocket Error");
    }
}
