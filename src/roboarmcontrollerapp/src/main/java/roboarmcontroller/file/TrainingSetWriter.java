package roboarmcontroller.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.InstructionLabel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TrainingSetWriter {
    private final Logger log = LoggerFactory.getLogger(TrainingSetWriter.class);

    FileWriter fileWriter;
    PrintWriter printWriter;
    String fileName;
    String fileStamp;

    public void init() {
        fileStamp = String.valueOf(System.currentTimeMillis());
        fileName = "trainingAssets/sets/trainingSet_" + fileStamp + ".txt";
        try {
            File file = new File(fileName);
            file.createNewFile();
            fileWriter = new FileWriter(file);
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException e) {
            log.error("Not possible to start with the training. Error opening the file.", e);
            throw new RuntimeException(e);
        }
    }

    public void writeLine(Hand hand, InstructionLabel instructionLabel) {
        String handLine = this.getLine(hand);
        printWriter.printf("%s|%s\n", handLine, String.valueOf(instructionLabel.ordinal()));
    }

    public void terminate() {
        printWriter.close();
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileStamp() {
        return fileStamp;
    }

    String getLine(Hand hand) {
        List<String> parts = new ArrayList<>();
        parts.add(String.valueOf(hand.getType().ordinal()));
        hand.getFingers().stream().forEach(finger -> {
            parts.add(String.valueOf(finger.getPosition().getX()));
            parts.add(String.valueOf(finger.getPosition().getY()));
            parts.add(String.valueOf(finger.getPosition().getZ()));
        });

        return String.join("|", parts);
    }

}
