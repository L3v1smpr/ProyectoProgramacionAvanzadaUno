package modelo;

public class EntrenamientoLibre extends Actividad {

    private boolean requiereAsistencia;


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

