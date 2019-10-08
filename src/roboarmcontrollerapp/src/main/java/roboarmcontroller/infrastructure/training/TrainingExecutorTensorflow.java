package roboarmcontroller.infrastructure.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import roboarmcontroller.domain.services.TrainingExecutor;

@Service
@Profile("training & tensorflow")
public class TrainingExecutorTensorflow implements TrainingExecutor {
    private final Logger log = LoggerFactory.getLogger(TrainingExecutor.class);

    @Override
    public void train(String datasetName) {
        log.error("Operation not supported. Please execute the training manually using the provided Jupyter Notebooks. More info on the documentation");
    }
}
