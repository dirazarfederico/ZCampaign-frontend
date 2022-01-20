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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.modelo.*;

public class GestionLugarHabilitado extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaLugarHabilitado;
	private JButton btnModificar;
	private Campaña campaña;
	private IFacade api;
	private JButton btnBorrarLugarHabilitado;

	public GestionLugarHabilitado(Campaña nuevaCampaña) {
		
		this.campaña = nuevaCampaña;
		
		setTitle("Lugares Habilitados - " + campaña.getNombre());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 764, 241);
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
		scrollPane.setBounds(10, 11, 534, 170);
		contentPane.add(scrollPane);
		
		tablaLugarHabilitado = new JTable();
		tablaLugarHabilitado.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnModificar.setEnabled(true);
				btnBorrarLugarHabilitado.setEnabled(true);
			}
		});
		tablaLugarHabilitado.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Nombre", "Contacto", "Dirección", ""
			}
		);
		
		tablaLugarHabilitado.setModel(modelo);
		
		tablaLugarHabilitado.getColumnModel().getColumn(0).setPreferredWidth(200);
		tablaLugarHabilitado.getColumnModel().getColumn(1).setPreferredWidth(200);
		tablaLugarHabilitado.getColumnModel().getColumn(2).setPreferredWidth(150);
		
		tablaLugarHabilitado.getColumnModel().getColumn(3).setMaxWidth(0);
		tablaLugarHabilitado.getColumnModel().getColumn(3).setMinWidth(0);
		tablaLugarHabilitado.getColumnModel().getColumn(3).setResizable(false);
		tablaLugarHabilitado.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
		tablaLugarHabilitado.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
		tablaLugarHabilitado.getTableHeader().getColumnModel().getColumn(3).setResizable(false);
		
		scrollPane.setViewportView(tablaLugarHabilitado);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener((e)->modificarLugarHabilitado());
		btnModificar.setBounds(554, 11, 184, 23);
		contentPane.add(btnModificar);
		
		JButton btnNuevoLugarHabilitado = new JButton("Nuevo Lugar habilitado");
		btnNuevoLugarHabilitado.addActionListener((e) -> nuevoLugarHabilitado());
		btnNuevoLugarHabilitado.setBounds(554, 158, 184, 23);
		contentPane.add(btnNuevoLugarHabilitado);
		
		btnBorrarLugarHabilitado = new JButton("Borrar Lugar Habilitado");
		btnBorrarLugarHabilitado.setEnabled(false);
		btnBorrarLugarHabilitado.setBounds(554, 86, 184, 23);
		btnBorrarLugarHabilitado.addActionListener((e)-> borrarLugarHabilitado());
		contentPane.add(btnBorrarLugarHabilitado);
		
		refrescar();
	}
	
	public void refrescar() {
		
		try {
		
			List<LugarHabilitado> lista = api.listarLugaresHabilitados();
		
			List<LugarHabilitado> lugarHabilitado = lista.stream().filter((l)-> l.getCampaña().getId()==this.campaña.getId() && l.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaLugarHabilitado.getModel();
			
			while(tablaLugarHabilitado.getRowCount() > 0) {
				modelo.removeRow(0);
			}
		
			for(LugarHabilitado l : lugarHabilitado) {
			
				modelo.addRow(new Object[] {l.getNombre(), l.getContacto(), l.getDireccion().toString(), l.getId()});
			
			}
		
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void nuevoLugarHabilitado() {
		
		AltaLugarHabilitado altaLugarHabilitado = new AltaLugarHabilitado(this, campaña);
		altaLugarHabilitado.setVisible(true);
		
	}
	
	private void modificarLugarHabilitado() {
		
		try {
			
			LugarHabilitado lugarHabilitado = api.buscarLugarHabilitado((Long) tablaLugarHabilitado.getValueAt(tablaLugarHabilitado.getSelectedRow(), tablaLugarHabilitado.getColumnCount() -1));
			
			AltaLugarHabilitado altaLugarHabilitado = new AltaLugarHabilitado(this, campaña, lugarHabilitado);
			altaLugarHabilitado.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void borrarLugarHabilitado() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionLugarHabilitado.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar el lugar habilitado?", "Borrar Lugar Habilitado", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				LugarHabilitado LugarHabilitado = api.buscarLugarHabilitado((Long) tablaLugarHabilitado.getValueAt(tablaLugarHabilitado.getSelectedRow(), tablaLugarHabilitado.getColumnCount() -1));
				
				this.api.borrarLugarHabilitado(LugarHabilitado);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
