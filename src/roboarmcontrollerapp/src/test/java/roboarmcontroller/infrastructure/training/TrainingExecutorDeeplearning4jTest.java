package roboarmcontroller.infrastructure.training;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roboarmcontroller.domain.dom.training.Evaluation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * This test is more like a backdoor to execute the training process again if required. Done this way for simplicity.
 *
 * If this project evolves to be more than an MVP, the training process needs to be executed in a different way.
 *
 * For a more meaningful test, please see {@link integration.deeplearning4j.RoboArmInstructionDeeplearning4jLifecycle}
 */
@Ignore
class TrainingExecutorDeeplearning4jTest {

    private static final String DATASET_IDENTIFIER = "1571087156311";
    private static final String DATASET_PATH = "../../trainingAssets/sets/trainingSet_" + DATASET_IDENTIFIER + ".txt";
    private static final String MODEL_OUTPUT_BASE_PATH = "trainingAssets/models/deeplearning4j/";


    TrainingExecutorDeeplearning4j trainingExecutorDeeplearning4j;

    @BeforeEach
    void setUp(){
        this.trainingExecutorDeeplearning4j = new TrainingExecutorDeeplearning4j(MODEL_OUTPUT_BASE_PATH);
    }

    @Test
    void train() throws Exception {
        Evaluation modelEvaluation = trainingExecutorDeeplearning4j.train(DATASET_PATH, DATASET_IDENTIFIER);
        // Arbitrary values -> good enough to work with them
        assertThat("Accuracy should be greater than 0.9", modelEvaluation.getAccuracy(), greaterThan(0.9));
        assertThat("Precision should be greater than 0.9", modelEvaluation.getPrecision(), greaterThan(0.9));
    }
}