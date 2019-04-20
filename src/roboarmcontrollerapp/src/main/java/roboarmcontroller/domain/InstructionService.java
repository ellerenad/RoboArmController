package roboarmcontroller.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstructionService {
    private final Logger log = LoggerFactory.getLogger(InstructionService.class);


    public void process(TrackingFrame trackingFrame) {
        log.info("Processing TrackingFrame {}", trackingFrame);
    }

}
