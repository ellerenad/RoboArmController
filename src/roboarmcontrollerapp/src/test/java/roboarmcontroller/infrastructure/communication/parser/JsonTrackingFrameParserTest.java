package roboarmcontroller.infrastructure.communication.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTrackingFrameParserTest {

    JsonTrackingFrameParser jsonTrackingFrameParser;

    @BeforeEach
    public void setUp() {
        this.jsonTrackingFrameParser = new JsonTrackingFrameParser();
    }

    @Test
    public void testParseOneHand() throws IOException {
        String jsonToTest = readFileFromResources("json/oneHand.json");
        JsonTrackingFrame jsonTrackingFrame = jsonTrackingFrameParser.parse(jsonToTest);

        assertNotNull(jsonTrackingFrame);

        assertNotNull(jsonTrackingFrame.getJsonHands());
        assertEquals(1, jsonTrackingFrame.getJsonHands().size());
        JsonHand jsonHand1 = jsonTrackingFrame.getJsonHands().get(0);
        assertEquals("left", jsonHand1.getType());
        assertEquals(0.762296, jsonHand1.getConfidence(), 0.000001f);
        assertEquals(7, jsonHand1.getId());

        assertNotNull(jsonTrackingFrame.getJsonPointables());
        assertEquals(5, jsonTrackingFrame.getJsonPointables().size());


        // Thumb
        JsonPointable jsonPointable = jsonTrackingFrame.getJsonPointables().get(0);
        assertEquals(0, jsonPointable.getType());
        assertFalse(jsonPointable.isExtended());
        assertArrayEquals(jsonPointable.getStabilizedTipPosition(), new float[]{75.3986f, 254.982f, -13.5298f});

        // Index
        jsonPointable = jsonTrackingFrame.getJsonPointables().get(1);
        assertEquals(1, jsonPointable.getType());
        assertFalse(jsonPointable.isExtended());

        // MIDDLE
        jsonPointable = jsonTrackingFrame.getJsonPointables().get(2);
        assertEquals(2, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

        // RING
        jsonPointable = jsonTrackingFrame.getJsonPointables().get(3);
        assertEquals(3, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

        // PINKY
        jsonPointable = jsonTrackingFrame.getJsonPointables().get(4);
        assertEquals(4, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

    }

    @Test
    public void testParseTwoHands() throws IOException {
        String jsonToTest = readFileFromResources("json/twoHands.json");
        JsonTrackingFrame jsonTrackingFrame = jsonTrackingFrameParser.parse(jsonToTest);

        assertNotNull(jsonTrackingFrame);

        assertNotNull(jsonTrackingFrame.getJsonHands());
        assertEquals(2, jsonTrackingFrame.getJsonHands().size());

        JsonHand jsonHand1 = jsonTrackingFrame.getJsonHands().get(0);
        assertEquals("right", jsonHand1.getType());

        JsonHand jsonHand2 = jsonTrackingFrame.getJsonHands().get(1);
        assertEquals("left", jsonHand2.getType());
    }


    private String readFileFromResources(String resourcePath) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(resourcePath).getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}