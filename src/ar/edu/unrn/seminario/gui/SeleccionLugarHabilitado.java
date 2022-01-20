package ar.edu.unrn.seminario.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
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
import ar.edu.unrn.seminario.modelo.Institucion;
import ar.edu.unrn.seminario.modelo.LugarHabilitado;

public class SeleccionLugarHabilitado extends VentanaSeleccion{

	private JPanel contentPane;
	private JTable tablaLugaresHabilitados;
	private IFacade api;
	private LugarHabilitado lugarHabilitadoSeleccionado;
	private JButton btnAceptar;
	private Campaña campaña;

	public SeleccionLugarHabilitado(Campaña nuevaCampaña, WrapperSeleccion wrapper) {
	
		super(wrapper);
		
		this.campaña = nuevaCampaña;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Seleccion de Lugar Habilitado");
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
		
		tablaLugaresHabilitados = new JTable();
		
		tablaLugaresHabilitados.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
			}
		});
		
		scrollPane.setViewportView(tablaLugaresHabilitados);
		tablaLugaresHabilitados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
				new Object[][] {
				},
				new String[] {
					"Nombre", "Contacto", "Dirección", ""
				}
			);
			
		tablaLugaresHabilitados.setModel(modelo);
			
		tablaLugaresHabilitados.getColumnModel().getColumn(0).setPreferredWidth(150);
		tablaLugaresHabilitados.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaLugaresHabilitados.getColumnModel().getColumn(2).setPreferredWidth(300);
			
		// Columna oculta con los id de las instituciones
		tablaLugaresHabilitados.getColumnModel().getColumn(3).setMaxWidth(0);
		tablaLugaresHabilitados.getColumnModel().getColumn(3).setMinWidth(0);
		tablaLugaresHabilitados.getColumnModel().getColumn(3).setResizable(false);
		tablaLugaresHabilitados.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
		tablaLugaresHabilitados.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
		tablaLugaresHabilitados.getTableHeader().getColumnModel().getColumn(3).setResizable(false);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(329, 171, 89, 23);
		btnAceptar.setEnabled(false);
		btnAceptar.addActionListener((e)-> seleccionarLugarHabilitado());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(230, 171, 89, 23);
		btnCancelar.addActionListener((e)-> this.dispose());
		contentPane.add(btnCancelar);
		
		refrescar();
	}
	
	private void refrescar() {
		
		try {
		
			List<LugarHabilitado> lista = api.listarLugaresHabilitados();
			
			List<LugarHabilitado> resultado = null;
		
			resultado = lista.stream().filter((l)->l.getCampaña().getId() == this.campaña.getId()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaLugaresHabilitados.getModel();
		
			for(LugarHabilitado l : resultado) {
			
				modelo.addRow(new Object[] {l.getNombre(), l.getContacto(), l.getDireccion().toString(), l.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void seleccionarLugarHabilitado() {
		
		try {
			
			lugarHabilitadoSeleccionado = api.buscarLugarHabilitado(((Long) tablaLugaresHabilitados.getValueAt(tablaLugaresHabilitados.getSelectedRow(), tablaLugaresHabilitados.getColumnCount() - 1)));
			
			this.mostrarSeleccion(lugarHabilitadoSeleccionado);
			
			this.dispose();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	public LugarHabilitado getLugarHabilitado() {
		return this.lugarHabilitadoSeleccionado;
	}
}
