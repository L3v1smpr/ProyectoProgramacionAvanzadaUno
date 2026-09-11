package modelo;

/**
 * Excepción personalizada lanzada cuando se intenta inscribir o reservar
 * en una actividad que ya ha alcanzado su límite máximo de participantes.
 * 
 * @author Reyes Alex
 * @version 1.0
 */
public class CupoMaximoException extends Exception {

    public CupoMaximoException(String mensaje) {
        super(mensaje);
    }
}