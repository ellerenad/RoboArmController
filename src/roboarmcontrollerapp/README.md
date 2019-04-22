# RoboArmControllerApp

Maven based project. 

Reads the data from the websocket, cleans it and transforms it to our preferred domain model.

On this version, this app also transforms the domain into instructions for the robot simulation.

Start point: Application.java
Data Input: LeapMotionHandler.java (Listener of the websocket) 
Data Output: executing a bash command that communicates to the robot simulation. More info below.



## Bash command for communication with the robot simulation

For this program to execute, change the path of the remote_ctrl_robo_arm command at CommandExecutor class

> DIR_PATH

Format of the command:
> remote_ctrl_robo_arm \<ServoNumber:1,2,3> \<delta:float> 

> remote_ctrl_robo_arm 1 -.3 


Note: There are huge performance problems on this approach of communication with the robot simulation. Abandoning it to refactor.

## Websocket

The websocket is started by the command

> sudo leapd

and with URI 

>ws://localhost:6437/v7.json