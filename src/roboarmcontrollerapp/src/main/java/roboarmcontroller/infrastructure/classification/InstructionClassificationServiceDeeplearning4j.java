package roboarmcontroller.infrastructure.classification;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.buffer.FloatBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Finger;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.services.InstructionClassificationService;

import java.io.File;
import java.io.IOException;

@Service
@Profile("controllingML & deeplearning4j")
public class InstructionClassificationServiceDeeplearning4j implements InstructionClassificationService {
    private final static int FIELDS_COUNT = 16;
    private static final int CLASSES_COUNT = 5;

    private static final String STORED_NORMALIZER_FILENAME = "normalizer";
    private static final String STORED_MODEL_FILENAME = "trainedModel.zip";

    private MultiLayerNetwork model;
    private DataNormalization dataNormalizer;
    private InstructionLabel[] instructionLabels;

    public InstructionClassificationServiceDeeplearning4j(@Value("${machine.learning.controlling.deeplearning4j.input.exported.model.path}") String savedModelBasePath) throws Exception {
        model = loadModel(savedModelBasePath);
        dataNormalizer = loadNormalizer(savedModelBasePath);
        instructionLabels = InstructionLabel.values();
    }

    /**
     * Predict a label given a domain object
     *
     * @param hand representation of the position of the hand
     * @return the predicted label
     */
    @Override
    public InstructionLabel classify(Hand hand) {

        // Transform the data to the required format
        INDArray indArray = getArray(hand);

        // Normalize the data the same way it was normalized in the training phase
        dataNormalizer.transform(indArray);

        // Do the prediction
        INDArray result = model.output(indArray, false);

        // Get the index with the greatest probabilities
        int predictedLabel = getIndexPredictedLabel(result);
        return instructionLabels[predictedLabel];
    }


    private MultiLayerNetwork loadModel(String basePath) throws IOException {
        File locationToSaveModel = new File(basePath + STORED_MODEL_FILENAME);
        MultiLayerNetwork restoredModel = MultiLayerNetwork.load(locationToSaveModel, false);
        return restoredModel;
    }

    private DataNormalization loadNormalizer(String basePath) throws Exception {
        File locationToSaveNormalizer = new File(basePath + STORED_NORMALIZER_FILENAME);
        DataNormalization restoredNormalizer = NormalizerSerializer.getDefault().restore(locationToSaveNormalizer);
        return restoredNormalizer;
    }

    /**
     * Transform the data from the domain (in this case a string) to the object required by the framework to work
     *
     * @param hand representation of the position of the hand
     * @return an INDArray the framework can work with
     */
    // This will be modified to transform the hand object on the RoboArmController project
    private static INDArray getArray(Hand hand) {
        float[] features = transform(hand);

        NDArray ndArray = new NDArray(1, 16); // The empty constructor causes a NPE in add method
        DataBuffer dataBuffer = new FloatBuffer(features);
        ndArray.setData(dataBuffer);

        return ndArray;
    }

    @SuppressWarnings("Duplicates")
    private static float[] transform(Hand hand){
        float[] input = new float[FIELDS_COUNT];
        input[0] = hand.getType().ordinal();

        for (int i = 0; i < hand.getFingers().size(); i++) {
            Finger finger = hand.getFingers().get(i);
            int indexInArray = i * 3;
            input[indexInArray+1] = finger.getPosition().getX();
            input[indexInArray + 2] = finger.getPosition().getY();
            input[indexInArray + 3] = finger.getPosition().getZ();
        }

        return input;
    }

    /**
     * Get the index of the predicted label
     *
     * @param predictions INDArray with the probabilities per label
     * @return the index with the greatest probabilities
     */
    private static int getIndexPredictedLabel(INDArray predictions) {
        int maxIndex = 0;
        // We should get max CLASSES_COUNT amount of predictions with probabilities.
        for (int i = 0; i < CLASSES_COUNT; i++) {
            if (predictions.getDouble(i) > predictions.getDouble(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
