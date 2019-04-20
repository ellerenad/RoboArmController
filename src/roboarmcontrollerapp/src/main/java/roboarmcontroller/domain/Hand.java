package roboarmcontroller.domain;

import java.util.List;

public class Hand {

    private int id;
    private float confidence;
    private HandType type;
    private List<Finger> fingers;

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
