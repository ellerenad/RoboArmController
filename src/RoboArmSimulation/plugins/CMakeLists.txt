cmake_minimum_required(VERSION 2.8 FATAL_ERROR)

# Find Gazebo
find_package(gazebo REQUIRED)
include_directories(${GAZEBO_INCLUDE_DIRS})
link_directories(${GAZEBO_LIBRARY_DIRS})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${GAZEBO_CXX_FLAGS}")

# Build our plugin
add_library(robo_arm_plugin SHARED RoboArmPlugin.cc )
target_link_libraries(robo_arm_plugin ${GAZEBO_LIBRARIES})

# Build the server test program
# add_executable(server_test Server.cc serverMain.cc)