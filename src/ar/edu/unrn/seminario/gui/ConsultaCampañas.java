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
import ar.edu.unrn.seminario.servicio.Helper;

public class ConsultaCampañas extends JFrame {

	private JPanel contentPane;
	private JTable tablaCampañas;
	private IFacade api;
	private JButton btnVerDonaciones;

	public ConsultaCampañas() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 265);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setTitle("Historial de Campañas");
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		setResizable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 16, 1000, 185);
		
		contentPane.setLayout(null);
		
		tablaCampañas = new JTable();
		tablaCampañas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnVerDonaciones.setEnabled(true);
			}
		});
		
		tablaCampañas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "Fecha Inicio", "Fecha Fin", "Institución", "Descripción", "Motivo", ""
			}
		);
		
		tablaCampañas.setModel(modelo);
		
		tablaCampañas.getColumnModel().getColumn(0).setPreferredWidth(170);
		tablaCampañas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCampañas.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaCampañas.getColumnModel().getColumn(3).setPreferredWidth(215);
		tablaCampañas.getColumnModel().getColumn(4).setPreferredWidth(215);
		tablaCampañas.getColumnModel().getColumn(5).setPreferredWidth(215);
		
		// Columna oculta con los id de las Campañas
		tablaCampañas.getColumnModel().getColumn(6).setMaxWidth(0);
		tablaCampañas.getColumnModel().getColumn(6).setMinWidth(0);
		tablaCampañas.getColumnModel().getColumn(6).setResizable(false);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
		tablaCampañas.getTableHeader().getColumnModel().getColumn(6).setResizable(false);
		contentPane.setLayout(null);
		
		scrollPane.setViewportView(tablaCampañas);
		contentPane.add(scrollPane);
		
		btnVerDonaciones = new JButton("Ver Donaciones");
		btnVerDonaciones.setBounds(1025, 19, 140, 23);
		btnVerDonaciones.setEnabled(false);
		btnVerDonaciones.addActionListener((e)-> verDonaciones());
		contentPane.add(btnVerDonaciones);
		
		cargar();
	}
	
	private void cargar() {
		
		try {
			
			List<Campaña> campañas = api.listarCampañas();
			
			List<Campaña> resultado = campañas.stream().filter((c)->c.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaCampañas.getModel();
			
			while(tablaCampañas.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Campaña c : resultado) {
				
				modelo.addRow(new Object[] {c.getNombre(), Helper.convertir(c.getFechaInicio()), Helper.convertir(c.getFechaFin()), c.getInstitucion().getNombre(), c.getDescripcion(), c.getMotivo(), c.getId()});
				
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void verDonaciones() {
		
		try {
			
			Campaña campaña = api.buscarCampaña((Long) tablaCampañas.getValueAt(tablaCampañas.getSelectedRow(), tablaCampañas.getColumnCount() - 1));
			
			if(!campaña.estaVigente()) {
				ConsultaDonaciones consultaDonaciones = new ConsultaDonaciones(campaña);
				consultaDonaciones.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(this, "La campaña seleccionada sigue vigente");
			}
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
}
