package roboarmcontroller.domain.services;

import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;

public interface TrainingSetWriter {
    void init();

    void writeLine(Hand hand, InstructionLabel instructionLabel);

    void terminate();

    String getFileName();

    String getFileStamp();
}
