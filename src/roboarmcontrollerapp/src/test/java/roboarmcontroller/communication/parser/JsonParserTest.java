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
        TrackingFrame trackingFrame = jsonParser.parse(jsonToTest);

        assertNotNull(trackingFrame);

        assertNotNull(trackingFrame.getHands());
        assertEquals(1, trackingFrame.getHands().length);
        Hand hand1 = trackingFrame.getHands()[0];
        assertEquals("left", hand1.getType());

        assertNotNull(trackingFrame.getPointables());
        assertEquals(5, trackingFrame.getPointables().length);


        // Thumb
        Pointable pointable = trackingFrame.getPointables()[0];
        assertEquals(0, pointable.getType());
        assertFalse(pointable.isExtended());

        // Index
        pointable = trackingFrame.getPointables()[1];
        assertEquals(1, pointable.getType());
        assertFalse(pointable.isExtended());

        // MIDDLE
        pointable = trackingFrame.getPointables()[2];
        assertEquals(2, pointable.getType());
        assertTrue(pointable.isExtended());

        // RING
        pointable = trackingFrame.getPointables()[3];
        assertEquals(3, pointable.getType());
        assertTrue(pointable.isExtended());

       // PINKY
        pointable = trackingFrame.getPointables()[4];
        assertEquals(4, pointable.getType());
        assertTrue(pointable.isExtended());

    }

    @Test
    public void testParseTwoHands() throws IOException {
        String jsonToTest = readFileFromResources("json/twoHands.json");
        TrackingFrame trackingFrame = jsonParser.parse(jsonToTest);

        assertNotNull(trackingFrame);

        assertNotNull(trackingFrame.getHands());
        assertEquals(2, trackingFrame.getHands().length);

        Hand hand1 = trackingFrame.getHands()[0];
        assertEquals("right", hand1.getType());

        Hand hand2 = trackingFrame.getHands()[1];
        assertEquals("left", hand2.getType());
    }



    public  String readFileFromResources( String resourcePath ) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(resourcePath).getFile());
        BufferedReader reader = new BufferedReader( new FileReader( file ));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}