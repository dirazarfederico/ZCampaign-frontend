package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.*;

public class SeleccionCiudadano extends VentanaSeleccion {

	private JPanel contentPane;
	private JTable tablaCiudadanos;
	private JButton btnAceptar;
	private Ciudadano seleccionado;
	private IFacade api;

	public SeleccionCiudadano(WrapperSeleccion nuevoWrapperSeleccion) {
		
		super(nuevoWrapperSeleccion);
		
		setTitle("Seleccionar Ciudadano");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 616, 260);
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
		scrollPane.setBounds(10, 11, 580, 153);
		contentPane.add(scrollPane);
		
		tablaCiudadanos = new JTable();
		tablaCiudadanos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
			}
		});
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Nombre", "Apellido", "Dirección", ""
			}
		);
		
		tablaCiudadanos.setModel(modelo);
		
		// Columna oculta con los id de las Campañas
		tablaCiudadanos.getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCiudadanos.getColumnModel().getColumn(4).setMinWidth(0);
		tablaCiudadanos.getColumnModel().getColumn(4).setResizable(false);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
		tablaCiudadanos.getTableHeader().getColumnModel().getColumn(4).setResizable(false);
		
		scrollPane.setViewportView(tablaCiudadanos);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setEnabled(false);
		btnAceptar.setBounds(501, 187, 89, 23);
		btnAceptar.addActionListener((e)-> seleccionarCiudadano());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(402, 187, 89, 23);
		btnCancelar.addActionListener((e)-> this.dispose());
		contentPane.add(btnCancelar);
		
		refrescar();
	}
	
	private void refrescar() {
		
		try {
		
			List<Ciudadano> lista = api.listarCiudadanos();
		
			DefaultTableModel modelo = (DefaultTableModel) tablaCiudadanos.getModel();
		
			for(Ciudadano c : lista) {
			
				modelo.addRow(new Object[] {c.getPersona().getDni(), c.getPersona().getNombre(), c.getPersona().getApellido(),c.getDireccion().toString(), c.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void seleccionarCiudadano() {
		
		try {
			
			this.seleccionado = api.buscarCiudadano((Long) tablaCiudadanos.getValueAt(tablaCiudadanos.getSelectedRow(), tablaCiudadanos.getColumnCount() - 1));
			
			this.mostrarSeleccion(this.seleccionado);
			
			this.dispose();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	public Ciudadano getCiudadano() {
		return this.seleccionado;
	}

}
