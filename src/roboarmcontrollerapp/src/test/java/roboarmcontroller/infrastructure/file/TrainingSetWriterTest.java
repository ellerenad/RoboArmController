package roboarmcontroller.infrastructure.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roboarmcontroller.domain.dom.hands.Finger;
import roboarmcontroller.domain.dom.hands.FingerPosition;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.dom.hands.HandType;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingSetWriterTest {


    TrainingSetWriter trainingSetWriter;

    @BeforeEach
    void setUp() {
        // TODO: Fix the path of the file if unit testing the actual writing
        this.trainingSetWriter = new TrainingSetWriter("");
    }

    @Test
    void getLine() {
        Finger finger1 = new Finger();
        finger1.setPosition(new FingerPosition(new float[]{0.1111f, 0.2222f, 0.3333f}));
        Finger finger2 = new Finger();
        finger2.setPosition(new FingerPosition(new float[]{0.4f, 0.5f, 0.6f}));
        Hand hand = new Hand();
        hand.setType(HandType.RIGHT);
        hand.setFingers(Arrays.asList(finger1, finger2));

        String line = trainingSetWriter.getLine(hand);
        assertEquals("1|0.1111|0.2222|0.3333|0.4|0.5|0.6", line);

        hand.setType(HandType.LEFT);
        line = trainingSetWriter.getLine(hand);
        assertEquals("0|0.1111|0.2222|0.3333|0.4|0.5|0.6", line);

    }
}