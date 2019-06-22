# RoboArmController

Repository for the Roboarm controller

## Prerequisites:

- gazebo http://gazebosim.org
- leap motion sdk (for the hands sensor)
- maven
- java sdk
- cmake
- plantuml (optional, architecture diagrams)



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

1- Each of the following commands on a different terminal 
```bash
sudo leapd
```
Note: Do the cd and then start the sh script. There is an open issue regarding it.
```bash
cd RoboArmSimulation
./startGazeboSim.sh
```
2- Optional to see the hand visualizer
```bash
 cd ~/.Leap\ Motion/
 Visualizer
```
3- Start the class roboarmcontroller.Application (in IntelliJ)


Note: There is a known issue making the simulation loading to halt unless data is provided, 
so it is important to execute the steps in the order specified here, and input data by
 placing the hands over the sensor for the simulation to actually load. 

