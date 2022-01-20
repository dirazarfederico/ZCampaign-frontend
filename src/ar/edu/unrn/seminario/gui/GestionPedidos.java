package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.*;
import ar.edu.unrn.seminario.modelo.*;
import ar.edu.unrn.seminario.servicio.Helper;
import ar.edu.unrn.seminario.excepciones.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestionPedidos extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaPedidos;
	private JButton btnModificar;
	private IFacade api;
	private AltaPedido altaPedido;
	private Campaña campaña;
	private JButton btnBorrarPedido;

	public GestionPedidos(Campaña nuevaCampaña) {
		setTitle("Pedidos - " + nuevaCampaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setResizable(false);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 508, 169);
		contentPane.add(scrollPane);
		
		tablaPedidos = new JTable();
		tablaPedidos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnBorrarPedido.setEnabled(true);
			}
		});
		tablaPedidos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Emisor", "Descripción", "Carga pesada", "Observación", "Fecha", ""
			}
		);
		
		tablaPedidos.setModel(modelo);
		
		tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(150);
		tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(300);
		tablaPedidos.getColumnModel().getColumn(4).setPreferredWidth(150);
		
		// Columna oculta con los id de los pedidos
		tablaPedidos.getColumnModel().getColumn(5).setMaxWidth(0);
		tablaPedidos.getColumnModel().getColumn(5).setMinWidth(0);
		tablaPedidos.getColumnModel().getColumn(5).setResizable(false);
		tablaPedidos.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);
		tablaPedidos.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
		tablaPedidos.getTableHeader().getColumnModel().getColumn(5).setResizable(false);
		
		scrollPane.setViewportView(tablaPedidos);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.setBounds(528, 14, 133, 23);
		btnModificar.addActionListener((e)-> modificarPedido());
		contentPane.add(btnModificar);
		
		JButton btnNuevoPedido = new JButton("Nuevo Pedido");
		btnNuevoPedido.addActionListener((e)-> nuevoPedido());
		btnNuevoPedido.setBounds(528, 156, 133, 23);
		contentPane.add(btnNuevoPedido);
		
		btnBorrarPedido = new JButton("Borrar Pedido");
		btnBorrarPedido.setBounds(528, 87, 133, 23);
		btnBorrarPedido.addActionListener((e)-> this.borrarPedido());
		btnBorrarPedido.setEnabled(false);
		contentPane.add(btnBorrarPedido);
		
		this.campaña = nuevaCampaña;
		
		refrescar();
		
	}
	
	public void refrescar() {
		
		try {
			
			List<PedidoDeRetiro> listaPedidos = api.listarPedidos();
			
			List<PedidoDeRetiro> pedidos = listaPedidos.stream().filter((p) -> p.getCampaña().getId() == this.campaña.getId() && !p.atendido() && p.activo() ).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
			
			while(tablaPedidos.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(PedidoDeRetiro p : pedidos) {
			
				modelo.addRow(new Object[] {p.getCiudadano().getPersona().getNombre() + " " + p.getCiudadano().getPersona().getApellido(), p.getDescripcion(), p.requiereCargaPesada(), p.getObservacion(), Helper.convertir(p.getFecha()), p.getId()});
			
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarPedido() {

		try {
			
			PedidoDeRetiro pedido = api.buscarPedido((Long) tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), tablaPedidos.getColumnCount() -1));
			
			this.altaPedido = new AltaPedido(this, pedido);
			this.altaPedido.setVisible(true);
			
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void nuevoPedido() {
		
		AltaPedido altaPedido = new AltaPedido(this, campaña);
		altaPedido.setVisible(true);
		
	}
	
	private void borrarPedido() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionPedidos.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar el pedido?", "Borrar Pedido", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				PedidoDeRetiro pedido = api.buscarPedido((Long) tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), tablaPedidos.getColumnCount() -1));
				
				this.api.borrarPedido(pedido);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}

}
