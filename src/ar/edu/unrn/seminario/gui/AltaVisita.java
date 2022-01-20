package ar.edu.unrn.seminario.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import com.toedter.calendar.JCalendar;

import ar.edu.unrn.seminario.api.ApiFacade;
import ar.edu.unrn.seminario.api.IFacade;
import ar.edu.unrn.seminario.excepciones.AppException;
import ar.edu.unrn.seminario.excepciones.DataEmptyException;
import ar.edu.unrn.seminario.excepciones.DateOutOfBoundariesException;
import ar.edu.unrn.seminario.excepciones.InvalidStringLengthException;
import ar.edu.unrn.seminario.modelo.OrdenDeRetiro;
import ar.edu.unrn.seminario.modelo.Visita;
import ar.edu.unrn.seminario.servicio.Helper;

import javax.swing.JSpinner;

public class AltaVisita extends JFrame {

	private JPanel contentPane;
	private JTextField textoObservacion;
	private IVentanaGestion padre;
	private IFacade api;
	private JCalendar calendario;
	private OrdenDeRetiro orden;
	private JButton btnAceptar;
	private JSpinner spinnerHora;
	JSpinner spinnerMinutos;

	/**
	 * 
	 * @wbp.parser.constructor
	 */
	public AltaVisita(IVentanaGestion nuevoPadre, OrdenDeRetiro orden) {
		setTitle("Alta de Visitas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(150, 100, 450, 362);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.padre = nuevoPadre;
		
		this.orden = orden;
		
		try {
			api = ApiFacade.getInstance();
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
		JLabel lblObservacion = new JLabel("Observación *");
		lblObservacion.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblObservacion.setBounds(10, 11, 98, 21);
		contentPane.add(lblObservacion);
		
		textoObservacion = new JTextField();
		textoObservacion.setBounds(10, 43, 315, 20);
		contentPane.add(textoObservacion);
		textoObservacion.setColumns(10);
		
		JLabel lblFecha = new JLabel("Fecha *");
		lblFecha.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblFecha.setBounds(10, 89, 94, 21);
		contentPane.add(lblFecha);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(335, 289, 89, 23);
		btnAceptar.addActionListener((e)->crearVisita());
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(236, 289, 89, 23);
		btnCancelar.addActionListener((e)->this.dispose());
		contentPane.add(btnCancelar);
		
		calendario = new JCalendar();
		calendario.setBounds(10, 121, 184, 153);
		calendario.setMinSelectableDate(orden.getFechaHora());
		contentPane.add(calendario);
		
		JLabel lblCamposObligatorios = new JLabel("Campos Obligatorios *");
		lblCamposObligatorios.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblCamposObligatorios.setBounds(261, 11, 163, 21);
		contentPane.add(lblCamposObligatorios);
		
		spinnerHora = new JSpinner();
		spinnerHora.setBounds(236, 121, 50, 20);
		SpinnerNumberModel modeloHora = new SpinnerNumberModel(12, 00, 23, 1);
		spinnerHora.setModel(modeloHora);
		contentPane.add(spinnerHora);
		
		spinnerMinutos = new JSpinner();
		spinnerMinutos.setBounds(314, 121, 50, 20);
		SpinnerNumberModel modeloMinutos = new SpinnerNumberModel(00, 00, 59, 1);
		spinnerMinutos.setModel(modeloMinutos);
		contentPane.add(spinnerMinutos);
		
		JLabel lblHora = new JLabel("Hora *");
		lblHora.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblHora.setBounds(236, 89, 94, 21);
		contentPane.add(lblHora);
		
		JLabel lblPuntos = new JLabel(":");
		lblPuntos.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblPuntos.setBounds(298, 120, 11, 21);
		contentPane.add(lblPuntos);
	}
	
	public AltaVisita(IVentanaGestion padre, Visita visita) {
		
		this(padre, visita.getOrden());
		
		this.setTitle("Actualización de Visita");
		
		textoObservacion.setText(visita.getObservacion());
		
		this.calendario.setDate(visita.getFechaHora());
		
		LocalDateTime fechaVisita = Helper.fechaHoraAFechaLocal(visita.getFechaHora());
		
		spinnerHora.setValue(fechaVisita.getHour());
		spinnerMinutos.setValue(fechaVisita.getMinute());
		
		// Borro los escuchas de eventos del botón Aceptar
		if(btnAceptar.getActionListeners().length > 0) {
			
			for (ActionListener al : btnAceptar.getActionListeners()) {
				btnAceptar.removeActionListener(al);
			}
		}
			
		// Agrego nuevo comportamiento
		btnAceptar.addActionListener((e)-> modificarVisita(visita));
		
	}
	
	private void crearVisita() {
		
		try {
			
			String observacion = textoObservacion.getText().trim();
			
			Date fechaHora = calendario.getDate();
			
			int hora = (int) spinnerHora.getValue();
			
			int minutos = (int) spinnerMinutos.getValue();
			
			LocalDateTime fechaCalendario, resultado;
			
			fechaCalendario = Helper.fechaHoraAFechaLocal(fechaHora);
			
			resultado = LocalDateTime.of(fechaCalendario.getYear(), fechaCalendario.getMonth(), fechaCalendario.getDayOfMonth(), hora, minutos);
			
			Long idOrden = orden.getId();
			
			fechaHora = Helper.fechaLocalAFecha(resultado);
			
			this.dispose();
			
			api.agregarVisita(observacion, fechaHora, idOrden);
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	
	private void modificarVisita(Visita visita) {
		try {
			
			String observacion;
			
			Date fechaHora;
			
			observacion = textoObservacion.getText().trim();
			
			fechaHora = calendario.getDate();
			
			int hora = (int) spinnerHora.getValue();
			
			int minutos = (int) spinnerMinutos.getValue();
			
			LocalDateTime fechaCalendario, resultado;
			
			fechaCalendario = Helper.fechaHoraAFechaLocal(fechaHora);
			
			resultado = LocalDateTime.of(fechaCalendario.getYear(), fechaCalendario.getMonth(), fechaCalendario.getDayOfMonth(), hora, minutos);
			
			fechaHora = Helper.fechaLocalAFecha(resultado);
			
			Visita nuevaVisita = new Visita(observacion, fechaHora, visita.getOrden());
			nuevaVisita.setId(visita.getId());
			
			api.actualizarVisita(nuevaVisita);
			
			this.dispose();
			
			padre.refrescar();
			
		}
		catch(AppException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DataEmptyException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (DateOutOfBoundariesException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		} catch (InvalidStringLengthException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
