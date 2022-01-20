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
import ar.edu.unrn.seminario.modelo.Donacion;
import ar.edu.unrn.seminario.modelo.LugarHabilitado;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class GestionDonaciones extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaDonaciones;
	private JButton btnModificar;
	private Campaña campaña;
	private AltaDonacion altaDonacion;
	private IFacade api;
	private JButton btnBorrarDonacion;

	public GestionDonaciones(Campaña nuevaCampaña) {
		setTitle("Donaciones - " + nuevaCampaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 186);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.campaña = nuevaCampaña;
		this.altaDonacion = new AltaDonacion(this, campaña);
		
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
		tablaDonaciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnBorrarDonacion.setEnabled(true);
			}
		});
		
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
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.setBounds(472, 11, 160, 23);
		btnModificar.addActionListener((e)->modificarDonacion());
		contentPane.add(btnModificar);
		
		JButton btnNuevaDonacion = new JButton("Nueva Donación");
		btnNuevaDonacion.addActionListener((e)->this.altaDonacion.setVisible(true));
		btnNuevaDonacion.setBounds(472, 112, 160, 23);
		contentPane.add(btnNuevaDonacion);
		
		btnBorrarDonacion = new JButton("Borrar Donacion");
		btnBorrarDonacion.setBounds(472, 60, 160, 23);
		btnBorrarDonacion.setEnabled(false);
		btnBorrarDonacion.addActionListener((e)-> this.borrarDonacion());
		contentPane.add(btnBorrarDonacion);
		
		refrescar();
	}

	@Override
	public void refrescar() {
		
		try {
			
			List<Donacion> donaciones = this.api.listarDonaciones();
			
			List<Donacion> resultado = donaciones.stream().filter((d)-> d.getCampaña().getId() == this.campaña.getId() && d.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaDonaciones.getModel();
			
			while(tablaDonaciones.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Donacion d : resultado) {
				
				modelo.addRow(new Object[] {Helper.convertir(d.getFecha()), d.getDescripcion(), d.getDireccion().toString(), d.getId()});
				
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarDonacion() {
		
		try {
			Donacion donacion = this.api.buscarDonacion((Long) tablaDonaciones.getValueAt(tablaDonaciones.getSelectedRow(), tablaDonaciones.getColumnCount() -1));
			
			if(donacion.getOrden()!=null) {
				FinalizarOrden finalizarOrden = new FinalizarOrden(this, donacion);
				finalizarOrden.setVisible(true);
			}
			else {
				
				List<LugarHabilitado> lugares = api.listarLugaresHabilitados();
				
				List<LugarHabilitado> resultado = lugares.stream().filter((l)->l.getDireccion().getId() == donacion.getDireccion().getId()).collect(Collectors.toList());
				
				LugarHabilitado lugarHabilitado = resultado.get(0);
				
				altaDonacion = new AltaDonacion(this, donacion, lugarHabilitado);
				altaDonacion.setVisible(true);
			}
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void borrarDonacion() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionDonaciones.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar la donacion?", "Borrar Donación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Donacion donacion = api.buscarDonacion((Long) tablaDonaciones.getValueAt(tablaDonaciones.getSelectedRow(), tablaDonaciones.getColumnCount() -1));
				
				this.api.borrarDonacion(donacion);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
