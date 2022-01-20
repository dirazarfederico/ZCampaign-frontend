package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.Donacion;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;

public class FinalizarOrden extends JFrame {

	private JPanel contentPane;
	private JTextField textoDescripcion;
	private JButton btnAceptar;
	private JTextField textoOrden;
	private IFacade api;
	private OrdenDeRetiro orden;
	private IVentanaGestion padre;

	/**
	 * @wbp.parser.constructor
	 */
	public FinalizarOrden(IVentanaGestion nuevoPadre, OrdenDeRetiro nuevaOrden) {
		setResizable(false);
		setTitle("Finalizar una orden");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 543, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.orden = nuevaOrden;
		this.padre = nuevoPadre;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JLabel lblDescripcion = new JLabel("Descripción *");
		lblDescripcion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblDescripcion.setBounds(10, 79, 94, 21);
		contentPane.add(lblDescripcion);
		
		textoDescripcion = new JTextField();
		textoDescripcion.setBounds(10, 111, 333, 20);
		contentPane.add(textoDescripcion);
		textoDescripcion.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(419, 142, 89, 23);
		btnAceptar.addActionListener((e)->finalizarOrden());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(321, 142, 89, 23);
		btnCancelar.addActionListener((e)->this.dispose());
		contentPane.add(btnCancelar);
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios  *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(345, 11, 163, 21);
		contentPane.add(lblCamposObligatorios);
		
		textoOrden = new JTextField();
		textoOrden.setEditable(false);
		textoOrden.setBounds(10, 48, 497, 20);
		textoOrden.setText(nuevaOrden.toString());
		contentPane.add(textoOrden);
		textoOrden.setColumns(10);
		
		JLabel lblOrden = new JLabel("Orden:");
		lblOrden.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblOrden.setBounds(10, 17, 94, 21);
		contentPane.add(lblOrden);
	}
	
	public FinalizarOrden(IVentanaGestion padre, Donacion donacion) {
		
		this(padre, donacion.getOrden());
		
		this.textoOrden.setText(donacion.getOrden().toString());
		this.textoOrden.setEnabled(false);
		
		this.setTitle("Actualización de Donación");
		
		this.textoDescripcion.setText(donacion.getDescripcion());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
			
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarDonacion(donacion));
		
	}
	
	private void finalizarOrden() {
		
		try {
			String descripcion = textoDescripcion.getText().trim();
			
			this.dispose();
			
			api.finalizarOrden(this.orden.getId(), descripcion);
			
			padre.refrescar();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarDonacion(Donacion donacion) {
	
		try {
			
			String descripcion = textoDescripcion.getText().trim();
			
			Donacion nuevaDonacion = new Donacion(donacion.getOrden(), descripcion);
			nuevaDonacion.setId(donacion.getId());
			
			api.actualizarDonacion(nuevaDonacion);
			
			this.dispose();
			
			padre.refrescar();
	
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
