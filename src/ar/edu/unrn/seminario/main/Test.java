package ar.edu.unrn.seminario.main;

import ar.edu.unrn.seminario.gui.*;

public class Test {
	
	public static void main(String[] args) {
		
		try {
			
			Setup setup = new Setup();
			setup.setVisible(true);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
