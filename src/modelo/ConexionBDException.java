package modelo;

public class ConexionBDException extends Exception{
	public ConexionBDException(String mensaje) {
        super(mensaje);
    }

    public ConexionBDException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
