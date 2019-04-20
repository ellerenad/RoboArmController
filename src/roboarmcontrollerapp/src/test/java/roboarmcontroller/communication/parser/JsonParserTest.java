package roboarmcontroller.communication.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    JsonParser jsonParser;

    @BeforeEach
    public void setUp(){
        this.jsonParser = new JsonParser();
    }

    @Test
    public void testParseOneHand() throws IOException {
        String jsonToTest = readFileFromResources("json/oneHand.json");
        JsonTrackingFrame jsonTrackingFrame = jsonParser.parse(jsonToTest);

        assertNotNull(jsonTrackingFrame);

        assertNotNull(jsonTrackingFrame.getJsonHands());
        assertEquals(1, jsonTrackingFrame.getJsonHands().length);
        JsonHand jsonHand1 = jsonTrackingFrame.getJsonHands()[0];
        assertEquals("left", jsonHand1.getType());

        assertNotNull(jsonTrackingFrame.getJsonPointables());
        assertEquals(5, jsonTrackingFrame.getJsonPointables().length);


        // Thumb
        JsonPointable jsonPointable = jsonTrackingFrame.getJsonPointables()[0];
        assertEquals(0, jsonPointable.getType());
        assertFalse(jsonPointable.isExtended());

        // Index
        jsonPointable = jsonTrackingFrame.getJsonPointables()[1];
        assertEquals(1, jsonPointable.getType());
        assertFalse(jsonPointable.isExtended());

        // MIDDLE
        jsonPointable = jsonTrackingFrame.getJsonPointables()[2];
        assertEquals(2, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

        // RING
        jsonPointable = jsonTrackingFrame.getJsonPointables()[3];
        assertEquals(3, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

       // PINKY
        jsonPointable = jsonTrackingFrame.getJsonPointables()[4];
        assertEquals(4, jsonPointable.getType());
        assertTrue(jsonPointable.isExtended());

    }

    @Test
    public void testParseTwoHands() throws IOException {
        String jsonToTest = readFileFromResources("json/twoHands.json");
        JsonTrackingFrame jsonTrackingFrame = jsonParser.parse(jsonToTest);

        assertNotNull(jsonTrackingFrame);

        assertNotNull(jsonTrackingFrame.getJsonHands());
        assertEquals(2, jsonTrackingFrame.getJsonHands().length);

        JsonHand jsonHand1 = jsonTrackingFrame.getJsonHands()[0];
        assertEquals("right", jsonHand1.getType());

        JsonHand jsonHand2 = jsonTrackingFrame.getJsonHands()[1];
        assertEquals("left", jsonHand2.getType());
    }



    public  String readFileFromResources( String resourcePath ) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(resourcePath).getFile());
        BufferedReader reader = new BufferedReader( new FileReader( file ));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}