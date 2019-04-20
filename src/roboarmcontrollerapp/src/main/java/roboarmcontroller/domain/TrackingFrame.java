package roboarmcontroller.domain;

import java.math.BigInteger;
import java.util.List;

public class TrackingFrame {

    private int id;
    private List<Hand> hands;
    private BigInteger timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Hand> getHands() {
        return hands;
    }

    public void setHands(List<Hand> hands) {
        this.hands = hands;
    }

    public BigInteger getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
