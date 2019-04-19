package roboarmcontroller.communication.parser;

import com.google.gson.Gson;

public class JsonParser {

    private Gson gson = new Gson();

    public TrackingFrame parse(String json){
        TrackingFrame trackingFrame ;

        trackingFrame = gson.fromJson(json, TrackingFrame.class);

        return  trackingFrame;
    }

}
