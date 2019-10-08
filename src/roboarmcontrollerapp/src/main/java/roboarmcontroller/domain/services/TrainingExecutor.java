package roboarmcontroller.domain.services;

public interface TrainingExecutor {
    /**
     * Executes the training with a given framework.
     * @param datasetName the name of the file containing the dataset to use for training
     */
    void train(String datasetName);
}
