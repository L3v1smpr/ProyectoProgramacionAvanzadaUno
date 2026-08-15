package controlador;


import java.util.HashMap;
import java.util.ArrayList;

public class SistemaClub {

	//Atributos
	private HashMap<String, Socio> mapaSocio;
	private ArrayList<Actividad> listaActividades;
	private DBConnection connection;
	
	
	
	//Constructor
	
	public SistemaClub(){
	
		this.mapaSocio = new HashMap<>();
		this.listaActividades = new ArrayList<>();
		this.connection = new DBConnection();
	}
	
	
	//Metodos relacionados a Socio
	public boolean agregarSocio(String rut, String nombre) {
		
	}
	
	public boolean modificarSocio(String rut, String nombre, int deuda, boolean esMoroso) {
		
	}
	
	public boolean eliminarSocio(String rut) {
		
	}
	
	public ArrayList<Socio> obtenerListaSocio(){
		
	}
	
	public ArrayList<Socio> obtenerListaSociosDeudores(){
		
	}
	
	public Socio buscarSocio(String rut) {
		
	}
	
	
	//Métodos relacionados a Actividad
	
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo) {
		
	}
	
	public boolean modificarActividad(String nuevoIdActividad, String nuevoNombre, int nuevoCupoMaximo) {
		
	}
	
	public boolean eliminarActividad(String idActividad) {
		
	}
	
	public Actividad buscarActividad(String idActividad) {
		
	}
	
	public Actividad buscarActividad(int idReserva) {
		
	}

	public ArrayList<Actividad> obtenerActivdades(){
		
	}
	
	//Métodos relacionados a Reserva
	
	public void agendarReserva(String rut, String idActividad, String fecha) {
		
	}
	
	public boolean modificarReserva(int idReserva, String fecha, boolean estado, String rutSocio, String idActividad) {
	
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
