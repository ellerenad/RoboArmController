# RoboArmController

Repository for the Roboarm controller

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

Execution mode

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
3- Start the class roboarmcontroller.Application (in IntelliJ) with the spring profile "controlling"



Training mode:

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



Note: There is a known issue making the simulation loading to halt unless data is provided, 
so it is important to execute the steps in the order specified here, and input data by
 placing the hands over the sensor for the simulation to actually load. 


Optional to see the hand visualizer
```bash
 cd ~/.Leap\ Motion/
 Visualizer
```
