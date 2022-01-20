package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.DateOutOfBoundariesException;
import ar.edu.unrn.seminario.modelo.*;

import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class AltaPedido extends JFrame {

	private JPanel contentPane;
	private JTextField textoDescripción;
	private JTextField textoObservación;
	private SeleccionCiudadano seleccionCiudadano;
	private JRadioButton rdbtnSi;
	private JRadioButton rdbtnNo;
	private IFacade api;
	private Campaña campaña;
	private JButton btnAceptar;
	private JTextField textoEmisorSeleccionado;
	private JLabel lblCamposObligatorios;
	private IVentanaGestion padre;
	
	/**
	 * @wbp.parser.constructor
	 * @param nuevaCampaña
	 */
	public AltaPedido(IVentanaGestion padre, Campaña nuevaCampaña) {
		setTitle("Alta de Pedido");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 403);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.padre = padre;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		setResizable(false);
		
		JLabel lblEmisor = new JLabel("Emisor *");
		lblEmisor.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblEmisor.setBounds(10, 11, 94, 21);
		contentPane.add(lblEmisor);
		
		JButton btnSeleccionar = new JButton("Seleccionar...");
		btnSeleccionar.addActionListener((e)-> seleccionarCiudadano());
		btnSeleccionar.setBounds(114, 13, 151, 23);
		contentPane.add(btnSeleccionar);
		
		JLabel label = new JLabel("Descripción *");
		label.setFont(new Font("Dialog", Font.PLAIN, 16));
		label.setBounds(10, 112, 94, 21);
		contentPane.add(label);
		
		textoDescripción = new JTextField();
		textoDescripción.setBounds(10, 144, 316, 20);
		contentPane.add(textoDescripción);
		textoDescripción.setColumns(10);
		
		JLabel lblRequiereCargaPesada = new JLabel("Requiere carga pesada *");
		lblRequiereCargaPesada.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblRequiereCargaPesada.setBounds(10, 187, 175, 21);
		contentPane.add(lblRequiereCargaPesada);
		
		rdbtnSi = new JRadioButton("Sí");
		rdbtnSi.setBounds(191, 189, 48, 23);
		contentPane.add(rdbtnSi);
		
		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setBounds(241, 189, 59, 23);
		contentPane.add(rdbtnNo);
		
		ButtonGroup btngroup = new ButtonGroup();
		btngroup.add(rdbtnSi);
		btngroup.add(rdbtnNo);
		
		JLabel lblObservacion = new JLabel("Observación *");
		lblObservacion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblObservacion.setBounds(10, 235, 99, 21);
		contentPane.add(lblObservacion);
		
		textoObservación = new JTextField();
		textoObservación.setBounds(10, 267, 316, 20);
		contentPane.add(textoObservación);
		textoObservación.setColumns(10);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(436, 316, 89, 23);
		btnAceptar.addActionListener((e)-> nuevoPedido());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(337, 316, 89, 23);
		btnCancelar.addActionListener((e)-> this.dispose());
		contentPane.add(btnCancelar);
		
		lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(347, 11, 175, 21);
		contentPane.add(lblCamposObligatorios);
		
		textoEmisorSeleccionado = new JTextField();
		textoEmisorSeleccionado.setBounds(10, 61, 512, 22);
		contentPane.add(textoEmisorSeleccionado);
		textoEmisorSeleccionado.setEditable(false);
		textoEmisorSeleccionado.setColumns(1);
		
		this.seleccionCiudadano = new SeleccionCiudadano(new WrapperSeleccion(textoEmisorSeleccionado));
		
		this.campaña = nuevaCampaña;
		
	}
	
	public AltaPedido(IVentanaGestion padre, PedidoDeRetiro pedido) {
		
		this(padre, pedido.getCampaña());
		
		setTitle("Actualización de pedido");
		
		textoDescripción.setText(pedido.getDescripcion());
		
		textoObservación.setText(pedido.getObservacion());
		
		textoEmisorSeleccionado.setText(pedido.getCiudadano().toString());
		
		if(pedido.requiereCargaPesada()) {
			rdbtnSi.setSelected(true);
		}
		else
		{
			rdbtnNo.setSelected(true);
		}
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
		
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
		
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarPedido(pedido));
		
	}
	
	private void seleccionarCiudadano() {
		
		seleccionCiudadano.setVisible(true);
		
	}
	
	private void nuevoPedido() {
		
		String observacion, descripcion;
		boolean cargaPesada;
		Ciudadano emisor;
		
		try {
			
			observacion = textoObservación.getText().trim();
			
			descripcion = textoDescripción.getText().trim();
			
			cargaPesada = rdbtnSi.isSelected();
			
			emisor = seleccionCiudadano.getCiudadano();
			
			if(emisor==null) {
				JOptionPane.showMessageDialog(this, "Debe seleccionar un ciudadano");
				return;
			}

			this.dispose();
			
			api.registrarPedido(emisor.getId(), descripcion, cargaPesada, observacion, campaña.getId());
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarPedido(PedidoDeRetiro pedido) {
		
		String observacion, descripcion;
		boolean cargaPesada;
		Ciudadano emisor;
		
		try {
			
			observacion = textoObservación.getText().trim();
			
			descripcion = textoDescripción.getText().trim();
			
			cargaPesada = rdbtnSi.isSelected();
			
			emisor = seleccionCiudadano.getCiudadano();
			
			if(emisor==null) {
				emisor = pedido.getCiudadano();
			}
			
			PedidoDeRetiro nuevoPedido = new PedidoDeRetiro(emisor, descripcion, cargaPesada, observacion, campaña, pedido.getFecha(), pedido.atendido());
			nuevoPedido.setId(pedido.getId());
			
			api.actualizarPedido(nuevoPedido);
			
			this.dispose();
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DateOutOfBoundariesException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
}
