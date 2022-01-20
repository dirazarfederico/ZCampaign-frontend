package ar.edu.unrn.seminario.gui;

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
import ar.edu.unrn.seminario.modelo.Visita;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class GestionVisitas extends JFrame implements IVentanaGestion {

	private JPanel contentPane;
	private JTable tablaVisitas;
	private JButton btnModificar;
	private JButton btnNuevaVisita;
	private IFacade api;
	private OrdenDeRetiro orden;
	private IVentanaGestion padre;
	private JButton btnBorrarVisita;

	public GestionVisitas(IVentanaGestion padre, OrdenDeRetiro orden) {
		setTitle("Visitas - Orden " + orden.getEstado().toLowerCase());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 623, 224);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.padre = padre;
		this.orden = orden;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 421, 151);
		contentPane.add(scrollPane);
		
		tablaVisitas = new JTable();
		if(!orden.estaFinalizada()) {
			tablaVisitas.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					btnModificar.setEnabled(true);
					btnBorrarVisita.setEnabled(true);
				}
			});
		}
		tablaVisitas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ModeloTabla modelo = new ModeloTabla(
			new Object[][] {
			},
			new String[] {
				"Fecha", "Observación", ""
			}
		);
		
		tablaVisitas.setModel(modelo);
		
		tablaVisitas.getColumnModel().getColumn(0).setPreferredWidth(150);;
		tablaVisitas.getColumnModel().getColumn(1).setPreferredWidth(350);;
		
		// Columna oculta con los id de las Visitas
		tablaVisitas.getColumnModel().getColumn(2).setMaxWidth(0);
		tablaVisitas.getColumnModel().getColumn(2).setMinWidth(0);
		tablaVisitas.getColumnModel().getColumn(2).setResizable(false);
		tablaVisitas.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
		tablaVisitas.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
		tablaVisitas.getTableHeader().getColumnModel().getColumn(2).setResizable(false);
		
		scrollPane.setViewportView(tablaVisitas);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.setBounds(441, 14, 153, 23);
		btnModificar.addActionListener((e)-> modificarVisita());
		contentPane.add(btnModificar);
		
		btnNuevaVisita = new JButton("Nueva Visita");
		btnNuevaVisita.addActionListener((e)->{
			AltaVisita altaVisita = new AltaVisita(this, orden);
			altaVisita.setVisible(true);
			});
		btnNuevaVisita.setBounds(441, 139, 153, 23);
		contentPane.add(btnNuevaVisita);
		
		if(orden.estaFinalizada()) {
			btnNuevaVisita.setEnabled(false);
			btnModificar.setEnabled(false);
		}
		
		btnBorrarVisita = new JButton("Borrar Visita");
		btnBorrarVisita.setEnabled(false);
		btnBorrarVisita.setBounds(441, 76, 153, 23);
		btnBorrarVisita.addActionListener((e)-> borrarVisita());
		contentPane.add(btnBorrarVisita);
		
		refrescar();
	}

	@Override
	public void refrescar() {
		try {
			
			List<Visita> visitas = this.api.listarVisitas();
			
			List<Visita> resultado = visitas.stream().filter((v)-> v.getOrden().getId() == this.orden.getId() && v.activo()).collect(Collectors.toList());
			
			DefaultTableModel modelo = (DefaultTableModel) tablaVisitas.getModel();
			
			while(tablaVisitas.getRowCount() > 0) {
				modelo.removeRow(0);
			}
			
			for(Visita v : resultado) {
				
				modelo.addRow(new Object[] {Helper.convertirFechaHora(v.getFechaHora()), v.getObservacion(), v.getId()});
				
			}
			
			padre.refrescar();
			
			setTitle("Visitas - Orden " + orden.getEstado().toLowerCase());
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void modificarVisita() {
		
		try {
			
			Visita visita = this.api.buscarVisita((Long) tablaVisitas.getValueAt(tablaVisitas.getSelectedRow(), tablaVisitas.getColumnCount() -1));
			
			AltaVisita modificarVisita = new AltaVisita(this, visita);
			modificarVisita.setVisible(true);
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void borrarVisita() {
		
		ImageIcon iconoAlerta = new ImageIcon(GestionVisitas.class.getResource("/ar/edu/unrn/seminario/imagenes/alerta.png"));
		iconoAlerta = new ImageIcon(iconoAlerta.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		
		int resultado = JOptionPane.showConfirmDialog(this, "¿Seguro que desea borrar la visita?", "Borrar Visita", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, iconoAlerta);
		
		if(resultado == 0) {

			try {
				Visita visita = api.buscarVisita((Long) tablaVisitas.getValueAt(tablaVisitas.getSelectedRow(), tablaVisitas.getColumnCount() -1));
				
				this.api.borrarVisita(visita);
				
				this.refrescar();
			}
			catch(AppException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
		
	}
}
