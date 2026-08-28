package modelo;
import java.util.ArrayList;

public class Socio {

	//Atributos
	private String rut, nombre;
	private int edad, deuda;
	private boolean esMoroso, activo;
	private ArrayList<Reserva> listaReservas;
	
	
	//Constructor
	
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
	
	public ArrayList<Reserva> getArrayReservas(){
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
	
	public void setArrayReservas(ArrayList<Reserva> arrayReservas) {
		this.listaReservas = arrayReservas;
	}
	
	//Metodos
	
	/* Sobrecarga 1: Realiza un abono parcial o total segun el monto indicado.
	 * Si el monto cubre o supera la deuda, queda en 0 y se anula la morosidad.
	 * Si es inferior, se descuenta el monto manteniendo el estado de morosidad.
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
	
	/* Sobrecarga 2: Salda la totalidad de la deuda pendiente.
	 * Fija la deuda en 0 y remueve automaticamente la morosidad del socio.
	 */
	public void abonarDeuda() {
		this.deuda = 0;
		this.esMoroso = false;
	}
	
	/* Agrega una reserva a la coleccion interna del socio si NO se encuentra repetida.
	 * Recibe como parametro un objeto Reserva a registrar.
	 * Retorna true si la reserva se agrego con exito.
	 * Retorna false si es nula o ya existe su id.
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
	
	/* Busca una reserva especifica del socio mediante su identificador.
	 * Recibe como parametro el identificador numerico de la reserva.
	 * Retorna un objeto Reserva encontrado (si existe) o null (si no existe).
	 */
	public Reserva buscarReserva(int idReserva) {
		for (Reserva r : this.listaReservas) {
			if ( (r != null) && (r.getIdReserva() == idReserva) ) {
				return r;
			}
		}
		return null;
	}
	
	/* Elimina una reserva de la coleccion del socio por su identificador.
	 * Recibe como parametro el identificador numerico de la reserva a remover.
	 * Retorna true (si se elimino) o false (si no existe).
	 */
	public boolean eliminarReserva(int idReserva) {
		Reserva reserva = buscarReserva(idReserva);
		if (reserva != null) {
			return this.listaReservas.remove(reserva); //true
		}
		return false;
	}
}
