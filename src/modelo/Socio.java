package modelo;
import java.util.ArrayList;

/**
 * Representa a un socio registrado en el club deportivo.
 * Mantiene su información personal, estado financiero y un historial de sus reservas.
 * 
 * @author Avalos Cristian
 * @version 1.0
 */
public class Socio {

	//Atributos
	private String rut, nombre;
	private int edad, deuda;
	private boolean esMoroso, activo;
	private ArrayList<Reserva> listaReservas;
	
	
	/**
	 * Constructor de la clase Socio.
	 * 
	 * @param rut RUT identificador del socio.
	 * @param nombre Nombre completo.
	 * @param edad Edad del socio.
	 * @param deuda Monto inicial adeudado.
	 * @param esMoroso Estado de morosidad (true si tiene deudas vencidas).
	 */
	public Socio(String rut, String nombre, int edad, int deuda, boolean esMoroso) {
		this.rut = rut;
		this.nombre = nombre;
		this.edad = edad;
		this.deuda = deuda;
		this.esMoroso = esMoroso;
		this.activo = true;
		this.listaReservas = new ArrayList<>();
	}
	
	//Getters
	public String getRut() {
		return rut;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getEdad() {
		return edad;
	}
	
	public int getDeuda() {
		return deuda;
	}
	
	public boolean getEsMoroso() {
		return esMoroso;
	}
	
	public boolean getActivo() {
		return activo;
	}
	
	public ArrayList<Reserva> getListaReservas(){
		return this.listaReservas;
	}
	
	//Setters
	public void setRut(String rut) {
		this.rut = rut;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public void setDeuda(int deuda) {
		this.deuda = deuda;
	}
	
	public void setEsMoroso(boolean esMoroso) {
		this.esMoroso = esMoroso;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public void setListaReservas(ArrayList<Reserva> listaReservas) {
		this.listaReservas = listaReservas;
	}
	
	//Metodos
	
	/** 
	 * Realiza un abono parcial o total según el monto indicado.
	 * Si el monto cubre o supera la deuda, queda en 0 y se anula la morosidad.
	 * Si es inferior, se descuenta el monto manteniendo el estado de morosidad.
	 * 
	 * @param monto Cantidad de dinero a abonar.
	 */
	public void abonarDeuda(int monto) {
		if (monto <= 0){
			return;
		}
		if (monto >= this.deuda) {
			this.deuda = 0;
			this.esMoroso = false;
		} else {
			this.deuda -= monto;
			this.esMoroso = (this.deuda > 0);
		}
	}
	
	/** 
	 * Salda la totalidad de la deuda pendiente.
	 * Fija la deuda en 0 y remueve automáticamente la morosidad del socio.
	 */
	public void abonarDeuda() {
		this.deuda = 0;
		this.esMoroso = false;
	}
	
	/** 
	 * Agrega una reserva a la colección interna del socio si NO se encuentra repetida.
	 * 
	 * @param reserva Objeto Reserva a registrar.
	 * @return true si la reserva se agregó con éxito, false si es nula o ya existe su ID.
	 */
	public boolean agregarReserva(Reserva reserva) {
		if (reserva == null) {
			return false;
		}
		for (Reserva r : this.listaReservas) {
			if ( (r != null) && (r.getIdReserva() == reserva.getIdReserva()) ) {
				return false;
			}
		}
		return this.listaReservas.add(reserva);
	}
	
	/** 
	 * Busca una reserva específica del socio mediante su identificador.
	 * 
	 * @param idReserva Identificador numérico de la reserva.
	 * @return Objeto Reserva encontrado, o null si no existe.
	 */
	public Reserva buscarReserva(int idReserva) {
		for (Reserva r : this.listaReservas) {
			if ( (r != null) && (r.getIdReserva() == idReserva) ) {
				return r;
			}
		}
		return null;
	}
	
	/** 
	 * Elimina una reserva de la colección del socio por su identificador.
	 * 
	 * @param idReserva Identificador numérico de la reserva a remover.
	 * @return true si se eliminó correctamente, false si no se encontró.
	 */
	public boolean eliminarReserva(int idReserva) {
		Reserva reserva = buscarReserva(idReserva);
		if (reserva != null) {
			return this.listaReservas.remove(reserva); //true
		}
		return false;
	}
}