package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.modelo.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import ar.edu.unrn.seminario.excepciones.*;

public class AltaInstitucion extends JFrame {

	private JPanel contentPane;
	private JTextField textoNombre;
	private JTextField textoCUIL;
	private JTextField textoContacto;
	private IFacade api;
	private AltaDireccion altaDireccion;
	private JButton btnAceptar;
	private JButton btnDireccion;
	private IVentanaGestion padre;
	private JTextField textoDireccionSeleccionada;
	
	/**
	 * 
	 * @param nuevoPadre La ventana de gestión que construye esta ventana
	 * 
	 * @wbp.parser.constructor
	 */
	public AltaInstitucion(IVentanaGestion nuevoPadre) {
		
		this.padre = nuevoPadre;
		
		setResizable(false);
		setTitle("Alta de Institución");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 421);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JLabel lblNombre = new JLabel("Nombre *");
		lblNombre.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoNombre = new JTextField();
		textoNombre.setColumns(10);
		
		JLabel lblCuil = new JLabel("CUIL *");
		lblCuil.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoCUIL = new JTextField();
		textoCUIL.setColumns(10);
		
		JLabel lblDireccin = new JLabel("Dirección *");
		lblDireccin.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblContacto = new JLabel("Contacto *");
		lblContacto.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoContacto = new JTextField();
		textoContacto.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener((e)-> crearInstitucion());
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener((e)-> this.dispose());

		textoDireccionSeleccionada = new JTextField();
		textoDireccionSeleccionada.setEditable(false);
		
		this.altaDireccion = new AltaDireccion(new WrapperSeleccion(textoDireccionSeleccionada));

		btnDireccion = new JButton("Agregar dirección");
		btnDireccion.addActionListener((e)-> this.altaDireccion.setVisible(true));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnDireccion)
							.addContainerGap())
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(lblDireccin, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(textoContacto, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
											.addComponent(btnCancelar)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnAceptar))
										.addComponent(lblContacto, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addComponent(textoCUIL, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCuil, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
									.addContainerGap())
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNombre, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
									.addComponent(lblCamposObligatorios, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
									.addGap(75))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textoDireccionSeleccionada, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombre)
						.addComponent(lblCamposObligatorios, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(lblCuil, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textoCUIL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblDireccin, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnDireccion)
					.addGap(18)
					.addComponent(textoDireccionSeleccionada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(lblContacto, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textoContacto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAceptar)
						.addComponent(btnCancelar))
					.addContainerGap(25, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public AltaInstitucion(Institucion institucion, IVentanaGestion nuevoPadre) {
		
		this(nuevoPadre);
		
		this.setTitle("Actualización de institución");
		
		textoContacto.setText(institucion.getContacto());
		
		textoCUIL.setText(institucion.getCuil());
		
		textoNombre.setText(institucion.getNombre());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
		
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
		
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarInstitucion(institucion));
		
		// Borro los escuchas de eventos del botón "Agregar Dirección"
		if(btnDireccion.getActionListeners().length > 0) {
			
			for (ActionListener al : btnDireccion.getActionListeners()) {
				btnDireccion.removeActionListener(al);
			}
		}
		
		this.altaDireccion = new AltaDireccion(institucion.getDireccion(), new WrapperSeleccion(textoDireccionSeleccionada));
		
		// Agrego nuevo comportamiento
		btnDireccion.addActionListener((e)-> this.altaDireccion.setVisible(true));
		btnDireccion.setText("Modificar Dirección");
	}
	
	private void crearInstitucion() {
		
		try {
			
			Direccion direccion = altaDireccion.getDireccion();
			
			if(direccion==null) {
				JOptionPane.showMessageDialog(this, "Debe ingresar una dirección");
				return;
			}
			
			String nombre, cuil, contacto;
			
			nombre = textoNombre.getText().trim();
			cuil = textoCUIL.getText().trim();
			contacto = textoContacto.getText().trim();

			this.dispose();
			
			api.guardarInstitucion(nombre, cuil, contacto, direccion.getCalle(), direccion.getNumero(), direccion.getLatitud(), direccion.getLongitud(), direccion.getCiudad());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarInstitucion(Institucion institucion) {
		
			String nombre, cuil, contacto;
			Direccion direccion = null;
			
		try {
			
			nombre = textoNombre.getText().trim();
			cuil = textoCUIL.getText().trim();
			contacto = textoContacto.getText().trim();
			
			direccion = altaDireccion.getDireccion();
			
			Institucion nuevaInstitucion = new Institucion(nombre, cuil, contacto, direccion);
			nuevaInstitucion.setId(institucion.getId());
			
			api.actualizarInstitucion(nuevaInstitucion);
			
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
