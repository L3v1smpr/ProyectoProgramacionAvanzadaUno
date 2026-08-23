package modelo;

public class Actividad {

	private String idActividad, nombre;
	private int cupoMaximo;
	
	//Getters
	public String getIdActividad() {
		return idActividad;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getCupoMaximo() {
		return cupoMaximo;
	}

	
	//Setters
	public void setIdActividad(String id) {
		this.idActividad = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}
	
	public String mostrarDetalles() {
		return "ID de actividad" + idActividad +
				" Nombre" + nombre +
				" Cupo Maximo" + cupoMaximo;
	}
	
	 public String mostrarDetalles(boolean formatoCorto) {
	        if (formatoCorto) {
	            return "Actividad: " + nombre + " (ID: " + idActividad + ")";
	        } else {
	            return mostrarDetalles();
	        }
	    }
	}
