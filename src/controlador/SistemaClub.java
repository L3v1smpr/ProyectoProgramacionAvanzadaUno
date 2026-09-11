package controlador;

import modelo.Socio;
import modelo.Evento;
import modelo.Actividad;
import modelo.Reserva;
import modelo.ConexionBDException;
import modelo.PersistenciaDatosException;
import modelo.CupoMaximoException;
import modelo.MorosidadException;
import modelo.EstadoReserva;
import modelo.ClaseGrupal;
import modelo.EntrenamientoLibre;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controlador principal que gestiona toda la lógica de negocio del club.
 * Coordina el mapa de socios, lista de actividades y las llamadas a la base de datos.
 * 
 * @author Peñaloza Elvis
 * @version 1.0
 */
public class SistemaClub {

	//Atributos
	private HashMap<String, Socio> mapaSocios;
	private ArrayList<Actividad> listaActividades;
	private DBConnection connection;
	
	
	
	/**
	 * Constructor principal del sistema. Inicializa las colecciones y la conexión a BD.
	 */
	public SistemaClub(){
	
		this.mapaSocios = new HashMap<>();
		this.listaActividades = new ArrayList<>();
		this.connection = new DBConnection();
	}
	
	
	/**
	 * Carga datos iniciales estáticos (mock) requeridos para demostrar las funcionalidades.
	 * Solo se ejecuta si el sistema está completamente vacío.
	 * 
	 * @return true si se insertaron datos iniciales, false si ya existían datos previos.
	 */
	public boolean cargarDatosIniciales() {

	    if (!mapaSocios.isEmpty() || !listaActividades.isEmpty()) {
	        return false;
	    }

	    // ---------------- SOCIOS ----------------

	    agregarSocio(
	        "11.111.111-1",
	        "Ana Torres",
	        24
	    );

	    agregarSocio(
	        "22.222.222-2",
	        "Bruno Diaz",
	        31
	    );

	    agregarSocio(
	        "33.333.333-3",
	        "Camila Soto",
	        19
	    );

	    agregarSocio(
	        "44.444.444-4",
	        "Diego Rojas",
	        28
	    );
	    
	    modificarSocio(
	        "22.222.222-2",
	        "Bruno Diaz",
	        31,
	        20000,
	        true
	    );
	    
	    desactivarSocio("44.444.444-4");


	    // ---------------- ACTIVIDADES ----------------
	    
	    agregarActividad(
	        "CG001",
	        "Entrenamiento Funcional",
	        20,
	        16,
	        "Laura Perez"
	    );

	    agregarActividad(
	        "EL001",
	        "Sala de Musculacion",
	        30,
	        18,
	        true
	    );

	    Calendar fechaEvento = Calendar.getInstance();
	    fechaEvento.add(Calendar.DAY_OF_MONTH, 30);

	    agregarActividad(
	        "EV001",
	        "Torneo Interno",
	        50,
	        16,
	        fechaEvento.getTime(),
	        "Cancha Central",
	        "Competencia"
	    );

	    // Actividad inactiva para probar reactivacion.
	    agregarActividad(
	        "CG002",
	        "Yoga Inicial",
	        15,
	        14,
	        "Marcela Soto"
	    );

	    desactivarActividad("CG002");


	    // ---------------- RESERVAS ----------------

	    Calendar fechaReservaPendiente = Calendar.getInstance();
	    fechaReservaPendiente.add(Calendar.DAY_OF_MONTH, 7);

	    Reserva reservaPendiente = new Reserva(
	        1001,
	        fechaReservaPendiente.getTime(),
	        EstadoReserva.PENDIENTE,
	        "11.111.111-1",
	        "CG001"
	    );

	    buscarSocio("11.111.111-1")
	        .agregarReserva(reservaPendiente);


	    Calendar fechaReservaCompletada = Calendar.getInstance();
	    fechaReservaCompletada.add(Calendar.DAY_OF_MONTH, -3);

	    Reserva reservaCompletada = new Reserva(
	        1002,
	        fechaReservaCompletada.getTime(),
	        EstadoReserva.COMPLETADA,
	        "33.333.333-3",
	        "EL001"
	    );

	    buscarSocio("33.333.333-3")
	        .agregarReserva(reservaCompletada);

	    return true;
	}
	
	
	//Metodos relacionados a Socio
	
	/**
	 * Agrega un nuevo socio al sistema si el RUT no se encuentra registrado.
	 *
	 * @param rut RUT que identifica al socio
	 * @param nombre nombre del socio
	 * @param edad edad del socio
	 * @return {@code true} si el socio fue registrado correctamente;
	 *         {@code false} si ya existe un socio con el mismo RUT
	 */
	public boolean agregarSocio(String rut, String nombre, int edad) {
		if (mapaSocios.containsKey(rut)) {
			return false;
		}
		
		Socio nuevoSocio = new Socio(rut, nombre, edad, 0, false);
		
		mapaSocios.put(rut, nuevoSocio);
		
		return true;
	}
	
	/**
	 * Modifica los datos personales y financieros de un socio existente.
	 *
	 * Si el socio deja de encontrarse en estado de morosidad, su deuda
	 * se establece automáticamente en cero.
	 *
	 * @param rut RUT del socio que se desea modificar
	 * @param nombre nuevo nombre del socio
	 * @param edad nueva edad del socio
	 * @param deuda nueva deuda asociada al socio
	 * @param esMoroso indica si el socio debe quedar marcado como moroso
	 * @return {@code true} si el socio fue encontrado y modificado;
	 *         {@code false} si no existe un socio con el RUT indicado
	 */
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
	
	/**
	 * Elimina físicamente un socio del mapa interno del sistema.
	 *
	 * Este método corresponde a una eliminación administrativa permanente.
	 * El flujo normal de las interfaces utiliza {@link #desactivarSocio(String)}
	 * para realizar una baja lógica sin perder la información del socio.
	 *
	 * @param rut RUT del socio que se desea eliminar
	 * @return {@code true} si el socio fue eliminado;
	 *         {@code false} si no se encontró un socio con ese RUT
	 */
	public boolean eliminarSocio(String rut) {
		if (!mapaSocios.containsKey(rut)) {
			return false;
		}
		
		mapaSocios.remove(rut);
		return true;
	}
	
	/**
	 * Desactiva un socio mediante una baja lógica.
	 *
	 * El socio permanece almacenado en el sistema, pero deja de aparecer
	 * en los listados normales de socios activos.
	 *
	 * @param rut RUT del socio que se desea desactivar
	 * @return {@code true} si el socio fue encontrado y desactivado;
	 *         {@code false} si no existe
	 */
	public boolean desactivarSocio(String rut) {
		Socio socioBuscado = buscarSocio(rut); // Reutilizas tu propio método
	    
	    if (socioBuscado == null) {
	        return false;
	    }
	    
	    socioBuscado.setActivo(false);
	    return true;

	}
	
	/**
	 * Reactiva un socio que se encontraba desactivado.
	 *
	 * @param rut RUT del socio que se desea reactivar
	 * @return {@code true} si el socio fue encontrado y reactivado;
	 *         {@code false} si no existe
	 */
	public boolean activarSocio(String rut) {
		Socio socioBuscado = buscarSocio(rut); // Reutilizas tu propio método
	    
	    if (socioBuscado == null) {
	        return false;
	    }
	    
	    socioBuscado.setActivo(true);
	    return true;
	}
	
	/**
	 * Obtiene todos los socios que se encuentran actualmente activos.
	 *
	 * @return lista de socios activos registrados en el sistema
	 */
	public ArrayList<Socio> obtenerListaSocios(){
		ArrayList<Socio> listaSocios = new ArrayList<>();
		
		for (Socio s : mapaSocios.values()) {
			if (s.getActivo()) {
				listaSocios.add(s);
			}
		}
		return listaSocios;
	}
	
	/**
	 * Obtiene los socios activos que presentan estado de morosidad.
	 *
	 * @return lista de socios activos y morosos
	 */
	public ArrayList<Socio> obtenerListaSociosDeudores(){
		ArrayList<Socio> listaSociosDeudores = new ArrayList<>();
		
		for (Socio s : mapaSocios.values()) {
			if (s.getActivo() && s.getEsMoroso()) {
				listaSociosDeudores.add(s);
			}
		}
		return listaSociosDeudores;
	}
	
	/**
	 * Busca un socio utilizando su RUT como clave.
	 *
	 * @param rut RUT del socio buscado
	 * @return socio encontrado o {@code null} si no existe
	 */
	public Socio buscarSocio(String rut) {
		return mapaSocios.get(rut);
	}
	
	/**
	 * Exporta la lista actual de socios a un archivo CSV.
	 * 
	 * @param rutaArchivo Destino del archivo.
	 * @return Cantidad de socios exportados.
	 * @throws IOException Si existe un problema al crear o escribir el archivo.
	 */
	public int exportarSociosCSV(String rutaArchivo) throws IOException {

	    ArrayList<Socio> sociosExportar =
	            new ArrayList<>(mapaSocios.values());

	    // HashMap no garantiza orden.
	    // Para que el archivo siempre sea legible y consistente,
	    // los socios se ordenan por RUT antes de exportar.
	    sociosExportar.sort((s1, s2) ->
	        s1.getRut().compareToIgnoreCase(s2.getRut())
	    );

	    try (PrintWriter escritor = new PrintWriter(
	            Files.newBufferedWriter(
	                Paths.get(rutaArchivo),
	                StandardCharsets.UTF_8
	            )
	    )) {

	        // BOM UTF-8 para mejorar compatibilidad con Excel
	        // y preservar correctamente tildes y caracteres especiales.
	        escritor.print('\uFEFF');

	        escritor.println(
	            "RUT;Nombre;Edad;Deuda;Moroso;Estado;CantidadReservas"
	        );

	        for (Socio socio : sociosExportar) {

	            String estadoMorosidad =
	                    socio.getEsMoroso() ? "SI" : "NO";

	            String estadoSocio =
	                    socio.getActivo() ? "ACTIVO" : "INACTIVO";

	            escritor.println(
	                escaparCSV(socio.getRut()) + ";"
	                + escaparCSV(socio.getNombre()) + ";"
	                + socio.getEdad() + ";"
	                + socio.getDeuda() + ";"
	                + estadoMorosidad + ";"
	                + estadoSocio + ";"
	                + socio.getListaReservas().size()
	            );
	        }
	    }

	    return sociosExportar.size();
	}
	
	private String escaparCSV(String valor) {

	    if (valor == null) {
	        return "\"\"";
	    }

	    String valorEscapado =
	            valor.replace("\"", "\"\"");

	    return "\"" + valorEscapado + "\"";
	}
	
	
	//Métodos relacionados a Actividad
	
	//------------------Sobrecarga de agregar actividad para la herencia----------------------------
	
	/**
	 * Registra una nueva actividad de tipo {@link ClaseGrupal}.
	 *
	 * @param idActividad identificador único de la actividad
	 * @param nombre nombre de la actividad
	 * @param cupoMaximo cantidad máxima de participantes
	 * @param edadMinima edad mínima requerida
	 * @param profesor nombre del profesor encargado
	 * @return {@code true} si la actividad fue registrada;
	 *         {@code false} si ya existe una actividad con el mismo identificador
	 */
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima, String profesor) {
		if (buscarActividad(idActividad) != null) {
			return false; //Ya existe la actividad con ese ID
		}
		
		Actividad nuevaActividad = new ClaseGrupal(idActividad, nombre, cupoMaximo, edadMinima, profesor);
		listaActividades.add(nuevaActividad);
		
		return true; //La clase se añadió correctamente
	}
	
	/**
	 * Registra una nueva actividad de tipo {@link EntrenamientoLibre}.
	 *
	 * @param idActividad identificador único de la actividad
	 * @param nombre nombre de la actividad
	 * @param cupoMaximo cantidad máxima de participantes
	 * @param edadMinima edad mínima requerida
	 * @param requiereAsistencia indica si se requiere control de asistencia
	 * @return {@code true} si la actividad fue registrada;
	 *         {@code false} si ya existe una actividad con el mismo identificador
	 */
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima, boolean requiereAsistencia) {
		if (buscarActividad(idActividad) != null) {
			return false; //Ya existe la actividad con ese ID
		}
		
		Actividad nuevaActividad = new EntrenamientoLibre(idActividad, nombre, cupoMaximo, edadMinima, requiereAsistencia);
		listaActividades.add(nuevaActividad);
		
		return true; //El entrenamiento se añadió correctamente
	
	}
	
	/**
	 * Registra una nueva actividad de tipo {@link Evento}.
	 *
	 * @param idActividad identificador único del evento
	 * @param nombre nombre del evento
	 * @param cupoMaximo cantidad máxima de participantes
	 * @param edadMinima edad mínima requerida
	 * @param fecha fecha programada para el evento
	 * @param lugar lugar donde se realizará
	 * @param tipoEvento clasificación del evento
	 * @return {@code true} si el evento fue registrado;
	 *         {@code false} si ya existe una actividad con el mismo identificador
	 */
	public boolean agregarActividad(String idActividad, String nombre, int cupoMaximo, int edadMinima, Date fecha, String lugar, String tipoEvento) {
		if (buscarActividad(idActividad) != null) {
			return false;
		}
		
		Actividad nuevaActividad = new Evento(idActividad, nombre, cupoMaximo, edadMinima, fecha, lugar, tipoEvento);
		listaActividades.add(nuevaActividad);
		
		return true; //El evento fue añadido correctamente
	}
	
	//-------------------------------------------FIN SOBRECARGA--------------------------------------
	
	/**
	 * Modifica los atributos comunes de una actividad existente.
	 *
	 * @param idActividad identificador de la actividad que se desea modificar
	 * @param nuevoNombre nuevo nombre de la actividad
	 * @param nuevoCupoMaximo nuevo límite máximo de participantes
	 * @param nuevaEdadMinima nueva edad mínima requerida
	 * @return {@code true} si la actividad fue encontrada y modificada;
	 *         {@code false} si no existe una actividad con el identificador indicado
	 * @throws CupoMaximoException si el nuevo cupo máximo es menor o igual a cero
	 */
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
	
	/**
	 * Elimina físicamente una actividad de la colección interna del sistema.
	 *
	 * El flujo normal de las interfaces utiliza
	 * {@link #desactivarActividad(String)} para realizar una baja lógica
	 * y conservar la información de la actividad.
	 *
	 * @param idActividad identificador de la actividad que se desea eliminar
	 * @return {@code true} si la actividad fue eliminada;
	 *         {@code false} si no fue encontrada
	 */
	public boolean eliminarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		listaActividades.remove(actividadBuscada);
		return true;
	}
	
	/**
	 * Desactiva una actividad mediante una baja lógica.
	 *
	 * @param idActividad identificador de la actividad que se desea desactivar
	 * @return {@code true} si la actividad fue encontrada y desactivada;
	 *         {@code false} si no existe
	 */
	public boolean desactivarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		actividadBuscada.setActivo(false);
		return true;		
	}
	
	/**
	 * Reactiva una actividad previamente desactivada.
	 *
	 * @param idActividad identificador de la actividad que se desea reactivar
	 * @return {@code true} si la actividad fue encontrada y activada;
	 *         {@code false} si no existe
	 */
	public boolean activarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		actividadBuscada.setActivo(true);
		return true;
	}
	
	/**
	 * Busca una actividad utilizando su identificador.
	 *
	 * @param idActividad identificador de la actividad buscada
	 * @return actividad encontrada o {@code null} si no existe
	 */
	public Actividad buscarActividad(String idActividad) {
		for (Actividad a : listaActividades) {
			if (a.getIdActividad().equals(idActividad)) return a;
		}
		
		return null;
	}
	
	/**
	 * Busca la actividad asociada a una reserva determinada.
	 *
	 * El método recorre las reservas almacenadas en los socios hasta
	 * encontrar la reserva con el identificador indicado.
	 *
	 * @param idReserva identificador de la reserva
	 * @return actividad asociada a la reserva o {@code null}
	 *         si la reserva no fue encontrada
	 */
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

	/**
	 * Obtiene todas las actividades que se encuentran activas.
	 *
	 * Las actividades desactivadas permanecen almacenadas internamente,
	 * pero no forman parte de la lista retornada.
	 *
	 * @return lista de actividades activas
	 */
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
	
	/**
	 * Agenda una nueva reserva para un socio en una actividad.
	 *
	 * Antes de crearla, verifica que el socio y la actividad existan,
	 * que el socio no presente morosidad y que la actividad disponga
	 * de cupos disponibles.
	 *
	 * La reserva se crea inicialmente con estado
	 * {@link EstadoReserva#PENDIENTE}.
	 *
	 * @param rut RUT del socio que realiza la reserva
	 * @param idActividad identificador de la actividad reservada
	 * @param fecha fecha asociada a la reserva
	 * @return {@code true} si la reserva fue creada correctamente;
	 *         {@code false} si el socio o la actividad no existen
	 * @throws MorosidadException si el socio presenta una deuda activa
	 * @throws CupoMaximoException si la actividad alcanzó su cupo máximo
	 */
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
	
	/**
	 * Modifica los datos de una reserva existente perteneciente a un socio.
	 *
	 * Permite actualizar la fecha, el estado y la actividad asociada.
	 *
	 * @param idReserva identificador de la reserva que se desea modificar
	 * @param fecha nueva fecha asociada a la reserva
	 * @param estado nuevo estado de la reserva
	 * @param rutSocio RUT del socio propietario de la reserva
	 * @param idActividad nuevo identificador de actividad asociado
	 * @return {@code true} si la reserva fue encontrada y modificada;
	 *         {@code false} si el socio o la reserva no existen
	 * @throws MorosidadException si el socio se encuentra en estado de morosidad
	 */
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
		
	/**
	 * Elimina una reserva de la colección interna del socio que la contiene.
	 *
	 * @param idReserva identificador de la reserva que se desea eliminar
	 * @return {@code true} si la reserva fue encontrada y eliminada;
	 *         {@code false} en caso contrario
	 */
	public boolean eliminarReserva(int idReserva) {
		
		for (Socio s : mapaSocios.values()) { //Recorre los socios del mapa
			if (s.eliminarReserva(idReserva)) {
				return true; //Se eliminó correctamente la reserva de ese socio
			}
		}
		
		return false; //Recorrió todos los socios pero no encontró la reserva
	}
	
	/**
	 * Recopila todas las reservas registradas en los socios y las ordena
	 * cronológicamente según su fecha.
	 *
	 * @return lista global de reservas ordenada por fecha
	 */
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
	
	/**
	 * Realiza el pago total de la deuda de un socio.
	 *
	 * @param rut RUT del socio cuya deuda se desea saldar
	 * @return {@code true} si el socio existe y se procesó el pago;
	 *         {@code false} si el socio no fue encontrado
	 */
	public boolean pagarFacturacion(String rut) {
		if (!mapaSocios.containsKey(rut)) {
			return false; //El socio no existe
		}
		
		Socio socioBuscado = buscarSocio(rut);
		
		socioBuscado.abonarDeuda();
		
		return true;
	}
	
	/**
	 * Realiza un abono parcial sobre la deuda de un socio.
	 *
	 * @param rut RUT del socio
	 * @param abono monto que se desea abonar
	 * @return {@code true} si el socio existe y se procesó el abono;
	 *         {@code false} si el socio no fue encontrado
	 */
	public boolean pagarFacturacion(String rut, int abono) {
		if (!mapaSocios.containsKey(rut)) {
			return false; //El socio no existe
		}
		
		Socio socioBuscado = buscarSocio(rut);
		
		socioBuscado.abonarDeuda(abono);
		return true;
	}
	
	/**
	 * Genera el cobro mensual para todos los socios activos del sistema.
	 *
	 * Actualmente se aplica una tarifa fija de $10.000, que se suma
	 * a la deuda existente de cada socio activo. Después del cobro,
	 * el socio queda marcado como moroso.
	 *
	 * @return {@code true} si existen socios registrados y se realizó el cobro;
	 *         {@code false} si no existen socios registrados
	 */
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
	
	/**
	 * Obtiene las actividades activas que corresponden específicamente
	 * a eventos.
	 *
	 * La identificación se realiza de forma polimórfica mediante
	 * {@link Actividad#esEvento()}.
	 *
	 * @return lista de eventos activos
	 */
	public ArrayList<Actividad> obtenerEventos(){
		ArrayList<Actividad> listaEventos = new ArrayList<>();
		
		for (Actividad actividad : listaActividades) {
			if (actividad.esEvento() && actividad.getActivo()) {
	            listaEventos.add(actividad);
	        }
		}
	
		return listaEventos;
	}

	
	//Relacionado a la base de datos
	
	/**
	 * Carga desde la base de datos el estado persistido del sistema.
	 *
	 * Primero reconstruye los socios, posteriormente las actividades
	 * y finalmente las reservas. Cada reserva se vuelve a incorporar
	 * en la colección interna del socio correspondiente.
	 *
	 * También se valida que las reservas almacenadas hagan referencia
	 * a socios y actividades existentes.
	 *
	 * @throws ConexionBDException si no es posible establecer o utilizar
	 *         la conexión con la base de datos
	 * @throws PersistenciaDatosException si los datos almacenados son
	 *         inválidos o presentan inconsistencias
	 */
	public void cargarDatosBatch() throws ConexionBDException, PersistenciaDatosException {

	    connection.crearTablas();

	    ArrayList<Socio> sociosCargados = connection.cargarSocios();

	    mapaSocios.clear();

	    for (Socio socio : sociosCargados) {
	        mapaSocios.put(socio.getRut(), socio);
	    }

	    ArrayList<Actividad> actividadesCargadas =
	            connection.cargarActividades();

	    listaActividades.clear();
	    listaActividades.addAll(actividadesCargadas);
	    
	    ArrayList<Reserva> reservasCargadas =
	            connection.cargarReservas();

	    for (Reserva reserva : reservasCargadas) {

	        Socio socio =
	                mapaSocios.get(reserva.getRutSocio());

	        Actividad actividad =
	                buscarActividad(reserva.getIdActividadEnReserva());

	        if (socio == null || actividad == null) {
	            throw new PersistenciaDatosException(
	                "Existe una reserva asociada a un socio o actividad inexistente."
	            );
	        }

	        if (!socio.agregarReserva(reserva)) {
	            throw new PersistenciaDatosException(
	                "No fue posible reconstruir la reserva "
	                + reserva.getIdReserva() + "."
	            );
	        }
	    }
	}
	
	/**
	 * Guarda en la base de datos el estado actual completo del sistema.
	 *
	 * Persiste socios y actividades y posteriormente reconstruye
	 * la tabla de reservas a partir de las reservas actualmente
	 * almacenadas dentro de cada socio.
	 *
	 * @throws ConexionBDException si no es posible acceder a la base de datos
	 * @throws PersistenciaDatosException si ocurre un error durante
	 *         el almacenamiento de la información
	 */
	public void guardarDatosBatch() throws ConexionBDException, PersistenciaDatosException {

	    connection.crearTablas();

	    for (Socio socio : mapaSocios.values()) {
	        connection.guardarSocio(socio);
	    }

	    for (Actividad actividad : listaActividades) {
	        connection.guardarActividad(actividad);
	    }

	    connection.limpiarReservas();

	    for (Socio socio : mapaSocios.values()) {

	        for (Reserva reserva : socio.getListaReservas()) {
	            connection.guardarReserva(reserva);
	        }
	    }
	}
}