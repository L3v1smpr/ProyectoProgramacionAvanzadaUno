package controlador;


import java.sql.Statement;
import modelo.PersistenciaDatosException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import modelo.ConexionBDException;
import modelo.PersistenciaDatosException;



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
            
            System.out.println("Conexión a SQLite establecida correctamente.");
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
