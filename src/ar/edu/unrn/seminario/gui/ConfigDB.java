package ar.edu.unrn.seminario.gui;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ar.edu.unrn.seminario.excepciones.AppException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class ConfigDB extends JFrame {
	
	private JPanel contentPane;
	private JComboBox comboBoxIdiomas;
	private JLabel lblUsuario;
	private Properties config = null;
	private JTextField textoUsuario;
	private JTextField textoPuerto;
	private JTextField textoContraseña;

	public ConfigDB() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(500, 230, 450, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("ZCampaign - Configuración de BD");
		
		try {
			File archivoPropiedadesDefecto = new File(System.getProperty("user.dir") + "/config/db.properties");
			FileInputStream entrada = new FileInputStream(archivoPropiedadesDefecto);
			config = new Properties();
			
			config.load(entrada);
			
			entrada.close();
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		lblUsuario = new JLabel("Usuario: ");
		lblUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblUsuario.setBounds(10, 11, 70, 21);
		contentPane.add(lblUsuario);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(335, 200, 89, 23);
		btnAceptar.addActionListener((e)-> guardarConfig());
		contentPane.add(btnAceptar);		
		
		textoUsuario = new JTextField();
		textoUsuario.setBounds(10, 43, 414, 20);
		textoUsuario.setText((String) config.get("user"));
		contentPane.add(textoUsuario);
		textoUsuario.setColumns(10);
		
		JLabel lblPuerto = new JLabel("Puerto:");
		lblPuerto.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblPuerto.setBounds(10, 137, 70, 21);
		contentPane.add(lblPuerto);
		
		textoPuerto = new JTextField();
		textoPuerto.setColumns(10);
		textoPuerto.setBounds(10, 169, 414, 20);
		textoPuerto.setText((String) config.get("port"));
		contentPane.add(textoPuerto);
		
		JLabel lblContraseña = new JLabel("Contraseña: ");
		lblContraseña.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblContraseña.setBounds(10, 74, 114, 21);
		contentPane.add(lblContraseña);
		
		textoContraseña = new JTextField();
		textoContraseña.setBounds(10, 106, 414, 20);
		textoContraseña.setText((String) config.get("pwd"));
		contentPane.add(textoContraseña);
	}
	
	private void guardarConfig() {
		
		try {
			
			File archivoPropiedadesSalida = new File(System.getProperty("user.dir") + "/config/db.properties");
			FileOutputStream salida = new FileOutputStream(archivoPropiedadesSalida);
			
			if(config != null) {
				config.setProperty("pwd", textoContraseña.getText().toString().trim());
				config.setProperty("user", textoUsuario.getText().trim());
				config.setProperty("port", textoPuerto.getText().trim());
				
				config.store(salida, "");
			}
			
			salida.close();
			
			this.dispose();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
}
