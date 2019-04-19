package roboarmcontroller;

import roboarmcontroller.communication.LeapMotionHandler;

import java.net.URI;
import java.net.URISyntaxException;


public class Application {

    public static void main(String...args){

        URI uri = null;
        try {
            //uri = new URI("ws://localhost:6437/v7.json");
            uri = new URI("ws://localhost:6437");
            LeapMotionHandler leapMotionHandler = new LeapMotionHandler(uri);
            leapMotionHandler.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
