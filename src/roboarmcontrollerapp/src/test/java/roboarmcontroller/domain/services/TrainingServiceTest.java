package roboarmcontroller.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roboarmcontroller.domain.dom.InstructionLabel;
import roboarmcontroller.domain.dom.hands.Hand;
import roboarmcontroller.domain.dom.hands.HandType;
import roboarmcontroller.domain.dom.hands.TrackingFrame;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    TrainingSetWriter trainingSetWriterMock;
    ExitService exitServiceMock;
    TrainingExecutor trainingExecutorMock;

    TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        this.trainingSetWriterMock = Mockito.mock(TrainingSetWriter.class);
        this.exitServiceMock = Mockito.mock(ExitService.class);
        this.trainingExecutorMock = Mockito.mock(TrainingExecutor.class);
        this.trainingService = new TrainingService(trainingSetWriterMock, exitServiceMock, trainingExecutorMock);
    }

    @Test
    void test_process() {
        String fileName = "fileName";
        when(trainingSetWriterMock.getFileName()).thenReturn(fileName);

        Hand leftHand = new Hand();
        leftHand.setType(HandType.LEFT);
        Hand rightHand = new Hand();
        rightHand.setType(HandType.RIGHT);
        TrackingFrame trackingFrame = new TrackingFrame();
        trackingFrame.setHands(Arrays.asList(leftHand, rightHand));

        TrainingService.MAX_CYCLES = 1;

        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).init();
        verify(trainingSetWriterMock, times(1)).writeLine(eq(leftHand), eq(InstructionLabel.SERVO1));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(eq(leftHand), eq(InstructionLabel.SERVO2));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(eq(leftHand), eq(InstructionLabel.SERVO3));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(eq(rightHand), eq(InstructionLabel.DELTA_POSITIVE));

        trainingService.process(trackingFrame);
        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, times(1)).writeLine(eq(rightHand), eq(InstructionLabel.DELTA_NEGATIVE));

        trainingService.process(trackingFrame);
        verify(trainingSetWriterMock, atLeastOnce()).getFileName();
        verify(trainingExecutorMock).train(eq(fileName));
        verify(trainingSetWriterMock, times(1)).terminate();
        verify(exitServiceMock, times(1)).terminateProgram(0);
    }
}