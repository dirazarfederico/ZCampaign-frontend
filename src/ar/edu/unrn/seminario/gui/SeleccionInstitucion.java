package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.Institucion;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

public class SeleccionInstitucion extends VentanaSeleccion {

	private JPanel contentPane;
	private JTable tablaInstituciones;
	private IFacade api;
	private Institucion institucionSeleccionada;
	private JButton btnAceptar;

	public SeleccionInstitucion(WrapperSeleccion wrapper) {
		
		super(wrapper);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Seleccion de institución");
		setBounds(100, 100, 436, 232);
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
		scrollPane.setBounds(10, 11, 408, 146);
		contentPane.add(scrollPane);
		
		tablaInstituciones = new JTable();
		
		tablaInstituciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
			}
		});
		
		scrollPane.setViewportView(tablaInstituciones);
		tablaInstituciones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
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
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(329, 171, 89, 23);
		btnAceptar.setEnabled(false);
		btnAceptar.addActionListener((e)-> seleccionarInstitucion());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(230, 171, 89, 23);
		btnCancelar.addActionListener((e)-> this.dispose());
		contentPane.add(btnCancelar);
	
		refrescar();
	}
	
	private void refrescar() {
		
		try {
		
			List<Institucion> lista = api.listarInstituciones();
		
			DefaultTableModel modelo = (DefaultTableModel) tablaInstituciones.getModel();
		
			for(Institucion i : lista) {
			
				modelo.addRow(new Object[] {i.getNombre(), i.getCuil(), i.getContacto(), i.getDireccion().toString(), i.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void seleccionarInstitucion() {
		
		try {
			
			institucionSeleccionada = api.buscarInstitucion((Long) tablaInstituciones.getValueAt(tablaInstituciones.getSelectedRow(), tablaInstituciones.getColumnCount() - 1));
			
			this.mostrarSeleccion(institucionSeleccionada);
			
			this.dispose();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public Institucion getInstitucion() {
		return this.institucionSeleccionada;
	}
}
