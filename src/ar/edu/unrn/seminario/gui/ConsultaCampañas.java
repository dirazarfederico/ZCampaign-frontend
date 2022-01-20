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
import ar.edu.unrn.seminario.modelo.Campa�a;
import ar.edu.unrn.seminario.servicio.Helper;

public class ConsultaCampa�as extends JFrame {

	private JPanel contentPane;
	private JTable tablaCampa�as;
	private IFacade api;
	private JButton btnVerDonaciones;

	public ConsultaCampa�as() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 265);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setTitle("Historial de Campa�as");
		
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
		
		tablaCampa�as = new JTable();
		tablaCampa�as.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnVerDonaciones.setEnabled(true);
			}
		});
		
		tablaCampa�as.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "Fecha Inicio", "Fecha Fin", "Instituci�n", "Descripci�n", "Motivo", ""
			}
		);
		
		tablaCampa�as.setModel(modelo);
		
		tablaCampa�as.getColumnModel().getColumn(0).setPreferredWidth(170);
		tablaCampa�as.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCampa�as.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaCampa�as.getColumnModel().getColumn(3).setPreferredWidth(215);
		tablaCampa�as.getColumnModel().getColumn(4).setPreferredWidth(215);
		tablaCampa�as.getColumnModel().getColumn(5).setPreferredWidth(215);
		
		// Columna oculta con los id de las Campa�as
		tablaCampa�as.getColumnModel().getColumn(6).setMaxWidth(0);
		tablaCampa�as.getColumnModel().getColumn(6).setMinWidth(0);
		tablaCampa�as.getColumnModel().getColumn(6).setResizable(false);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
		tablaCampa�as.getTableHeader().getColumnModel().getColumn(6).setResizable(false);
		contentPane.setLayout(null);
		
		scrollPane.setViewportView(tablaCampa�as);
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
			
			List<Campa�a> campa�as = api.listarCampa�as();
			
			List<Campa�a> resultado = campa�as.stream().filter((c)->c.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaCampa�as.getModel();
			
			while(tablaCampa�as.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Campa�a c : resultado) {
				
				modelo.addRow(new Object[] {c.getNombre(), Helper.convertir(c.getFechaInicio()), Helper.convertir(c.getFechaFin()), c.getInstitucion().getNombre(), c.getDescripcion(), c.getMotivo(), c.getId()});
				
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void verDonaciones() {
		
		try {
			
			Campa�a campa�a = api.buscarCampa�a((Long) tablaCampa�as.getValueAt(tablaCampa�as.getSelectedRow(), tablaCampa�as.getColumnCount() - 1));
			
			if(!campa�a.estaVigente()) {
				ConsultaDonaciones consultaDonaciones = new ConsultaDonaciones(campa�a);
				consultaDonaciones.setVisible(true);
			}
			else {
				JOptionPane.showMessageDialog(this, "La campa�a seleccionada sigue vigente");
			}
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
}
