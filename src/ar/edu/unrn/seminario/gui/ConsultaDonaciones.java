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
import ar.edu.unrn.seminario.modelo.Donacion;
import ar.edu.unrn.seminario.servicio.Helper;

public class ConsultaDonaciones extends JFrame {

	private JPanel contentPane;
	private JTable tablaDonaciones;
	private IFacade api;
	private Campaña campaña;
	
	public ConsultaDonaciones(Campaña nuevaCampaña) {
		setTitle("Donaciones - " + nuevaCampaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 486, 186);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.campaña = nuevaCampaña;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		setResizable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 452, 127);
		contentPane.add(scrollPane);
		
		tablaDonaciones = new JTable();
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Fecha", "Descripción", "Dirección", ""
			}
		);
		
		tablaDonaciones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tablaDonaciones.setModel(modelo);
		
		tablaDonaciones.getColumnModel().getColumn(0).setPreferredWidth(100);
		tablaDonaciones.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaDonaciones.getColumnModel().getColumn(2).setPreferredWidth(150);
		
		// Columna oculta con los id de las donaciones
		tablaDonaciones.getColumnModel().getColumn(3).setMaxWidth(0);
		tablaDonaciones.getColumnModel().getColumn(3).setMinWidth(0);
		tablaDonaciones.getColumnModel().getColumn(3).setResizable(false);
		tablaDonaciones.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
		tablaDonaciones.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
		tablaDonaciones.getTableHeader().getColumnModel().getColumn(3).setResizable(false);
		
		scrollPane.setViewportView(tablaDonaciones);
		
		refrescar();
	}

	private void refrescar() {
		
		try {
			
			List<Donacion> donaciones = this.api.listarDonaciones();
			
			List<Donacion> resultado = donaciones.stream().filter((d)-> d.getCampaña().getId() == this.campaña.getId() && d.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaDonaciones.getModel();
			
			for(Donacion d : resultado) {
				
				modelo.addRow(new Object[] {Helper.convertir(d.getFecha()), d.getDescripcion(), d.getDireccion().toString(), d.getId()});
				
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}

}
