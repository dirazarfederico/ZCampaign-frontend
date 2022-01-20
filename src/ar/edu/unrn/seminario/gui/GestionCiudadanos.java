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
import ar.edu.unrn.seminario.modelo.Ciudadano;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;

public class GestionCiudadanos extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaCiudadanos;
	private JButton btnModificar;
	private IFacade api;
	private AltaCiudadano altaCiudadano;
	private JButton btnBorrarCiudadano;

	public GestionCiudadanos() {
		setTitle("Ciudadanos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 623, 223);
		setResizable(false);
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
		scrollPane.setBounds(10, 11, 445, 162);
		contentPane.add(scrollPane);
		
		tablaCiudadanos = new JTable();
		tablaCiudadanos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnBorrarCiudadano.setEnabled(true);
			}
		});
		
		ModeloTabla modelo= new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Apellido", "Nombre", "Dirección", ""
			}
		);
		
		tablaCiudadanos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		tablaCiudadanos.setModel(modelo);
		
		tablaCiudadanos.getColumnModel().getColumn(0).setPreferredWidth(90);
		tablaCiudadanos.getColumnModel().getColumn(1).setPreferredWidth(100);
		tablaCiudadanos.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaCiudadanos.getColumnModel().getColumn(3).setPreferredWidth(155);
		
		// Columna oculta con los id de los Ciudadanos
		tablaCiudadanos.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCiudadanos.getColumnModel().getColumn(4).setMinWidth(0);
		tablaCiudadanos.getColumnModel().getColumn(4).setResizable(false);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
	
		scrollPane.setViewportView(tablaCiudadanos);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(465, 11, 134, 23);
		btnModificar.setEnabled(false);
		btnModificar.addActionListener((e)-> modificarCiudadano());
		contentPane.add(btnModificar);
		
		JButton btnNuevoCiudadano = new JButton("Nuevo Ciudadano");
		btnNuevoCiudadano.addActionListener((e)-> nuevoCiudadano());
		btnNuevoCiudadano.setBounds(465, 147, 134, 23);
		contentPane.add(btnNuevoCiudadano);
		
		btnBorrarCiudadano = new JButton("Borrar Ciudadano");
		btnBorrarCiudadano.setEnabled(false);
		btnBorrarCiudadano.setBounds(465, 80, 134, 23);
		btnBorrarCiudadano.addActionListener((e)-> borrarCiudadano());
		contentPane.add(btnBorrarCiudadano);
		
		refrescar();
		
	}
	
	public void refrescar() {
		
		try {
		
			List<Ciudadano> lista = api.listarCiudadanos();
			
			List<Ciudadano> resultado = lista.stream().filter((c)->c.activo()).collect(Collectors.toList());
		
			DefaultTableModel modelo = (DefaultTableModel) tablaCiudadanos.getModel();
			
			while(tablaCiudadanos.getRowCount() > 0) {
				modelo.removeRow(0);
			}
		
			for(Ciudadano c : resultado) {
			
				modelo.addRow(new Object[] {c.getPersona().getDni(), c.getPersona().getNombre(), c.getPersona().getApellido(),c.getDireccion().toString(), c.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void nuevoCiudadano() {
		
		altaCiudadano = new AltaCiudadano(this);
		altaCiudadano.setVisible(true);
	}
	
	private void modificarCiudadano() {
		
		try {
			
			Ciudadano ciudadano = api.buscarCiudadano((Long) tablaCiudadanos.getValueAt(tablaCiudadanos.getSelectedRow(), tablaCiudadanos.getColumnCount() -1));
			
			altaCiudadano = new AltaCiudadano(this, ciudadano);
			altaCiudadano.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void borrarCiudadano() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionCiudadanos.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar el ciudadano?", "Borrar Ciudadano", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Ciudadano ciudadano = api.buscarCiudadano((Long) tablaCiudadanos.getValueAt(tablaCiudadanos.getSelectedRow(), tablaCiudadanos.getColumnCount() -1));
				
				this.api.borrarCiudadano(ciudadano);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
