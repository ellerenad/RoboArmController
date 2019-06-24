package roboarmcontroller.domain.dom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FingerPositionTest {

    @Test
    public void testConstructor() {
        float values[] = new float[]{0.1f, 0.2f, 0.3f};
        FingerPosition fingerPosition = new FingerPosition(values);

        assertEquals(values[0], fingerPosition.getX());
        assertEquals(values[1], fingerPosition.getY());
        assertEquals(values[2], fingerPosition.getZ());
    }

    @Test
    public void testConstructor_nullParameter() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new FingerPosition(null);
        });
    }

    @Test
    public void testConstructor_illegalLengthOfArrayParameter() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new FingerPosition(new float[]{0.1f});
        });
    }

}