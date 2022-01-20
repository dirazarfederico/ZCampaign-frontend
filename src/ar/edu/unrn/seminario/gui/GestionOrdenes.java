package ar.edu.unrn.seminario.gui;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class GestionOrdenes extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaOrdenes;
	private JButton btnFinalizar;
	private JButton btnVisitas;
	private Campaña campaña;
	private AltaOrden altaOrden;
	private IFacade api;
	private GestionVisitas gestionVisitas;
	private FinalizarOrden finalizarOrden;
	private JButton btnModificarOrden;
	private JButton btnVerPedido;
	private VerPedido verPedido;
	private JButton btnBorrarOrden;
	
	public GestionOrdenes(Campaña campaña) {
		setResizable(false);
		setTitle("Ordenes - " + campaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 844, 289);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.campaña = campaña;
		altaOrden = new AltaOrden(this, campaña);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 16, 646, 232);
		
		btnFinalizar = new JButton("Finalizar");
		btnFinalizar.setEnabled(false);
		btnFinalizar.addActionListener((e)->this.finalizarOrden());
		btnFinalizar.setBounds(671, 145, 157, 23);
		contentPane.setLayout(null);
		
		tablaOrdenes = new JTable();
		tablaOrdenes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaOrdenes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnFinalizar.setEnabled(true);
				btnVisitas.setEnabled(true);
				btnModificarOrden.setEnabled(true);
				btnVerPedido.setEnabled(true);
				btnBorrarOrden.setEnabled(true);
			}
		});
		tablaOrdenes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Encargado", "Domicilio", "Fecha", "Estado", ""
			}
		);
		
		tablaOrdenes.setModel(modelo);
		
		tablaOrdenes.getColumnModel().getColumn(0).setPreferredWidth(150);
		tablaOrdenes.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaOrdenes.getColumnModel().getColumn(2).setPreferredWidth(150);
		tablaOrdenes.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		// Columna oculta con los id de las Ordenes
		tablaOrdenes.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaOrdenes.getColumnModel().getColumn(4).setMinWidth(0);
		tablaOrdenes.getColumnModel().getColumn(4).setResizable(false);
		tablaOrdenes.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaOrdenes.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaOrdenes.getTableHeader().getColumnModel().getColumn(4).setResizable(false);

		scrollPane.setViewportView(tablaOrdenes);
		contentPane.add(scrollPane);
		contentPane.add(btnFinalizar);
		
		JButton btnNuevaOrden = new JButton("Nueva Orden");
		btnNuevaOrden.addActionListener((e)->this.altaOrden.setVisible(true));
		btnNuevaOrden.setBounds(671, 225, 157, 23);
		contentPane.add(btnNuevaOrden);
		
		btnVisitas = new JButton("Visitas");
		btnVisitas.setEnabled(false);
		btnVisitas.addActionListener((e)-> gestionVisitas());
		btnVisitas.setBounds(671, 101, 157, 23);
		contentPane.add(btnVisitas);
		
		btnModificarOrden = new JButton("Modificar Orden");
		btnModificarOrden.setBounds(671, 60, 157, 23);
		btnModificarOrden.setEnabled(false);
		btnModificarOrden.addActionListener((e)->this.modificarOrden());
		contentPane.add(btnModificarOrden);
		
		btnVerPedido = new JButton("Ver Pedido");
		btnVerPedido.setBounds(671, 16, 154, 23);
		btnVerPedido.setEnabled(false);
		btnVerPedido.addActionListener((e)->verPedido());
		contentPane.add(btnVerPedido);
		
		btnBorrarOrden = new JButton("Borrar Orden");
		btnBorrarOrden.setEnabled(false);
		btnBorrarOrden.setBounds(671, 184, 154, 23);
		btnBorrarOrden.addActionListener((e)-> borrarOrden());
		contentPane.add(btnBorrarOrden);
		
		refrescar();
	}

	@Override
	public void refrescar() {
		try {
			
			List<OrdenDeRetiro> ordenes = this.api.listarOrdenes();
			
			List<OrdenDeRetiro> resultado = ordenes.stream().filter((o)-> o.getCampaña().getId() == this.campaña.getId() && o.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaOrdenes.getModel();
			
			while(tablaOrdenes.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(OrdenDeRetiro o : resultado) {
				
				modelo.addRow(new Object[] {o.getPersonal().getPersona().getNombre() + " " + o.getPersonal().getPersona().getApellido(), o.getDireccion().toString(), Helper.convertirFechaHora(o.getFechaHora()), o.getEstado(), o.getId()});
				
			}
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void gestionVisitas() {
		
		try {
			OrdenDeRetiro orden = api.buscarOrden((Long) tablaOrdenes.getValueAt(tablaOrdenes.getSelectedRow(), tablaOrdenes.getColumnCount() -1 ));
			
			gestionVisitas = new GestionVisitas(this, orden);
			gestionVisitas.setVisible(true);
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void finalizarOrden() {
		
		try {
			
			OrdenDeRetiro orden = api.buscarOrden((Long) tablaOrdenes.getValueAt(tablaOrdenes.getSelectedRow(), tablaOrdenes.getColumnCount() -1 ));
			
			finalizarOrden = new FinalizarOrden(this, orden);
			finalizarOrden.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarOrden() {
		
		try {
			
			OrdenDeRetiro orden = api.buscarOrden((Long) tablaOrdenes.getValueAt(tablaOrdenes.getSelectedRow(), tablaOrdenes.getColumnCount() -1 ));
			
			this.altaOrden = new AltaOrden(this, orden);
			this.altaOrden.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void verPedido() {
		
		try {
			
			OrdenDeRetiro orden = api.buscarOrden((Long) tablaOrdenes.getValueAt(tablaOrdenes.getSelectedRow(), tablaOrdenes.getColumnCount() -1 ));
			
			this.verPedido = new VerPedido(orden.getPedido());
			this.verPedido.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void borrarOrden() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionOrdenes.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar la orden?", "Borrar Orden", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				OrdenDeRetiro orden = api.buscarOrden((Long) tablaOrdenes.getValueAt(tablaOrdenes.getSelectedRow(), tablaOrdenes.getColumnCount() -1));
				
				this.api.borrarOrden(orden);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
