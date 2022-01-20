package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class Setup extends JFrame {

	private JPanel contentPane;
	private JComboBox comboBoxIdiomas;
	private JLabel lblIdioma;

	public Setup() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(500, 230, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("ZCampaign - Setup");
		
		lblIdioma = new JLabel("Seleccione el idioma a utilizar:");
		lblIdioma.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblIdioma.setBounds(10, 11, 221, 21);
		contentPane.add(lblIdioma);
		
		Locale [] idiomas = {new Locale("es", "AR"), new Locale("en", "US")};
		
		comboBoxIdiomas = new JComboBox(idiomas);
		comboBoxIdiomas.setBounds(10, 43, 264, 20);
		comboBoxIdiomas.setSelectedIndex(0);
		contentPane.add(comboBoxIdiomas);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(185, 74, 89, 23);
		btnAceptar.addActionListener((e)->cargarIdioma());
		contentPane.add(btnAceptar);		
	}
	
	private void cargarIdioma() {
		
		Locale seleccion = null;
		
		seleccion = (Locale) comboBoxIdiomas.getSelectedItem();
		
		ResourceBundle textos = ResourceBundle.getBundle("TextBundle", seleccion);
		
		this.dispose();
		
		try {
			VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(textos);
			ventanaPrincipal.setVisible(true);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
}
