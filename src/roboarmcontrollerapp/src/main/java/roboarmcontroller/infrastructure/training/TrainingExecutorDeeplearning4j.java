package roboarmcontroller.infrastructure.training;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.IteratorDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.services.TrainingExecutor;

import java.io.File;
import java.io.IOException;

@Service
@Profile("training & deeplearning4j")
public class TrainingExecutorDeeplearning4j  implements TrainingExecutor {
    private static Logger logger = LoggerFactory.getLogger(TrainingExecutorDeeplearning4j.class);

    private static final int CLASSES_COUNT = 5;
    private static final int LABEL_INDEX = 16;
    private static final int FEATURES_COUNT = 16;

    private static final double LEARNING_RATE = 0.1;
    private static final int BATCH_SIZE = 70;
    private static final int TOTAL_LINES = 5000;
    private static final double TRAIN_TO_TEST_RATIO = 0.6;
    private static final int SHUFFLE_SEED = 42;

    static final String STORED_MODEL_FILENAME = "/trainedModel.zip";
    static final String STORED_NORMALIZER_FILENAME = "/normalizer";


    private String modelOutputBasePath;

    public TrainingExecutorDeeplearning4j(@Value("${machine.learning.training.output.model.path}") String modelOutputBasePath) {
        this.modelOutputBasePath = modelOutputBasePath;
    }

    /**
     * Perform the whole training process, consisting in:
     * - Load the data
     * - Prepare it: shuffle, normalize
     * - Split into test and training sets
     * - Configure and train the Neural Network
     * - Store the model and the normalizer
     *
     * @return an object to evaluate the performance of the training of the Neural Network
     * @throws Exception
     */
    @Override
    // TODO: Check the path of the file
    public roboarmcontroller.domain.dom.training.Evaluation train(String datasetPath, String datasetIdentifier) throws Exception {

        // Load data
        DataSet allData = loadData(datasetPath);

        // Prepare data
        allData.shuffle(SHUFFLE_SEED);

        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(allData);
        normalizer.transform(allData);

        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(TRAIN_TO_TEST_RATIO); // The Neural Network seems to be overfitting - It has a better performance with less data
        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        DataSetIterator trainingDataSetIterator = new IteratorDataSetIterator(trainingData.iterator(), BATCH_SIZE);

        // Configure Neural Network
        MultiLayerConfiguration configuration = getMultiLayerConfiguration();

        // Train Neural Network
        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();
        model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates
        model.fit(trainingDataSetIterator);
        //model.fit(trainingData); // This won't work. You kidding me? maybe because of the normalization?

        // Save the model and the normalizer
        String modelOutputPath =  modelOutputBasePath + "/"+ datasetIdentifier + "/";
        store(model, normalizer, modelOutputPath);

        // Evaluate Neural Network
        Evaluation evaluation = evaluate(model, testData);
        logger.info(evaluation.stats());
        return map(evaluation);
    }

    private void store(MultiLayerNetwork model, DataNormalization normalizer, String outputPath) throws IOException {
        // Creating the folder to store the data
        File baseLocationToSaveModel = new File(outputPath);
        baseLocationToSaveModel.mkdirs();

        // Storing the model
        File locationToSaveModel = new File(outputPath + STORED_MODEL_FILENAME);
        model.save(locationToSaveModel, false);

        // Storing the normalizer
        File locationToSaveNormalizer = new File(outputPath + STORED_NORMALIZER_FILENAME);
        NormalizerSerializer.getDefault().write(normalizer, locationToSaveNormalizer);
        logger.info("Model and normalizer stored at {}", outputPath);
    }

    /**
     * Evaluate the trained MultiLayerNetwork
     *
     * @param testData the previously separated data to perform the test on
     * @param model    the model to test
     * @return Evaluation object
     */
    private static Evaluation evaluate(MultiLayerNetwork model, DataSet testData) {
        INDArray output = model.output(testData.getFeatures());
        Evaluation eval = new Evaluation(CLASSES_COUNT);
        eval.eval(testData.getLabels(), output);
        return eval;
    }

    /**
     * Load the data of a training set on a file
     *
     * @param path path relative to the classpath
     * @return a DataSet containing the data of the file
     * @throws IOException
     * @throws InterruptedException
     */
    private static DataSet loadData(String path) throws IOException, InterruptedException {
        DataSet allData;
        try (RecordReader recordReader = new CSVRecordReader(0, '|')) {
            recordReader.initialize(new FileSplit(new File(path)));
            DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, TOTAL_LINES, LABEL_INDEX, CLASSES_COUNT);
            allData = iterator.next();
        }
        return allData;
    }

    /**
     * Get the configuration of the Neural Network
     */
    private static MultiLayerConfiguration getMultiLayerConfiguration() {
        return new NeuralNetConfiguration.Builder()
                .activation(Activation.TANH)
                .updater(new Nesterovs(LEARNING_RATE, 0.9))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(3).build())
                .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nIn(3).nOut(CLASSES_COUNT).build())
                .build();
    }

    private static roboarmcontroller.domain.dom.training.Evaluation map(Evaluation evaluation){
        return new roboarmcontroller.domain.dom.training.Evaluation(evaluation.accuracy(),
                evaluation.precision(),
                evaluation.recall(),
                evaluation.f1());
    }

}
