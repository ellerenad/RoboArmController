#!/bin/bash

export GAZEBO_PLUGIN_PATH=${GAZEBO_PLUGIN_PATH}:$(pwd)/plugins/build/
export GAZEBO_MODEL_PATH=$(pwd)/models/:${GAZEBO_MODEL_PATH}

gazebo --verbose ./worlds/robo_arm.world
