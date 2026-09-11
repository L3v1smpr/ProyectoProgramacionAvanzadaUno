package modelo;

/**
 * Excepción personalizada lanzada cuando un socio intenta realizar una acción 
 * (como reservar) pero se encuentra en estado de morosidad.
 * 
 * @author Reyes Alex
 * @version 1.0
 */
public class MorosidadException extends Exception {

    public MorosidadException(String mensaje) {
        super(mensaje);
    }
}