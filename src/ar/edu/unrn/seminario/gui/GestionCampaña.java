package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.*;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.JButton;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCampaña extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaCampañas;
	private JButton btnDonaciones;
	private JButton btnLugaresHabilitados;
	private JButton btnPedidos;
	private JButton btnOrdenes;
	private JButton btnModificarCampaña;
	private JButton btnBorrarCampaña;
	private IFacade api;
	private AltaCampaña altaCampaña;

	public GestionCampaña() {
		setTitle("Campañas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 817, 275);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 16, 588, 185);
		
		JButton btnNuevaCampaña = new JButton("Nueva Campaña");
		btnNuevaCampaña.setBounds(613, 212, 178, 23);
		btnNuevaCampaña.addActionListener((e)->nuevaCampaña());
		
		btnLugaresHabilitados = new JButton("Lugares Habilitados");
		btnLugaresHabilitados.addActionListener((e)-> gestionLugarHabilitado());
		btnLugaresHabilitados.setBounds(613, 50, 178, 23);
		
		btnPedidos = new JButton("Pedidos");
		btnPedidos.addActionListener((e)-> gestionPedidos());
		btnPedidos.setBounds(613, 81, 178, 23);
		
		btnOrdenes = new JButton("Ordenes");
		btnOrdenes.setBounds(613, 112, 178, 23);
		btnOrdenes.addActionListener((e)-> gestionOrdenes());
		
		btnDonaciones = new JButton("Donaciones");
		btnDonaciones.setBounds(613, 144, 178, 23);
		btnDonaciones.addActionListener((e)-> gestionDonaciones());
		

		btnModificarCampaña = new JButton("Modificar Campaña");
		btnModificarCampaña.setBounds(613, 19, 178, 23);
		btnModificarCampaña.addActionListener((e)-> modificarCampaña());
		
		btnBorrarCampaña = new JButton("Borrar Campaña");
		btnBorrarCampaña.setBounds(613, 178, 178, 23);
		btnBorrarCampaña.addActionListener((e)-> borrarCampaña());
		
		btnDonaciones.setEnabled(false);
		btnOrdenes.setEnabled(false);
		btnLugaresHabilitados.setEnabled(false);
		btnPedidos.setEnabled(false);
		btnModificarCampaña.setEnabled(false);
		btnBorrarCampaña.setEnabled(false);
		
		contentPane.setLayout(null);
		
		tablaCampañas = new JTable();
		tablaCampañas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDonaciones.setEnabled(true);
				btnOrdenes.setEnabled(true);
				btnLugaresHabilitados.setEnabled(true);
				btnPedidos.setEnabled(true);
				btnModificarCampaña.setEnabled(true);
				btnBorrarCampaña.setEnabled(true);
			}
		});
		
		tablaCampañas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "Fecha Inicio", "Fecha Fin", "Institución", ""
			}
		);
		
		tablaCampañas.setModel(modelo);
		
		tablaCampañas.getColumnModel().getColumn(0).setPreferredWidth(170);
		tablaCampañas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCampañas.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaCampañas.getColumnModel().getColumn(3).setPreferredWidth(215);
		
		// Columna oculta con los id de las Campañas
		tablaCampañas.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCampañas.getColumnModel().getColumn(4).setMinWidth(0);
		tablaCampañas.getColumnModel().getColumn(4).setResizable(false);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		scrollPane.setViewportView(tablaCampañas);
		contentPane.add(scrollPane);
		contentPane.add(btnNuevaCampaña);
		contentPane.add(btnPedidos);
		contentPane.add(btnLugaresHabilitados);
		contentPane.add(btnOrdenes);
		contentPane.add(btnDonaciones);
		contentPane.add(btnModificarCampaña);
		contentPane.add(btnBorrarCampaña);
		
		refrescar();
	}
	
	private void nuevaCampaña() {
		
		altaCampaña = new AltaCampaña(this);
		altaCampaña.setVisible(true);
	}
	
	private void modificarCampaña() {
	
		try {
			
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));

			AltaCampaña modificarCampaña = new AltaCampaña(campaña, this);
			modificarCampaña.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void gestionLugarHabilitado() {

		try {
			
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));
			
			GestionLugarHabilitado gestionLugarHabilitado = new GestionLugarHabilitado(campaña);
			gestionLugarHabilitado.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionPedidos() {
		
		try {
			
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));
			
			GestionPedidos gestionPedidos = new GestionPedidos(campaña);
			gestionPedidos.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionOrdenes() {

		try {
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));
			
			GestionOrdenes gestionOrdenes = new GestionOrdenes(campaña);
			gestionOrdenes.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionDonaciones() {

		try {
			
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));
			
			GestionDonaciones gestionDonaciones = new GestionDonaciones(campaña);
			gestionDonaciones.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void refrescar() {
		
		try {
			
			List<Campaña> lista = api.listarCampañas();
			
			List<Campaña> resultado = lista.stream().filter((c)-> c.estaVigente() && c.activo()).collect(Collectors.toList());
		
			DefaultTableModel modelo = (DefaultTableModel) tablaCampañas.getModel();
			
			while(tablaCampañas.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Campaña c : resultado) {
			
				modelo.addRow(new Object[] {c.getNombre(), Helper.convertir(c.getFechaInicio()), Helper.convertir(c.getFechaFin()), c.getInstitucion().getNombre(), c.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void borrarCampaña() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionCampaña.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar la campaña?", "Borrar Campaña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() -1));
				
				this.api.borrarCampaña(campaña);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
