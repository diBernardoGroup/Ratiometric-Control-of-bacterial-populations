package bControl;

import java.util.Vector;

import bBacterium.BSimControlledBacterium;

public class Controller {

	private double ref;
	private double aTcVal;
	private double IPTGVal;
	private int state_c;
	private int controller_type;
	private double Ts;
	private double katc1,katc2,kiptg1,kiptg2;
	private double Ia,Ii;
	
	
	public Controller (double ref,double aTcVal,double IPTGVal) {
		this.aTcVal=aTcVal;
		this.ref=ref;
		this.IPTGVal=IPTGVal;
		state_c=1;
		controller_type=0;
	}
	
	public Controller (double ref,double aTcVal,double IPTGVal,double katc1,double katc2,double kiptg1,double kiptg2,double Ts) {
		this.aTcVal=aTcVal;
		this.ref=ref;
		this.IPTGVal=IPTGVal;
		this.katc1=katc1;
		this.katc2=katc2;
		this.kiptg1=kiptg1;
		this.kiptg2=kiptg2;
		this.Ts=Ts;
		Ia=0;
		Ii=0;
		controller_type=1;
	}
	
	
	public double Evaluate_Control(double [] e) {
		
		double Eh=e[0];
		double El=e[1];
		
		double u_a=-1;
		
		if(controller_type==0) {	
			
			if (Math.abs(Eh)>Math.abs(El)) {
	            state_c=1;
	        }
	        else {
	            state_c=0;
	        }
	        

	        if (state_c==1) {	                
	            if (Eh<0) {
	                u_a=0;
	            }   
	            else {
	                u_a=aTcVal;
	            }
	        }   
	        else {
	            
	            if (El<0) {
	                u_a=aTcVal;
	            }
	            else {
	                u_a=0;
	            }
	        }
	        
		}
		
		if(controller_type==1) {
			double sata;
			double Pa,Pi;
			
			
			if (Math.abs(El)>Math.abs(Eh)) {
	            sata=50;
	        }
	        else {
	            sata=aTcVal;
	        }
	        
	        
	        Pa=katc1*Eh;
	        Pi=kiptg1*El;
	        if ((Pa+Ia+katc2*Eh*Ts-kiptg1*El-(Ii+kiptg2*El*Ts))>=sata) {
	           if (Eh>0) {
	               
	           }else {
	               Ia=Ia+katc2*Eh*Ts;
	           }
	           if (El>0) {
	               Ii=Ii+kiptg2*El*Ts;
	           }else {
	               
	           }
	        }else{
	        	if ((Pa+Ia+katc2*Eh*Ts-kiptg1*El-(Ii+kiptg2*El*Ts))<=0) {
	        
	        		if (Eh>0) {
	        			Ia=Ia+katc2*Eh*Ts;
	        		}else {
	               
	        		}
	        		if (El>0) {
	               
	        		}else {
	        			Ii=Ii+kiptg2*El*Ts;
	        		}
	        
	        	}else {
	        		Ia=Ia+katc2*Eh*Ts;
	                Ii=Ii+kiptg2*El*Ts;
	        	}        
	        }
	        
	        u_a=Pa+Ia-(Pi+Ii);
	        
			
	        if (u_a>sata) {
	            u_a=sata;
	        }
	        if (u_a<=0) {
	        	u_a=0;
	        }
		}
		

        
        return u_a;
	}
	
	
	public double[] Evaluate_Error(Vector<BSimControlledBacterium> bacteria) {
		
		
		// Each bacterium performs its action
		double hRatio=0;
		double lRatio=0;
		double iRatio=0;
		double lim=2;
		
		
		double [] errs=new double [2];
		
		
		for(BSimControlledBacterium b : bacteria) {
			if(b.y[2]>(lim*b.y[3])) {
				hRatio=hRatio+1;
			}
			else {
				if(b.y[3]>(lim*b.y[2])) {
					lRatio=lRatio+1;
				}
				else {
					iRatio=iRatio+1;
				}
			}
		}
		
		hRatio=hRatio/bacteria.size();
		lRatio=lRatio/bacteria.size();
		iRatio=iRatio/bacteria.size();
		
		double Eh=ref-hRatio;
		double El=(1-ref)-lRatio;
		
		errs[0]=Eh;
		errs[1]=El;
		
		return errs;
		
		
	}
	
	
	public double getIPTGVal() {
		return IPTGVal;		
	}
	
	public double getaTcVal() {
		return aTcVal;		
	}
	
	
	
}
