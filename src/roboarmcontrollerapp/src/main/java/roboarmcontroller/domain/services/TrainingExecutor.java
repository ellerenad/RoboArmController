package roboarmcontroller.domain.services;

import roboarmcontroller.domain.dom.training.Evaluation;

public interface TrainingExecutor {
    /**
     * Executes the training with a given framework.
     * @param datasetPath the name of the file containing the dataset to use for training
     */
    Evaluation train(String datasetPath, String datasetIdentifier) throws Exception;
}
