package vista;
import java.util.Scanner;
import java.util.InputMismatchException;
import modelo.CupoMaximoException;
import controlador.SistemaClub;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import modelo.Socio;

public class MenuConsola {
	private Scanner scanner;
	private SistemaClub controlador;
	
	
	// Recibe el controlador que le envía el Main.java
	public MenuConsola(SistemaClub controlador) { 
		this.scanner = new Scanner(System.in);
		this.controlador = controlador; 
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
				System.out.println("3. Registrar Evento");
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

				    if (controlador.agregarActividad(id, nombre, cupo, edadMin, profesor)) {
				        System.out.println("Clase grupal agregada exitosamente.");
				    } else {
				        System.out.println("Error: Ya existe una actividad registrada con ese ID.");
				    }

				} else if (tipoAct == 2) {

				    System.out.print("¿Requiere asistencia obligatoria? (true/false): ");
				    boolean requiereAsistencia = scanner.nextBoolean();
				    scanner.nextLine();

				    if (controlador.agregarActividad(id, nombre, cupo, edadMin, requiereAsistencia)) {
				        System.out.println("Entrenamiento libre agregado exitosamente.");
				    } else {
				        System.out.println("Error: Ya existe una actividad registrada con ese ID.");
				    }

				} else if (tipoAct == 3) {

				    System.out.print("Ingrese la fecha (dd-MM-yyyy): ");
				    String fechaStr = scanner.nextLine();

				    System.out.print("Ingrese la ubicación: ");
				    String ubicacion = scanner.nextLine();

				    System.out.print("Ingrese el tipo del evento: ");
				    String tipoEvento = scanner.nextLine();

				    try {
				        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

				        sdf.setLenient(false);
				        java.util.Date fecha = sdf.parse(fechaStr);

				        if (controlador.agregarActividad(id, nombre, cupo, edadMin, fecha, ubicacion, tipoEvento)) {
				            System.out.println("Evento agregado correctamente.");

				        } else {
				            System.out.println("Error: Ya existe una actividad registrada con ese ID.");
				        }

				    } catch (java.text.ParseException e) {
				        System.out.println(
				            "Error: La fecha ingresada no es válida. Utilice el formato dd-MM-yyyy."
				        );
				    }

				} else {
				    System.out.println("Error: Tipo de actividad no válido.");
				}
				
			case 2: //Modificar Actividad
				System.out.println("--- MODIFICAR ACTIVIDAD ---");
				System.out.print("Ingrese el ID de la actividad a modificar: ");
				String idMod = scanner.nextLine();
				
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

					String nombreFinal = actActual.getNombre();
					int cupoFinal = actActual.getCupoMaximo();
					int edadFinal = actActual.getEdadMinima();

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
							opcMod = -1;
					}

					if (opcMod != -1) {
						try {
							controlador.modificarActividad(idMod, nombreFinal, cupoFinal, edadFinal); 
							System.out.println("Actividad modificada correctamente.");
						} catch (CupoMaximoException e) {
							System.out.println(e.getMessage()); 
						}
					}
				}
				break;
			
			case 3: // Listar Actividades
				System.out.println("--- LISTADO DE ACTIVIDADES ---");
				java.util.ArrayList<modelo.Actividad> listaAct = controlador.obtenerActividades();
				
				if (listaAct.isEmpty()) {
					System.out.println("No hay actividades registradas o activas en el sistema.");
				} else {
					for (modelo.Actividad a : listaAct) {
						System.out.println("ID: " + a.getIdActividad() + 
										   " | Nombre: " + a.getNombre() + 
										   " | Cupos: " + a.getCupoMaximo() + 
										   " | Edad Mínima: " + a.getEdadMinima());
					}
				}
				System.out.println("------------------------------");
				break;
				
			case 4: // Buscar Actividad
				System.out.println("--- BUSCAR ACTIVIDAD ---");
				System.out.println("1. Buscar por ID de Actividad");
				System.out.println("2. Buscar por ID de Reserva asociada");
				System.out.print("Seleccione una opción: ");
				int tipoBusqueda = scanner.nextInt();
				scanner.nextLine();

				modelo.Actividad actEncontrada = null;

				if (tipoBusqueda == 1) {
					System.out.print("Ingrese el ID de la Actividad: ");
					String idBuscado = scanner.nextLine();
					actEncontrada = controlador.buscarActividad(idBuscado);
				} else if (tipoBusqueda == 2) {
					System.out.print("Ingrese el ID de la Reserva: ");
					int idReservaBuscado = scanner.nextInt();
					scanner.nextLine();
					actEncontrada = controlador.buscarActividad(idReservaBuscado);
				} else {
					System.out.println("Opción inválida.");
				}

				if (actEncontrada != null) {
					System.out.println("Actividad encontrada: " + actEncontrada.getNombre() + 
									   " | Cupos: " + actEncontrada.getCupoMaximo() + 
									   " | Edad Mínima: " + actEncontrada.getEdadMinima());
				} else if (tipoBusqueda == 1 || tipoBusqueda == 2) {
					System.out.println("No se encontró ninguna actividad con los datos proporcionados.");
				}
				break;

			case 5: // Eliminar Actividad
				System.out.println("--- ELIMINAR ACTIVIDAD ---");
				System.out.print("Ingrese el ID de la actividad a eliminar (desactivar): ");
				String idEliminar = scanner.nextLine();

				if (controlador.desactivarActividad(idEliminar)) {
					System.out.println("Actividad eliminada exitosamente del catálogo activo.");
				} else {
					System.out.println("Error: No se encontró una actividad con ese ID.");
				}
				break;
				
			default:
				System.out.println("Opción no válida.");
		}
	}

	private void submenuSocios() {
        System.out.println("\n--- GESTIONAR SOCIOS ---");
        System.out.println("1. Agregar Socio");
        System.out.println("2. Modificar Socio");
        System.out.println("3. Listar Socios");
        System.out.println("4. Listar Socios con Deuda");
        System.out.println("5. Listar Reservas de Socio");
        System.out.println("6. Buscar Socio");
        System.out.println("7. Eliminar Socio");
        System.out.print("Opción: ");
        int opt = scanner.nextInt();
        scanner.nextLine();

        switch (opt) {
            case 1: // Agregar Socio
                System.out.println("--- NUEVO SOCIO ---");
                System.out.print("RUT: ");
                String rut = scanner.nextLine();
                System.out.print("Nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("Edad: ");
                int edad = scanner.nextInt();
                scanner.nextLine();
                
                if (controlador.agregarSocio(rut, nombre, edad)) {
                    System.out.println("Socio agregado exitosamente al sistema.");
                } else {
                    System.out.println("Error: Ya existe un socio registrado con ese RUT.");
                }
                break;

            case 2: // Modificar Socio
                System.out.println("--- MODIFICAR SOCIO ---");
                System.out.print("Ingrese el RUT del socio a modificar: ");
                String rutMod = scanner.nextLine();

                modelo.Socio socioActual = controlador.buscarSocio(rutMod);

                if (socioActual == null) {
                    System.out.println("Error: No se encontró un socio con ese RUT.");
                } else {
                    System.out.println("¿Qué atributo desea modificar?");
                    System.out.println("1. Nombre (Actual: " + socioActual.getNombre() + ")");
                    System.out.println("2. Edad (Actual: " + socioActual.getEdad() + ")");
                    System.out.println("3. Deuda (Actual: $" + socioActual.getDeuda() + ")");
                    System.out.println("4. Estado Moroso (Actual: " + (socioActual.getEsMoroso() ? "Sí" : "No") + ")");
                    System.out.print("Opción: ");
                    int opcModSocio = scanner.nextInt();
                    scanner.nextLine();

                    String nombreFinal = socioActual.getNombre();
                    int edadFinal = socioActual.getEdad();
                    int deudaFinal = socioActual.getDeuda();
                    boolean morosoFinal = socioActual.getEsMoroso();

                    switch (opcModSocio) {
                        case 1:
                            System.out.print("Ingrese nuevo nombre: ");
                            nombreFinal = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("Ingrese nueva edad: ");
                            edadFinal = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("Ingrese nueva deuda: ");
                            deudaFinal = scanner.nextInt();
                            scanner.nextLine();
                            break;
                        case 4:
                            System.out.print("¿Es moroso? (true/false): ");
                            morosoFinal = scanner.nextBoolean();
                            scanner.nextLine();
                            break;
                        default:
                            System.out.println("Opción no válida. Cancelando modificación.");
                            opcModSocio = -1;
                    }

                    if (opcModSocio != -1) {
                        controlador.modificarSocio(rutMod, nombreFinal, edadFinal, deudaFinal, morosoFinal);
                        System.out.println("Socio modificado correctamente.");
                    }
                }
                break;

            case 3: // Listar Socios
                System.out.println("--- LISTADO DE SOCIOS ---");
                java.util.ArrayList<modelo.Socio> listaS = controlador.obtenerListaSocios();
                
                if (listaS.isEmpty()) {
                    System.out.println("No hay socios registrados o activos en el sistema.");
                } else {
                    for (modelo.Socio s : listaS) {
                        System.out.println("RUT: " + s.getRut() + " | Nombre: " + s.getNombre() +
                                           " | Edad: " + s.getEdad() + " | Deuda: $" + s.getDeuda() +
                                           " | Moroso: " + (s.getEsMoroso() ? "Sí" : "No"));
                    }
                }
                break;

            case 4: // Listar Socios con Deuda
                System.out.println("--- SOCIOS CON DEUDA ---");
                java.util.ArrayList<modelo.Socio> listaDeudores = controlador.obtenerListaSociosDeudores();
                
                if (listaDeudores.isEmpty()) {
                    System.out.println("No hay socios morosos en el sistema actualmente.");
                } else {
                    for (modelo.Socio s : listaDeudores) {
                        System.out.println("RUT: " + s.getRut() + " | Nombre: " + s.getNombre() + " | Deuda: $" + s.getDeuda());
                    }
                }
                break;

            case 5: // Listar Reservas de Socio
                System.out.println("--- RESERVAS DE SOCIO ---");
                System.out.print("Ingrese el RUT del socio: ");
                String rutRes = scanner.nextLine();
                
                modelo.Socio socioRes = controlador.buscarSocio(rutRes);

                if (socioRes == null || !socioRes.getActivo()) {
                    System.out.println("Error: Socio no encontrado o inactivo.");
                } else {
                    java.util.ArrayList<modelo.Reserva> reservas = socioRes.getListaReservas();
                    if (reservas == null || reservas.isEmpty()) {
                        System.out.println("El socio no tiene reservas registradas.");
                    } else {
                        for (modelo.Reserva r : reservas) {
                            System.out.println("ID Reserva: " + r.getIdReserva() +
                                               " | ID Actividad: " + r.getIdActividadEnReserva() +
                                               " | Estado: " + r.getEstado() +
                                               " | Fecha: " + r.getFecha());
                        }
                    }
                }
                break;

            case 6:
                System.out.println("--- BUSCAR SOCIO ---");
                System.out.print("Ingrese RUT: ");
                String rutBuscar = scanner.nextLine();

                Socio socioEncontrado = controlador.buscarSocio(rutBuscar);

                if (socioEncontrado == null) {
                    System.out.println("No se encontró un socio con ese RUT.");
                } else {
                    System.out.println(
                        "RUT: " + socioEncontrado.getRut()
                        + " | Nombre: " + socioEncontrado.getNombre()
                        + " | Edad: " + socioEncontrado.getEdad()
                        + " | Deuda: $" + socioEncontrado.getDeuda()
                        + " | Moroso: "
                        + (socioEncontrado.getEsMoroso() ? "Sí" : "No")
                    );
                }
                break;
                
                
            case 7: // Eliminar Socio
                System.out.println("--- ELIMINAR SOCIO ---");
                System.out.print("Ingrese el RUT del socio a eliminar (desactivar): ");
                String rutElim = scanner.nextLine();

                if (controlador.desactivarSocio(rutElim)) {
                    System.out.println("Socio eliminado exitosamente del catálogo activo.");
                } else {
                    System.out.println("Error: No se encontró un socio con ese RUT.");
                }
                break;

            default:
                System.out.println("Opción no válida.");
        }
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

        // Herramienta para manejar las fechas de tipo Date
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

        switch (opt) {
            case 1:
                System.out.println("--- AGENDAR RESERVA ---");
                System.out.print("RUT del socio: ");
                String rut = scanner.nextLine();
                System.out.print("ID de la actividad: ");
                String idAct = scanner.nextLine();
                System.out.print("Fecha (DD-MM-YYYY): ");
                String fechaStr = scanner.nextLine();
                
                try {
                    java.util.Date fecha = sdf.parse(fechaStr);
                    
                    if (controlador.agendarReserva(rut, idAct, fecha)) {
                        System.out.println("Reserva agendada exitosamente.");
                    } else {
                        System.out.println("Error: El socio o la actividad no existen.");
                    }
                } catch (java.text.ParseException e) {
                    System.out.println("Error: Formato de fecha incorrecto. Utilice DD-MM-YYYY.");
                } catch (modelo.MorosidadException | modelo.CupoMaximoException e) {
                    System.out.println("Error al agendar: " + e.getMessage());
                }
                break;
                
            case 2:
                System.out.println("--- MODIFICAR RESERVA ---");
                System.out.print("Ingrese el RUT del titular de la reserva: ");
                String rutMod = scanner.nextLine();
                System.out.print("Ingrese el ID de la reserva a modificar: ");
                int idResMod = scanner.nextInt();
                scanner.nextLine();
                
                modelo.Socio socio = controlador.buscarSocio(rutMod);
                if (socio == null) {
                    System.out.println("Error: Socio no encontrado.");
                    break;
                }
                
                modelo.Reserva resActual = socio.buscarReserva(idResMod);
                if (resActual == null) {
                    System.out.println("Error: Reserva no encontrada para este socio.");
                    break;
                }
                
                System.out.println("¿Qué desea modificar?");
                System.out.println("1. Fecha (Actual: " + sdf.format(resActual.getFecha()) + ")");
                System.out.println("2. Estado (Actual: " + resActual.getEstado() + ")");
                System.out.println("3. ID Actividad (Actual: " + resActual.getIdActividadEnReserva() + ")");
                System.out.print("Opción: ");
                int opc = scanner.nextInt();
                scanner.nextLine();
                
                java.util.Date nuevaFecha = resActual.getFecha();
                modelo.EstadoReserva nuevoEstado = resActual.getEstado();
                String nuevaActividad = resActual.getIdActividadEnReserva();
                
                if (opc == 1) {
                    System.out.print("Nueva fecha (DD-MM-YYYY): ");
                    try {
                        nuevaFecha = sdf.parse(scanner.nextLine());
                    } catch (java.text.ParseException e) {
                        System.out.println("Formato incorrecto. Se cancela la modificación.");
                        break;
                    }
                } else if (opc == 2) {
                    System.out.println("Seleccione nuevo estado: 1. PENDIENTE 2. COMPLETADA 3. CANCELADA");
                    int est = scanner.nextInt();
                    scanner.nextLine();
                    if (est == 1) nuevoEstado = modelo.EstadoReserva.PENDIENTE;
                    else if (est == 2) nuevoEstado = modelo.EstadoReserva.COMPLETADA;
                    else if (est == 3) nuevoEstado = modelo.EstadoReserva.CANCELADA;
                } else if (opc == 3) {
                    System.out.print("Nuevo ID de Actividad: ");
                    nuevaActividad = scanner.nextLine();
                } else {
                    System.out.println("Opción inválida.");
                    break;
                }
                
                try {
                    if (controlador.modificarReserva(idResMod, nuevaFecha, nuevoEstado, rutMod, nuevaActividad)) {
                        System.out.println("Reserva modificada correctamente.");
                    }
                } catch (modelo.MorosidadException e) {
                    System.out.println("Error de morosidad: " + e.getMessage());
                }
                break;

            case 3:
                System.out.println("--- LISTADO GLOBAL DE RESERVAS ---");
                java.util.ArrayList<modelo.Reserva> listaGlobal = controlador.listarReservasGlobales();
                
                if (listaGlobal.isEmpty()) {
                    System.out.println("No hay reservas registradas en el sistema.");
                } else {
                    for (modelo.Reserva r : listaGlobal) {
                        System.out.println("ID: " + r.getIdReserva() + 
                                           " | RUT: " + r.getRutSocio() + 
                                           " | Actividad: " + r.getIdActividadEnReserva() + 
                                           " | Fecha: " + (r.getFecha() != null ? sdf.format(r.getFecha()) : "N/A") + 
                                           " | Estado: " + r.getEstado());
                    }
                }
                break;
                
            case 4:
                System.out.println("--- CANCELAR RESERVA ---");
                System.out.print("Ingrese el ID de la reserva a eliminar: ");
                int idCanc = scanner.nextInt();
                scanner.nextLine();
                
                if (controlador.eliminarReserva(idCanc)) {
                    System.out.println("Reserva eliminada exitosamente del sistema.");
                } else {
                    System.out.println("Error: No se encontró una reserva con ese ID.");
                }
                break;
                
            default:
                System.out.println("Opción no válida.");
        }
    }
	
	private void submenuPagarFacturacion() {
        System.out.println("\n--- PAGO DE FACTURACIÓN ---");
        System.out.print("Ingrese el RUT del socio: ");
        String rut = scanner.nextLine();

        // 1. Buscamos al socio primero para verificar su estado de morosidad
        modelo.Socio socioActual = controlador.buscarSocio(rut);

        if (socioActual == null) {
            System.out.println("Error: No se encontró al socio en el sistema.");
            return; // Cortamos la ejecución aquí
        }

        // 2. Bloqueo de cobro doble si el socio está al día
        if (!socioActual.getEsMoroso() || socioActual.getDeuda() <= 0) {
            System.out.println("Aviso: El socio " + socioActual.getNombre() + " está al día y no presenta deuda.");
            return; // Cortamos la ejecución aquí
        }

        // 3. UX: Mostramos el monto exacto antes de cobrar
        System.out.println("Deuda actual de " + socioActual.getNombre() + ": $" + socioActual.getDeuda());
        System.out.println("¿Desea pagar el total de la deuda o hacer un abono parcial?");
        System.out.println("1. Pagar total");
        System.out.println("2. Abono parcial");
        System.out.print("Opción: ");
        int tipoPago = scanner.nextInt();
        scanner.nextLine();

        if (tipoPago == 1) {
            if (controlador.pagarFacturacion(rut)) {
                System.out.println("Deuda saldada en su totalidad exitosamente. Morosidad removida.");
            }
        } else if (tipoPago == 2) {
            System.out.print("Ingrese el monto a abonar: ");
            int abono = scanner.nextInt();
            scanner.nextLine();

            // Evitar abonos mayores a la deuda que dejen saldos negativos
            if (abono > socioActual.getDeuda()) {
                System.out.println("El abono supera la deuda. Se recomienda utilizar el 'Pago total' (Opción 1).");
            } else if (controlador.pagarFacturacion(rut, abono)) {
                System.out.println("Abono registrado correctamente.");
            }
        } else {
            System.out.println("Opción de pago inválida. Cancelando operación.");
        }
    }

    private void ejecutarCobroMensual() {
        System.out.println("\n--- GENERAR COBRO MENSUAL ---");
        
        // Ejecuta el cargo fijo de $10.000 a todos los socios activos
        if (controlador.generarCobroMensual()) {
            System.out.println("Cobro mensual de $10.000 generado y asignado correctamente a todos los socios activos.");
            System.out.println("Los socios han sido marcados con estado de morosidad automáticamente.");
        } else {
            System.out.println("Aviso: No hay socios registrados en el sistema para realizar el cobro.");
        }
    }
	
	
	
}