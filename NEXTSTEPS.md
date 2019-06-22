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


Open Issues:

- TCP Sockets on the simulation are blocking, causing the gazebo simulation to halt when there is no data. Possible partial fix: Open the socket on some event called after loading of the world.
- There are TODOs on the code :) 