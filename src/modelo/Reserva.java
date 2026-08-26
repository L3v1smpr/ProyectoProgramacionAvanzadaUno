package modelo;
import java.util.Date;

public class Reserva {
	private int idReserva;
	private Date fecha;
	private EstadoReserva estado;
	private String rutSocio, idActividad;
	
	//Constructor
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
