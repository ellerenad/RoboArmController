package roboarmcontroller.communication.parser;

import java.math.BigInteger;

public class TrackingFrame {
    private int id;
    private Hand[] hands;
    private Pointable[] pointables;
    private BigInteger timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Hand[] getHands() {
        return hands;
    }

    public void setHands(Hand[] hands) {
        this.hands = hands;
    }

    public Pointable[] getPointables() {
        return pointables;
    }

    public void setPointables(Pointable[] pointables) {
        this.pointables = pointables;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
