package controlador;


import java.sql.Statement;
import modelo.PersistenciaDatosException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import modelo.ConexionBDException;
import java.sql.PreparedStatement;
import modelo.Socio;
import java.sql.ResultSet;
import java.util.ArrayList;



public class DBConnection {

    private String url;
    private Connection connection;

    public DBConnection() {
        this.url = "jdbc:sqlite:club_deportivo.db";
    }

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
}
