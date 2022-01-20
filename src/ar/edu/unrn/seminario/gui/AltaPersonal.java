package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.Institucion;
import ar.edu.unrn.seminario.modelo.Persona;
import ar.edu.unrn.seminario.modelo.Personal;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class AltaPersonal extends JFrame {

	private JPanel contentPane;
	private JTextField textoNombre;
	private IVentanaGestion padre;
	private IFacade api;
	private Institucion institucion;
	private JTextField textoApellido;
	private JTextField textoDNI;
	private JTextField textoMail;
	private JTabbedPane pestañas;
	private JTextField textoSeleccionPersona;
	private SeleccionPersona seleccionPersona;
	private JButton btnAceptar;
	private final JFileChooser elegirArchivo = new JFileChooser();
	private JLabel imagenPersonal;
	private JTextField textoRutaFoto;

	/**
	 * @wbp.parser.constructor
	 * @param nuevoPadre
	 * @param institucion
	 */
	public AltaPersonal(IVentanaGestion nuevoPadre, Institucion institucion) {
		this.padre = nuevoPadre;
		this.institucion = institucion;
		setResizable(false);
		setTitle("Alta de Personal");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 50, 573, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		pestañas = new JTabbedPane(JTabbedPane.TOP);
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JLabel lblEmail = new JLabel("E-Mail *");
		lblEmail.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoMail = new JTextField();
		textoMail.setColumns(10);
		
		JLabel lblFoto = new JLabel("Foto :");
		lblFoto.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JButton btnSubir = new JButton("Subir...");
		btnSubir.addActionListener((e)-> subirFoto());
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener((e)->this.dispose());
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener((e)->agregarPersonal());
		
		imagenPersonal = new JLabel("");
		
		textoRutaFoto = new JTextField();
		textoRutaFoto.setEditable(false);
		textoRutaFoto.setColumns(10);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(pestañas, GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblCamposObligatorios)
							.addGap(76))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(470, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(textoMail, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(346, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblFoto, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addGap(66)
							.addComponent(btnSubir, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(315, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textoRutaFoto, Alignment.LEADING)
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addComponent(imagenPersonal, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
									.addGap(254)
									.addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnAceptar, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)))
							.addGap(19))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCamposObligatorios, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pestañas, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textoMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFoto, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSubir))
					.addGap(18)
					.addComponent(textoRutaFoto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(imagenPersonal, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
							.addGap(32))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAceptar)
								.addComponent(btnCancelar))
							.addContainerGap())))
		);
		
		JPanel cargarPersona = new JPanel();
		pestañas.addTab("Cargar Persona", null, cargarPersona, null);
		
		JLabel label = new JLabel("Nombre *");
		label.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoNombre = new JTextField();
		textoNombre.setColumns(10);
		
		JLabel lblApellido = new JLabel("Apellido *");
		lblApellido.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoApellido = new JTextField();
		textoApellido.setColumns(10);
		
		JLabel lblDni = new JLabel("DNI *");
		lblDni.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		textoDNI = new JTextField();
		textoDNI.setColumns(10);
		GroupLayout gl_cargarPersona = new GroupLayout(cargarPersona);
		gl_cargarPersona.setHorizontalGroup(
			gl_cargarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cargarPersona.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_cargarPersona.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblApellido, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoApellido, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDni, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
						.addComponent(textoDNI, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(321, Short.MAX_VALUE))
		);
		gl_cargarPersona.setVerticalGroup(
			gl_cargarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cargarPersona.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textoNombre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblApellido)
					.addGap(18)
					.addComponent(textoApellido, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblDni)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textoDNI, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		cargarPersona.setLayout(gl_cargarPersona);
		
		JPanel seleccionarPersona = new JPanel();
		pestañas.addTab("Seleccionar Persona", null, seleccionarPersona, null);
		
		JButton btnSeleccionarPersona = new JButton("Seleccionar...");
		btnSeleccionarPersona.addActionListener((e)->this.seleccionarPersona());
		
		textoSeleccionPersona = new JTextField();
		textoSeleccionPersona.setText("Seleccione una persona...");
		textoSeleccionPersona.setEditable(false);
		textoSeleccionPersona.setColumns(10);
		
		this.seleccionPersona = new SeleccionPersona(new WrapperSeleccion(textoSeleccionPersona), institucion);
		
		GroupLayout gl_seleccionarPersona = new GroupLayout(seleccionarPersona);
		gl_seleccionarPersona.setHorizontalGroup(
			gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seleccionarPersona.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
						.addComponent(textoSeleccionPersona, GroupLayout.PREFERRED_SIZE, 496, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSeleccionarPersona))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		gl_seleccionarPersona.setVerticalGroup(
			gl_seleccionarPersona.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seleccionarPersona.createSequentialGroup()
					.addGap(35)
					.addComponent(btnSeleccionarPersona)
					.addGap(43)
					.addComponent(textoSeleccionPersona, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(114, Short.MAX_VALUE))
		);
		seleccionarPersona.setLayout(gl_seleccionarPersona);
		contentPane.setLayout(gl_contentPane);
	}
	
	public AltaPersonal(IVentanaGestion nuevoPadre, Institucion institucion, Personal personal) {
		
		this(nuevoPadre, institucion);
		
		setTitle("Actualización de personal");
		
		textoApellido.setText(personal.getPersona().getApellido());
		textoDNI.setText(new Integer(personal.getPersona().getDni()).toString());
		textoMail.setText(personal.getEmail());
		textoNombre.setText(personal.getPersona().getNombre());
		
		pestañas.remove(pestañas.getComponentAt(1));
		pestañas.setTitleAt(0, "Editar persona");
		
		File foto = new File(personal.getFoto());
		
		if(foto.canRead()) {
			cargarFoto(foto);
			elegirArchivo.setSelectedFile(foto);
			textoRutaFoto.setText(personal.getFoto());
		}
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
		
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
		
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarPersonal(personal));
		
	}
	
	private void seleccionarPersona() {
		this.seleccionPersona.setVisible(true);
	}
	
	private void agregarPersonal() {
		
		String nombre = "", apellido = "", mail, foto;
		int dni = 0;
		Persona nuevaPersona = null;
		Long idPersona = null;
		
		try {
			switch (this.pestañas.getSelectedIndex()) {
			case 0:
				
				nombre = textoNombre.getText().trim();
				apellido = textoApellido.getText().trim();
				
				dni = Integer.parseInt(textoDNI.getText().trim());
				
				nuevaPersona = api.guardarPersona(nombre, apellido, dni);
				
				idPersona = nuevaPersona.getId();
				
				break;

			case 1:
				Persona persona = this.seleccionPersona.getPersona();
				
				if(persona==null) {
					throw new AppException("Debe seleccionar una persona");
				}
				
				idPersona = persona.getId();
				
				break;
			}
			
			foto = textoRutaFoto.getText().trim();
			
			mail = textoMail.getText().trim();

			this.dispose();
			
			api.guardarPersonal(this.institucion.getId(), idPersona, mail, foto);
			
			padre.refrescar();	
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch(NumberFormatException nFEx) {
			JOptionPane.showMessageDialog(this, "Debe ingresar un DNI válido");
		}		
	}
	
	private void modificarPersonal(Personal personal) {
		
		try {
			
			String nombre = "", apellido = "", mail, foto;
			int dni = 0;
			
			nombre = textoNombre.getText().trim();
			apellido = textoApellido.getText().trim();
			
			dni = Integer.parseInt(textoDNI.getText().trim());
			
			foto = textoRutaFoto.getText().trim();
			
			mail = textoMail.getText().trim();
			
			Long idPersonal = personal.getId();
			Long idPersona = personal.getPersona().getId();
			
			Persona persona = new Persona(nombre, apellido, dni);
			persona.setId(idPersona);
			
			personal = new Personal(foto, mail, persona, institucion);
			personal.setId(idPersonal);
			
			api.actualizarPersonal(personal);
			
			this.dispose();
			
			padre.refrescar();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		catch(NumberFormatException nFEx) {
			JOptionPane.showMessageDialog(this, "Debe ingresar un DNI válido");
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (InvalidStringLengthException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void subirFoto() {
		
		// Inicializa FileChooser
		elegirArchivo.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes JPG, JPEG y PNG", "jpg", "jpeg", "png");
		
		elegirArchivo.setFileFilter(filtro);
		elegirArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
		elegirArchivo.setAcceptAllFileFilterUsed(false);
		
		int resultado = elegirArchivo.showOpenDialog(this);

		//El usuario seleccionó un archivo
		if(resultado == JFileChooser.APPROVE_OPTION) {
			File seleccion = elegirArchivo.getSelectedFile();
			
			if(seleccion.canRead() && seleccion.isFile()) {
				cargarFoto(seleccion);
			}
			else {
				JOptionPane.showMessageDialog(this, "No se pudo cargar la foto especificada");
			}
		}
		
	}
	
	private void cargarFoto(File foto) {
		
		ImageIcon fotoPersonal = null;
		try {
			fotoPersonal = new ImageIcon(ImageIO.read(foto));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No se pudo abrir la foto seleccionada");
		}
		if(fotoPersonal!=null) {
			fotoPersonal = new ImageIcon(fotoPersonal.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH));
			imagenPersonal.setIcon(fotoPersonal);
			textoRutaFoto.setText(foto.getPath());
		}
		
	}
}
