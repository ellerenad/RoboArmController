cmake_minimum_required(VERSION 2.8 FATAL_ERROR)

# Find Gazebo
find_package(gazebo REQUIRED)
include_directories(${GAZEBO_INCLUDE_DIRS})
link_directories(${GAZEBO_LIBRARY_DIRS})
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${GAZEBO_CXX_FLAGS}")

# Build our plugin
add_library(robo_arm_plugin SHARED robo_arm_plugin.cc)
target_link_libraries(robo_arm_plugin ${GAZEBO_LIBRARIES})



# Build the stand-alone test program
add_executable(remote_ctrl_robo_arm remote_ctrl_robo_arm.cc)

if (${gazebo_VERSION_MAJOR} LESS 6)
  # These two
  include(FindBoost)
  find_package(Boost ${MIN_BOOST_VERSION} REQUIRED system filesystem regex)
  target_link_libraries(remote_ctrl_robo_arm ${GAZEBO_LIBRARIES} ${Boost_LIBRARIES})
else()
  target_link_libraries(remote_ctrl_robo_arm ${GAZEBO_LIBRARIES})
endif()
# END OF Build the stand-alone test program
