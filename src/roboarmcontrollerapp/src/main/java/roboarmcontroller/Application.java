package roboarmcontroller;

import roboarmcontroller.communication.LeapMotionWebSocketListener;

import java.net.URI;
import java.net.URISyntaxException;


public class Application {

    public static void main(String...args){

        URI uri;
        try {
            // TODO: Extract to properties config file
            uri = new URI("ws://localhost:6437/v7.json");

            LeapMotionWebSocketListener leapMotionWebSocketListener = new LeapMotionWebSocketListener(uri);

            leapMotionWebSocketListener.startListener();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
