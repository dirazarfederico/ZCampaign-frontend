package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class AltaLugarHabilitado extends JFrame {

	private JPanel contentPane;
	private JTextField textoNombre;
	private JTextField textoContacto;
	private JButton btnDireccion;
	private AltaDireccion altaDireccion;
	private Campaña campaña;
	private IFacade api;
	private JButton btnAceptar;
	private JTextField textoDireccionSeleccionada;
	private IVentanaGestion padre;
	
	/**
	 * @wbp.parser.constructor
	 * 
	 * @param nuevaCampaña
	 */
	public AltaLugarHabilitado(IVentanaGestion padre, Campaña nuevaCampaña) {
		
		this.campaña = nuevaCampaña;
		
		this.padre = padre;
		
		setTitle("Alta de Lugares Habilitados - " + campaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		setResizable(false);
		
		JLabel lblNombre = new JLabel("Nombre *");
		lblNombre.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblNombre.setBounds(10, 11, 89, 14);
		contentPane.add(lblNombre);
		
		textoNombre = new JTextField();
		textoNombre.setBounds(10, 36, 163, 20);
		contentPane.add(textoNombre);
		textoNombre.setColumns(10);
		
		JLabel lblContacto = new JLabel("Contacto *");
		lblContacto.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblContacto.setBounds(10, 67, 73, 14);
		contentPane.add(lblContacto);
		
		textoContacto = new JTextField();
		textoContacto.setBounds(10, 92, 163, 20);
		contentPane.add(textoContacto);
		textoContacto.setColumns(10);
		
		JLabel lblDireccin = new JLabel("Dirección *");
		lblDireccin.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblDireccin.setBounds(10, 123, 89, 14);
		contentPane.add(lblDireccin);
		
		altaDireccion = new AltaDireccion(new WrapperSeleccion(textoDireccionSeleccionada));
		
		btnDireccion = new JButton("Agregar Dirección");
		btnDireccion.setBounds(10, 148, 145, 23);
		btnDireccion.addActionListener((e) -> this.altaDireccion.setVisible(true));
		contentPane.add(btnDireccion);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(385, 230, 89, 23);
		btnAceptar.addActionListener((e)->nuevoLugarHabilitado());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(287, 230, 89, 23);
		btnCancelar.addActionListener((e) -> this.dispose());
		contentPane.add(btnCancelar);
		
		JLabel lblCamposObligatorios = new JLabel("* Campos Obligatorios");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(261, 5, 163, 20);
		contentPane.add(lblCamposObligatorios);
		
		textoDireccionSeleccionada = new JTextField();
		textoDireccionSeleccionada.setBounds(13, 182, 461, 20);
		contentPane.add(textoDireccionSeleccionada);
		textoDireccionSeleccionada.setColumns(10);
		this.textoDireccionSeleccionada.setEditable(false);
		
		
	}
	
	public AltaLugarHabilitado(IVentanaGestion padre, Campaña nuevaCampaña, LugarHabilitado lugarHabilitado) {
		
		this(padre, nuevaCampaña);
		
		this.setTitle("Actualización de lugar habilitado - " + campaña.getNombre());
		
		btnDireccion.setText("Actualizar Dirección");
		
		textoContacto.setText(lugarHabilitado.getContacto());
		
		textoNombre.setText(lugarHabilitado.getNombre());
		
		this.textoDireccionSeleccionada.setText(lugarHabilitado.getDireccion().toString());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
		
		btnAceptar.addActionListener((e)-> modificarLugarHabilitado(lugarHabilitado));
		
		
		// Borro los escuchas de eventos del botón Agregar Dirección
		if(btnDireccion.getActionListeners().length > 0) {
			
			for (ActionListener al : btnDireccion.getActionListeners()) {
				btnDireccion.removeActionListener(al);
			}
		}
				
		this.altaDireccion = new AltaDireccion(lugarHabilitado.getDireccion(), new WrapperSeleccion(textoDireccionSeleccionada));
		
		// Agrego nuevo comportamiento
		btnDireccion.addActionListener((e)-> this.altaDireccion.setVisible(true));
		btnDireccion.setText("Modificar Dirección");
		
	}
	
	private void nuevoLugarHabilitado() {
		
		try {
			
			String contacto = textoContacto.getText().trim();
			
			String nombre = textoNombre.getText().trim();
			
			Direccion direccion = altaDireccion.getDireccion();
			
			if(direccion==null) {
				JOptionPane.showMessageDialog(this, "Debe seleccionar una dirección");
				return;
			}

			this.dispose();
			
			api.guardarLugarHabilitado(nombre, contacto, direccion.getCalle(), direccion.getNumero(), direccion.getLatitud(), direccion.getLongitud(), direccion.getCiudad(), campaña.getId());
			
			padre.refrescar();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarLugarHabilitado(LugarHabilitado lugarHabilitado) {
		
		try {
			
			String contacto = textoContacto.getText().trim();
			
			String nombre = textoNombre.getText().trim();
			
			Direccion direccion = altaDireccion.getDireccion();
			
			LugarHabilitado nuevoLugarHabilitado = new LugarHabilitado(nombre, contacto, direccion, campaña);
			nuevoLugarHabilitado.setId(lugarHabilitado.getId());
			
			api.actualizarLugarHabilitado(nuevoLugarHabilitado);
			
			this.dispose();
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (InvalidStringLengthException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
