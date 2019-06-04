package roboarmcontroller.domain.dom;

/**
 * This class is an abstraction of the position of the tip of the finger
 */
public class FingerPosition {
    private float x;
    private float y;
    private float z;

    public FingerPosition(float[] positions) {
        if (positions == null || positions.length != 3) {
            throw new RuntimeException("Invalid parameters");
        }

        this.x = positions[0];
        this.y = positions[1];
        this.z = positions[2];

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

}
