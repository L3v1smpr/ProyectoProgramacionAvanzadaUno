package vista;
import java.util.Scanner;
import java.util.InputMismatchException;
import modelo.CupoMaximoException;
import controlador.SistemaClub;


public class MenuConsola {
	private Scanner scanner;
	private SistemaClub controlador;
	
	
	public MenuConsola() { 
		this.scanner = new Scanner(System.in);
		this.controlador = new SistemaClub(); 
	}
	
	public void iniciarConsola() {
		int opcion = -1;
		
		do {
			System.out.println("\n--- SISTEMA CLUB ---");
            System.out.println("1. Gestionar Actividades");
            System.out.println("2. Gestionar Socios");
            System.out.println("3. Gestionar Reservas");
            System.out.println("4. Pago de facturación");
            System.out.println("5. Generar cobro nuevo mes");
            System.out.println("6. Exportar Socios");
            System.out.println("7. Guardar últimas modificaciones");
            System.out.println("8. Salir del programa");
            System.out.print("Ingrese una opción: ");
		
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcion) {
                    case 1: submenuActividades(); break;
                    case 2: submenuSocios(); break;
                    case 3: submenuReservas(); break;
                    case 4: submenuPagarFacturacion(); break;
                    case 5: ejecutarCobroMensual(); break;
                    case 6: System.out.println("Exportando datos de socios (En construcción)..."); break;
                    case 7: System.out.println("Guardando en base de datos (En construcción)..."); break;
                    case 8: System.out.println("Cerrando el programa..."); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                scanner.nextLine(); 
            }
		} while (opcion != 8);		
	}
	
	private void submenuActividades() {
		System.out.println("\n--- GESTIONAR ACTIVIDADES ---");
        System.out.println("1. Agregar Actividad");
        System.out.println("2. Modificar Actividad");
        System.out.println("3. Listar Actividades");
        System.out.println("4. Buscar Actividad");
        System.out.println("5. Eliminar Actividad");
        System.out.print("Opción: ");
        int opt = scanner.nextInt();
        scanner.nextLine();
       
        switch (opt) {
        	case 1: //Agregar Actividad
        		System.out.println("--- NUEVA ACTIVIDAD ---");
            	System.out.println("1. Clase Grupal (Requiere profesor)");
            	System.out.println("2. Entrenamiento Libre (Define asistencia)");
            	System.out.print("Seleccione el tipo: ");
            	int tipoAct = scanner.nextInt();
            	scanner.nextLine();
            	
            	System.out.print("ID Actividad: ");
                String id = scanner.nextLine();
                System.out.print("Nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("Cupo Máximo: ");
                int cupo = scanner.nextInt();
                System.out.print("Edad Mínima: ");
                int edadMin = scanner.nextInt();
                scanner.nextLine();
                
                if (tipoAct == 1) {
                    System.out.print("Nombre del Profesor: ");
                    String profesor = scanner.nextLine();
                    // Llama a la sobrecarga que recibe un String al final
                    controlador.agregarActividad(id, nombre, cupo, edadMin, profesor);
                    System.out.println("Clase grupal agregada exitosamente.");
                    
                } else if (tipoAct == 2) {
                    System.out.print("¿Requiere asistencia obligatoria? (true/false): ");
                    boolean requiereAsistencia = scanner.nextBoolean();
                    scanner.nextLine();
                    // Llama a la sobrecarga que recibe un boolean al final
                    controlador.agregarActividad(id, nombre, cupo, edadMin, requiereAsistencia);
                    System.out.println("Entrenamiento libre agregado exitosamente.");
                    
                } else {
                    System.out.println("Error: Tipo de actividad no válido.");
                }
                break;
               
        	case 2: //Modificar Actividad
        		System.out.println("--- MODIFICAR ACTIVIDAD ---");
        	    System.out.print("Ingrese el ID de la actividad a modificar: ");
        	    String idMod = scanner.nextLine();
        	    
        	    // 1. Buscamos la actividad para mantener sus datos previos[cite: 7]
        	    modelo.Actividad actActual = controlador.buscarActividad(idMod); 
        	    
        	    if (actActual == null) {
        	        System.out.println("Error: No se encontró una actividad con ese ID.");
        	    } else {
        	        System.out.println("¿Qué atributo desea modificar?");
        	        System.out.println("1. Nombre (Actual: " + actActual.getNombre() + ")");
        	        System.out.println("2. Cupo Máximo (Actual: " + actActual.getCupoMaximo() + ")");
        	        System.out.println("3. Edad Mínima (Actual: " + actActual.getEdadMinima() + ")");
        	        System.out.print("Opción: ");
        	        int opcMod = scanner.nextInt();
        	        scanner.nextLine();

        	        // 2. Variables temporales que inician con los valores actuales
        	        String nombreFinal = actActual.getNombre();
        	        int cupoFinal = actActual.getCupoMaximo();
        	        int edadFinal = actActual.getEdadMinima();

        	        // 3. Sobrescribimos solo la variable que eligió el usuario
        	        switch(opcMod) {
        	            case 1:
        	                System.out.print("Ingrese el nuevo nombre: ");
        	                nombreFinal = scanner.nextLine();
        	                break;
        	            case 2:
        	                System.out.print("Ingrese el nuevo cupo máximo: ");
        	                cupoFinal = scanner.nextInt();
        	                scanner.nextLine();
        	                break;
        	            case 3:
        	                System.out.print("Ingrese la nueva edad mínima: ");
        	                edadFinal = scanner.nextInt();
        	                scanner.nextLine();
        	                break;
        	            default:
        	                System.out.println("Opción no válida. Se cancela la modificación.");
        	                opcMod = -1; // Bandera para evitar que se ejecute el guardado
        	        }

        	        // 4. Enviamos todos los parámetros juntos a tu controlador[cite: 7]
        	        if (opcMod != -1) {
        	            try {
        	                controlador.modificarActividad(idMod, nombreFinal, cupoFinal, edadFinal); 
        	                System.out.println("Actividad modificada correctamente.");
        	            } catch (CupoMaximoException e) {
        	                System.out.println(e.getMessage()); // Imprime el error si el cupo es <= 0[cite: 6, 7]
        	            }
        	        }
        	    }
        	    break;
        	
        	case 3:
        		System.out.println("--- LISTADO DE ACTIVIDADES ---");
        	    
        	    // Obtenemos la lista ya filtrada (solo activas) desde el controlador
        	    java.util.ArrayList<modelo.Actividad> listaAct = controlador.obtenerActividades();
        	    
        	    if (listaAct.isEmpty()) {
        	        System.out.println("No hay actividades registradas o activas en el sistema.");
        	    } else {
        	        // Recorremos la lista e imprimimos los atributos base
        	        for (modelo.Actividad a : listaAct) {
        	            System.out.println("ID: " + a.getIdActividad() + 
        	                               " | Nombre: " + a.getNombre() + 
        	                               " | Cupos: " + a.getCupoMaximo() + 
        	                               " | Edad Mínima: " + a.getEdadMinima());
        	        }
        	    }
        	    System.out.println("------------------------------");
        	    break;
        }
	}

	private void submenuSocios() {
        System.out.println("\n--- GESTIONAR SOCIOS ---");
        System.out.println("1. Agregar Socio");
        System.out.println("2. Modificar Socio");
        System.out.println("3. Listar Socios");
        System.out.println("4. Listar Socios con Deuda");
        System.out.println("5. Listar Reservas de Socio");
        System.out.println("6. Eliminar Socio");
        System.out.print("Opción: ");
        int opt = scanner.nextInt();
        scanner.nextLine();
        // Lógica a implementar...
	}
	
	private void submenuReservas() {
        System.out.println("\n--- GESTIONAR RESERVAS ---");
        System.out.println("1. Agendar Reserva");
        System.out.println("2. Modificar Reserva");
        System.out.println("3. Listar Reservas");
        System.out.println("4. Cancelar Reserva");
        System.out.print("Opción: ");
        int opt = scanner.nextInt();
        scanner.nextLine();
        // Lógica a implementar...
    }

	private void submenuPagarFacturacion() {
        System.out.println("Ejecutando pago..."); 
    }
	
	private void ejecutarCobroMensual() {
        System.out.println("Ejecutando cobro...");
    }
	
	
	
}






