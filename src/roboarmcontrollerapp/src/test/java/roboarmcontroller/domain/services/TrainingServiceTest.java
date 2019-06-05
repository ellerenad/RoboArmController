package roboarmcontroller.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import roboarmcontroller.domain.dom.Hand;
import roboarmcontroller.domain.dom.HandType;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.TrackingFrame;
import roboarmcontroller.file.TrainingSetWriter;

import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TrainingServiceTest {

    TrainingSetWriter trainingSetWriterMock;
    ExitService exitServiceMock;

    TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        this.trainingSetWriterMock = Mockito.mock(TrainingSetWriter.class);
        this.exitServiceMock = Mockito.mock(ExitService.class);
        this.trainingService = new TrainingService(trainingSetWriterMock, exitServiceMock);
    }

    @Test
    void test_process() {
        Hand leftHand = new Hand();
        leftHand.setType(HandType.LEFT);
        Hand rightHand = new Hand();
        rightHand.setType(HandType.RIGHT);
        TrackingFrame trackingFrame = new TrackingFrame();
        trackingFrame.setHands(Arrays.asList(leftHand, rightHand));

        trainingService.MAX_CYCLES = 1;

        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).init();
        verify(trainingSetWriterMock, times(1)).writeLine(Matchers.eq(leftHand), Matchers.eq(InstructionLabel.SERVO1));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(Matchers.eq(leftHand), Matchers.eq(InstructionLabel.SERVO2));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(Matchers.eq(leftHand), Matchers.eq(InstructionLabel.SERVO3));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(Matchers.eq(rightHand), Matchers.eq(InstructionLabel.DELTA_POSITIVE));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(Matchers.eq(rightHand), Matchers.eq(InstructionLabel.DELTA_NEGATIVE));

        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).terminate();
        verify(exitServiceMock, times(1)).terminateProgram(0);
    }
}