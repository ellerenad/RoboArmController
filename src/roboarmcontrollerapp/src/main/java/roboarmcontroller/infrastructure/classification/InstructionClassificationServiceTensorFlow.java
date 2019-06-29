package roboarmcontroller.infrastructure.classification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import roboarmcontroller.domain.dom.Finger;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.services.InstructionClassificationService;

@Service
public class InstructionClassificationServiceTensorFlow implements InstructionClassificationService {

    public final static int FIELD_COUNT = 16;
    private final static String FEED_OPERATION = "dnn/input_from_feature_columns/input_layer/concat";
    private final static String FETCH_OPERATION_CLASS_ID = "dnn/head/predictions/class_ids";
    private final static String SAVED_MODEL_TAGS = "serve";

    private Session modelBundleSession;
    private InstructionLabel[] instructionLabels;

    String savedModelPath;

    public InstructionClassificationServiceTensorFlow(@Value("${machine.learning.controlling.input.exported.model.path}") String savedModelPath) {
        this.savedModelPath = savedModelPath;
    }

    @Override
    public InstructionLabel classify(Hand hand) {
        if (modelBundleSession == null) {
            modelBundleSession = SavedModelBundle.load(savedModelPath, SAVED_MODEL_TAGS).session();
            instructionLabels = InstructionLabel.values();
        }

        Tensor inputTensor = InstructionClassificationServiceTensorFlow.createInputTensor(hand);

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
        // the order of the data on the input was taken from the saved_model, node dnn/input_from_feature_columns/input_layer/concat
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
