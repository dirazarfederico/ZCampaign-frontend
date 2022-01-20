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

public class GestionCampa�a extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaCampa�as;
	private JButton btnDonaciones;
	private JButton btnLugaresHabilitados;
	private JButton btnPedidos;
	private JButton btnOrdenes;
	private JButton btnModificarCampa�a;
	private JButton btnBorrarCampa�a;
	private IFacade api;
	private AltaCampa�a altaCampa�a;

	public GestionCampa�a() {
		setTitle("Campa�as");
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
		
		JButton btnNuevaCampa�a = new JButton("Nueva Campa�a");
		btnNuevaCampa�a.setBounds(613, 212, 178, 23);
		btnNuevaCampa�a.addActionListener((e)->nuevaCampa�a());
		
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
		

		btnModificarCampa�a = new JButton("Modificar Campa�a");
		btnModificarCampa�a.setBounds(613, 19, 178, 23);
		btnModificarCampa�a.addActionListener((e)-> modificarCampa�a());
		
		btnBorrarCampa�a = new JButton("Borrar Campa�a");
		btnBorrarCampa�a.setBounds(613, 178, 178, 23);
		btnBorrarCampa�a.addActionListener((e)-> borrarCampa�a());
		
		btnDonaciones.setEnabled(false);
		btnOrdenes.setEnabled(false);
		btnLugaresHabilitados.setEnabled(false);
		btnPedidos.setEnabled(false);
		btnModificarCampa�a.setEnabled(false);
		btnBorrarCampa�a.setEnabled(false);
		
		contentPane.setLayout(null);
		
		tablaCampa�as = new JTable();
		tablaCampa�as.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnDonaciones.setEnabled(true);
				btnOrdenes.setEnabled(true);
				btnLugaresHabilitados.setEnabled(true);
				btnPedidos.setEnabled(true);
				btnModificarCampa�a.setEnabled(true);
				btnBorrarCampa�a.setEnabled(true);
			}
		});
		
		tablaCampa�as.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "Fecha Inicio", "Fecha Fin", "Instituci�n", ""
			}
		);
		
		tablaCampa�as.setModel(modelo);
		
		tablaCampa�as.getColumnModel().getColumn(0).setPreferredWidth(170);
		tablaCampa�as.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCampa�as.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaCampa�as.getColumnModel().getColumn(3).setPreferredWidth(215);
		
		// Columna oculta con los id de las Campa�as
		tablaCampa�as.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCampa�as.getColumnModel().getColumn(4).setMinWidth(0);
		tablaCampa�as.getColumnModel().getColumn(4).setResizable(false);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		scrollPane.setViewportView(tablaCampa�as);
		contentPane.add(scrollPane);
		contentPane.add(btnNuevaCampa�a);
		contentPane.add(btnPedidos);
		contentPane.add(btnLugaresHabilitados);
		contentPane.add(btnOrdenes);
		contentPane.add(btnDonaciones);
		contentPane.add(btnModificarCampa�a);
		contentPane.add(btnBorrarCampa�a);
		
		refrescar();
	}
	
	private void nuevaCampa�a() {
		
		altaCampa�a = new AltaCampa�a(this);
		altaCampa�a.setVisible(true);
	}
	
	private void modificarCampa�a() {
	
		try {
			
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));

			AltaCampa�a modificarCampa�a = new AltaCampa�a(campa�a, this);
			modificarCampa�a.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void gestionLugarHabilitado() {

		try {
			
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));
			
			GestionLugarHabilitado gestionLugarHabilitado = new GestionLugarHabilitado(campa�a);
			gestionLugarHabilitado.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionPedidos() {
		
		try {
			
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));
			
			GestionPedidos gestionPedidos = new GestionPedidos(campa�a);
			gestionPedidos.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionOrdenes() {

		try {
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));
			
			GestionOrdenes gestionOrdenes = new GestionOrdenes(campa�a);
			gestionOrdenes.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionDonaciones() {

		try {
			
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));
			
			GestionDonaciones gestionDonaciones = new GestionDonaciones(campa�a);
			gestionDonaciones.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public void refrescar() {
		
		try {
			
			List<Campa�a> lista = api.listarCampa�as();
			
			List<Campa�a> resultado = lista.stream().filter((c)-> c.estaVigente() && c.activo()).collect(Collectors.toList());
		
			DefaultTableModel modelo = (DefaultTableModel) tablaCampa�as.getModel();
			
			while(tablaCampa�as.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Campa�a c : resultado) {
			
				modelo.addRow(new Object[] {c.getNombre(), Helper.convertir(c.getFechaInicio()), Helper.convertir(c.getFechaFin()), c.getInstitucion().getNombre(), c.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void borrarCampa�a() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionCampa�a.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "�Seguro que desea borrar la campa�a?", "Borrar Campa�a", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() -1));
				
				this.api.borrarCampa�a(campa�a);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
