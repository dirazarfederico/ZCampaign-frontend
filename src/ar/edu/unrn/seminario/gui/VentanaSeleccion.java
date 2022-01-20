package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;

public abstract class VentanaSeleccion extends JFrame {

	private WrapperSeleccion seleccion;
	
	public VentanaSeleccion(WrapperSeleccion seleccion) {
		this.seleccion = seleccion;
	}
	
	public void mostrarSeleccion(Object o) {
		this.seleccion.mostrarSeleccion(o);
	}
	
}
