package roboarmcontroller.communication.parser;

public class JsonPointable {
    private int id;
    private int handId;
    private boolean extended;
    private float[] stabilizedTipPosition;
    private int type;

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

    public float[] getStabilizedTipPosition() {
        return stabilizedTipPosition;
    }

    public void setStabilizedTipPosition(float[] stabilizedTipPosition) {
        this.stabilizedTipPosition = stabilizedTipPosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
