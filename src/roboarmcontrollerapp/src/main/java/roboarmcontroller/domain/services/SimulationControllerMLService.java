package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.Command;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.dom.hands.HandType;
import roboarmcontroller.domain.dom.hands.TrackingFrame;
import roboarmcontroller.domain.gateways.SimulationGateway;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
@Service
@Profile("controllingML")
public class SimulationControllerMLService implements TrackingFrameProcessor {
    private final Logger log = LoggerFactory.getLogger(SimulationControllerMLService.class);

    private final int DELTA = 10;
    private SimulationGateway simulationGateway;
    private InstructionClassificationService instructionClassificationService;

    @Autowired
    public SimulationControllerMLService(SimulationGateway simulationGateway,
                                         InstructionClassificationService instructionClassificationService) {
        this.simulationGateway = simulationGateway;
        this.instructionClassificationService = instructionClassificationService;
    }

    @PostConstruct
    public void afterInit() {
        log.info("Running on Controlling Mode (Machine Learning). Please place your hands over the sensor.");
    }

    @Override
    public void process(TrackingFrame trackingFrame) {
        Optional<Command> commandParameters = this.buildParameters(trackingFrame);
        if (commandParameters.isPresent()) {
            log.debug("Processing TrackingFrame. Command = {}", commandParameters.get());
            this.simulationGateway.send(commandParameters.get());
        }
    }

    private Optional<Command> buildParameters(TrackingFrame trackingFrame) {
        try {
            Optional<Hand> leftHand = trackingFrame.getHand(HandType.LEFT);
            Optional<Hand> rightHand = trackingFrame.getHand(HandType.RIGHT);

            if (leftHand.isPresent() && rightHand.isPresent()) {
                int servoId = this.findServoId(leftHand.get());
                log.debug("Calculated ServoId={}", servoId);
                int delta = this.findDelta(rightHand.get());
                log.debug("Calculated Delta={}", delta);
                return Optional.of(new Command(servoId, delta));
            }

        } catch (RuntimeException re) {
            log.debug("RuntimeException: {}", re.getMessage());
        }

        return Optional.empty();
    }

    private int findServoId(Hand hand) {
        InstructionLabel instructionLabel = this.instructionClassificationService.classify(hand);
        switch (instructionLabel) {
            case SERVO1:
                return 1;
            case SERVO2:
                return 2;
            case SERVO3:
                return 3;
            default:
                throw new RuntimeException("ServoId not found. InstructionLabel=" + instructionLabel);
        }
    }

    private int findDelta(Hand hand) {
        InstructionLabel instructionLabel = this.instructionClassificationService.classify(hand);
        switch (instructionLabel) {
            case DELTA_POSITIVE:
                return DELTA;
            case DELTA_NEGATIVE:
                return DELTA * -1;
            default:
                throw new RuntimeException("No Delta Found. InstructionLabel=" + instructionLabel);
        }
    }
}
