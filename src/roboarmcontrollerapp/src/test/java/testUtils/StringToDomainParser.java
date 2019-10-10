package testUtils;

import org.junit.jupiter.api.Test;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Finger;
import roboarmcontroller.domain.dom.hands.FingerPosition;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.dom.hands.HandType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringToDomainParser {

    private static final int FIELD_COUNT = 17;

    /**
     * Utility method to make testing easier. There is a unit test for it as well.
     *
     * @param line
     * @return
     */
    public static Hand getHandFromTrainingSetLine(String line) {
        String[] tokens = line.split(Pattern.quote("|"));

        if (tokens.length != FIELD_COUNT) {
            throw new RuntimeException("Invalid count of arguments");
        }

        List<Finger> fingers = new ArrayList<>();
        for (int i = 1; i < FIELD_COUNT - 1; i = i + 3) {

            float x = Float.parseFloat(tokens[i].trim());
            float y = Float.parseFloat(tokens[i + 1].trim());
            float z = Float.parseFloat(tokens[i + 2].trim());

            float[] positions = new float[]{x, y, z};

            FingerPosition fingerPosition = new FingerPosition(positions);
            Finger finger = new Finger();
            finger.setPosition(fingerPosition);
            fingers.add(finger);
        }

        Hand hand = new Hand();

        int handTypeOrdinal = Integer.parseInt(tokens[0].trim());
        hand.setType(HandType.values()[handTypeOrdinal]);

        hand.setFingers(fingers);

        return hand;
    }

    /**
     *  Utility method to make testing easier. There is a unit test for it as well.
     * @param line
     * @return
     */
    public static InstructionLabel getInstructionLabelFromTrainingSetLine(String line) {
        String[] tokens = line.split(Pattern.quote("|"));

        if (tokens.length != FIELD_COUNT) {
            throw new RuntimeException("Invalid count of arguments");
        }

        int ordinal = Integer.parseInt(tokens[FIELD_COUNT - 1].trim());
        return InstructionLabel.values()[ordinal];
    }

    /* UNIT TESTING THIS SAME CLASS! (I understand it can be controversial to put this here, so if you have a better suggestion, let me know :) */

    @Test
    void testGetHandFromTrainingSetLine() {
        String line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|0";
        Hand hand = StringToDomainParser.getHandFromTrainingSetLine(line);
        assertEquals(HandType.LEFT, hand.getType());
        assertEquals(5, hand.getFingers().size());
        assertEquals(34.3208, hand.getFingers().get(0).getPosition().getX(), 4);
        assertEquals(159.161, hand.getFingers().get(0).getPosition().getY(), 3);
        assertEquals(84.9007, hand.getFingers().get(0).getPosition().getZ(), 4);
        assertEquals(-58.1933, hand.getFingers().get(3).getPosition().getX(), 4);
        assertEquals(170.954, hand.getFingers().get(3).getPosition().getY(), 3);
        assertEquals(-57.0651, hand.getFingers().get(3).getPosition().getZ(), 4);
    }

    @Test
    void testGetInstructionLabelFromTrainingSetLine() {
        String line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|0";
        InstructionLabel instructionLabel = StringToDomainParser.getInstructionLabelFromTrainingSetLine(line);

        assertEquals(InstructionLabel.SERVO1, instructionLabel);

        line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|3";
        instructionLabel = StringToDomainParser.getInstructionLabelFromTrainingSetLine(line);

        assertEquals(InstructionLabel.DELTA_POSITIVE, instructionLabel);

    }
}
