package main;
import vista.MenuConsola;
import vista.MenuVentana;
import controlador.SistemaClub;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;



public class Main {

	public static void main(String[] args) {
		
		SistemaClub controlador = new SistemaClub();
		BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
		
		boolean opcionValida = false;
		String lineaLeida = "";
		
		System.out.println("=== BIENVENIDO AL SISTEMA DEL CLUB ===");
		
		while (!opcionValida) {

			System.out.println("¿Qué interfaz desea utilizar?");
			System.out.println("1. Modo Consola (Texto)");
			System.out.println("2. Modo Ventana (Gráfico)");
			System.out.println("Ingrese una opción:");
			
			try {
				lineaLeida = lector.readLine();
				
				if (lineaLeida != null && (lineaLeida.equals("1") || lineaLeida.equals("2"))) {
					opcionValida = true;
				} else {
					System.out.println("Opción inválida. Seleccione 1 o 2.");
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
			MenuVentana menuVentana = new MenuVentana(controlador);
			menuVentana.iniciarVentana();
		}
	}
	
}
