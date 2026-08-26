package modelo;
import java.util.ArrayList;

public class Socio {

	//Atributos
	private String rut, nombre;
	private int edad, deuda;
	private boolean esMoroso;
	private ArrayList<Reserva> arrayReservas;
	
	
	//Constructor
	
	public Socio(String rut, String nombre, int edad, int deuda, boolean esMoroso) {
		this.rut = rut;
		this.nombre = nombre;
		this.edad = edad;
		this.deuda = deuda;
		this.esMoroso = esMoroso;
		this.arrayReservas = new ArrayList<>();
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
	
	public boolean getIsMoroso() {
		return esMoroso;
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
	
	
	/*
	 * Sobrecarga 1: Realiza un abono parcial o total segun el monto indicado.
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
	
	/*
	 * Sobrecarga 2: Salda la totalidad de la deuda pendiente.
	 * Fija la deuda en 0 y remueve automaticamente la morosidad del socio.
	 */
	public void abonarDeuda() {
		this.deuda = 0;
		this.esMoroso = false;
	}
		
}
