package ar.edu.unrn.seminario.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ar.edu.unrn.seminario.modelo.PedidoDeRetiro;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class VerPedido extends JFrame {

	private JPanel contentPane;
	private JTextField textoCampa�a;
	private JTextField textoEmisor;
	private JTextField textoDireccion;
	private JTextField textoDescripcion;
	
	public VerPedido(PedidoDeRetiro pedido) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 256);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener((e)->this.dispose());
		
		JLabel lblCampa�a = new JLabel("Campa�a ");
		lblCampa�a.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoCampa�a = new JTextField();
		textoCampa�a.setEditable(false);
		textoCampa�a.setText(pedido.getCampa�a().getNombre());
		textoCampa�a.setColumns(10);
		
		JLabel lblEmisor = new JLabel("Emisor");
		lblEmisor.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoEmisor = new JTextField();
		textoEmisor.setEditable(false);
		textoEmisor.setText(pedido.getCiudadano().getPersona().toString());
		textoEmisor.setColumns(10);
		
		JLabel lblDireccion = new JLabel("Direcci�n");
		lblDireccion.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoDireccion = new JTextField();
		textoDireccion.setEditable(false);
		textoDireccion.setText(pedido.getCiudadano().getDireccion().toString());
		textoDireccion.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripci�n");
		lblDescripcion.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoDescripcion = new JTextField();
		textoDescripcion.setEditable(false);
		textoDescripcion.setText(pedido.getDescripcion());
		textoDescripcion.setColumns(10);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDescripcion)
						.addComponent(lblDireccion)
						.addComponent(lblEmisor, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCampa�a))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(textoEmisor, 552, 552, 552)
						.addComponent(textoDireccion, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoDescripcion, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoCampa�a, 552, 552, 552))
					.addContainerGap(68, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(651, Short.MAX_VALUE)
					.addComponent(btnCerrar)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCampa�a, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoCampa�a, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmisor, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoEmisor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDireccion, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoDireccion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescripcion, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoDescripcion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnCerrar)
					.addContainerGap(49, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		
	}
}
