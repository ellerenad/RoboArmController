This document aims to gather ideas for next steps:

- Add kafka to communicate the components?
- Add a more formal reactive programming approach to the controller?
- Currently, the controlling of the arm is solved as a classification problem, but it could be seen also as a regression problem. This would enable more expresive instructions.
- Improve UX: 
    - Initialization
    - Messages
    - Packaging of training sets and trained models (maybe put them together on a folder? is it really required to store them?) 
- Improve unit test coverage 
- Do integration tests 
- Data types on the TCP sockets communication: Would it be useful to send float instead of int?
- Generating the training data set: evaluate whether getting a relative position of the fingers make more sense. e.g. (tip position) - (center of the palm position) or (tip position) - (base of the metacarpal position)
- Use Ansible or similar to setup the environment with the required tools.

Open Issues:

- TCP Sockets on the simulation are blocking, causing the gazebo simulation to halt when there is no data. Possible partial fix: Open the socket on some event called after loading of the world.
- There are TODOs on the code :) 
- The simulation is just loaded if the startGazeboSim.sh script is executed on the RoboArmSimulation path, like:


```bash
cd RoboArmSimulation
./startGazeboSim.sh
```

------------------------------------------------

- add a shortcut to perform the training again on a given dataset and store it on the right place for loading it later 
    -> taken as an example the dataset 1571087156311, it got the following awful stats:
    ========================Evaluation Metrics========================
     # of classes:    5
     Accuracy:        0.5219
     Precision:       0.6009	(1 class excluded from average)
     Recall:          0.5377
     F1 Score:        0.5623	(1 class excluded from average)
    Precision, recall & F1: macro-averaged (equally weighted avg. of 5 classes) 
    
  - FIXME Fix the path where the model is stored after 
  

- consider adding a gitignore for the jupyter checkpoint file
- Migrate to tensorflow 2.0

------------------------