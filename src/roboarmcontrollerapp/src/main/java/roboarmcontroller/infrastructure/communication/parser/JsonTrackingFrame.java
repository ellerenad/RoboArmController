package roboarmcontroller.infrastructure.communication.parser;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class JsonTrackingFrame {
    private int id;

    @SerializedName("hands")
    private List<JsonHand> jsonHands;

    @SerializedName("pointables")
    private List<JsonPointable> jsonPointables;

    private BigInteger timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<JsonHand> getJsonHands() {
        return jsonHands;
    }

    public void setJsonHands(List<JsonHand> jsonHands) {
        this.jsonHands = jsonHands;
    }

    public List<JsonPointable> getJsonPointables() {
        return jsonPointables;
    }

    public void setJsonPointables(List<JsonPointable> jsonPointables) {
        this.jsonPointables = jsonPointables;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
