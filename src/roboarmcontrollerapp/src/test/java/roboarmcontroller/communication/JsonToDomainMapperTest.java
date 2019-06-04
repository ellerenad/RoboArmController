package roboarmcontroller.communication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roboarmcontroller.communication.parser.JsonTrackingFrame;
import roboarmcontroller.communication.parser.JsonTrackingFrameParser;
import roboarmcontroller.domain.dom.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JsonToDomainMapperTest {

    JsonToDomainMapper jsonToDomainMapper;

    // TODO: Delete and use previously constructed Json*** Objects instead of directly parsing the JSON
    JsonTrackingFrameParser jsonTrackingFrameParser;

    @BeforeEach
    void setUp() {
        this.jsonToDomainMapper = new JsonToDomainMapper();
        this.jsonTrackingFrameParser = new JsonTrackingFrameParser();
    }

    @Test
    void mapOneHand() throws IOException {
        String jsonToTest = readFileFromResources("json/oneHand.json");
        JsonTrackingFrame jsonTrackingFrame = jsonTrackingFrameParser.parse(jsonToTest);

        TrackingFrame trackingFrame = this.jsonToDomainMapper.map(jsonTrackingFrame);

        assertNotNull(trackingFrame);
        assertNotNull(trackingFrame.getHands());
        assertEquals(1, trackingFrame.getHands().size());
        Hand hand = trackingFrame.getHands().get(0);
        assertNotEquals(0, hand.getId());
        assertNotEquals(0, hand.getConfidence());
        assertEquals(HandType.LEFT, hand.getType());

        assertNotNull(hand.getFingers());
        List<Finger> fingerList = hand.getFingers();
        assertEquals(5, fingerList.size());

        Finger finger0 = fingerList.get(0);
        assertNotNull(finger0);
        assertNotEquals(0, finger0.getId());
        assertEquals(hand.getId(), finger0.getHandId());
        assertFalse(finger0.isExtended());
        assertEquals(FingerType.THUMB, finger0.getType());
        assertNotNull(finger0.getPosition());

        Finger finger2 = fingerList.get(2);
        assertNotNull(finger2);
        assertNotEquals(0, finger2.getId());
        assertEquals(hand.getId(), finger2.getHandId());
        assertTrue(finger2.isExtended());
        assertEquals(FingerType.MIDDLE, finger2.getType());
        assertNotNull(finger2.getPosition());

    }


    private String readFileFromResources(String resourcePath) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(resourcePath).getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }


}