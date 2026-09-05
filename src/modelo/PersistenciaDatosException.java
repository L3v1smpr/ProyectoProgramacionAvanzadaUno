package modelo;

public class PersistenciaDatosException extends Exception{
	public PersistenciaDatosException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
