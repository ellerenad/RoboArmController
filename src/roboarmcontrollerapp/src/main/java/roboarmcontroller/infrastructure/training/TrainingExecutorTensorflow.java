package roboarmcontroller.infrastructure.training;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.dom.training.Evaluation;
import roboarmcontroller.domain.services.TrainingExecutor;

import javax.naming.OperationNotSupportedException;

@Service
@Profile("training & tensorflow")
public class TrainingExecutorTensorflow implements TrainingExecutor {

    @Override
    public Evaluation train(String datasetPath, String datasetIdentifier) throws Exception {
        throw new OperationNotSupportedException("Please execute the training manually using the provided Jupyter Notebooks. More info on the documentation.");
    }

}
