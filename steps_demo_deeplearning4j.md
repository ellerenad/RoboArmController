Preparation:

prepare 3 terminal tabs

1-----------------
sudo leapd

2----------------
cdrobo
cd RoboArmSimulation
./startGazeboSim.sh

3----------------
cd .Leap\ Motion/
Visualizer

On the IDE, open the following files:
application-deeplearning4j.properties
TrainingExecutorDeeplearning4jTest.java

- Start leapd
- Start the visualizer
- Start the app in training mode
- if required, copy the stamp to the constant TrainingExecutorDeeplearning4jTest.DATASET_IDENTIFIER and run the test to generate the model
- copy the file stamp to the property machine.learning.controlling.input.exported.model.path in application-deeplearning4j.properties
- start gazebo
- start app in controlling ml mode
- have fun :)

