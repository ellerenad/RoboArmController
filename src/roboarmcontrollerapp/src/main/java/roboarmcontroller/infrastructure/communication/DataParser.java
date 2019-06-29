package roboarmcontroller.infrastructure.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import roboarmcontroller.domain.dom.hands.TrackingFrame;
import roboarmcontroller.domain.services.InstructionService;
import roboarmcontroller.infrastructure.communication.parser.JsonTrackingFrame;
import roboarmcontroller.infrastructure.communication.parser.JsonTrackingFrameParser;

import java.math.BigInteger;

@SuppressWarnings("WeakerAccess")
// TODO: Refactor - find a better name (and update the architecture)
@Component
public class DataParser {

    private final Logger log = LoggerFactory.getLogger(DataParser.class);
    private final static BigInteger ZERO_BIG_INTEGER = BigInteger.valueOf(0);

    private JsonTrackingFrameParser jsonTrackingFrameParser;
    private JsonToDomainMapper jsonToDomainMapper;
    private InstructionService instructionService;

    @Autowired
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
