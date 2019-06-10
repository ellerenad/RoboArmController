package roboarmcontroller.domain.services;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import roboarmcontroller.domain.dom.Finger;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.InstructionLabel;

public class InstructionClassificationService {

    public final static int FIELD_COUNT = 16;

    // TODO: Extract to properties
    // TODO: Provide properly: this path format is for unit tests
    //private final String savedModelPath = "../../trainingAssets/models/1560101516";
    // TODO: Provide properly: this path format is for execution
    private final String savedModelPath = "trainingAssets/models/1560159859969/1560160000";
    private final String savedModelTags = "serve";

    private final static String FEED_OPERATION = "dnn/input_from_feature_columns/input_layer/concat";
    private final static String FETCH_OPERATION_CLASS_ID = "dnn/head/predictions/class_ids";
    private Session modelBundleSession;
    private InstructionLabel[] instructionLabels;


    public InstructionLabel classify(Hand hand) {
        if (modelBundleSession == null) {
            modelBundleSession = SavedModelBundle.load(savedModelPath, savedModelTags).session();
            instructionLabels = InstructionLabel.values();
        }

        Tensor inputTensor = InstructionClassificationService.createInputTensor(hand);

        Tensor result = this.modelBundleSession.runner()
                .feed(FEED_OPERATION, inputTensor)
                .fetch(FETCH_OPERATION_CLASS_ID)
                .run().get(0);

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
