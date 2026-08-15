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
		
	}
	
	//Getters
	public String getRut() {
		
	}
	
	public String getNombre() {
		
	}
	
	public int getEdad() {
		
	}
	
	public int getDeuda() {
		
	}
	
	public boolean getIsMoroso() {
		
	}
	
	
	
	
	
	
	//Setters
	public void setRut(String rut) {
		
	}
	
	public void setNombre(String nombre) {
		
	}
	
	public void setEdad(int edad) {
		
	}
	
	public void setDeuda(int deuda) {
		
	}
	
	public void setEsMoroso(boolean estado) {
		
	}
	
	
	//Sobrecarga - Si se ingresa un valor inferior a la deuda total, es solo un abono a la deuda, no deja de ser moroso
	public void abonarDeuda(int monto) {
		
	}
	
	//Si no se ingresa un valor, salda la deuda completa, deja de ser moroso
	public void abonarDeuda() {
		
	}
		
}
