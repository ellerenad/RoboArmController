package roboarmcontroller.domain.dom.training;

/**
 * Holds information concerning the evaluation of a trained model
 */
public class Evaluation {

    double accuracy;
    double precision;
    double recall;
    double f1;

    public Evaluation(double accuracy, double precision, double recall, double f1) {
        this.accuracy = accuracy;
        this.precision = precision;
        this.recall = recall;
        this.f1 = f1;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getF1() {
        return f1;
    }
}
