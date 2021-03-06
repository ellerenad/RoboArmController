package roboarmcontroller.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.HandType;
import roboarmcontroller.domain.dom.hands.TrackingFrame;

import javax.annotation.PostConstruct;

/**
 * Orchestrates the creation of the training dataset and starts the training itself
 */
@Service
@Profile("training")
public class TrainingService implements TrackingFrameProcessor {
    private final Logger log = LoggerFactory.getLogger(TrainingService.class);
    static int MAX_CYCLES = 1000;

    private int step = 0;
    private int cycle = 0;

    TrainingSetWriter trainingSetWriter;
    ExitService exitService;
    TrainingExecutor trainingExecutor;

    @Autowired
    public TrainingService(TrainingSetWriter trainingSetWriter, ExitService exitService, TrainingExecutor trainingExecutor) {
        this.trainingSetWriter = trainingSetWriter;
        this.exitService = exitService;
        this.trainingExecutor = trainingExecutor;
    }

    @PostConstruct
    public void afterInit() {
        log.info("Running on Training Mode. Please place your hands over the sensor.");
    }


    @Override
    public void process(TrackingFrame trackingFrame) {
        switch (step) {
            case 0:
                if (cycle == 0) {
                    log.info("Reading training data for the Servo 1. (Place your left hand on the position you want for it)");
                    trainingSetWriter.init();
                }

                if (cycle < MAX_CYCLES) {
                    trackingFrame.getHand(HandType.LEFT).ifPresent(hand -> trainingSetWriter.writeLine(hand, InstructionLabel.SERVO1));
                    cycle++;
                } else {
                    cycle = 0;
                    step = 1;
                }
                break;
            case 1:
                if (cycle == 0) {
                    log.info("Reading training data for the Servo 2. (Place your left hand on the position you want for it)");
                }

                if (cycle < MAX_CYCLES) {
                    trackingFrame.getHand(HandType.LEFT).ifPresent(hand -> trainingSetWriter.writeLine(hand, InstructionLabel.SERVO2));
                    cycle++;
                } else {
                    cycle = 0;
                    step = 2;
                }
                break;
            case 2:
                if (cycle == 0) {
                    log.info("Reading training data for the Servo 3. (Place your left hand on the position you want for it)");
                }

                if (cycle < MAX_CYCLES) {
                    trackingFrame.getHand(HandType.LEFT).ifPresent(hand -> trainingSetWriter.writeLine(hand, InstructionLabel.SERVO3));
                    cycle++;
                } else {
                    cycle = 0;
                    step = 3;
                }
                break;
            case 3:
                if (cycle == 0) {
                    log.info("Reading training data for the positive delta. (Place your right hand on the position you want for it)");
                }

                if (cycle < MAX_CYCLES) {
                    trackingFrame.getHand(HandType.RIGHT).ifPresent(hand -> trainingSetWriter.writeLine(hand, InstructionLabel.DELTA_POSITIVE));
                    cycle++;
                } else {
                    cycle = 0;
                    step = 4;
                }
                break;
            case 4:
                if (cycle == 0) {
                    log.info("Reading training data for the negative delta. (Place your right hand on the position you want for it)");
                }

                if (cycle < MAX_CYCLES) {
                    trackingFrame.getHand(HandType.RIGHT).ifPresent(hand -> trainingSetWriter.writeLine(hand, InstructionLabel.DELTA_NEGATIVE));
                    cycle++;
                } else {
                    cycle = 0;
                    step = 5;
                    log.info("Training set written. File:{}. File stamp:{}", trainingSetWriter.getFileName(), trainingSetWriter.getFileStamp());
                    trainingSetWriter.terminate();
                    try {
                        trainingExecutor.train(trainingSetWriter.getFileName(), trainingSetWriter.getFileStamp());
                    } catch(Exception ex){
                        log.error("Exception:", ex);
                    } finally {
                        exitService.terminateProgram(0);
                    }
                }
                break;

        }

    }


}
