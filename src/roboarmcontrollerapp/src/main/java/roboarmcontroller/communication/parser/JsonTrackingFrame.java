package roboarmcontroller.communication.parser;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class JsonTrackingFrame {
    private int id;

    @SerializedName("hands")
    private JsonHand[] jsonHands;

    @SerializedName("pointables")
    private JsonPointable[] jsonPointables;

    private BigInteger timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JsonHand[] getJsonHands() {
        return jsonHands;
    }

    public void setJsonHands(JsonHand[] jsonHands) {
        this.jsonHands = jsonHands;
    }

    public JsonPointable[] getJsonPointables() {
        return jsonPointables;
    }

    public void setJsonPointables(JsonPointable[] jsonPointables) {
        this.jsonPointables = jsonPointables;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
