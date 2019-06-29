package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.HandType;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.TrackingFrame;
import roboarmcontroller.domain.services.command.Command;
import roboarmcontroller.domain.services.command.SimulationGateway;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
@Service
public class InstructionService {
    private final Logger log = LoggerFactory.getLogger(InstructionService.class);

    private final int DELTA = 10;
    private SimulationGateway simulationGateway;
    private TrainingService trainingService;
    private InstructionClassificationService instructionClassificationService;

    private boolean trainingMode;

    @Autowired
    public InstructionService(@Value("${execution.mode.training}") boolean trainingMode,
                              SimulationGateway simulationGateway,
                              TrainingService trainingService,
                              InstructionClassificationService instructionClassificationService) {
        this.simulationGateway = simulationGateway;
        this.trainingService = trainingService;
        this.instructionClassificationService = instructionClassificationService;
        this.trainingMode = trainingMode;
    }

    @PostConstruct
    public void afterInit() {
        if (this.trainingMode) {
            log.info("Running on Training Mode. Please place your hands over the sensor.");
        } else {
            log.info("Running on Controlling Mode. Please place your hands over the sensor.");
        }
    }

    public void process(TrackingFrame trackingFrame) {

        if (this.trainingMode) {
            this.trainingService.process(trackingFrame);
        } else {
            Optional<Command> commandParameters = this.buildParameters(trackingFrame);
            if (commandParameters.isPresent()) {
                log.debug("Processing TrackingFrame. Command = {}", commandParameters.get());
                this.simulationGateway.send(commandParameters.get());
            }
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
