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
import ar.edu.unrn.seminario.modelo.Institucion;
import ar.edu.unrn.seminario.modelo.PedidoDeRetiro;
import ar.edu.unrn.seminario.modelo.Persona;

public class SeleccionPersona extends VentanaSeleccion {

	private JPanel contentPane;
	private JTable tablaPersonas;
	private JButton btnAceptar;
	private Persona persona = null;
	private IFacade api;
	private Institucion institucion;

	public SeleccionPersona(WrapperSeleccion nuevaSeleccion) {
		
		super(nuevaSeleccion);
		
		setTitle("Asignar una Persona");
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
		
		institucion = null;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 579, 153);
		contentPane.add(scrollPane);
		
		tablaPersonas = new JTable();
		tablaPersonas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnAceptar.setEnabled(true);
			}
		});
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"DNI", "Nombre", "Apellido", ""
			}
		);
		
		tablaPersonas.setModel(modelo);
		scrollPane.setViewportView(tablaPersonas);
		
		//Columna invisible con los id de la persona
		tablaPersonas.getColumnModel().getColumn(3).setMaxWidth(0);
		tablaPersonas.getColumnModel().getColumn(3).setMinWidth(0);
		tablaPersonas.getColumnModel().getColumn(3).setResizable(false);
		tablaPersonas.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
		tablaPersonas.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
		tablaPersonas.getTableHeader().getColumnModel().getColumn(3).setResizable(false);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setEnabled(false);
		btnAceptar.addActionListener((e)->seleccionarPersona());
		btnAceptar.setBounds(500, 176, 89, 23);
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Omitir");
		btnCancelar.setBounds(401, 176, 89, 23);
		btnCancelar.addActionListener((e) -> this.dispose());
		contentPane.add(btnCancelar);
		
		refrescar();
	}
	
	public SeleccionPersona(WrapperSeleccion nuevaSeleccion, Institucion institucion) {
		
		this(nuevaSeleccion);
		
		this.institucion = institucion;
		
		refrescar();
	}
	
	public Persona getPersona() {
		return this.persona;
	}

	private void refrescar() {
		
		try {
			
			List<Persona> personas = api.listarPersonas();
			
			List<Persona> resultado = null;
			
			if(institucion != null) {
				
				//Busco los id de personas asociadas con un personal
				List<Long> idPersonales = api.listarPersonales().stream().map((p)-> p.getPersona().getId()).collect(Collectors.toList());
				
				resultado = personas.stream().filter((p)-> {
					//Se asume que no está
					boolean esta = false;
					
					for(Long id : idPersonales) {
						//Si se encuentra, ya es personal
						if(id.compareTo(p.getId()) == 0) {
							esta = true;
						}
					}
					
					return !esta;
					
				}).collect(Collectors.toList());
			}
			else {
				
				//Busco los id de personas asociadas con un Ciudadano
				List<Long> idCiudadanos = api.listarCiudadanos().stream().map((c)-> c.getPersona().getId()).collect(Collectors.toList());
				
				resultado = personas.stream().filter((p)-> {
					
					//Se asume que no está
					boolean esta = false;
					
					for(Long id : idCiudadanos) {
						//Si se encuentra, ya es ciudadano
						if(id.compareTo(p.getId()) == 0) {
							esta = true;
						}
					}
					
					return !esta;
					
				}).collect(Collectors.toList());
			}
			
			DefaultTableModel modelo = (DefaultTableModel) tablaPersonas.getModel();
			
			while(tablaPersonas.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Persona p : resultado) {
			
				modelo.addRow(new Object[] {p.getDni(), p.getNombre(), p.getApellido(), p.getId()});
			
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void seleccionarPersona() {
		
		try {
			
			this.persona = this.api.buscarPersona((Long) tablaPersonas.getValueAt(tablaPersonas.getSelectedRow(), tablaPersonas.getColumnCount() - 1));
			
			this.mostrarSeleccion(this.persona);
			
			this.dispose();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
}
