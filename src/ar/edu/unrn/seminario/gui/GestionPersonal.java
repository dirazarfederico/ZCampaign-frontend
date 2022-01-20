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

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.Campaña;
import ar.edu.unrn.seminario.modelo.Personal;
import ar.edu.unrn.seminario.modelo.Institucion;
import ar.edu.unrn.seminario.modelo.Personal;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class GestionPersonal extends JFrame implements IVentanaGestion {

	private Institucion institucion;
	private JPanel contentPane;
	private JTable tablaPersonal;
	private JButton btnModificar;
	private JButton btnNuevoPersonal;
	private IFacade api;
	private AltaPersonal altaPersonal;
	private JButton btnBorrarPersonal;

	public GestionPersonal(Institucion institucion) {
		this.institucion = institucion;
		setTitle("Personal - " + institucion.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 682, 225);
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
		scrollPane.setBounds(10, 11, 463, 159);
		contentPane.add(scrollPane);
		
		tablaPersonal = new JTable();
		tablaPersonal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnBorrarPersonal.setEnabled(true);
			}
		});
		tablaPersonal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Nombre", "Apellido", "Email", ""
			}
		);
		
		tablaPersonal.setModel(modelo);
		
		tablaPersonal.getColumnModel().getColumn(0).setPreferredWidth(100);
		tablaPersonal.getColumnModel().getColumn(1).setPreferredWidth(150);
		tablaPersonal.getColumnModel().getColumn(2).setPreferredWidth(150);
		tablaPersonal.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		// Columna oculta con los id de los personales
		tablaPersonal.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaPersonal.getColumnModel().getColumn(4).setMinWidth(0);
		tablaPersonal.getColumnModel().getColumn(4).setResizable(false);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		scrollPane.setViewportView(tablaPersonal);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener((e)->modificarPersonal());
		btnModificar.setBounds(483, 14, 173, 23);
		contentPane.add(btnModificar);
		
		btnNuevoPersonal = new JButton("Nuevo Personal");
		btnNuevoPersonal.addActionListener((e)->agregarPersonal());
		btnNuevoPersonal.setBounds(483, 147, 173, 23);
		contentPane.add(btnNuevoPersonal);
		
		btnBorrarPersonal = new JButton("Borrar Personal");
		btnBorrarPersonal.setEnabled(false);
		btnBorrarPersonal.setBounds(483, 79, 173, 23);
		btnBorrarPersonal.addActionListener((e)-> borrarPersonal());
		contentPane.add(btnBorrarPersonal);
		
		refrescar();
	}
	
	public void refrescar() {
		
		try {
			
			List<Personal> lista = api.listarPersonales();
			
			List<Personal> personal = lista.stream().filter((p)-> p.getInstitucion().getId()==this.institucion.getId() && p.activo()).collect(Collectors.toList());
		
			DefaultTableModel modelo = (DefaultTableModel) tablaPersonal.getModel();
			
			while(tablaPersonal.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Personal p : personal) {
			
				modelo.addRow(new Object[] {p.getPersona().getDni(), p.getPersona().getNombre(), p.getPersona().getApellido(), p.getEmail(), p.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void agregarPersonal() {
		this.altaPersonal = new AltaPersonal(this, this.institucion);
		this.altaPersonal.setVisible(true);
	}
	
	private void modificarPersonal() {
		
		try {
			Personal personal = api.buscarPersonal((Long) tablaPersonal.getValueAt(tablaPersonal.getSelectedRow(), tablaPersonal.getColumnCount() - 1));
		
			this.altaPersonal = new AltaPersonal(this, this.institucion, personal);
			this.altaPersonal.setVisible(true);
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void borrarPersonal() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionPersonal.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar el personal?", "Borrar Personal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Personal Personal = api.buscarPersonal((Long) tablaPersonal.getValueAt(tablaPersonal.getSelectedRow(), tablaPersonal.getColumnCount() -1));
				
				this.api.borrarPersonal(Personal);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
