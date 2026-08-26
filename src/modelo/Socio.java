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
	
	
	//Sobrecarga - Si se ingresa un valor inferior a la deuda total, es solo un abono a la deuda, no deja de ser moroso.
	public void abonarDeuda(int monto) {
		
	}
	
	//Si no se ingresa un valor, salda la deuda completa, deja de ser moroso.
	public void abonarDeuda() {
		
	}
		
}
