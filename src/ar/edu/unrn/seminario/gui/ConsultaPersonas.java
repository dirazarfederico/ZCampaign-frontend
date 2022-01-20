package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import ar.edu.unrn.seminario.modelo.Ciudadano;
import ar.edu.unrn.seminario.modelo.Persona;

public class ConsultaPersonas extends JFrame {

	private JPanel contentPane;
	private JTable tablaPersonas;
	private IFacade api;
	
	public ConsultaPersonas() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 520, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		setResizable(false);
		
		setTitle("Listado de personas");
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 489, 251);
		contentPane.add(scrollPane);
		
		tablaPersonas = new JTable();
		
		ModeloTabla modelo= new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Apellido", "Nombre", "Personal", "Ciudadano", ""
			}
		);
		
		tablaPersonas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tablaPersonas.setModel(modelo);
		
		tablaPersonas.getColumnModel().getColumn(0).setPreferredWidth(90);
		tablaPersonas.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaPersonas.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaPersonas.getColumnModel().getColumn(3).setPreferredWidth(100);
		tablaPersonas.getColumnModel().getColumn(4).setPreferredWidth(100);
			
		scrollPane.setViewportView(tablaPersonas);
		
		cargar();
	}
	
	private void cargar() {
		
		try {
			
			List<Persona> personas = api.listarPersonas();
			
			//Id de las personas que son personal
			List<Long> idPersonal = api.listarPersonales().stream().filter((p)->p.activo()).map((p) -> p.getPersona().getId()).collect(Collectors.toList());
			
			//Id de las personas que son ciudadanos
			List<Long> idCiudadanos = api.listarCiudadanos().stream().filter((c)->c.activo()).map((c) -> c.getPersona().getId()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaPersonas.getModel();
		
			for(Persona p : personas) {
			
				boolean esPersonal = false, esCiudadano = false;
				
				for(Long id : idPersonal) {
					if(id.compareTo(p.getId()) == 0) 
						esPersonal = true;
				}
				
				for(Long id : idCiudadanos) {
					if(id.compareTo(p.getId()) == 0) 
						esCiudadano = true;
				}
				
				String personal = esPersonal ? "Sí" : "No";
				
				String ciudadano = esCiudadano ? "Sí" : "No";
				
				modelo.addRow(new Object[] {p.getDni(), p.getNombre(), p.getApellido(), personal, ciudadano});
			
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}

}
