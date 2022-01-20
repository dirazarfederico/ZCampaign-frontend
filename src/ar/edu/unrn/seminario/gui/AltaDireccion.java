package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.JButton;

import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.modelo.*;

public class AltaDireccion extends VentanaSeleccion {

	private JPanel contentPane;
	private JTextField txtCalle;
	private JTextField txtNumero;
	private JTextField txtLatitud;
	private JTextField txtLongitud;
	private JTextField txtCiudad;
	private Direccion direccion;
	private JButton btnAceptar;

	/**
	 * @wbp.parser.constructor
	 */
	public AltaDireccion(WrapperSeleccion nuevoWrapperSeleccion) {
		
		super(nuevoWrapperSeleccion);
		
		setTitle("Alta de Dirección");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		setResizable(false);
		
		JLabel lblCalle = new JLabel("Calle *");
		lblCalle.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCalle.setBounds(10, 11, 81, 27);
		contentPane.add(lblCalle);
		
		JLabel lblCamposObligatorios = new JLabel("* Campos obligatorios");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(263, 11, 156, 27);
		contentPane.add(lblCamposObligatorios);
		
		txtCalle = new JTextField();
		txtCalle.setBounds(10, 49, 220, 20);
		contentPane.add(txtCalle);
		txtCalle.setColumns(10);
		
		JLabel lblNumero = new JLabel("Número *");
		lblNumero.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblNumero.setBounds(10, 80, 81, 27);
		contentPane.add(lblNumero);
		
		txtNumero = new JTextField();
		txtNumero.setBounds(10, 118, 92, 20);
		contentPane.add(txtNumero);
		txtNumero.setColumns(10);
		
		JLabel lblLatitud = new JLabel("Latitud *");
		lblLatitud.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblLatitud.setBounds(10, 149, 81, 27);
		contentPane.add(lblLatitud);
		
		txtLatitud = new JTextField();
		txtLatitud.setBounds(10, 187, 220, 20);
		contentPane.add(txtLatitud);
		txtLatitud.setColumns(10);
		
		JLabel lblLongitud = new JLabel("Longitud *");
		lblLongitud.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblLongitud.setBounds(10, 218, 81, 27);
		contentPane.add(lblLongitud);
		
		txtLongitud = new JTextField();
		txtLongitud.setBounds(10, 256, 220, 20);
		contentPane.add(txtLongitud);
		txtLongitud.setColumns(10);
		
		JLabel lblCiudad = new JLabel("Ciudad *");
		lblCiudad.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCiudad.setBounds(10, 287, 81, 27);
		contentPane.add(lblCiudad);
		
		txtCiudad = new JTextField();
		txtCiudad.setBounds(10, 325, 220, 20);
		contentPane.add(txtCiudad);
		txtCiudad.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(335, 367, 89, 23);
		contentPane.add(btnAceptar);
		btnAceptar.addActionListener((e)-> this.obtenerDireccion());
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(236, 367, 89, 23);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener((e)->this.dispose());
	}
	
	public AltaDireccion(Direccion direccionPrevia, WrapperSeleccion nuevoWrapperSeleccion) {
		
		this(nuevoWrapperSeleccion);
		
		this.direccion = direccionPrevia;
		
		this.mostrarSeleccion(direccionPrevia);
		
		setTitle("Actualización de dirección");
		
		txtCalle.setText(direccionPrevia.getCalle());
		txtNumero.setText(new Integer(direccionPrevia.getNumero()).toString());
		txtLatitud.setText(direccionPrevia.getLatitud());
		txtLongitud.setText(direccionPrevia.getLongitud());
		txtCiudad.setText(direccionPrevia.getCiudad());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
		
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
		
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarDireccion(direccionPrevia));
		
	}
	
	private void obtenerDireccion() {
		
		String calle, latitud, longitud, ciudad;
		int numero;
		
		try {
			
			calle = txtCalle.getText().trim();
			latitud = txtLatitud.getText().trim();
			longitud = txtLongitud.getText().trim();
			ciudad = txtCiudad.getText().trim();
		
			numero = Integer.parseInt(txtNumero.getText().trim());
		
			this.direccion = new Direccion(calle, numero, latitud, longitud, ciudad);
			
			this.mostrarSeleccion(this.direccion);
			
			this.dispose();
		
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		}
	}
	
	private void modificarDireccion(Direccion nuevaDireccion) {
		
		String calle, latitud, longitud, ciudad;
		int numero;
		
		try {
			
			calle = txtCalle.getText().trim();
			latitud = txtLatitud.getText().trim();
			longitud = txtLongitud.getText().trim();
			ciudad = txtCiudad.getText().trim();
		
			numero = Integer.parseInt(txtNumero.getText().trim());
			
			Long idDireccion = nuevaDireccion.getId();
			
			nuevaDireccion = new Direccion(calle, numero, latitud, longitud, ciudad);
			
			nuevaDireccion.setId(idDireccion);
			
			this.direccion = nuevaDireccion;
			
			this.mostrarSeleccion(this.direccion);
			
			this.dispose();
		
		}
		catch(Exception e) {
			
			JOptionPane.showMessageDialog(this, e.getMessage());
			
		}
	}
	
	public Direccion getDireccion() {
		return this.direccion;
	}
}
