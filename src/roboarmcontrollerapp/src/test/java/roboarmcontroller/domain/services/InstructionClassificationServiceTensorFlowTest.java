package roboarmcontroller.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roboarmcontroller.domain.dom.*;
import roboarmcontroller.infrastructure.classification.InstructionClassificationServiceTensorFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: Improve
// Note: Asserting that all the predictions are what we expect is not the best way of testing:
// Machine learning models rarely have 100% confidence - some degree of tolerance should be accepted
class InstructionClassificationServiceTensorFlowTest {

    private static int FIELD_COUNT = InstructionClassificationServiceTensorFlow.FIELD_COUNT + 1;

    InstructionClassificationServiceTensorFlow instructionClassificationServiceTensorFlow;

    @BeforeEach
    void setUp() {
        String savedModelPath = this.readFileFromResources("trainedModel");
        this.instructionClassificationServiceTensorFlow = new InstructionClassificationServiceTensorFlow(savedModelPath);
    }

    @Test
    void classify_servo1() {
        List<String> lines = Arrays.asList(
                "0|40.0193|156.623|75.39|13.1936|168.191|-41.5366|-27.9762|169.497|-66.5691|-58.6029|167.19|-62.0447|-108.579|159.5|-40.8897|0",
                "0|40.023|156.624|75.3813|13.1976|168.195|-41.5931|-27.979|169.497|-66.6139|-58.6086|167.183|-62.0912|-108.592|159.493|-40.9196|0",
                "0|40.0258|156.626|75.3745|13.2014|168.198|-41.6489|-27.9821|169.496|-66.6595|-58.6149|167.176|-62.1393|-108.606|159.487|-40.95|0",
                "0|40.0283|156.627|75.3687|13.205|168.202|-41.7033|-27.9852|169.495|-66.7057|-58.6216|167.168|-62.1888|-108.621|159.48|-40.9812|0",
                "0|40.0305|156.628|75.3634|13.2082|168.205|-41.7562|-27.9887|169.494|-66.7526|-58.6287|167.16|-62.2396|-108.636|159.473|-41.0129|0"
        );

        lines.stream().forEach(line -> {

            Hand hand = this.getHandFromTrainingSetLine(line);

            InstructionLabel expectedInstructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

            InstructionLabel instructionLabel = this.instructionClassificationServiceTensorFlow.classify(hand);

            assertEquals(expectedInstructionLabel, instructionLabel);
        });
    }

    @Test
    void classify_servo2() {
        List<String> lines = Arrays.asList(
                "0|42.2366|159.253|55.567|-5.02637|182.497|-62.1005|-111.615|167.318|-57.1722|-45.2639|94.3725|-23.8295|-65.0036|95.4211|-11.6838|1",
                "0|42.2353|159.248|55.5738|-5.02637|182.497|-62.1005|-111.621|167.318|-57.1719|-45.2706|94.3808|-23.7878|-65.0057|95.4358|-11.6643|1",
                "0|42.2342|159.244|55.5788|-5.02637|182.497|-62.1005|-111.628|167.319|-57.1715|-45.2757|94.387|-23.7565|-65.0075|95.4483|-11.6478|1",
                "0|42.2334|159.242|55.5822|-5.02637|182.497|-62.1005|-111.633|167.319|-57.1712|-45.2788|94.3909|-23.7371|-65.0089|95.4582|-11.6346|1",
                "0|42.2328|159.24|55.5845|-5.02637|182.497|-62.1005|-111.637|167.319|-57.1709|-45.2808|94.3933|-23.7249|-65.0097|95.4638|-11.6272|1"
        );

        lines.stream().forEach(line -> {

            Hand hand = this.getHandFromTrainingSetLine(line);

            InstructionLabel expectedInstructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

            InstructionLabel instructionLabel = this.instructionClassificationServiceTensorFlow.classify(hand);

            assertEquals(expectedInstructionLabel, instructionLabel);
        });
    }

    @Test
    void classify_servo3() {
        List<String> lines = Arrays.asList(
                "0|-22.7949|120.709|7.14718|-27.389|130.103|5.17263|-40.779|123.408|5.5011|-61.7925|122.781|13.954|-76.3067|130.589|11.6654|2",
                "0|-22.7922|120.711|7.14907|-27.3911|130.115|5.21146|-40.7758|123.413|5.51605|-61.7649|122.775|14.0883|-76.2738|130.578|11.7462|2",
                "0|-22.7886|120.713|7.15158|-27.3934|130.129|5.25606|-40.771|123.42|5.53824|-61.7366|122.77|14.2248|-76.2404|130.567|11.828|2",
                "0|-22.7861|120.714|7.1533|-27.3963|130.147|5.3114|-40.7639|123.43|5.57123|-61.7079|122.764|14.3624|-76.2076|130.556|11.9081|2",
                "0|-22.7846|120.715|7.1544|-27.3993|130.166|5.37041|-40.7552|123.443|5.61131|-61.6791|122.758|14.4991|-76.1722|130.544|11.9942|2"
        );

        lines.stream().forEach(line -> {

            Hand hand = this.getHandFromTrainingSetLine(line);

            InstructionLabel expectedInstructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

            InstructionLabel instructionLabel = this.instructionClassificationServiceTensorFlow.classify(hand);

            assertEquals(expectedInstructionLabel, instructionLabel);
        });
    }

    @Test
    void classify_deltaPositive() {
        List<String> lines = Arrays.asList(
                "1|-57.0164|155.346|76.4006|-49.3625|156.233|-40.8263|-11.7221|155.567|-72.0333|36.5118|146.932|-76.6021|84.4475|129.491|-54.3838|3",
                "1|-57.0224|155.34|76.3954|-49.3601|156.223|-40.8357|-11.7215|155.557|-72.0391|36.5154|146.922|-76.6063|84.4499|129.482|-54.3847|3",
                "1|-57.0284|155.334|76.3903|-49.3577|156.212|-40.8451|-11.7209|155.547|-72.0449|36.5189|146.913|-76.6105|84.4522|129.472|-54.3857|3",
                "1|-57.0345|155.328|76.3843|-49.3554|156.202|-40.8544|-11.7203|155.538|-72.0506|36.5224|146.904|-76.6147|84.4544|129.463|-54.3866|3",
                "1|-57.0406|155.321|76.3785|-49.3531|156.192|-40.8636|-11.7197|155.529|-72.0563|36.5257|146.895|-76.6188|84.4566|129.454|-54.3876|3"
        );

        lines.stream().forEach(line -> {

            Hand hand = this.getHandFromTrainingSetLine(line);

            InstructionLabel expectedInstructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

            InstructionLabel instructionLabel = this.instructionClassificationServiceTensorFlow.classify(hand);

            assertEquals(expectedInstructionLabel, instructionLabel);
        });
    }

    @Test
    void classify_deltaNegative() {
        List<String> lines = Arrays.asList(
                "1|-62.5644|157.401|59.8666|13.4574|62.2588|32.7588|22.0244|58.9226|36.2842|34.6257|65.1053|32.006|39.8354|76.8773|19.1269|4",
                "1|-62.5626|157.381|59.8631|13.4722|62.2563|32.7705|22.0409|58.923|36.2975|34.6415|65.1069|32.0192|39.8471|76.8765|19.1401|4",
                "1|-62.5605|157.363|59.8595|13.487|62.254|32.7821|22.0576|58.924|36.311|34.6572|65.1087|32.0324|39.8587|76.876|19.1533|4",
                "1|-62.5584|157.344|59.8559|13.5016|62.2519|32.7936|22.0743|58.9254|36.3244|34.6728|65.1108|32.0454|39.8703|76.8758|19.1666|4",
                "1|-62.5564|157.327|59.8522|13.5162|62.2501|32.8053|22.0909|58.927|36.3378|34.6883|65.1131|32.0585|39.8818|76.8759|19.1799|4"
        );

        lines.stream().forEach(line -> {

            Hand hand = this.getHandFromTrainingSetLine(line);

            InstructionLabel expectedInstructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

            InstructionLabel instructionLabel = this.instructionClassificationServiceTensorFlow.classify(hand);

            assertEquals(expectedInstructionLabel, instructionLabel);
        });
    }

    @Test
    void testGetHandFromTrainingSetLine() {
        String line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|0";
        Hand hand = this.getHandFromTrainingSetLine(line);
        assertEquals(HandType.LEFT, hand.getType());
        assertEquals(5, hand.getFingers().size());
        assertEquals(34.3208, hand.getFingers().get(0).getPosition().getX(), 4);
        assertEquals(159.161, hand.getFingers().get(0).getPosition().getY(), 3);
        assertEquals(84.9007, hand.getFingers().get(0).getPosition().getZ(), 4);
        assertEquals(-58.1933, hand.getFingers().get(3).getPosition().getX(), 4);
        assertEquals(170.954, hand.getFingers().get(3).getPosition().getY(), 3);
        assertEquals(-57.0651, hand.getFingers().get(3).getPosition().getZ(), 4);
    }

    @Test
    void testGetInstructionLabelFromTrainingSetLine() {
        String line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|0";
        InstructionLabel instructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

        assertEquals(InstructionLabel.SERVO1, instructionLabel);

        line = "0|34.3208|159.161|84.9007|12.1871|171.868|-32.9388|-27.626|174.173|-59.5757|-58.1933|170.954|-57.0651|-107.979|160.152|-39.7621|3";
        instructionLabel = this.getInstructionLabelFromTrainingSetLine(line);

        assertEquals(InstructionLabel.DELTA_POSITIVE, instructionLabel);

    }

    /**
     * Utility method to make testing easier. There is a unit test for it as well.
     *
     * @param line
     * @return
     */
    private Hand getHandFromTrainingSetLine(String line) {
        String[] tokens = line.split(Pattern.quote("|"));

        if (tokens.length != FIELD_COUNT) {
            throw new RuntimeException("Invalid count of arguments");
        }

        List<Finger> fingers = new ArrayList<>();
        for (int i = 1; i < FIELD_COUNT - 1; i = i + 3) {

            float x = Float.parseFloat(tokens[i].trim());
            float y = Float.parseFloat(tokens[i + 1].trim());
            float z = Float.parseFloat(tokens[i + 2].trim());

            float[] positions = new float[]{x, y, z};

            FingerPosition fingerPosition = new FingerPosition(positions);
            Finger finger = new Finger();
            finger.setPosition(fingerPosition);
            fingers.add(finger);
        }

        Hand hand = new Hand();

        int handTypeOrdinal = Integer.parseInt(tokens[0].trim());
        hand.setType(HandType.values()[handTypeOrdinal]);

        hand.setFingers(fingers);

        return hand;
    }

    /**
     *  Utility method to make testing easier. There is a unit test for it as well.
     * @param line
     * @return
     */
    private InstructionLabel getInstructionLabelFromTrainingSetLine(String line) {
        String[] tokens = line.split(Pattern.quote("|"));

        if (tokens.length != FIELD_COUNT) {
            throw new RuntimeException("Invalid count of arguments");
        }

        int ordinal = Integer.parseInt(tokens[FIELD_COUNT - 1].trim());
        return InstructionLabel.values()[ordinal];
    }

    private String readFileFromResources(String resourcePath) {
        return getClass().getClassLoader().getResource(resourcePath).getPath();

    }

}