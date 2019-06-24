package roboarmcontroller.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.communication.parser.JsonTrackingFrame;
import roboarmcontroller.communication.parser.JsonTrackingFrameParser;
import roboarmcontroller.domain.dom.TrackingFrame;
import roboarmcontroller.domain.services.InstructionService;

import java.math.BigInteger;

@SuppressWarnings("WeakerAccess")
// TODO: Refactor - find a better name (and update the architecture)
public class DataParser {

    private final Logger log = LoggerFactory.getLogger(DataParser.class);
    private final static BigInteger ZERO_BIG_INTEGER = BigInteger.valueOf(0);

    private JsonTrackingFrameParser jsonTrackingFrameParser;
    private JsonToDomainMapper jsonToDomainMapper;
    private InstructionService instructionService;

    public DataParser() {
        this(new JsonTrackingFrameParser(), new JsonToDomainMapper(), new InstructionService());
    }

    public DataParser(JsonTrackingFrameParser jsonTrackingFrameParser, JsonToDomainMapper jsonToDomainMapper, InstructionService instructionService) {
        this.jsonTrackingFrameParser = jsonTrackingFrameParser;
        this.jsonToDomainMapper = jsonToDomainMapper;
        this.instructionService = instructionService;
    }

    public void publish(String json) {
        try {
            JsonTrackingFrame jsonTrackingFrame = jsonTrackingFrameParser.parse(json);

            if (this.isValidMessage(jsonTrackingFrame)) {
                TrackingFrame trackingFrame = jsonToDomainMapper.map(jsonTrackingFrame);
                instructionService.process(trackingFrame);
            }
        } catch (Exception ex) {
            log.info("Exception on data publish.", ex);
        }

    }

    public boolean isValidMessage(JsonTrackingFrame jsonTrackingFrame) {
        return jsonTrackingFrame != null
                && jsonTrackingFrame.getTimestamp() != null
                && jsonTrackingFrame.getTimestamp().compareTo(ZERO_BIG_INTEGER) > 0
                && jsonTrackingFrame.getJsonHands() != null
                && jsonTrackingFrame.getJsonHands().size() > 0;
    }

}
