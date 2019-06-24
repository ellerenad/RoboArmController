# Description
This folder contains the jupyter notebook required to train the model of the RoboArmController.

It expects a training set on the path ```../../trainingAssets/sets/``` . Add the name of the training set to the variable ```trainingSetName```

It stores the trained model on the path ```../../trainingAssets/models```

# Setup

1.Install conda (visit website)
2.Configure environment
```
conda create -n roboarmcontroller pip python=3.7
conda activate roboarmcontroller
pip install --ignore-installed --upgrade jupyter
pip install --ignore-installed --upgrade tensorflow
pip install --ignore-installed --upgrade scipy
pip install --ignore-installed --upgrade pandas
```

# Execution

Execute the following shell commands

```
conda activate roboarmcontroller
jupyter notebook
```