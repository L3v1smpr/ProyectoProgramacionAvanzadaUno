package modelo;
import java.util.Date;

public class Evento extends Actividad {
	
	private Date fecha;
	private String lugar;
	private String tipoEvento;
	
	public Evento(String idActividad, String nombre, int cupoMaximo, int edadMinima, Date fecha, String lugar, String tipoEvento){
		super(idActividad, nombre, cupoMaximo, edadMinima);

        this.fecha = fecha;
        this.lugar = lugar;
        this.tipoEvento = tipoEvento;
	}
	
	//Getters
	
	public Date getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }
    
    @Override
    public boolean esEvento() {
        return true;
    }


	//Setters

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
    
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    
    
    //Sobreescritura
    @Override
    public String mostrarDetalles() {
        return super.mostrarDetalles()
            + " | Tipo: " + tipoEvento
            + " | Fecha: " + fecha
            + " | Lugar: " + lugar;
    }
}
