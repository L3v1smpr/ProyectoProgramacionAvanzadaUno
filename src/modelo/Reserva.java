package modelo;
import java.util.Date;

/**
 * Representa una reserva realizada por un socio para una actividad específica.
 * 
 * @author Avalos Cristian
 * @version 1.0
 */
public class Reserva {
	private int idReserva;
	private Date fecha;
	private EstadoReserva estado;
	private String rutSocio, idActividad;
	
	/**
	 * Constructor de la clase Reserva.
	 * 
	 * @param idReserva Identificador numérico único de la reserva.
	 * @param fecha Fecha en la que se generó la reserva.
	 * @param estado Estado actual (Pendiente, Completada, Cancelada).
	 * @param rutSocio RUT del socio que efectúa la reserva.
	 * @param idActividad Identificador de la actividad reservada.
	 */
	public Reserva(int idReserva, Date fecha, EstadoReserva estado, String rutSocio, String idActividad) {
		this.idReserva = idReserva;
		this.fecha = fecha;
		this.estado = estado;
		this.rutSocio = rutSocio;
		this.idActividad = idActividad;
	}
	
	
	//Getters
	public int getIdReserva() {
		return idReserva;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public EstadoReserva getEstado() {
		return estado;
	}
	
	public String getRutSocio() {
		return rutSocio;
	}
	
	public String getIdActividadEnReserva() {
		return idActividad;
	}
	
	//Setters
	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}
	public void setRutSocio(String rutSocio) {
		this.rutSocio = rutSocio;
	}
	
	public void setIdActividadEnReserva(String idActividad) {
		this.idActividad = idActividad;
	}
	
}