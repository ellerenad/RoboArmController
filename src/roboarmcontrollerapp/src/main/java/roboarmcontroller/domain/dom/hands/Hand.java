package roboarmcontroller.domain.dom.hands;

import java.util.List;
import java.util.Optional;

public class Hand {

    private int id;
    private float confidence;
    private HandType type;
    private List<Finger> fingers;

    public Optional<Finger> getFinger(FingerType fingerType) {
        return this.getFingers().stream().filter(finger -> finger.getType().equals(fingerType)).findFirst();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public HandType getType() {
        return type;
    }

    public void setType(HandType type) {
        this.type = type;
    }

    public List<Finger> getFingers() {
        return fingers;
    }

    public void setFingers(List<Finger> fingers) {
        this.fingers = fingers;
    }
}
