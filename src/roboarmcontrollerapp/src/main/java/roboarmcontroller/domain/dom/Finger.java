package roboarmcontroller.domain.dom;

public class Finger {
    private int id;
    private int handId;
    private boolean extended;
    private FingerType type;
    private FingerPosition position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHandId() {
        return handId;
    }

    public void setHandId(int handId) {
        this.handId = handId;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public FingerType getType() {
        return type;
    }

    public void setType(FingerType type) {
        this.type = type;
    }

    public FingerPosition getPosition() {
        return position;
    }

    public void setPosition(FingerPosition position) {
        this.position = position;
    }
}
