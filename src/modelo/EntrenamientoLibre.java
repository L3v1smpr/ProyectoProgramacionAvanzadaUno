package modelo;

/**
 * Representa una actividad de entrenamiento libre, en la cual se define 
 * si es necesario o no registrar la asistencia.
 * 
 * @author Reyes Alex
 * @version 1.0
 */
public class EntrenamientoLibre extends Actividad {

    private boolean requiereAsistencia;

    /**
     * Constructor de EntrenamientoLibre.
     * 
     * @param idActividad Identificador único.
     * @param nombre Nombre descriptivo.
     * @param cupoMaximo Capacidad máxima permitida.
     * @param edadMinima Edad mínima requerida.
     * @param requiereAsistencia Indica si la actividad lleva control de asistencia (true) o no (false).
     */
 	public EntrenamientoLibre(String idActividad, String nombre, int cupoMaximo, int edadMinima, boolean requiereAsistencia) {
 		super(idActividad, nombre, cupoMaximo, edadMinima); // Se añade edadMinima
 		this.requiereAsistencia = requiereAsistencia;
 	}
    // Getter
 	@Override
 	public Boolean getRequiereAsistencia() {
 	    return requiereAsistencia;
 	}

    // Setter
    public void setRequiereAsistencia(boolean requiere) {
        this.requiereAsistencia = requiere;
    }
    
    //Sobreescritura

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | Requiere asistencia: " + (requiereAsistencia ? "Sí" : "No");
    }
    
    @Override
    public String getTipoActividad() {
        return "ENTRENAMIENTO_LIBRE";
    }
}