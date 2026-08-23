package modelo;

public class ClaseGrupal extends Actividad {

	//Atributos
	private String profesor;
	
	
	//Métodos
	public String getProfesor() {
		return profesor;
	}
	
	public void setProfesor(String profesor) {
		this.profesor = profesor;
	}
	
	@Override
	public String mostrarDetalles() {
		return "Nombre Profesor" + profesor;
	}
	
}
