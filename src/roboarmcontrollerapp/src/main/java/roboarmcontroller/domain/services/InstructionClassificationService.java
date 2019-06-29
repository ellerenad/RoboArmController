package roboarmcontroller.domain.services;

import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;

public interface InstructionClassificationService {
    InstructionLabel classify(Hand hand);
}
