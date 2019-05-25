package roboarmcontroller.domain.services.command;

@SuppressWarnings("WeakerAccess")
public class CommandParameters {
    private int servoId;
    private int delta;

    public CommandParameters(int servoId, int delta) {
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
        return "CommandParameters{" +
                "servoId=" + servoId +
                ", delta=" + delta +
                '}';
    }
}
