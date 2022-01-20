package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import com.toedter.calendar.JCalendar;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.Donacion;
import ar.edu.unrn.seminario.modelo.LugarHabilitado;

public class AltaDonacion extends JFrame {

	private JPanel contentPane;
	private JTextField textoDescripcion;
	private JButton btnAceptar;
	private JTextField textoLugarHabilitado;
	private JButton btnSeleccionLugarHabilitado;
	private SeleccionLugarHabilitado seleccionLugarHabilitado;
	private IFacade api;
	private JCalendar calendario;
	private Campaña campaña;
	private IVentanaGestion padre;
	
	/**
	 * @wbp.parser.constructor
	 */
	public AltaDonacion(IVentanaGestion nuevoPadre, Campaña campaña) {
		setResizable(false);
		setTitle("Alta de Donaciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 543, 485);
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
		
		this.padre = nuevoPadre;
		this.campaña = campaña;
		
		JLabel lblDescripcion = new JLabel("Descripción *");
		lblDescripcion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblDescripcion.setBounds(10, 43, 94, 21);
		contentPane.add(lblDescripcion);
		
		textoDescripcion = new JTextField();
		textoDescripcion.setBounds(10, 75, 333, 20);
		contentPane.add(textoDescripcion);
		textoDescripcion.setColumns(10);
		
		JLabel lblLugarHabilitado = new JLabel("Lugar Habilitado *");
		lblLugarHabilitado.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblLugarHabilitado.setBounds(10, 114, 125, 21);
		contentPane.add(lblLugarHabilitado);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(419, 412, 89, 23);
		btnAceptar.addActionListener((e)->crearDonacion());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(321, 412, 89, 23);
		btnCancelar.addActionListener((e)->this.dispose());
		contentPane.add(btnCancelar);
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios  *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(345, 11, 163, 21);
		contentPane.add(lblCamposObligatorios);
		
		textoLugarHabilitado = new JTextField();
		textoLugarHabilitado.setBounds(10, 180, 498, 20);
		contentPane.add(textoLugarHabilitado);
		textoLugarHabilitado.setColumns(10);
		
		this.seleccionLugarHabilitado = new SeleccionLugarHabilitado(this.campaña, new WrapperSeleccion(textoLugarHabilitado));
		
		btnSeleccionLugarHabilitado = new JButton("Buscar...");
		btnSeleccionLugarHabilitado.addActionListener((e)->this.seleccionLugarHabilitado.setVisible(true));
		btnSeleccionLugarHabilitado.setBounds(10, 146, 89, 23);
		contentPane.add(btnSeleccionLugarHabilitado);
		
		calendario = new JCalendar();
		calendario.setBounds(10, 245, 184, 153);
		contentPane.add(calendario);
		
		JLabel lblFecha = new JLabel("Fecha *");
		lblFecha.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblFecha.setBounds(10, 213, 63, 21);
		contentPane.add(lblFecha);
	}
	
	public AltaDonacion(IVentanaGestion padre, Donacion donacion, LugarHabilitado lugarHabilitado) {
		
		this(padre, donacion.getCampaña());
		
		this.setTitle("Modificación de Donación");
		
		textoDescripcion.setText(donacion.getDescripcion());
		
		textoLugarHabilitado.setText(lugarHabilitado.toString());
		textoLugarHabilitado.setEditable(false);
		
		calendario.setDate(donacion.getFecha());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
			
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarDonacion(donacion, lugarHabilitado));
		
	}
	
	private void crearDonacion() {
		
		try {
			
			LugarHabilitado lugar = seleccionLugarHabilitado.getLugarHabilitado();
			
			String descripcion = textoDescripcion.getText().trim();
			
			Date fecha = calendario.getDate();

			this.dispose();
			
			api.generarDonacionLugarHabilitado(lugar.getId(), descripcion, fecha, this.campaña.getId());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void modificarDonacion(Donacion donacion, LugarHabilitado lugarHabilitado) {
		
		try {
			
			String descripcion = textoDescripcion.getText().trim();
			
			Date fecha = calendario.getDate();
			
			Donacion nuevaDonacion = new Donacion(descripcion, lugarHabilitado.getDireccion(), fecha, donacion.getCampaña());
			nuevaDonacion.setId(donacion.getId());
			
			api.actualizarDonacion(nuevaDonacion);
			
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
