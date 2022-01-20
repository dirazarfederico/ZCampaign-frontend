package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Acerca extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public Acerca() {
		setTitle("Acerca de");
		setBounds(100, 100, 490, 207);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Consultora GUI: Zaida Abadía");
			lblNewLabel.setBounds(138, 63, 236, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel mensaje = new JLabel("ZCampaign \u00A9 2019 - 2021");
			mensaje.setBounds(152, 11, 178, 14);
			contentPanel.add(mensaje);
		}
		
		JLabel lblDiseoEImplementacin = new JLabel("Diseño e implementación: Federico Dirazar");
		lblDiseoEImplementacin.setBounds(109, 119, 265, 14);
		contentPanel.add(lblDiseoEImplementacin);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener((e)-> this.dispose());
		btnAceptar.setBounds(375, 134, 89, 23);
		contentPanel.add(btnAceptar);
	}
}
