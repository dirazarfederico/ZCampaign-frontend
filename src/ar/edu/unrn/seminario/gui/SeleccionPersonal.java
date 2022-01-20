package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.Institucion;
import ar.edu.unrn.seminario.modelo.Persona;
import ar.edu.unrn.seminario.modelo.Personal;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SeleccionPersonal extends VentanaSeleccion {

	private JPanel contentPane;
	private JTable tablaPersonal;
	private JButton btnAceptar;
	private Personal personal = null;
	private IFacade api;
	private Institucion institucion;

	public SeleccionPersonal(WrapperSeleccion nuevoWrapperSeleccion, Institucion institucion) {
		
		super(nuevoWrapperSeleccion);
		
		this.institucion = institucion;
		
		setTitle("Asignar un Personal");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 615, 249);
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
		scrollPane.setBounds(10, 11, 579, 153);
		contentPane.add(scrollPane);
		
		tablaPersonal = new JTable();
		tablaPersonal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
			}
		});
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Nombre", "Apellido", "Email", ""
			}
		);
		
		tablaPersonal.setModel(modelo);
		scrollPane.setViewportView(tablaPersonal);
		
		//Columna invisible con los id de la persona
		tablaPersonal.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaPersonal.getColumnModel().getColumn(4).setMinWidth(0);
		tablaPersonal.getColumnModel().getColumn(4).setResizable(false);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaPersonal.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setEnabled(false);
		btnAceptar.setBounds(500, 176, 89, 23);
		btnAceptar.addActionListener((e)->this.seleccionarPersonal());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Omitir");
		btnCancelar.setBounds(401, 176, 89, 23);
		btnCancelar.addActionListener((e) -> this.dispose());
		contentPane.add(btnCancelar);
		
		refrescar();
		
	}
	
	public Personal getPersonal() {
		return this.personal;
	}

	private void refrescar() {
		
		try {
			
			List<Personal> personales = api.listarPersonales();
			
			List<Personal> resultado = null;
			
			resultado = personales.stream().filter((p)->p.getInstitucion().getId() == this.institucion.getId()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaPersonal.getModel();
			
			for(Personal p : resultado) {
			
				modelo.addRow(new Object[] {p.getPersona().getDni(), p.getPersona().getNombre(), p.getPersona().getApellido(), p.getEmail(), p.getId()});
			
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void seleccionarPersonal() {
		
		try {
			
			this.personal = this.api.buscarPersonal((Long) tablaPersonal.getValueAt(tablaPersonal.getSelectedRow(), tablaPersonal.getColumnCount() - 1));
			
			this.mostrarSeleccion(this.personal);
			
			this.dispose();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
}
