package ar.edu.unrn.seminario.gui;

import javax.swing.JTextField;

public class WrapperSeleccion {
	private JTextField campoTexto;
	
	public WrapperSeleccion(JTextField nuevoCampoTexto) {
		this.campoTexto = nuevoCampoTexto;
	}
	
	public void mostrarSeleccion(Object o) {
		this.campoTexto.setText(o.toString());
	}
}
