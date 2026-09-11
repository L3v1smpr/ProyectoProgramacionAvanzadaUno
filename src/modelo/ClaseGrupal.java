package modelo;

/**
 * Representa una actividad de tipo clase grupal, la cual es impartida por un profesor.
 * 
 * @author Reyes Alex
 * @version 1.0
 */
public class ClaseGrupal extends Actividad {

    // Atributos
    private String profesor;

    /**
     * Constructor de ClaseGrupal.
     * 
     * @param idActividad Identificador único de la actividad.
     * @param nombre Nombre descriptivo.
     * @param cupoMaximo Límite de participantes.
     * @param edadMinima Edad mínima requerida.
     * @param profesor Nombre del profesor a cargo de impartir la clase.
     */
 	public ClaseGrupal(String idActividad, String nombre, int cupoMaximo, int edadMinima, String profesor) {
 		super(idActividad, nombre, cupoMaximo, edadMinima); // Se añade edadMinima
 		this.profesor = profesor;
 	}
    // Métodos
 	@Override
 	public String getProfesor() {
        return profesor;
    }

    
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    //Sobreescritura
    
    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | Profesor: " + profesor;
    }
    
    @Override
    public String getTipoActividad() {
        return "CLASE_GRUPAL";
    }
}