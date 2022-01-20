package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.modelo.*;
import ar.edu.unrn.seminario.excepciones.AppException;
import java.util.*;
import java.util.stream.Collectors;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestionInstituciones extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaInstituciones;
	private JButton btnModificar;
	private JButton btnPersonal;
	
	private IFacade api;
	private JButton btnBorrarInstitucion;

	public GestionInstituciones() {
		setTitle("Instituciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 714, 220);
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
		scrollPane.setBounds(10, 11, 518, 169);
		contentPane.add(scrollPane);
		
		tablaInstituciones = new JTable();
		tablaInstituciones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablaInstituciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnPersonal.setEnabled(true);
				btnBorrarInstitucion.setEnabled(true);
			}
		});
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "CUIL", "Contacto", "Dirección", ""
			}
		);
		
		tablaInstituciones.setModel(modelo);
		
		tablaInstituciones.getColumnModel().getColumn(0).setPreferredWidth(150);
		tablaInstituciones.getColumnModel().getColumn(1).setPreferredWidth(110);
		tablaInstituciones.getColumnModel().getColumn(2).setPreferredWidth(200);
		tablaInstituciones.getColumnModel().getColumn(3).setPreferredWidth(300);
		
		// Columna oculta con los id de las instituciones
		tablaInstituciones.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaInstituciones.getColumnModel().getColumn(4).setMinWidth(0);
		tablaInstituciones.getColumnModel().getColumn(4).setResizable(false);
		tablaInstituciones.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaInstituciones.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaInstituciones.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		scrollPane.setViewportView(tablaInstituciones);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener((e)-> modificarInstitucion());
		btnModificar.setBounds(538, 11, 150, 23);
		contentPane.add(btnModificar);
		
		btnPersonal = new JButton("Personal");
		btnPersonal.setEnabled(false);
		btnPersonal.addActionListener((e)-> gestionarPersonal());
		
		btnPersonal.setBounds(538, 60, 150, 23);
		contentPane.add(btnPersonal);
		
		JButton btnNuevaInstitucion = new JButton("Nueva Institución");
		btnNuevaInstitucion.addActionListener((e)->nuevaInstitucion());
		btnNuevaInstitucion.setBounds(538, 157, 150, 23);
		contentPane.add(btnNuevaInstitucion);
		
		btnBorrarInstitucion = new JButton("Borrar Institucion");
		btnBorrarInstitucion.setEnabled(false);
		btnBorrarInstitucion.setBounds(538, 111, 150, 23);
		btnBorrarInstitucion.addActionListener((e) -> borrarInstitucion());
		contentPane.add(btnBorrarInstitucion);
		
		refrescar();
	}

	public void refrescar() {
		
		try {
		
			List<Institucion> lista = api.listarInstituciones();
			
			List<Institucion> resultado = lista.stream().filter((i)-> i.activo()).collect(Collectors.toList());
		
			DefaultTableModel modelo = (DefaultTableModel) tablaInstituciones.getModel();
			
			while(tablaInstituciones.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Institucion i : resultado) {
			
				modelo.addRow(new Object[] {i.getNombre(), i.getCuil(), i.getContacto(), i.getDireccion().toString(), i.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void modificarInstitucion() {
		
		try {
			
			Institucion institucion = api.buscarInstitucion((Long) tablaInstituciones.getValueAt(tablaInstituciones.getSelectedRow(), tablaInstituciones.getColumnCount() -1));

			AltaInstitucion modificarInstitucion = new AltaInstitucion(institucion, this);
			modificarInstitucion.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void gestionarPersonal() {
		
		try {
			Institucion institucion = api.buscarInstitucion((Long) tablaInstituciones.getValueAt(tablaInstituciones.getSelectedRow(), tablaInstituciones.getColumnCount() -1));
			
			GestionPersonal gestionPersonal = new GestionPersonal(institucion);
			gestionPersonal.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void nuevaInstitucion() {
		
		AltaInstitucion altaInstitucion = new AltaInstitucion(this);
		altaInstitucion.setVisible(true);
	}
	
	private void borrarInstitucion() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionInstituciones.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar la institución?", "Borrar Institución", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Institucion Institucion = api.buscarInstitucion((Long) tablaInstituciones.getValueAt(tablaInstituciones.getSelectedRow(), tablaInstituciones.getColumnCount() -1));
				
				this.api.borrarInstitucion(Institucion);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
