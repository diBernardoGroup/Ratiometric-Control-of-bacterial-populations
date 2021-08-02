package bLogger;


import bsim.BSim;
import bsim.BSimChemicalField;
import bsim.export.BSimLogger;

public class ControlLogger extends BSimLogger {

	
	BSimChemicalField a;
	BSimChemicalField i;
	
	public ControlLogger(BSim sim, String filename,BSimChemicalField a,BSimChemicalField i) {
		super(sim,filename);
		this.a=a;
		this.i=i;
	}
	
	
	@Override
	public void during() {
		
		String buffer = new String();
		buffer=a.getConc(0,0,0)+","+i.getConc(0,0,0)+" ";
		write(buffer);
		
	}

}
