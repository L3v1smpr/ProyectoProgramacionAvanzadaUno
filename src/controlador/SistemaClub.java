package controlador;
import modelo.Socio;
import modelo.Actividad;
import modelo.Reserva;
import controlador.DBConnection;


import java.util.HashMap;
import java.util.ArrayList;

public class SistemaClub {

	//Atributos
	private HashMap<String, Socio> mapaSocios;
	private ArrayList<Actividad> arrayActividades;
	private DBConnection connection;
	
	
	
	//Constructor
	
	public SistemaClub(){
	
		this.mapaSocios = new HashMap<>();
		this.arrayActividades = new ArrayList<>();
		this.connection = new DBConnection();
	}
	
	
	//Metodos relacionados a Socio
	public boolean agregarSocio(String rut, String nombre, int edad) {
		
	}
	
	public boolean modificarSocio(String rut, String nombre, int edad, int deuda, boolean esMoroso) throws MorosidadException{
		
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
	
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo) throws MorosidadException, CupoMaximoException{
		
	}
	
	public boolean modificarActividad(String nuevoIdActividad, String nuevoNombre, int nuevoCupoMaximo) throws MorosidadException, CupoMaximoException{
		
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
	
	public void agendarReserva(String rut, String idActividad, String fecha) throws MorosidadException{
		
	}
	
	public boolean modificarReserva(int idReserva, String fecha, boolean estado, String rutSocio, String idActividad) throws MorosidadException{
	
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
