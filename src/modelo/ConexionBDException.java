package modelo;

/**
 * Excepción personalizada para manejar errores relacionados con la conexión
 * a la base de datos (por ejemplo, fallos de conexión o credenciales).
 * 
 * @author Peñaloza Elvis
 * @version 1.0
 */
public class ConexionBDException extends Exception{
	public ConexionBDException(String mensaje) {
        super(mensaje);
    }

    public ConexionBDException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}