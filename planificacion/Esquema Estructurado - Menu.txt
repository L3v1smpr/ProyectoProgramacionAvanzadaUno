1. Gestionar Actividades :
	1.1 Agregar Actividad [idActividad, nombre, cupoMaximo, edadMinima...] (Preguntar 1 o 2 - Crear Clase Grupal o Entrenamiento Libre)
	1.2 Modificar Actividad [idActividad]
	1.3 Listar Actividades [TODAS] [SIA-7]
	1.4 Buscar Actividad [idActividad/idReserva/Rut -> Sobrecarga de métodos (SIA 5[1])]
	1.5 Listar Reservas de la Actividad [idActividad] [SIA-7]
	1.6 Eliminar Actividad [idActividad]

2. Gestionar Socios :
	2.1 Agregar Socio [Atributos: RUT, nombre, edad, morosidad, deuda (Preguntar qué modificar)]
	2.2 Modificar Socio [RUT | Modificaciones: edad, morosidad, deuda (Preguntar qué modificar)]
	2.3 Listar Socios [SIA-7]
	2.4 Listar Socios con Deuda [SIA-9]
	2.5 Listar Reservas Socio [No deudor] [SIA-7]
	2.6 Eliminar Socio

3. Gestionar Reservas :
	3.1 Agendar Reserva [Verificar que socio exista, si existe verificar que no tenga deuda y que la actividad tenga cupo [SIA]]
	3.2 Modificar Reserva [idReserva - Verificar morosidad del socio | Modificaciones: fecha, estado]
	3.3 Listar Reservas [Orden Cronológico - Más antigua a más recientes | Incorporar estado: completada, pendiente] [SIA-7]
	3.4 Cancelar Reserva [idReserva - Verificar además que su estado sea pendiente]

4. Pago de facturación : Ingresar RUT Socio -> Si se paga completo eliminar morosidad - Con posibilidad de abonar (int abono)
5. Generar cobro nuevo mes [Genera una facturación en todos los socios registrados]
6. Exportar Socios [Reservas - Deudas - Rut - Nombre - Edad] -> Directamente de base de datos.
7. Guardar últimas modificaciones.
8. Salir del programa

		
