RoboArm simulation using gazebosim

1.- build the controller plugin with
./plugins/build.sh

2.- start gazebo with the world using 
./startGazeboSim.sh

3.- A read socket is open on port 52333. It reads 2 integers from a package of bytes, to indicate the number of the servo and the delta. This delta is divided by a constant. More info in known issues.


TODOS




Known Issues

1.- The server socket currently reads just integers. To compensate this, the received delta is divided by a constant, since the servo requires decimal positions. 
2.- The server socket is blocking, so for the simulation to load properly, is required to open a connection to it.
3.- In some tests, it appeared to be important to wait at least 1 millisecond before sending a new message to the socket.