package roboarmcontroller.infrastructure.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingSetWriter {
    private final Logger log = LoggerFactory.getLogger(TrainingSetWriter.class);

    private static final String OUTPUT_FILE_EXTENTION = ".txt";

    FileWriter fileWriter;
    PrintWriter printWriter;
    String fileName;
    String fileStamp;

    public TrainingSetWriter(@Value("${machine.learning.training.output.file.path}") String fileName) {
        this.fileName = fileName;
    }

    public void init() {
        fileStamp = String.valueOf(System.currentTimeMillis());
        fileName = this.fileName + fileStamp + OUTPUT_FILE_EXTENTION;
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
