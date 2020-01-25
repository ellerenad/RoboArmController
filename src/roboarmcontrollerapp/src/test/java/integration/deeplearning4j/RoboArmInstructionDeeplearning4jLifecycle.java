package integration.deeplearning4j;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.dom.training.Evaluation;
import roboarmcontroller.infrastructure.classification.InstructionClassificationServiceDeeplearning4j;
import roboarmcontroller.infrastructure.training.TrainingExecutorDeeplearning4j;
import testUtils.StringToDomainParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class RoboArmInstructionDeeplearning4jLifecycle {

    private Logger logger = LoggerFactory.getLogger(RoboArmInstructionDeeplearning4jLifecycle.class);

    @Test
    void testLifecycle() throws Exception {
        try {
            // Test training and evaluate model
            String modelOutputBasePath = "deeplearning4jTestModels/trainedModel/";
            TrainingExecutorDeeplearning4j roboArmInstructionModelTrainer = new TrainingExecutorDeeplearning4j(modelOutputBasePath);

            String dataSetPath = this.getPathFromResources("deeplearning4j/dataset/trainingSet_1559827006805.txt");
            String datasetIdentifier = "1559827006805";
            Evaluation modelEvaluation = roboArmInstructionModelTrainer.train(dataSetPath, datasetIdentifier);
            // Arbitrary values -> good enough to work with them
            assertThat("Accuracy should be greater than 0.9", modelEvaluation.getAccuracy(), greaterThan(0.9));
            assertThat("Precision should be greater than 0.9", modelEvaluation.getPrecision(), greaterThan(0.9));

            // Test loading the model and perform some predictions
            String savedModelBasePath = modelOutputBasePath + datasetIdentifier + "/";
            // The model is loaded on the constructor
            InstructionClassificationServiceDeeplearning4j roboArmInstructionClassifier = new InstructionClassificationServiceDeeplearning4j(savedModelBasePath);
            assertPredictions(roboArmInstructionClassifier);

        } finally {
            // cleanup
            File outputDirectory = new File("deeplearning4jTestModels");
            FileUtils.deleteDirectory(outputDirectory);
        }
    }

    private void assertPredictions(InstructionClassificationServiceDeeplearning4j roboArmInstructionClassifier) {
        // Note 1: Data directly taken from the data file. It might be the case that this data was used to train
        // the Neural Network - Testing the NN with data it was trained on should be avoided.
        // Note 2: The Neural Network has an accuracy of around 90% - it might be the case these labels are not
        // properly predicted, and breaks the test, though the value is still accepted. This could break a CI/CD pipeline. As this is a MVP, this is accepted.
        List<String> lines = Arrays.asList(
                "0|38.208|156.451|80.1864|12.8463|168.467|-35.969|-27.7574|170.312|-61.8114|-58.2967|168.344|-58.3535|-108.05|159.967|-39.629|0",
                "0|42.3547|159.677|54.8158|-5.02637|182.497|-62.1005|-110.879|167.142|-57.1002|-45.0468|94.0677|-25.2277|-64.9443|94.9761|-12.2357|1",
                "0|-23.8724|119.959|6.16023|-26.6377|128.075|-2.1761|-41.7599|121.839|-0.334683|-63.474|122.708|1.38042|-78.5949|131.032|4.72392|2",
                "1|-57.9509|154.695|75.8706|-48.7798|153.478|-43.7015|-11.3466|152.738|-74.2601|37.5707|144.084|-78.3605|85.2095|127.172|-55.4767|3",
                "1|-57.7108|164.532|57.0743|9.09145|65.1718|26.2156|16.9844|60.849|30.1358|29.3373|65.982|25.5115|35.3826|78.686|13.2991|4");

        long errorsCount = lines.stream().filter(line -> {

            Hand hand = StringToDomainParser.getHandFromTrainingSetLine(line);
            InstructionLabel expectedInstructionLabel = StringToDomainParser.getInstructionLabelFromTrainingSetLine(line);
            InstructionLabel predictedInstructionLabel = roboArmInstructionClassifier.classify(hand);

            if (expectedInstructionLabel != predictedInstructionLabel) {
                logger.warn("Potential error on prediction: Expected {} , predicted {}", expectedInstructionLabel, predictedInstructionLabel);
                return true;
            }
            return false;
        }).count();

        assertThat(errorsCount, equalTo(0L));
    }

    private String getPathFromResources(String resourcePath) {
        return getClass().getClassLoader().getResource(resourcePath).getPath();

    }
}
