RoboArm simulation using gazebosim

1.- build the plugin with
./plugins/build.sh

2.- start gazebo with the world using 
./startGazeboSim.sh

3.- a simple remote contoller can be used like:
./plugins/build/remote_ctrl_robo_arm <servoNumer> <delta>

./remote_ctrl_robo_arm 1 -.3


TODOS

1.- create a folder for the servo controller plugin
2.- split the remote controller to a separate folder (and update this doc with the change)
