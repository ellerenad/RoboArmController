package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.domain.dom.*;
import roboarmcontroller.domain.services.command.CommandExecutor;
import roboarmcontroller.domain.services.command.Parameters;

import java.util.Optional;

public class InstructionService {
    private final Logger log = LoggerFactory.getLogger(InstructionService.class);

    private final double DELTA = 0.01;
    private final int SERVOS_COUNT = 3;
    private CommandExecutor commandExecutor;

    public InstructionService() {
        this(new CommandExecutor());
    }

    public InstructionService(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void process(TrackingFrame trackingFrame) {
        Optional<Parameters> commandParameters = this.buildParameters(trackingFrame);
        if (commandParameters.isPresent()) {
            log.debug("Processing TrackingFrame. Parameters = {}", commandParameters.get());
            this.commandExecutor.execute(commandParameters.get());
        }
    }

    private Optional<Parameters> buildParameters(TrackingFrame trackingFrame) {
        // find servo number

        try {
            Optional<Hand> leftHand = trackingFrame.getHand(HandType.LEFT);
            Optional<Hand> rightHand = trackingFrame.getHand(HandType.RIGHT);

            if (leftHand.isPresent() && rightHand.isPresent()) {
                int servoId = this.findServoId(leftHand.get());
                double delta = this.findDelta(rightHand.get());
                return Optional.of(new Parameters(servoId, delta));
            }


        } catch (RuntimeException re) {
            log.debug("RuntimeException: {}", re.getMessage());
        }

        return Optional.empty();
    }

    private int findServoId(Hand hand) {
        int servoId = (int) hand.getFingers().stream()
                .filter(Finger::isExtended)
                .count();

        if (servoId <= 0 || servoId > SERVOS_COUNT) {
            throw new RuntimeException("ServoId Out of Bounds, ServoId=" + servoId);
        }

        return servoId;
    }

    private double findDelta(Hand hand) {
        Optional<Finger> index = hand.getFinger(FingerType.INDEX);
        if (index.isPresent() && index.get().isExtended()) {
            return DELTA;
        }

        Optional<Finger> thumb = hand.getFinger(FingerType.THUMB);
        if (thumb.isPresent() && thumb.get().isExtended()) {
            return DELTA * -1;
        }

        throw new RuntimeException("No Delta Found");
    }
}
