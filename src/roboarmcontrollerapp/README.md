# RoboArmControllerApp

Maven based project. 

Reads the data from the websocket, cleans it and transforms it to our preferred domain model.

On this version, this app also transforms the domain into instructions for the robot simulation.

Start point: Application.java
Data Input: LeapMotionHandler.java (Listener of the websocket) 
Data Output: executing a bash command that communicates to the robot simulation.

## Websocket

The websocket is started by the command

> sudo leapd

and with URI 

>ws://localhost:6437/v7.json