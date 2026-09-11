package modelo;
import java.util.Date;

/**
 * Clase abstracta que representa una actividad base dentro del sistema.
 * Define los atributos comunes y el comportamiento base para los diferentes tipos de actividades.
 * 
 * @author Reyes Alex
 * @version 1.0
 */
public abstract class Actividad {

	private String idActividad, nombre;
	private int cupoMaximo;
	private int edadMinima; 
	private boolean activo; 
	
	/**
	 * Constructor principal de Actividad.
	 * Inicializa una nueva actividad y por defecto la marca como activa.
	 * 
	 * @param idActividad Identificador único de la actividad.
	 * @param nombre Nombre descriptivo de la actividad.
	 * @param cupoMaximo Cantidad máxima de personas que pueden participar.
	 * @param edadMinima Edad mínima requerida para la inscripción.
	 */
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
	
	// Getters de atributos específicos (Polimorfismo)
	public String getProfesor() {
	    return null;
	}

	public Boolean getRequiereAsistencia() {
	    return null;
	}

	public Date getFecha() {
	    return null;
	}

	public String getLugar() {
	    return null;
	}

	public String getTipoEvento() {
	    return null;
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
	/**
	 * Genera una cadena de texto con la información detallada de la actividad.
	 * 
	 * @return Cadena formateada con el ID, nombre, cupos, edad y estado de la actividad.
	 */
	public String mostrarDetalles() {
		return "ID: " + idActividad + " | Nombre: " + nombre + " | Cupos: " + cupoMaximo + " | Edad Mínima: " + edadMinima + " años | Activa: " + (activo ? "Sí" : "No");
	}
	
	/**
	 * Muestra los detalles de la actividad permitiendo elegir entre un formato resumido o completo.
	 * 
	 * @param formatoCorto true para mostrar solo ID y nombre, false para mostrar todos los detalles.
	 * @return Cadena de texto con la información solicitada.
	 */
	public String mostrarDetalles(boolean formatoCorto) {
		if (formatoCorto) {
			return idActividad + " - " + nombre;
		}
		return mostrarDetalles();
	}
	
	//Método Abstracto
	/**
	 * Obtiene el tipo específico de la actividad.
	 * Método abstracto que debe ser implementado por las subclases.
	 * 
	 * @return Tipo de actividad en formato de texto.
	 */
	public abstract String getTipoActividad();
}