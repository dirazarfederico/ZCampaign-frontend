package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.PedidoDeRetiro;
import ar.edu.unrn.seminario.modelo.Personal;
import ar.edu.unrn.seminario.servicio.Helper;

public class SeleccionPedido extends VentanaSeleccion {

	private JPanel contentPane;
	private IFacade api;
	private JTable tablaPedidos;
	private JButton btnAceptar;
	private PedidoDeRetiro pedido;
	private Campaña campaña;
	
	public SeleccionPedido(WrapperSeleccion nuevoWrapper, Campaña campaña) {
		
		super(nuevoWrapper);
		
		this.campaña = campaña;
		
		setTitle("Selección de un Pedido");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 615, 249);
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 579, 153);
		contentPane.add(scrollPane);
		
		tablaPedidos = new JTable();
		tablaPedidos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
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
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setEnabled(false);
		btnAceptar.setBounds(500, 176, 89, 23);
		btnAceptar.addActionListener((e)->this.seleccionarPedido());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Omitir");
		btnCancelar.setBounds(401, 176, 89, 23);
		btnCancelar.addActionListener((e) -> this.dispose());
		contentPane.add(btnCancelar);
		
		refrescar();
		
	}
	
	public PedidoDeRetiro getPedido() {
		return this.pedido;
	}

	private void refrescar() {
		
		try {
			
			List<PedidoDeRetiro> listaPedidos = api.listarPedidos();
			
			List<PedidoDeRetiro> pedidos = listaPedidos.stream().filter((PedidoDeRetiro p) -> p.getCampaña().getId().equals(this.campaña.getId()) && !p.atendido()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaPedidos.getModel();
			
			for(PedidoDeRetiro p : pedidos) {
			
				modelo.addRow(new Object[] {p.getCiudadano().getPersona().getNombre() + " " + p.getCiudadano().getPersona().getApellido(), p.getDescripcion(), p.requiereCargaPesada(), p.getObservacion(), Helper.convertir(p.getFecha()), p.getId()});
			
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void seleccionarPedido() {
		
		try {
			
			this.pedido = this.api.buscarPedido((Long) tablaPedidos.getValueAt(tablaPedidos.getSelectedRow(), tablaPedidos.getColumnCount() - 1));
			
			this.mostrarSeleccion(this.pedido);
			
			this.dispose();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	

}
