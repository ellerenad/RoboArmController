package roboarmcontroller;

import roboarmcontroller.communication.LeapMotionHandler;

import java.net.URI;
import java.net.URISyntaxException;


public class Application {

    public static void main(String...args){

        URI uri;
        try {
            // TODO: Extract to properties config file
            uri = new URI("ws://localhost:6437/v7.json");

            LeapMotionHandler leapMotionHandler = new LeapMotionHandler(uri);

            leapMotionHandler.startListener();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
