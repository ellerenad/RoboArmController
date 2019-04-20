package roboarmcontroller.communication.parser;

import com.google.gson.Gson;

public class JsonParser {

    private Gson gson = new Gson();

    public JsonTrackingFrame parse(String json) {
        JsonTrackingFrame jsonTrackingFrame;

        jsonTrackingFrame = gson.fromJson(json, JsonTrackingFrame.class);

        return jsonTrackingFrame;
    }

}
