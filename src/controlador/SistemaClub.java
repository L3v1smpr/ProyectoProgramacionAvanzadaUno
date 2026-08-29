package controlador;
import modelo.Socio;
import java.util.Date;
import modelo.Actividad;
import modelo.Reserva;
import controlador.DBConnection;
import modelo.CupoMaximoException;
import modelo.MorosidadException;
import modelo.EstadoReserva;
import modelo.ClaseGrupal;
import modelo.EntrenamientoLibre;

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
	
	
	//De uso administrativo, no será utilizado en el menú - Ya que se implementará un sistema de soft-delete
	//con el atributo "activo : boolean" de Socio.
	public boolean eliminarSocio(String rut) {
		if (!mapaSocios.containsKey(rut)) {
			return false;
		}
		
		mapaSocios.remove(rut);
		return true;
	}
	
	public boolean desactivarSocio(String rut) {
		Socio socioBuscado = buscarSocio(rut); // Reutilizas tu propio método
	    
	    if (socioBuscado == null) {
	        return false;
	    }
	    
	    socioBuscado.setActivo(false);
	    return true;

	}
	
	public boolean activarSocio(String rut) {
		Socio socioBuscado = buscarSocio(rut); // Reutilizas tu propio método
	    
	    if (socioBuscado == null) {
	        return false;
	    }
	    
	    socioBuscado.setActivo(true);
	    return true;
	}
	
	
	public ArrayList<Socio> obtenerListaSocios(){
		ArrayList<Socio> listaSocios = new ArrayList<>();
		
		for (Socio s : mapaSocios.values()) {
			if (s.getActivo()) {
				listaSocios.add(s);
			}
		}
		return listaSocios;
	}
	
	public ArrayList<Socio> obtenerListaSociosDeudores(){
		ArrayList<Socio> listaSociosDeudores = new ArrayList<>();
		
		for (Socio s : mapaSocios.values()) {
			if (s.getActivo() && s.getEsMoroso()) {
				listaSociosDeudores.add(s);
			}
		}
		return listaSociosDeudores;
	}
	
	public Socio buscarSocio(String rut) {
		return mapaSocios.get(rut);
	}
	
	
	//Métodos relacionados a Actividad
	
	//------------------Sobrecarga de agregar actividad para la herencia----------------------------
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima, String profesor) {
		if (buscarActividad(idActividad) != null) {
			return false; //Ya existe la actividad con ese ID
		}
		
		Actividad nuevaActividad = new ClaseGrupal(idActividad, nombre, cupoMaximo, edadMinima, profesor);
		listaActividades.add(nuevaActividad);
		
		return true; //La clase se añadió correctamente
	}
	
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima, boolean requiereAsistencia) {
		if (buscarActividad(idActividad) != null) {
			return false; //Ya existe la actividad con ese ID
		}
		
		Actividad nuevaActividad = new EntrenamientoLibre(idActividad, nombre, cupoMaximo, edadMinima, requiereAsistencia);
		listaActividades.add(nuevaActividad);
		
		return true; //El entrenamiento se añadió correctamente
	
	}
	
	//-------------------------------------------FIN SOBRECARGA--------------------------------------
	
	public boolean modificarActividad(String idActividad, String nuevoNombre, int nuevoCupoMaximo, int nuevaEdadMinima) throws CupoMaximoException {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		if (nuevoCupoMaximo <= 0) {
			throw new CupoMaximoException("Error: El cupo máximo no puede ser cero o negativo.");
		}
		
		actividadBuscada.setNombre(nuevoNombre);
		actividadBuscada.setCupoMaximo(nuevoCupoMaximo);
		actividadBuscada.setEdadMinima(nuevaEdadMinima);
		
		return true; //La actividad fue modificada correctamente
	}
	
		public boolean eliminarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		listaActividades.remove(actividadBuscada);
		return true;
	}
	
	
	public boolean desactivarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		actividadBuscada.setActivo(false);
		return true;		
	}
	
	public boolean activarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		actividadBuscada.setActivo(true);
		return true;
	}
	
	public Actividad buscarActividad(String idActividad) {
		for (Actividad a : listaActividades) {
			if (a.getIdActividad().equals(idActividad)) return a;
		}
		
		return null;
	}
	
	public Actividad buscarActividad(int idReserva) {
		for (Socio s : mapaSocios.values()) {
			for (Reserva r : s.getListaReservas()) {
				if (r.getIdReserva() == idReserva) {
					String idBuscado = r.getIdActividadEnReserva();
					return buscarActividad(idBuscado);
				}
			}
		}
		
		return null;
	}

	public ArrayList<Actividad> obtenerActividades(){
		ArrayList<Actividad> listaFiltrada = new ArrayList<>();
	    
	    for (Actividad a : listaActividades) {
	        if (a.getActivo()) { // Protegemos el encapsulamiento filtrando las inactivas
	            listaFiltrada.add(a);
	        }
	    }
	    
	    return listaFiltrada;
	}
	
	//Métodos relacionados a Reserva
	
	public boolean agendarReserva(String rut, String idActividad, Date fecha) throws MorosidadException, CupoMaximoException{
		Socio socioBuscado = buscarSocio(rut);
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (socioBuscado == null || actividadBuscada == null) {
			return false;
		}
		
		if (socioBuscado.getEsMoroso()) {
			throw new MorosidadException("El socio mantiene una deuda activa y no puede agendar.");
		}
		
		int inscritos = 0;
		for (Socio s: mapaSocios.values()) {
			for (Reserva r: s.getListaReservas()) {
				if (r.getIdActividadEnReserva().equals(idActividad) && r.getEstado() != EstadoReserva.CANCELADA) {
					inscritos++;
				}
			}
		}
		
		if (inscritos >= actividadBuscada.getCupoMaximo()) {
			throw new CupoMaximoException("La actividad alcanzó su límite (" + actividadBuscada.getCupoMaximo() + " cupos).");
		}
		
		int idNuevaReserva = (int) (System.currentTimeMillis() % 100000);
		Reserva nuevaReserva = new Reserva(idNuevaReserva, fecha, EstadoReserva.PENDIENTE, rut, idActividad);
		
		socioBuscado.agregarReserva(nuevaReserva);
		return true;
		
		
	}
	
	public boolean modificarReserva(int idReserva, Date fecha, EstadoReserva estado, String rutSocio, String idActividad) throws MorosidadException{
		Socio socioBuscado = buscarSocio(rutSocio);
		
		if (socioBuscado == null) {
			return false; //Socio no existe
		}
		
		if (socioBuscado.getEsMoroso()) {
			throw new MorosidadException("El socio presenta morosidad y no puede modificar reservas.");
		}
		
		Reserva reservaAModificar = socioBuscado.buscarReserva(idReserva);
		
		if (reservaAModificar == null) {
			return false; //La reserva no existe
		}
		
		reservaAModificar.setFecha(fecha);
		reservaAModificar.setEstado(estado);
		reservaAModificar.setIdActividadEnReserva(idActividad);
		
		return true;
	}
		
	public boolean eliminarReserva(int idReserva) {
		
		for (Socio s : mapaSocios.values()) { //Recorre los socios del mapa
			if (s.eliminarReserva(idReserva)) {
				return true; //Se eliminó correctamente la reserva de ese socio
			}
		}
		
		return false; //Recorrió todos los socios pero no encontró la reserva
	}
	
	public ArrayList<Reserva> listarReservasGlobales(){
		
		//Recopilación de reservas en un arreglo global
		ArrayList<Reserva> reservasGlobales = new ArrayList<>();
		
		for (Socio s: mapaSocios.values()) {
			if (s.getListaReservas() != null) {
				reservasGlobales.addAll(s.getListaReservas());
			}
		}
		
		
		//Ordenamiento cronológico
		reservasGlobales.sort((r1, r2) -> {
			if (r1.getFecha() == null || r2.getFecha() == null) {
				return 0;
			}
			
			//Comparación
			return r1.getFecha().compareTo(r2.getFecha());
		});
		
		return reservasGlobales;
		
	}
	
	//Otras opciones del menú
	
	public boolean pagarFacturacion(String rut) {
		if (!mapaSocios.containsKey(rut)) {
			return false; //El socio no existe
		}
		
		Socio socioBuscado = buscarSocio(rut);
		
		socioBuscado.abonarDeuda();
		
		return true;
	}
	
	public boolean pagarFacturacion(String rut, int abono) {
		if (!mapaSocios.containsKey(rut)) {
			return false; //El socio no existe
		}
		
		Socio socioBuscado = buscarSocio(rut);
		
		socioBuscado.abonarDeuda(abono);
		return true;
	}
	
	public boolean generarCobroMensual() { //El precio fijo mensual será de 10.000
		if (mapaSocios.isEmpty()) {
			return false; //No hay socios registrados en el sistema
		}
		
		int tarifaMensual = 10000;
		
		for (Socio s: mapaSocios.values()) {
			if (s.getActivo()) {
				int deudaAnterior = s.getDeuda();
				s.setDeuda(deudaAnterior + tarifaMensual);
				s.setEsMoroso(true);
			}
		}
		
		return true; //Se hizo el cobro correctamente a cada socio del sistema
	}

	
	//Relacionado a la base de datos

	public void cargarDatosBatch() {
		
	}
	
	public void guardarDatosBatch() {
		
	}
}
