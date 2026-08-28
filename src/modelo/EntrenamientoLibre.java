package modelo;

public class EntrenamientoLibre extends Actividad {

    private boolean requiereAsistencia;

    // Getter
    public boolean isRequiereAsistencia() {
        return requiereAsistencia;
    }

    // Setter
    public void setRequiereAsistencia(boolean requiere) {
        this.requiereAsistencia = requiere;
    }

    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles() + " | Requiere asistencia: " + (requiereAsistencia ? "Sí" : "No");
    }
}

