package roboarmcontroller.domain.services;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import roboarmcontroller.domain.dom.Finger;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.InstructionLabel;

public class InstructionClassificationService {

    public final static int FIELD_COUNT = 16;
    private final static String FEED_OPERATION = "dnn/input_from_feature_columns/input_layer/concat";
    private final static String FETCH_OPERATION_CLASS_ID = "dnn/head/predictions/class_ids";
    private final static String SAVED_MODEL_TAGS = "serve";

    private Session modelBundleSession;
    private InstructionLabel[] instructionLabels;

    // TODO: Extract to properties
    String savedModelPath = "trainingAssets/models/1560159859969/1560160000";

    void setSavedModelPath(String savedModelPath) {
        this.savedModelPath = savedModelPath;
    }

    public InstructionLabel classify(Hand hand) {
        if (modelBundleSession == null) {
            modelBundleSession = SavedModelBundle.load(savedModelPath, SAVED_MODEL_TAGS).session();
            instructionLabels = InstructionLabel.values();
        }

        Tensor inputTensor = InstructionClassificationService.createInputTensor(hand);

        Tensor result = this.modelBundleSession.runner()
                .feed(FEED_OPERATION, inputTensor)
                .fetch(FETCH_OPERATION_CLASS_ID)
                .run()
                .get(0);

        long[] buffer = new long[1];
        result.copyTo(buffer);

        int ordinal = (int) buffer[0];
        return instructionLabels[ordinal];
    }


    private static Tensor createInputTensor(Hand hand) {
        // order of the data on the input: PetalLength, PetalWidth, SepalLength, SepalWidth
        // (taken from the saved_model, node dnn/input_from_feature_columns/input_layer/concat)
        float[] input = new float[FIELD_COUNT];

        for (int i = 0; i < hand.getFingers().size(); i++) {
            Finger finger = hand.getFingers().get(i);
            int indexInArray = i * 3;
            input[indexInArray] = finger.getPosition().getX();
            input[indexInArray + 1] = finger.getPosition().getY();
            input[indexInArray + 2] = finger.getPosition().getZ();
        }

        input[FIELD_COUNT - 1] = hand.getType().ordinal();

        float[][] data = new float[1][FIELD_COUNT];
        data[0] = input;
        return Tensor.create(data);
    }
}
