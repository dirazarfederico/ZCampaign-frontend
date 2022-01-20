package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.Ciudadano;
import ar.edu.unrn.seminario.modelo.Direccion;
import ar.edu.unrn.seminario.modelo.Persona;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

public class AltaCiudadano extends JFrame {

	private JPanel contentPane;
	private JTextField textoNombre;
	private JTextField textoApellido;
	private JTextField textoDNI;
	private JButton btnDireccion;
	private AltaDireccion altaDireccion;
	private IFacade api;
	private JButton btnAceptar;
	private JTextField textoDireccionSeleccionada;
	private JTextField textoSeleccionPersona;
	private SeleccionPersona seleccionPersona;
	private JTabbedPane pestañas;
	IVentanaGestion padre;

	/**
	 * @wbp.parser.constructor
	 * @param nuevoPadre
	 * @param institucion
	 */
	public AltaCiudadano(IVentanaGestion padre) {
		setTitle("Alta de ciudadano");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 521, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		this.padre = padre;
		
		JLabel lblDomicilio = new JLabel("Domicilio *");
		lblDomicilio.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener((e)-> crearCiudadano());
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener((e)-> this.dispose());
		
		btnDireccion = new JButton("Agregar Dirección");
		btnDireccion.addActionListener((e)-> this.altaDireccion.setVisible(true));
		
		textoDireccionSeleccionada = new JTextField();
		textoDireccionSeleccionada.setEditable(false);
		
		altaDireccion = new AltaDireccion(new WrapperSeleccion(textoDireccionSeleccionada));
		
		pestañas = new JTabbedPane(JTabbedPane.TOP);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDireccion)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(textoDireccionSeleccionada, GroupLayout.PREFERRED_SIZE, 476, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnCancelar)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnAceptar)))
							.addGap(19))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDomicilio, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(256)
									.addComponent(lblCamposObligatorios)))
							.addContainerGap(80, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(pestañas, GroupLayout.PREFERRED_SIZE, 415, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(80, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(7)
					.addComponent(lblCamposObligatorios, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(pestañas, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblDomicilio, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnDireccion)
					.addGap(18)
					.addComponent(textoDireccionSeleccionada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelar)
						.addComponent(btnAceptar))
					.addContainerGap())
		);
		
		JPanel cargarPersona = new JPanel();
		pestañas.addTab("Cargar persona", null, cargarPersona, null);
		
		JLabel label = new JLabel("Nombre *");
		label.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoNombre = new JTextField();
		textoNombre.setColumns(10);
		
		JLabel lblApellido = new JLabel("Apellido *");
		lblApellido.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoApellido = new JTextField();
		textoApellido.setColumns(10);
		
		JLabel lblDni = new JLabel("DNI *");
		lblDni.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoDNI = new JTextField();
		textoDNI.setColumns(10);
		GroupLayout gl_cargarPersona = new GroupLayout(cargarPersona);
		gl_cargarPersona.setHorizontalGroup(
			gl_cargarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cargarPersona.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cargarPersona.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblApellido, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoApellido, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDni, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoDNI, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(207, Short.MAX_VALUE))
		);
		gl_cargarPersona.setVerticalGroup(
			gl_cargarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cargarPersona.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblApellido)
					.addGap(18)
					.addComponent(textoApellido, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblDni)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textoDNI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		cargarPersona.setLayout(gl_cargarPersona);
		
		JPanel seleccionarPersona = new JPanel();
		pestañas.addTab("Seleccionar persona", null, seleccionarPersona, null);
		
		JButton btnSeleccionarPersona = new JButton("Seleccionar...");
		btnSeleccionarPersona.addActionListener((e)->this.seleccionarPersona());
		
		textoSeleccionPersona = new JTextField();
		textoSeleccionPersona.setEditable(false);
		textoSeleccionPersona.setText("Seleccione una persona...");
		textoSeleccionPersona.setColumns(10);
		
		this.seleccionPersona = new SeleccionPersona(new WrapperSeleccion(textoSeleccionPersona));
		
		GroupLayout gl_seleccionarPersona = new GroupLayout(seleccionarPersona);
		gl_seleccionarPersona.setHorizontalGroup(
			gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seleccionarPersona.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
						.addComponent(textoSeleccionPersona, GroupLayout.PREFERRED_SIZE, 376, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSeleccionarPersona))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		gl_seleccionarPersona.setVerticalGroup(
			gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seleccionarPersona.createSequentialGroup()
					.addGap(33)
					.addComponent(btnSeleccionarPersona)
					.addGap(53)
					.addComponent(textoSeleccionPersona, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(94, Short.MAX_VALUE))
		);
		seleccionarPersona.setLayout(gl_seleccionarPersona);
		contentPane.setLayout(gl_contentPane);
	
	}
	
	public AltaCiudadano(IVentanaGestion padre, Ciudadano ciudadano) {
		
		this(padre);
		
		this.altaDireccion = new AltaDireccion(ciudadano.getDireccion(), new WrapperSeleccion(textoDireccionSeleccionada));
		
		setTitle("Actualización de ciudadano");
		
		// Deshabilita la pestaña de seleccionarPersona
		pestañas.remove(pestañas.getComponentAt(1));
		pestañas.setTitleAt(0, "Editar persona");
		
		textoDNI.setText(new Integer(ciudadano.getPersona().getDni()).toString());
		
		textoNombre.setText(ciudadano.getPersona().getNombre());
		
		textoApellido.setText(ciudadano.getPersona().getApellido());
		
		this.mostrarSeleccion(ciudadano.getDireccion());
		
		btnDireccion.setText("Actualizar Dirección");
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
				
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarCiudadano(ciudadano));
		
		// Borro los escuchas de eventos del botón "Agregar Dirección"
		if(btnDireccion.getActionListeners().length > 0) {
					
			for (ActionListener al : btnDireccion.getActionListeners()) {
				btnDireccion.removeActionListener(al);
			}
		}
		
		this.altaDireccion = new AltaDireccion(ciudadano.getDireccion(), new WrapperSeleccion(textoDireccionSeleccionada));
		
		// Agrego nuevo comportamiento
		btnDireccion.addActionListener((e)-> this.altaDireccion.setVisible(true));
		btnDireccion.setText("Modificar Dirección");
	}
	
	private void seleccionarPersona() {
		this.seleccionPersona.setVisible(true);
	}
	
	private void crearCiudadano() {
		
		try {
			//TODO probar agregar y modificar ciudadano
			Direccion direccion = altaDireccion.getDireccion();
			String nombre = "", apellido = "";
			int dni = 0;
			Persona nuevaPersona = null;
			Long idPersona = null;
			
			switch (this.pestañas.getSelectedIndex()) {
			case 0:
				
				dni = Integer.parseInt(textoDNI.getText().trim());
				
				nombre = textoNombre.getText().trim();
				
				apellido = textoApellido.getText().trim();
				
				nuevaPersona = api.guardarPersona(nombre, apellido, dni);
				
				idPersona = nuevaPersona.getId();
				
				break;
				
			case 1:
				
				Persona persona = this.seleccionPersona.getPersona();
				
				if(persona==null) {
					JOptionPane.showMessageDialog(this, "Debe seleccionar una persona");
					return;
				}
				
				idPersona = persona.getId();
				
				break;
			}
			
			this.dispose();
			
			api.guardarCiudadano(idPersona, direccion.getCalle(), direccion.getNumero(), direccion.getLatitud(), direccion.getLongitud(), direccion.getCiudad());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch(NumberFormatException nFex) {
			JOptionPane.showMessageDialog(this, "El DNI ingresado no es válido");
		}
		
	}
	
	public void modificarCiudadano(Ciudadano ciudadano) {
		
		try {
			
			Direccion direccion = altaDireccion.getDireccion();
			
			String nombre, apellido;
			
			int dni = Integer.parseInt(textoDNI.getText().trim());
			
			nombre = textoNombre.getText().trim();
			
			apellido = textoApellido.getText().trim();
			
			Persona persona = new Persona(nombre, apellido, dni);
			persona.setId(ciudadano.getPersona().getId());
			
			Ciudadano nuevoCiudadano = new Ciudadano(direccion, persona);
			nuevoCiudadano.setId(ciudadano.getId());
			
			api.actualizarCiudadano(nuevoCiudadano);
			
			this.dispose();
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch(NumberFormatException nFex) {
			JOptionPane.showMessageDialog(this, "El DNI ingresado no es válido");
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (InvalidStringLengthException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void mostrarSeleccion(Object o) {
		
		this.textoDireccionSeleccionada.setText(o.toString());
		
	}
}
