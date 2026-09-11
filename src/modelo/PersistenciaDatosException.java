package modelo;

/**
 * Excepción personalizada para manejar errores surgidos durante el guardado 
 * o lectura de datos en el medio de almacenamiento persistente.
 * 
 * @author Peñaloza Elvis
 * @version 1.0
 */
public class PersistenciaDatosException extends Exception{
	public PersistenciaDatosException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}