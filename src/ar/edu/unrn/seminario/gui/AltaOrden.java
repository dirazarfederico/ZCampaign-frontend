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
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;
import ar.edu.unrn.seminario.modelo.PedidoDeRetiro;
import ar.edu.unrn.seminario.modelo.Personal;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JCalendar;

public class AltaOrden extends JFrame {

	private JPanel contentPane;
	private JTextField textoSeleccionPersonal;
	private SeleccionPersonal seleccionPersonal;
	private IVentanaGestion padre;
	private SeleccionPedido seleccionPedido;
	private JTextField textoSeleccionPedido;
	private IFacade api;
	private JLabel lblCamposObligatorios;
	private JButton btnAceptar;
	private JButton btnBuscarPedido;
	private JButton btnBuscarPersonal;

	/**
	 * @wbp.parser.constructor
	 */
	public AltaOrden(IVentanaGestion nuevoPadre, Campaña campaña) {
		setTitle("Alta de Ordenes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.padre = nuevoPadre;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JLabel lblAsignarPersonal = new JLabel("Asignar Personal *");
		lblAsignarPersonal.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblAsignarPersonal.setBounds(10, 11, 139, 25);
		contentPane.add(lblAsignarPersonal);
		
		btnBuscarPersonal = new JButton("Buscar...");
		btnBuscarPersonal.addActionListener((e)->this.seleccionPersonal.setVisible(true));
		btnBuscarPersonal.setBounds(180, 15, 115, 23);
		contentPane.add(btnBuscarPersonal);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(455, 182, 89, 23);
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(356, 182, 89, 23);
		contentPane.add(btnCancelar);
		
		textoSeleccionPersonal = new JTextField();
		textoSeleccionPersonal.setEditable(false);
		textoSeleccionPersonal.setBounds(10, 49, 534, 20);
		contentPane.add(textoSeleccionPersonal);
		textoSeleccionPersonal.setColumns(10);
		
		this.seleccionPersonal = new SeleccionPersonal(new WrapperSeleccion(textoSeleccionPersonal), campaña.getInstitucion());
		
		JLabel lblSeleccionarPedido = new JLabel("Seleccionar Pedido *");
		lblSeleccionarPedido.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblSeleccionarPedido.setBounds(10, 94, 148, 25);
		contentPane.add(lblSeleccionarPedido);
		
		btnBuscarPedido = new JButton("Buscar...");
		btnBuscarPedido.setBounds(180, 98, 115, 23);
		contentPane.add(btnBuscarPedido);
		btnBuscarPedido.addActionListener((e)->this.seleccionPedido.setVisible(true));
		
		textoSeleccionPedido = new JTextField();
		textoSeleccionPedido.setEditable(false);
		textoSeleccionPedido.setColumns(10);
		textoSeleccionPedido.setBounds(10, 130, 534, 20);
		contentPane.add(textoSeleccionPedido);
		
		this.seleccionPedido = new SeleccionPedido(new WrapperSeleccion(textoSeleccionPedido), campaña);
		
		lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(378, 11, 166, 25);
		contentPane.add(lblCamposObligatorios);
		
		btnCancelar.addActionListener((e)->this.dispose());
		
		btnAceptar.addActionListener((e)->crearOrden());
		
	}
	
	public AltaOrden(IVentanaGestion nuevoPadre, OrdenDeRetiro orden) {
		this(nuevoPadre, orden.getCampaña());
		
		setTitle("Actualización de Orden de retiro");
		
		textoSeleccionPedido.setText(orden.getPedido().toString());
		textoSeleccionPedido.setEnabled(false);
		btnBuscarPedido.setEnabled(false);
		
		textoSeleccionPersonal.setText(orden.getPersonal().toString());
		textoSeleccionPersonal.setEnabled(false);
		if(orden.estaRetirando() || orden.estaFinalizada()) {
			btnBuscarPersonal.setEnabled(false);
		}
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
			
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarOrden(orden));
	}
	
	private void crearOrden() {
		
		try {
			
			PedidoDeRetiro origen = this.seleccionPedido.getPedido();
			
			Personal encargado = this.seleccionPersonal.getPersonal();
			
			this.dispose();
			
			this.api.generarOrdenDeRetiro(origen.getId(), encargado.getId());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void modificarOrden(OrdenDeRetiro orden) {
		
		try {
			
			Personal personal;
			
			if(seleccionPersonal.getPersonal()!=null) {
				
				personal = seleccionPersonal.getPersonal();
				
				personal = orden.getPersonal();
				
				OrdenDeRetiro nuevaOrden = new OrdenDeRetiro(orden.getPedido(), personal);
				nuevaOrden.setId(orden.getId());
				
				this.api.actualizarOrden(orden);
			}
			
			this.dispose();
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch(DataEmptyException dEEx) {
			JOptionPane.showMessageDialog(this, dEEx.getMessage());
		}
		
	}
}
