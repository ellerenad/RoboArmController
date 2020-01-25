package roboarmcontroller.domain.dom.hands;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class TrackingFrame {

    private int id;
    private List<Hand> hands;
    private BigInteger timestamp;

    public Optional<Hand> getHand(HandType handType) {
        return this.hands.stream()
                .filter(hand -> hand.getType().equals(handType))
                .findFirst();
    }


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
