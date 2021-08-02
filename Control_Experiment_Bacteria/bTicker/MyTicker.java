package bTicker;

import java.util.Vector;


import bBacterium.*;
import bControl.Controller;
import bsim.BSim;
import bsim.BSimChemicalField;
import bsim.BSimTicker;
import bsim.capsule.BSimCapsuleBacterium;
import bsim.capsule.Mover;
import bsim.capsule.RelaxationMover;
import bsim.capsule.RelaxationMoverGrid;
import bField.*;

public class MyTicker extends BSimTicker{
	
	//Bacteria Vectors
	private Vector<BSimControlledBacterium> bacteria;
	private Vector<BSimControlledBacterium> childBacteria;
	private Vector<BSimCapsuleBacterium> moveBacteria;
	private Vector<BSimControlledBacterium> removedBacteria;
	private Vector<BSimControlledBacterium> allBacteria;
	
	//Chemical Fields
	private ControlledFIeld faTc;
	private ControlledFIeld fIPTG;
	
	//Simulation Parameters
	private BSim sim;
	private int poplim;
	
	//Auxiliary Class to move Bacteria
	private Mover mover;
	
	//Constraints Parameters
	private double Ts;
	private double Tc;
	private double Td;
	
	//Controller Class
	private Controller ctrl;
	
	
	//Auxiliary Variables 
	private double L_s;
	private double L_c;
	private boolean F_c;
	private double u_a;
	private double u_p;
	private double [] curr_e;
	

	
	public MyTicker(Vector<BSimControlledBacterium> bacteria,BSimChemicalField faTc,BSimChemicalField fIPTG,BSim simul,Vector<BSimCapsuleBacterium> moveBacteria,Vector<BSimControlledBacterium> allBacteria,int poplim,Controller ctrl,double Ts,double Tc,double Td) {
		this.bacteria=bacteria; 									//The current bacteria
		this.faTc=(ControlledFIeld)faTc; 							//The aTC field
		this.fIPTG=(ControlledFIeld)fIPTG;	 						//the IPTG field
		sim=simul;													//Main simulation class
		this.moveBacteria=moveBacteria;								//Bacteria to move
		this.childBacteria=new Vector<BSimControlledBacterium>();	// Temporary array to storage new bacteria
		this.removedBacteria=new Vector<BSimControlledBacterium>(); // Temporary array to storage flushed out bacteria
		this.allBacteria=allBacteria;								// All bacteria ever created in this simulation
		this.poplim=poplim;											//Population limit (Computational burden)
		mover=new RelaxationMoverGrid(moveBacteria, sim);			// Mover class (moves bacteria)
		
		//CONSTRAINTS PARAMETERS
		this.Ts=Ts;
		this.Tc=Tc;
		this.Td=Td;
		
		//Controller class
		this.ctrl=ctrl;
		
		
		//AUXILIARY VARIABLES
		L_s=-Ts;
		L_c=-Tc;
		F_c=false;
		u_a=0;
		u_p=0;
		
		
	}
	
	
	// This will be called once at each global time step
				@Override
				public void tick() {
					
					/** Step 1: Update all the dynamics correctly   */
					
					
					for (BSimControlledBacterium b : bacteria) {
                        b.action();
                    }
					
					
					
					double curr_t=sim.getTime()/60;
					
					//Something I do every sampling time
					if (curr_t>=L_s+Ts) {
						
						
						curr_e=ctrl.Evaluate_Error(bacteria);				//Compute the control error
						System.out.println("eh: "+curr_e[0]+" el: "+curr_e[1]);
						u_a=ctrl.Evaluate_Control(curr_e);					//Compute the control input
						u_p=(1-u_a/ctrl.getaTcVal())*ctrl.getIPTGVal();		//Compute u_p (convex combination)
						
						
						
						L_s=curr_t;											//Update the last time it has been sampled
						
						
					}
					
					//Something I do every control time
					if (curr_t>=L_c+Tc) {
						
						L_c=curr_t;											//Update the last time it has been updated the control		
						F_c=true;
						
					}
					
					
					//Wait Tdiff and update the actual control provided to cells 
					if (F_c==true) {
						
						if(curr_t>=L_c+Td) {
							faTc.setControl(u_a);
							fIPTG.setControl(u_p);
							F_c=false;
						}
						
						
					}
					
					
					
					
					
					// Update our chemical fields values with their ODE
					faTc.updateValues();
					fIPTG.updateValues();

					
					/** Step 2: Update all the position and performs the diffusion */
					
					//Update by diffusion and degradation
					faTc.update();
					fIPTG.update();


					for (BSimControlledBacterium b : bacteria) {
                        if(bacteria.size()+childBacteria.size()<poplim) {
                        	b.grow();
                            // Divide if grown past threshold
                            if (b.L > b.L_th) {
                            	childBacteria.add(b.divide()); // The initialization is done in the divide()
                            }
                        }
                    }
					
					
					// Add freshly bred bacteria
					bacteria.addAll(childBacteria);
					moveBacteria.addAll(childBacteria);
					allBacteria.addAll(childBacteria);
					childBacteria.clear();
					
					
					//move the bacteria
					mover.move();
					
					//Remove the flushed out bacteria
					for (BSimControlledBacterium b : bacteria) {
                        if(b.position.x < 0 || b.position.x > sim.getBound().x || b.position.y < 0 || b.position.y > sim.getBound().y || b.position.z < 0 || b.position.z > sim.getBound().z){
                            removedBacteria.add(b);
                            for(int i=0;i<6;i++) { //Set their state to 0 (Distinguish from not flushed out bacteria)
                            	b.y[i]=0;
                            }
                        }
                    }
					bacteria.removeAll(removedBacteria);
					moveBacteria.removeAll(removedBacteria);
					removedBacteria.clear();
					

					
				}

}
