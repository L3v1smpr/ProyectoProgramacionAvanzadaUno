package controlador;
import modelo.Socio;
import modelo.Actividad;
import modelo.Reserva;
import controlador.DBConnection;
import modelo.CupoMaximoException;
import modelo.MorosidadException;
import modelo.EstadoReserva;

import java.util.HashMap;
import java.util.ArrayList;

public class SistemaClub {

	//Atributos
	private HashMap<String, Socio> mapaSocios;
	private ArrayList<Actividad> listaActividades;
	private DBConnection connection;
	
	
	
	//Constructor
	
	public SistemaClub(){
	
		this.mapaSocios = new HashMap<>();
		this.listaActividades = new ArrayList<>();
		this.connection = new DBConnection();
	}
	
	
	//Metodos relacionados a Socio
	public boolean agregarSocio(String rut, String nombre, int edad) {
		if (mapaSocios.containsKey(rut)) {
			return false;
		}
		
		Socio nuevoSocio = new Socio(rut, nombre, edad, 0, false);
		
		mapaSocios.put(rut, nuevoSocio);
		
		return true;
	}
	
	public boolean modificarSocio(String rut, String nombre, int edad, int deuda, boolean esMoroso) {
		if (!mapaSocios.containsKey(rut)) {
			return false;
		}
		
		Socio socioBuscado = mapaSocios.get(rut);
		
		socioBuscado.setNombre(nombre);
		socioBuscado.setEdad(edad);
		socioBuscado.setEsMoroso(esMoroso);
		if (esMoroso) {
			socioBuscado.setDeuda(deuda);
		} else {
			socioBuscado.setDeuda(0);
		}
		
		return true;
	}
	
	public boolean eliminarSocio(String rut) {
		
	}
	
	public ArrayList<Socio> obtenerListaSocios(){
		
	}
	
	public ArrayList<Socio> obtenerListaSociosDeudores(){
		
	}
	
	public Socio buscarSocio(String rut) {
		
	}
	
	
	//Métodos relacionados a Actividad
	
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima) {
		
	}
	
	public boolean modificarActividad(String nuevoIdActividad, String nuevoNombre, int nuevoCupoMaximo, int nuevaEdadMinima) throws CupoMaximoException {
		
	}
	
	public boolean eliminarActividad(String idActividad) {
		
	}
	
	public Actividad buscarActividad(String idActividad) {
		
	}
	
	public Actividad buscarActividad(int idReserva) {
		
	}

	public ArrayList<Actividad> obtenerActividades(){
		
	}
	
	//Métodos relacionados a Reserva
	
	public void agendarReserva(String rut, String idActividad, String fecha) throws MorosidadException, CupoMaximoException{
		
	}
	
	public boolean modificarReserva(int idReserva, String fecha, EstadoReserva estado, String rutSocio, String idActividad) throws MorosidadException{
	
	}
		
	public boolean eliminarReserva(int idReserva) {
		
	}
	
	public ArrayList<Reserva> listarReservasGlobales(){
		
	}
	
	//Otras opciones del menú
	
	public boolean pagarFacturacion(String rut) {
		
	}
	
	public boolean pagarFacturacion(String rut, int abono) {
		
	}
	
	public boolean generarCobroMensual() {
		
	}

	
	//Relacionado a la base de datos

	public void cargarDatosBatch() {
		
	}
	
	public void guardarDatosBatch() {
		
	}
}
