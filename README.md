# RoboArmController

Repository for the Roboarm controller. 

This is a controller for a Simulated Robotic Arm. With the help of a Leap Motion hands sensor, we train a Machine Learning model to identify the position of the fingers of a person, and we use that to indicate a movement to the Robotic Arm. The main focus of this exercise is to learn the different technologies used for it: Gazebo, Kafka, Tensorflow, and Java.

Here you can find a couple of youtube videos to show it working:

Controlling

[![Controlling](https://img.youtube.com/vi/hz7BYlV3m8k/0.jpg)](https://www.youtube.com/watch?v=hz7BYlV3m8k)

Training and controlling steps

[![Training and controlling steps](https://img.youtube.com/vi/deWE3Fv8uAE/0.jpg)](https://www.youtube.com/watch?v=deWE3Fv8uAE)

Disclaimer: The arm seems to move slowly in the video, because of issues on the recording of the video itself.


## Prerequisites:

- gazebo http://gazebosim.org
- leap motion sdk (for the hands sensor)
- maven
- java sdk
- cmake
- plantuml (optional, architecture diagrams)
- conda (see readme of the training module)


## Start leapmotion

```bash
sudo leapd
```

### Other important commands

```bash
cd .Leap\ Motion/
Visualizer
LeapControlPanel
LeapControlPanel --showsettings
```

### Websocket info:
https://developer-archive.leapmotion.com/documentation/java/supplements/Leap_JSON.html



### Start the system:

####Machine Learning Controlling mode

Each of the following commands on a different terminal 

1- Start the leap motion framework
```bash
sudo leapd
```
2- Start the simulation
Note: Do the cd and then start the sh script. There is an open issue regarding it.
```bash
cd RoboArmSimulation
./startGazeboSim.sh
```
3- Start the class roboarmcontroller.Application (in IntelliJ) with the spring profile "controllingML"



#### Training mode:

1- Start the leap motion framework
```bash
sudo leapd
```

2- Start the class roboarmcontroller.Application (in IntelliJ) with the spring profile "training"

3- Start the training module
```bash
cd RoboArmController/src
conda activate roboarmcontroller
jupyter notebook
```

4- Copy the timestamp printed out on the roboarmcontroller logs

5- Execute the jupyter notebook `training/RoboArmControllerTraining.ipynb`

6- Copy the path of the exported model to the property `training.input.exported.model.path` 

#### Simple Controlling Mode

Each of the following commands on a different terminal 

1- Start the leap motion framework
```bash
sudo leapd
```
2- Start the simulation
Note: Do the cd and then start the sh script. There is an open issue regarding it.
```bash
cd RoboArmSimulation
./startGazeboSim.sh
```
3- Start the class roboarmcontroller.Application (in IntelliJ) with the spring profile "controllingSimple"


Note: There is a known issue making the simulation loading to halt unless data is provided, 
so it is important to execute the steps in the order specified here, and input data by
 placing the hands over the sensor for the simulation to actually load. 


Optional to see the hand visualizer
```bash
 cd ~/.Leap\ Motion/
 Visualizer
```
