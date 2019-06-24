package roboarmcontroller.infrastructure.communication.parser;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class JsonTrackingFrameParser {

    private Gson gson = new Gson();

    public JsonTrackingFrame parse(String json) {
        JsonTrackingFrame jsonTrackingFrame;

        jsonTrackingFrame = gson.fromJson(json, JsonTrackingFrame.class);

        return jsonTrackingFrame;
    }

}
