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

	    // Socio moroso para probar facturacion y MorosidadException.
	    modificarSocio(
	        "22.222.222-2",
	        "Bruno Diaz",
	        31,
	        20000,
	        true
	    );

	    // Socio inactivo para probar reactivacion.
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
	 * Agrega un nuevo socio al sistema si el RUT no está duplicado.
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
	 * Modifica los datos principales y de facturación de un socio existente.
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
	public boolean eliminarSocio(String rut) {
		if (!mapaSocios.containsKey(rut)) {
			return false;
		}
		
		mapaSocios.remove(rut);
		return true;
	}
	
	/**
	 * Desactiva un socio en el sistema (baja lógica).
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
	 * Reactiva un socio previamente desactivado.
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
	 * Obtiene una lista con todos los socios marcados como activos.
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
	 * Obtiene la lista exclusiva de socios que presentan deudas activas.
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
	 * Busca a un socio por su RUT específico.
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
	 * Agrega una actividad de tipo Clase Grupal al sistema.
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
	 * Agrega una actividad de tipo Entrenamiento Libre al sistema.
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
	 * Agrega una actividad de tipo Evento al sistema.
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
	 * Modifica los atributos base de una actividad ya existente.
	 * 
	 * @throws CupoMaximoException Si se intenta definir un cupo igual o menor a cero.
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
	
	public boolean eliminarActividad(String idActividad) {
		Actividad actividadBuscada = buscarActividad(idActividad);
		
		if (actividadBuscada == null) {
			return false;
		}
		
		listaActividades.remove(actividadBuscada);
		return true;
	}
	
	/**
	 * Desactiva una actividad (baja lógica).
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
	 * Reactiva una actividad inactiva.
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
	 * Busca una actividad en base a su ID.
	 */
	public Actividad buscarActividad(String idActividad) {
		for (Actividad a : listaActividades) {
			if (a.getIdActividad().equals(idActividad)) return a;
		}
		
		return null;
	}
	
	/**
	 * Busca la actividad vinculada a un número de reserva específico.
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
	 * Filtra y devuelve la lista de actividades habilitadas.
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
	 * Intenta agendar una nueva reserva verificando deuda y cupos disponibles.
	 * 
	 * @throws MorosidadException Si el socio tiene deudas pendientes.
	 * @throws CupoMaximoException Si la actividad solicitada ya llenó sus vacantes.
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
	 * Actualiza los parámetros de una reserva ya registrada.
	 * 
	 * @throws MorosidadException Si el socio está moroso.
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
	 * Localiza y elimina una reserva del registro del respectivo socio.
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
	 * Extrae y ordena cronológicamente el total de reservas activas del club.
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
	 * Liquida totalmente la deuda de un socio.
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
	 * Efectúa un cargo estándar por cuota mensual a todos los socios activos.
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
	 * Devuelve un listado de actividades que son específicamente de tipo Evento.
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
	 * Carga toda la información desde la base de datos a memoria (socios, actividades y reservas).
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
	 * Persiste el estado completo actual del sistema hacia la base de datos.
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