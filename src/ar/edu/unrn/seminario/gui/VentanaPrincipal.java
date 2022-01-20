package ar.edu.unrn.seminario.gui;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JMenuItem mnuItemSalir;

	public VentanaPrincipal(ResourceBundle textos) throws Exception {
		setTitle("ZCampaign - Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 759, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 753, 40);
		contentPane.add(menuBar);
		
		JMenu mnuGestion = new JMenu(textos.getString("management"));
		ImageIcon iconoGestion = new ImageIcon(VentanaPrincipal.class.getResource("/ar/edu/unrn/seminario/imagenes/iconoGestion.png"));
		iconoGestion = new ImageIcon(iconoGestion.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		mnuGestion.setIcon(iconoGestion);
		menuBar.add(mnuGestion);
		
		JMenuItem mnuItemCampañas = new JMenuItem(textos.getString("campaigns"));
		mnuItemCampañas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionCampaña gestionCampaña = new GestionCampaña();
				gestionCampaña.setVisible(true);
			}
		});
		
		mnuGestion.add(mnuItemCampañas);
		
		JMenuItem mnuItemInstituciones = new JMenuItem(textos.getString("institutions"));
		mnuItemInstituciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionInstituciones gestionInstituciones = new GestionInstituciones();
				gestionInstituciones.setVisible(true);
			}
		});
		
		mnuGestion.add(mnuItemInstituciones);
		
		JMenuItem mnuItemCiudadanos = new JMenuItem(textos.getString("citizens"));
		mnuItemCiudadanos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionCiudadanos gestionCiudadanos = new GestionCiudadanos();
				gestionCiudadanos.setVisible(true);
			}
		});
		mnuGestion.add(mnuItemCiudadanos);
		
		JMenu mnuConsulta = new JMenu(textos.getString("query"));
		ImageIcon iconoConsulta = new ImageIcon(VentanaPrincipal.class.getResource("/ar/edu/unrn/seminario/imagenes/iconoConsulta.png"));
		iconoConsulta = new ImageIcon(iconoConsulta.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		mnuConsulta.setIcon(iconoConsulta);
		menuBar.add(mnuConsulta);
		
		JMenuItem mnuItemPersonas = new JMenuItem(textos.getString("people"));
		mnuItemPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultaPersonas consultaPersonas = new ConsultaPersonas();
				consultaPersonas.setVisible(true);
			}
		});
		mnuConsulta.add(mnuItemPersonas);
		
		JMenuItem mnuItemConsultaCampañas = new JMenuItem(textos.getString("campaigns"));
		mnuItemConsultaCampañas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultaCampañas consultaCampañas = new ConsultaCampañas();
				consultaCampañas.setVisible(true);
			}
		});
		mnuConsulta.add(mnuItemConsultaCampañas);
		
		
		JMenu mnuConfig = new JMenu(textos.getString("settings"));
		ImageIcon iconoConfig = new ImageIcon(VentanaPrincipal.class.getResource("/ar/edu/unrn/seminario/imagenes/iconoConfig.png"));
		iconoConfig = new ImageIcon(iconoConfig.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		mnuConfig.setIcon(iconoConfig);
		menuBar.add(mnuConfig);
		
		JMenuItem mnuItemBD = new JMenuItem(textos.getString("db"));
		mnuItemBD.addActionListener((e)-> {ConfigDB config = new ConfigDB(); config.setVisible(true);});
		mnuConfig.add(mnuItemBD);
		
		mnuItemSalir = new JMenuItem(textos.getString("exit"));
		mnuItemSalir.addActionListener((e)-> System.exit(0));
		mnuConfig.add(mnuItemSalir);
		
		JMenu mnuAyuda = new JMenu(textos.getString("help"));
		ImageIcon iconoAyuda = new ImageIcon(VentanaPrincipal.class.getResource("/ar/edu/unrn/seminario/imagenes/iconoAyuda.png"));
		iconoAyuda = new ImageIcon(iconoAyuda.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
		mnuAyuda.setIcon(iconoAyuda);
		menuBar.add(mnuAyuda);
		
		JMenuItem mnuItemAcerca = new JMenuItem(textos.getString("about"));
		mnuItemAcerca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Acerca acerca = new Acerca();
				acerca.setVisible(true);
			}
		});
		mnuAyuda.add(mnuItemAcerca);
		
		init();
	}
	
	private void init( ) {
		
		try {
			
			Properties defecto = new Properties();
			File archivoPropiedadesDefecto = new File("db.properties"),
					directorioSalida = new File(System.getProperty("user.dir") + "/config/"),
					archivoPropiedadesSalida = new File(System.getProperty("user.dir") + "/config/db.properties");
			
			FileInputStream entradaPropiedades = new FileInputStream(archivoPropiedadesDefecto);
			FileOutputStream salidaPropiedades = null;
			
			// Leo propiedades por defecto
			defecto.load(entradaPropiedades);
			
			// El directorio de salida no existe
			if(!directorioSalida.canWrite()) {
				directorioSalida.mkdir();
			}
			
			// El archivo de propiedades no existe
			if(!archivoPropiedadesSalida.canWrite()) {
				archivoPropiedadesSalida.createNewFile();
				
				 salidaPropiedades = new FileOutputStream(archivoPropiedadesSalida);
				
				defecto.store(salidaPropiedades, "");
			}
			
			entradaPropiedades.close();
			if(salidaPropiedades!=null)
				salidaPropiedades.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al inicializar el sistema");
		}
		
	}
}
