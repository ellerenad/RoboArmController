package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.domain.dom.*;
import roboarmcontroller.domain.services.command.CommandExecutor;
import roboarmcontroller.domain.services.command.CommandParameters;

import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public class InstructionService {
    private final Logger log = LoggerFactory.getLogger(InstructionService.class);

    private final int DELTA = 10;
    private final int SERVOS_COUNT = 3;
    private CommandExecutor commandExecutor;
    private TrainingService trainingService;
    // TODO: Get from parameters
    private boolean trainingMode = true;

    public InstructionService() {
        this(new CommandExecutor(), new TrainingService());
    }

    public InstructionService(CommandExecutor commandExecutor, TrainingService trainingService) {
        this.commandExecutor = commandExecutor;
        this.trainingService = trainingService;
    }

    public void process(TrackingFrame trackingFrame) {

        if (this.trainingMode) {
            this.trainingService.process(trackingFrame);
        } else {
            Optional<CommandParameters> commandParameters = this.buildParameters(trackingFrame);
            if (commandParameters.isPresent()) {
                log.debug("Processing TrackingFrame. CommandParameters = {}", commandParameters.get());
                this.commandExecutor.execute(commandParameters.get());
            }
        }
    }

    private Optional<CommandParameters> buildParameters(TrackingFrame trackingFrame) {
        try {
            Optional<Hand> leftHand = trackingFrame.getHand(HandType.LEFT);
            Optional<Hand> rightHand = trackingFrame.getHand(HandType.RIGHT);

            if (leftHand.isPresent() && rightHand.isPresent()) {
                int servoId = this.findServoId(leftHand.get());
                int delta = this.findDelta(rightHand.get());
                return Optional.of(new CommandParameters(servoId, delta));
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

    private int findDelta(Hand hand) {
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
