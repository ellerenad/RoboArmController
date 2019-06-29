package roboarmcontroller.domain.dom;

@SuppressWarnings("WeakerAccess")
public class Command {
    private int servoId;
    private int delta;

    public Command(int servoId, int delta) {
        this.servoId = servoId;
        this.delta = delta;
    }

    public int getServoId() {
        return servoId;
    }

    public void setServoId(int servoId) {
        this.servoId = servoId;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "Command{" +
                "servoId=" + servoId +
                ", delta=" + delta +
                '}';
    }
}
