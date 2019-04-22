package roboarmcontroller.domain.services.command;

public class Parameters {
    private int servoId;
    private double delta;

    public Parameters(int servoId, double delta) {
        this.servoId = servoId;
        this.delta = delta;
    }

    public int getServoId() {
        return servoId;
    }

    public void setServoId(int servoId) {
        this.servoId = servoId;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "Parameters{" +
                "servoId=" + servoId +
                ", delta=" + delta +
                '}';
    }
}
