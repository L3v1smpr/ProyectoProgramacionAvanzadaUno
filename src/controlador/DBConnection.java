package controlador;

import modelo.PersistenciaDatosException;
import modelo.ConexionBDException;
import modelo.Socio;
import modelo.Actividad;
import modelo.ClaseGrupal;
import modelo.EntrenamientoLibre;
import modelo.Evento;
import modelo.Reserva;
import modelo.EstadoReserva;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Maneja la conexión y las operaciones CRUD directamente con la base de datos SQLite.
 * 
 * @author Peñaloza Elvis
 * @version 1.0
 */
public class DBConnection {

    private String url;
    private Connection connection;

    /**
     * Constructor por defecto que establece la ruta de la base de datos.
     */
    public DBConnection() {
        this.url = "jdbc:sqlite:club_deportivo.db";
    }

    /**
     * Establece la conexión con la base de datos y habilita las llaves foráneas.
     * 
     * @throws ConexionBDException Si ocurre un error al conectar.
     */
    public void conectar() throws ConexionBDException {
        try {
            connection = DriverManager.getConnection(url);
            
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON;");
            }
            
            
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible conectar con la base de datos.", e);
        }
    }

    /**
     * Obtiene la conexión activa a la base de datos, abriéndola si está cerrada.
     * 
     * @return Objeto Connection activo.
     * @throws ConexionBDException Si hay un problema verificando o estableciendo la conexión.
     */
    public Connection getConnection() throws ConexionBDException {
        try {
            if (connection == null || connection.isClosed()) {
            	conectar();
            }

            return connection;

        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible comprobar el estado de la conexión.", e);
        }
    }

    /**
     * Cierra la conexión activa con la base de datos.
     * 
     * @throws ConexionBDException Si ocurre un error al intentar cerrar la conexión.
     */
    public void cerrarConexion() throws ConexionBDException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new ConexionBDException("No fue posible cerrar la conexión con la base de datos.",
                e
            );
        }
    }
    
    /**
     * Guarda o actualiza los datos de un socio en la tabla SOCIOS.
     * 
     * @param socio Objeto Socio a persistir.
     * @throws ConexionBDException si ocurre un problema al acceder a la conexión
     * @throws PersistenciaDatosException Si la consulta SQL falla.
     */
    public void guardarSocio(Socio socio)
            throws ConexionBDException, PersistenciaDatosException {

    	String sql =
    		    "INSERT INTO SOCIOS "
    		    + "(rut, nombre, edad, deuda, es_moroso, activo) "
    		    + "VALUES (?, ?, ?, ?, ?, ?) "
    		    + "ON CONFLICT(rut) DO UPDATE SET "
    		    + "nombre = excluded.nombre, "
    		    + "edad = excluded.edad, "
    		    + "deuda = excluded.deuda, "
    		    + "es_moroso = excluded.es_moroso, "
    		    + "activo = excluded.activo;";

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql)) {

            statement.setString(1, socio.getRut());
            statement.setString(2, socio.getNombre());
            statement.setInt(3, socio.getEdad());
            statement.setInt(4, socio.getDeuda());
            statement.setInt(5, socio.getEsMoroso() ? 1 : 0);
            statement.setInt(6, socio.getActivo() ? 1 : 0);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaDatosException(
                "No fue posible guardar el socio en la base de datos.",
                e
            );
        }
    }
    
    /**
     * Carga todos los socios registrados en la base de datos.
     * 
     * @return Lista de objetos Socio extraídos de la BD.
     * @throws ConexionBDException Si falla la conexión.
     * @throws PersistenciaDatosException Si la consulta SQL falla.
     */
    public ArrayList<Socio> cargarSocios()
            throws ConexionBDException, PersistenciaDatosException {

        String sql =
            "SELECT rut, nombre, edad, deuda, es_moroso, activo "
            + "FROM SOCIOS;";

        ArrayList<Socio> socios = new ArrayList<>();

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql);

             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                Socio socio = new Socio(
                    resultado.getString("rut"),
                    resultado.getString("nombre"),
                    resultado.getInt("edad"),
                    resultado.getInt("deuda"),
                    resultado.getInt("es_moroso") == 1
                );

                socio.setActivo(
                    resultado.getInt("activo") == 1
                );

                socios.add(socio);
            }

            return socios;

        } catch (SQLException e) {
            throw new PersistenciaDatosException(
                "No fue posible cargar los socios desde la base de datos.",
                e
            );
        }
    }
    
    /**
     * Crea las tablas necesarias para la persistencia del sistema
     * si todavía no existen.
     *
     * Se crean las tablas SOCIOS, ACTIVIDADES y RESERVAS junto con
     * las relaciones de claves foráneas correspondientes.
     *
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si ocurre un error al ejecutar
     *         las sentencias de creación de tablas
     */
    public void crearTablas()
            throws ConexionBDException, PersistenciaDatosException {

        String sqlSocios =
            "CREATE TABLE IF NOT EXISTS SOCIOS ("
            + "rut TEXT PRIMARY KEY, "
            + "nombre TEXT NOT NULL, "
            + "edad INTEGER NOT NULL, "
            + "deuda INTEGER NOT NULL, "
            + "es_moroso INTEGER NOT NULL, "
            + "activo INTEGER NOT NULL"
            + ");";

        String sqlActividades =
            "CREATE TABLE IF NOT EXISTS ACTIVIDADES ("
            + "id_actividad TEXT PRIMARY KEY, "
            + "nombre TEXT NOT NULL, "
            + "cupo_maximo INTEGER NOT NULL, "
            + "edad_minima INTEGER NOT NULL, "
            + "tipo TEXT NOT NULL, "
            + "profesor TEXT, "
            + "requiere_asistencia INTEGER, "
            + "fecha_evento TEXT, "
            + "lugar TEXT, "
            + "tipo_evento TEXT, "
            + "activo INTEGER NOT NULL"
            + ");";

        String sqlReservas =
            "CREATE TABLE IF NOT EXISTS RESERVAS ("
            + "id_reserva INTEGER PRIMARY KEY, "
            + "fecha TEXT NOT NULL, "
            + "estado TEXT NOT NULL, "
            + "rut_socio TEXT NOT NULL, "
            + "id_actividad TEXT NOT NULL, "
            + "FOREIGN KEY (rut_socio) REFERENCES SOCIOS(rut), "
            + "FOREIGN KEY (id_actividad) REFERENCES ACTIVIDADES(id_actividad)"
            + ");";

        try (Statement statement = getConnection().createStatement()) {

            statement.execute(sqlSocios);
            statement.execute(sqlActividades);
            statement.execute(sqlReservas);

        } catch (SQLException e) {

            throw new PersistenciaDatosException(
                "No fue posible crear las tablas de la base de datos.",
                e
            );
        }
    }
    
    /**
     * Guarda o actualiza una actividad en la tabla ACTIVIDADES.
     *
     * Los atributos específicos de cada subtipo se obtienen mediante
     * métodos polimórficos definidos en {@link Actividad}.
     *
     * @param actividad actividad que se desea persistir
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si ocurre un error durante
     *         el almacenamiento de la actividad
     */
    public void guardarActividad(Actividad actividad)
            throws ConexionBDException, PersistenciaDatosException {

        String sql =
            "INSERT INTO ACTIVIDADES "
            + "(id_actividad, nombre, cupo_maximo, edad_minima, tipo, "
            + "profesor, requiere_asistencia, fecha_evento, lugar, tipo_evento, activo) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
            + "ON CONFLICT(id_actividad) DO UPDATE SET "
            + "nombre = excluded.nombre, "
            + "cupo_maximo = excluded.cupo_maximo, "
            + "edad_minima = excluded.edad_minima, "
            + "tipo = excluded.tipo, "
            + "profesor = excluded.profesor, "
            + "requiere_asistencia = excluded.requiere_asistencia, "
            + "fecha_evento = excluded.fecha_evento, "
            + "lugar = excluded.lugar, "
            + "tipo_evento = excluded.tipo_evento, "
            + "activo = excluded.activo;";

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql)) {

            statement.setString(1, actividad.getIdActividad());
            statement.setString(2, actividad.getNombre());
            statement.setInt(3, actividad.getCupoMaximo());
            statement.setInt(4, actividad.getEdadMinima());
            statement.setString(5, actividad.getTipoActividad());

            statement.setString(6, actividad.getProfesor());

            if (actividad.getRequiereAsistencia() == null) {
                statement.setNull(7, Types.INTEGER);
            } else {
                statement.setInt(
                    7,
                    actividad.getRequiereAsistencia() ? 1 : 0
                );
            }

            if (actividad.getFecha() == null) {
                statement.setNull(8, Types.VARCHAR);
            } else {
                SimpleDateFormat formato =
                    new SimpleDateFormat("yyyy-MM-dd");

                statement.setString(
                    8,
                    formato.format(actividad.getFecha())
                );
            }

            statement.setString(9, actividad.getLugar());
            statement.setString(10, actividad.getTipoEvento());

            statement.setInt(
                11,
                actividad.getActivo() ? 1 : 0
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaDatosException(
                "No fue posible guardar la actividad en la base de datos.",
                e
            );
        }
    }
    
    /**
     * Carga todas las actividades almacenadas y reconstruye
     * el subtipo correspondiente según el campo de tipo persistido.
     *
     * Puede reconstruir instancias de {@link ClaseGrupal},
     * {@link EntrenamientoLibre} y {@link Evento}.
     *
     * @return lista con las actividades reconstruidas desde SQLite
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si los datos no pueden ser leídos,
     *         convertidos o contienen un tipo de actividad desconocido
     */
    public ArrayList<Actividad> cargarActividades()
            throws ConexionBDException, PersistenciaDatosException {

        String sql =
            "SELECT id_actividad, nombre, cupo_maximo, edad_minima, tipo, "
            + "profesor, requiere_asistencia, fecha_evento, lugar, "
            + "tipo_evento, activo "
            + "FROM ACTIVIDADES;";

        ArrayList<Actividad> actividades = new ArrayList<>();

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {

                String tipo = resultado.getString("tipo");

                Actividad actividad;

                switch (tipo) {

                    case "CLASE_GRUPAL":
                        actividad = new ClaseGrupal(
                            resultado.getString("id_actividad"),
                            resultado.getString("nombre"),
                            resultado.getInt("cupo_maximo"),
                            resultado.getInt("edad_minima"),
                            resultado.getString("profesor")
                        );
                        break;

                    case "ENTRENAMIENTO_LIBRE":
                        actividad = new EntrenamientoLibre(
                            resultado.getString("id_actividad"),
                            resultado.getString("nombre"),
                            resultado.getInt("cupo_maximo"),
                            resultado.getInt("edad_minima"),
                            resultado.getInt("requiere_asistencia") == 1
                        );
                        break;

                    case "EVENTO":
                        String fechaTexto =
                            resultado.getString("fecha_evento");

                        if (fechaTexto == null) {
                            throw new PersistenciaDatosException(
                                "El evento almacenado no posee una fecha válida."
                            );
                        }

                        actividad = new Evento(
                            resultado.getString("id_actividad"),
                            resultado.getString("nombre"),
                            resultado.getInt("cupo_maximo"),
                            resultado.getInt("edad_minima"),
                            new SimpleDateFormat("yyyy-MM-dd").parse(fechaTexto),
                            resultado.getString("lugar"),
                            resultado.getString("tipo_evento")
                        );
                        break;

                    default:
                        throw new PersistenciaDatosException(
                            "Tipo de actividad desconocido: " + tipo
                        );
                }

                actividad.setActivo(
                    resultado.getInt("activo") == 1
                );

                actividades.add(actividad);
            }

            return actividades;

        } catch (SQLException | ParseException e) {
            throw new PersistenciaDatosException(
                "No fue posible cargar las actividades desde la base de datos.",
                e
            );
        }
    }
    
    /**
     * Guarda o actualiza una reserva en la tabla RESERVAS.
     *
     * @param reserva reserva que se desea persistir
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si la reserva contiene datos inválidos
     *         o se produce un error durante su almacenamiento
     */
    public void guardarReserva(Reserva reserva)
            throws ConexionBDException, PersistenciaDatosException {

        if (reserva.getFecha() == null || reserva.getEstado() == null) {
            throw new PersistenciaDatosException(
                "La reserva posee datos inválidos para ser guardada."
            );
        }

        String sql =
            "INSERT INTO RESERVAS "
            + "(id_reserva, fecha, estado, rut_socio, id_actividad) "
            + "VALUES (?, ?, ?, ?, ?) "
            + "ON CONFLICT(id_reserva) DO UPDATE SET "
            + "fecha = excluded.fecha, "
            + "estado = excluded.estado, "
            + "rut_socio = excluded.rut_socio, "
            + "id_actividad = excluded.id_actividad;";

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql)) {

            SimpleDateFormat formato =
                new SimpleDateFormat("yyyy-MM-dd");

            statement.setInt(
                1,
                reserva.getIdReserva()
            );

            statement.setString(
                2,
                formato.format(reserva.getFecha())
            );

            statement.setString(
                3,
                reserva.getEstado().name()
            );

            statement.setString(
                4,
                reserva.getRutSocio()
            );

            statement.setString(
                5,
                reserva.getIdActividadEnReserva()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaDatosException(
                "No fue posible guardar la reserva en la base de datos.",
                e
            );
        }
    }
    
    /**
     * Carga todas las reservas almacenadas en la base de datos.
     *
     * Los valores persistidos de fecha y estado se convierten nuevamente
     * a {@link java.util.Date} y {@link EstadoReserva}, respectivamente.
     *
     * @return lista de reservas reconstruidas desde la base de datos
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si los datos almacenados no pueden
     *         ser interpretados correctamente
     */
    public ArrayList<Reserva> cargarReservas()
            throws ConexionBDException, PersistenciaDatosException {

        String sql =
            "SELECT id_reserva, fecha, estado, rut_socio, id_actividad "
            + "FROM RESERVAS;";

        ArrayList<Reserva> reservas = new ArrayList<>();

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            SimpleDateFormat formato =
                new SimpleDateFormat("yyyy-MM-dd");

            while (resultado.next()) {

                Reserva reserva = new Reserva(
                    resultado.getInt("id_reserva"),
                    formato.parse(resultado.getString("fecha")),
                    EstadoReserva.valueOf(
                        resultado.getString("estado")
                    ),
                    resultado.getString("rut_socio"),
                    resultado.getString("id_actividad")
                );

                reservas.add(reserva);
            }

            return reservas;

        } catch (SQLException | ParseException | IllegalArgumentException e) {
            throw new PersistenciaDatosException(
                "No fue posible cargar las reservas desde la base de datos.",
                e
            );
        }
    }
    
    /**
     * Elimina todos los registros almacenados en la tabla RESERVAS.
     *
     * Esta operación se utiliza durante el guardado batch para volver
     * a insertar únicamente las reservas que existen actualmente
     * en las colecciones en memoria.
     *
     * @throws ConexionBDException si no es posible acceder a la base de datos
     * @throws PersistenciaDatosException si ocurre un error durante
     *         la eliminación de los registros
     */
    public void limpiarReservas()
            throws ConexionBDException, PersistenciaDatosException {

        String sql = "DELETE FROM RESERVAS;";

        try (PreparedStatement statement =
                getConnection().prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaDatosException(
                "No fue posible limpiar las reservas de la base de datos.",
                e
            );
        }
    }
    
}