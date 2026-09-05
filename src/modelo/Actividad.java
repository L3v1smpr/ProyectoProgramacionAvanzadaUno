package modelo;

public abstract class Actividad {

	private String idActividad, nombre;
	private int cupoMaximo;
	private int edadMinima; 
	private boolean activo; 
	
	
	public Actividad(String idActividad, String nombre, int cupoMaximo, int edadMinima) {
		this.idActividad = idActividad;
		this.nombre = nombre;
		this.cupoMaximo = cupoMaximo;
		this.edadMinima = edadMinima;
		this.activo = true; 
	}
	
	// Getters
	public String getIdActividad() {
		return idActividad;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getCupoMaximo() {
		return cupoMaximo;
	}
	
	public int getEdadMinima() {
		return edadMinima;
	}

	public boolean getActivo() {
		return activo;
	}
	
	// Setters
	public void setIdActividad(String id) {
		this.idActividad = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setCupoMaximo(int cupoMaximo) {
		this.cupoMaximo = cupoMaximo;
	}
	
	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public boolean esEvento() {
	    return false;
	}
	
	// Métodos
	public String mostrarDetalles() {
		return "ID: " + idActividad + " | Nombre: " + nombre + " | Cupos: " + cupoMaximo + " | Edad Mínima: " + edadMinima + " años | Activa: " + (activo ? "Sí" : "No");
	}
	
	public String mostrarDetalles(boolean formatoCorto) {
		if (formatoCorto) {
			return idActividad + " - " + nombre;
		}
		return mostrarDetalles();
	}
}