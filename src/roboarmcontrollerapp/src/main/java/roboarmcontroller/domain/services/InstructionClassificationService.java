package roboarmcontroller.domain.services;

import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.InstructionLabel;

public interface InstructionClassificationService {
    InstructionLabel classify(Hand hand);
}
