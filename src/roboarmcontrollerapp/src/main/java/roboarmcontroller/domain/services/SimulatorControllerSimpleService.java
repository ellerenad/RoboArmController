package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.Command;
import roboarmcontroller.domain.dom.hands.*;
import roboarmcontroller.domain.gateways.SimulationGateway;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
@Service
@Profile("controllingSimple")
public class SimulatorControllerSimpleService implements TrackingFrameProcessor {
    private final Logger log = LoggerFactory.getLogger(SimulatorControllerSimpleService.class);

    private final int DELTA = 10;
    private final int SERVOS_COUNT = 3;
    private SimulationGateway simulationGateway;

    @Autowired
    public SimulatorControllerSimpleService(SimulationGateway simulationGateway) {
        this.simulationGateway = simulationGateway;
    }

    @PostConstruct
    public void afterInit() {
        log.info("Running on Controlling Mode (Simple). Please place your hands over the sensor.");
    }


    public void process(TrackingFrame trackingFrame) {
        Optional<Command> command = this.buildParameters(trackingFrame);
        if (command.isPresent()) {
            log.debug("Processing TrackingFrame. Command = {}", command.get());
            this.simulationGateway.send(command.get());
        }
    }

    private Optional<Command> buildParameters(TrackingFrame trackingFrame) {
        try {
            Optional<Hand> leftHand = trackingFrame.getHand(HandType.LEFT);
            Optional<Hand> rightHand = trackingFrame.getHand(HandType.RIGHT);

            if (leftHand.isPresent() && rightHand.isPresent()) {
                int servoId = this.findServoId(leftHand.get());
                int delta = this.findDelta(rightHand.get());
                return Optional.of(new Command(servoId, delta));
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
