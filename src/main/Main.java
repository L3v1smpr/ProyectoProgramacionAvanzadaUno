package main;

import vista.MenuConsola;
import vista.MenuVentana;
import controlador.SistemaClub;
import modelo.ConexionBDException;
import modelo.PersistenciaDatosException;

import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

//Clase encargada de darle la decision al usuario para ejecutar consola o ventana (GUI)
public class Main {

    public static void main(String[] args) {
        
        SistemaClub controlador = new SistemaClub();
        
        try {

            controlador.cargarDatosBatch();

            // Si la base de datos esta vacia, carga los datos iniciales
            // requeridos para demostrar las funcionalidades del sistema.
            if (controlador.cargarDatosIniciales()) {

                controlador.guardarDatosBatch();

                System.out.println("Datos iniciales del sistema cargados correctamente.");
            }

        } catch (ConexionBDException | PersistenciaDatosException e) {

            System.out.println("Error al cargar los datos del sistema: " + e.getMessage());

            return;
        }
        
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        
        boolean opcionValida = false;
        String lineaLeida = "";
        
        System.out.println("=== BIENVENIDO AL SISTEMA DEL CLUB ===");
        
        while (!opcionValida) {
            System.out.println("Que interfaz desea utilizar?");
            System.out.println("1. Modo Consola (Texto)");
            System.out.println("2. Modo Ventana (Grafico)");
            System.out.println("Ingrese una opcion:");
            
            try {
                lineaLeida = lector.readLine();
                
                if (lineaLeida != null && (lineaLeida.equals("1") || lineaLeida.equals("2"))) {
                    opcionValida = true;
                } else {
                    System.out.println("Opcion invalida. Seleccione 1 o 2.");
                }
            } catch (IOException error) {
                System.out.println("Error: " + error.getMessage());
            }
        }
        
        if (lineaLeida.equals("1")) {
            System.out.println("Ejecutando modo consola...");
            MenuConsola menuConsola = new MenuConsola(controlador);
            menuConsola.iniciarConsola();
            
        } else {
            System.out.println("Ejecutando modo ventana...");
            //Llamada segura a la interfaz Swing dentro del Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                MenuVentana menuVentana = new MenuVentana(controlador);
                menuVentana.iniciarVentana();
            });
        }
    }
}