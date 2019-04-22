package roboarmcontroller.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.communication.parser.JsonTrackingFrame;
import roboarmcontroller.communication.parser.JsonTrackingFrameParser;
import roboarmcontroller.domain.dom.TrackingFrame;
import roboarmcontroller.domain.services.InstructionService;

import java.math.BigInteger;

public class DataPublisher {

    private final Logger log = LoggerFactory.getLogger(DataPublisher.class);
    private final static BigInteger ZERO_BIG_INTEGER = BigInteger.valueOf(0);

    private JsonTrackingFrameParser jsonTrackingFrameParser;
    private JsonToDomainMapper jsonToDomainMapper;
    private InstructionService instructionService;

    public DataPublisher() {
        this(new JsonTrackingFrameParser(), new JsonToDomainMapper(), new InstructionService());
    }

    public DataPublisher(JsonTrackingFrameParser jsonTrackingFrameParser, JsonToDomainMapper jsonToDomainMapper, InstructionService instructionService) {
        this.jsonTrackingFrameParser = jsonTrackingFrameParser;
        this.jsonToDomainMapper = jsonToDomainMapper;
        this.instructionService = instructionService;
    }

    public void publish(String json) {
        try {
            JsonTrackingFrame jsonTrackingFrame = jsonTrackingFrameParser.parse(json);

            if (this.isAValidMessage(jsonTrackingFrame)) {
                TrackingFrame trackingFrame = jsonToDomainMapper.map(jsonTrackingFrame);
                instructionService.process(trackingFrame);
            }
        } catch (Exception ex) {
            log.info("Exception: {}", ex.getMessage());
            ex.printStackTrace();
        }

    }

    public boolean isAValidMessage(JsonTrackingFrame jsonTrackingFrame) {
        boolean isValid = jsonTrackingFrame != null
                && jsonTrackingFrame.getTimestamp() != null
                && jsonTrackingFrame.getTimestamp().compareTo(ZERO_BIG_INTEGER) > 0
                && jsonTrackingFrame.getJsonHands() != null
                && jsonTrackingFrame.getJsonHands().size() > 0;

        return isValid;
    }

}
