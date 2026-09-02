package modelo;

public class ClaseGrupal extends Actividad {

    // Atributos
    private String profesor;

 	public ClaseGrupal(String idActividad, String nombre, int cupoMaximo, int edadMinima, String profesor) {
 		super(idActividad, nombre, cupoMaximo, edadMinima); // Se añade edadMinima
 		this.profesor = profesor;
 	}
    // Métodos
    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | Profesor: " + profesor;
    }
}

