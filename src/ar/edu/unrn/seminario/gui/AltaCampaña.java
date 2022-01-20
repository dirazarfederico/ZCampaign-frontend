package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import com.toedter.calendar.JCalendar;
import java.util.Date;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.DateOutOfBoundariesException;
import ar.edu.unrn.seminario.excepciones.InvalidDateRangeException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.*;

public class AltaCampa�a extends JFrame {

	private JPanel contentPane;
	private JTextField textoNombre;
	private JTextField textoMotivo;
	private JTextField textoDescripcion;
	private JButton btnSeleccionarInstitucion;
	private SeleccionInstitucion seleccionInstitucion;
	private IFacade api;
	private JCalendar calendarioInicio;
	private JCalendar calendarioFin;
	private JLabel lblAgregarInstitucion;
	private JButton btnAceptar;
	private IVentanaGestion padre;
	private JTextField textoInstitucion;

	/**
	 * @wbp.parser.constructor
	 */
	public AltaCampa�a(IVentanaGestion nuevoPadre) {
		
		this.padre = nuevoPadre;
		
		setTitle("Alta de Campa�a");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 686, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setResizable(false);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JLabel lblNombre = new JLabel("Nombre *");
		lblNombre.setBounds(24, 22, 113, 25);
		lblNombre.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoNombre = new JTextField();
		textoNombre.setBounds(24, 50, 320, 20);
		textoNombre.setColumns(10);
		
		JLabel lblCamposObligatorios = new JLabel("* Campos Obligatorios");
		lblCamposObligatorios.setBounds(412, 22, 222, 22);
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblFechaDeInicio = new JLabel("Fecha de Inicio *");
		lblFechaDeInicio.setBounds(24, 111, 184, 25);
		lblFechaDeInicio.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblFechaDeFin = new JLabel("Fecha de Fin *");
		lblFechaDeFin.setBounds(303, 114, 168, 18);
		lblFechaDeFin.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblMotivo = new JLabel("Motivo");
		lblMotivo.setBounds(24, 407, 113, 25);
		lblMotivo.setFont(new Font("Dialog", Font.PLAIN, 16));
		contentPane.setLayout(null);
		contentPane.add(lblNombre);
		contentPane.add(textoNombre);
		contentPane.add(lblCamposObligatorios);
		contentPane.add(lblFechaDeInicio);
		contentPane.add(lblFechaDeFin);
		contentPane.add(lblMotivo);
		
		textoMotivo = new JTextField();
		textoMotivo.setBounds(24, 443, 320, 20);
		contentPane.add(textoMotivo);
		textoMotivo.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripci�n");
		lblDescripcion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblDescripcion.setBounds(24, 486, 96, 25);
		contentPane.add(lblDescripcion);
		
		textoDescripcion = new JTextField();
		textoDescripcion.setBounds(24, 522, 320, 20);
		contentPane.add(textoDescripcion);
		textoDescripcion.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(545, 553, 89, 23);
		btnAceptar.addActionListener((e)-> nuevaCampa�a());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(446, 553, 89, 23);
		btnCancelar.addActionListener((e)-> this.dispose());
		contentPane.add(btnCancelar);
		
		btnSeleccionarInstitucion = new JButton("Seleccionar...");
		btnSeleccionarInstitucion.setBounds(193, 335, 113, 23);
		btnSeleccionarInstitucion.addActionListener((e)-> seleccionInstitucion.setVisible(true));
		contentPane.add(btnSeleccionarInstitucion);
		
		calendarioInicio = new JCalendar();
		calendarioInicio.setBounds(24, 151, 184, 153);
		contentPane.add(calendarioInicio);
		
		calendarioFin = new JCalendar();
		calendarioFin.setBounds(303, 151, 184, 153);
		contentPane.add(calendarioFin);
		
		lblAgregarInstitucion = new JLabel("Agregar Instituci�n *");
		lblAgregarInstitucion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblAgregarInstitucion.setBounds(24, 333, 159, 25);
		contentPane.add(lblAgregarInstitucion);
		
		textoInstitucion = new JTextField();
		textoInstitucion.setBounds(24, 369, 610, 20);
		contentPane.add(textoInstitucion);
		textoInstitucion.setColumns(10);
		
		seleccionInstitucion = new SeleccionInstitucion(new WrapperSeleccion(textoInstitucion));
			
	}
	
	public AltaCampa�a(Campa�a campa�a, IVentanaGestion nuevoPadre) {
		
		this(nuevoPadre);
		
		setTitle("Actualizaci�n de campa�a");
		
		lblAgregarInstitucion.setText("Modificar Instituci�n");
		
		textoNombre.setText(campa�a.getNombre());
		textoMotivo.setText(campa�a.getMotivo());
		textoDescripcion.setText(campa�a.getDescripcion());
		
		textoInstitucion.setText(campa�a.getInstitucion().toString());
		
		calendarioInicio.setDate(campa�a.getFechaInicio());
		calendarioFin.setDate(campa�a.getFechaFin());
		
		// Borro los escuchas de eventos del bot�n Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
			
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarCampa�a(campa�a));
		
	}

	private void nuevaCampa�a() {
		
		try {
			
			String nombre, motivo, descripcion;
			Date fechaInicio, fechaFin;
			Institucion institucion;
			
			nombre = textoNombre.getText().trim();
			motivo = textoMotivo.getText().trim();
			descripcion = textoDescripcion.getText().trim();
			
			fechaInicio = calendarioInicio.getDate();
			
			fechaFin = calendarioFin.getDate();
			
			institucion = seleccionInstitucion.getInstitucion();
			
			this.dispose();
			
			api.guardarCampa�a(nombre, fechaInicio, fechaFin, descripcion, motivo, institucion.getId());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarCampa�a(Campa�a campa�a) {
		
		String nombre, motivo, descripcion;
		Date fechaInicio, fechaFin;
		Institucion institucion;
		
		try {
			
			nombre = textoNombre.getText().trim();
			motivo = textoMotivo.getText().trim();
			descripcion = textoDescripcion.getText().trim();
			
			fechaInicio = calendarioInicio.getDate();
			
			fechaFin = calendarioFin.getDate();
			
			if(seleccionInstitucion.getInstitucion()==null)
				institucion = campa�a.getInstitucion();
			else
				institucion = seleccionInstitucion.getInstitucion();
			
			Campa�a nuevaCampa�a = new Campa�a(fechaInicio, fechaFin, descripcion, motivo, nombre, institucion);
			nuevaCampa�a.setId(campa�a.getId());
			
			api.actualizarCampa�a(nuevaCampa�a);
			
			this.dispose();
			
			padre.refrescar();
			
		}
		
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
