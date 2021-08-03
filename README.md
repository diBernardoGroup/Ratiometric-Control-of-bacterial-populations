# Ratiometric-Control-of-bacterial-populations

Code for the validation in silico of the ralay and PI control strategies in the context of solving the ratiometric control problem. The simulations are performed in BSim [1]. The source code for the BSim simulator can be found at https://github.com/CellSimulationLabs/bsim. More details on the simulation parmeters, on the control laws and on the ratiometric control problem can be found in [2]. 

[1] Matyjaszkiewicz, A., Fiore, G., Annunziata, F., Grierson, C. S., Savery, N. J., Marucci, L., & di Bernardo, M. (2017). BSim 2.0: an advanced agent-based cell simulator. ACS synthetic biology, 6(10), 1969-1972.
[2] Salzano, D., Fiore, D., di Bernardo, M., (2021) Controlling Reversible cell differentiation for labor division in microbial consortia. Under review.

# Description of the files

The contents of the folders /docs, /legacy, /sec/BSim, /test constitute the simulation environment BSim [1]. A description of their function can be found at https://github.com/CellSimulationLabs/bsim.

The folder /Control_Experiment_Bacteria contains all the code developed to validate in silico the control strategies developed in [2] to solve the ratietric control problem on a genetic toggle switch.
Specifically:

The folder bBacterium implements all the classes needed for the simulation of a E.Coli hosting a genetic toggle switch GRN.

The folder bControl contains the code implementing the control algorithms.

The folder bDrawer implements the code for drawing the bacteria and the microfuidoc chamber.

The folder bField contains the code for the implementation of the dynamics of the inducer molecules diffusing in the environment and inside the cells.

The folder bLogger allows for the storage of the experimental data

The folder bSolver implements an SDE solver algorithm (Euler Maruyama)

The folder bTicker overrides the ticker class native of BSim to allow diffusion of chemicals, dynamical evolution of the species inside each cell, cell movement, growth, division and flush out. 

The folder bsimMain contains the main script for the simulation and implements all the initializations needed.
