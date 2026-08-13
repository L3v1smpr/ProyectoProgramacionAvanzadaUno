```mermaid

classDiagram

&#x20;   %% ==========================================

&#x20;   %% CAPA VISTA

&#x20;   %% ==========================================

&#x20;   class Main {

&#x20;       +main(args: String\[]) void

&#x20;   }

&#x20;   

&#x20;   class MenuConsola {

&#x20;       -controlador : SistemaClub

&#x20;       +iniciarMenu() void

&#x20;   }

&#x20;   

&#x20;   class MenuVentana {

&#x20;       -controlador : SistemaClub

&#x20;       +iniciarVentana() void

&#x20;   }



&#x20;   %% ==========================================

&#x20;   %% CAPA CONTROLADOR

&#x20;   %% ==========================================

&#x20;   class SistemaClub {

&#x20;       -mapaSocios : HashMap\~String, Socio\~

&#x20;       -listaActividades : ArrayList\~Actividad\~

&#x20;       -db : DBConnection

&#x20;       +agregarSocio(rut: String, nombre: String) boolean

&#x20;       +buscarActividad(idActividad: String) Actividad

&#x20;       +buscarActividad(rutSocio: String) Actividad

&#x20;       +agendarReserva(rut: String, idAct: String) void

&#x20;       +cargarDatosBatch() void

&#x20;       +guardarDatosBatch() void

&#x20;   }

&#x20;   

&#x20;   class DBConnection {

&#x20;       -url : String

&#x20;       +conectar() void

&#x20;       +ejecutarQuery(query: String) void

&#x20;   }



&#x20;   %% ==========================================

&#x20;   %% CAPA MODELO

&#x20;   %% ==========================================

&#x20;   class Actividad {

&#x20;       -idActividad : String

&#x20;       -nombre : String

&#x20;       -cupoMaximo : int

&#x20;       -reservasAnidadas : ArrayList\~Reserva\~

&#x20;       +mostrarDetalles() void

&#x20;   }

&#x20;   

&#x20;   class ClaseGrupal {

&#x20;       -profesor : String

&#x20;       +mostrarDetalles() void

&#x20;   }

&#x20;   

&#x20;   class EntrenamientoLibre {

&#x20;       -requiereAsistencia : boolean

&#x20;       +mostrarDetalles() void

&#x20;   }

&#x20;   

&#x20;   class Socio {

&#x20;       -rut : String

&#x20;       -nombre : String

&#x20;       -edad : int

&#x20;       -deuda : int

&#x20;       -esMoroso : boolean

&#x20;       +abonarDeuda(monto: int) void

&#x20;       +abonarDeuda() void

&#x20;   }

&#x20;   

&#x20;   class Reserva {

&#x20;       -idReserva : int

&#x20;       -fecha : Date

&#x20;       -estado : String

&#x20;       -rutSocio : String

&#x20;   }

&#x20;   

&#x20;   class MorosidadException {

&#x20;       +getMessage() String

&#x20;   }



&#x20;   %% ==========================================

&#x20;   %% RELACIONES (El motor del diagrama)

&#x20;   %% ==========================================

&#x20;   

&#x20;   %% 1. Herencia (SIA-6) -> Flecha de línea continua con punta hueca

&#x20;   Actividad <|-- ClaseGrupal

&#x20;   Actividad <|-- EntrenamientoLibre



&#x20;   %% 2. Composición (SIA-4 Colecciones Anidadas) -> Rombo negro

&#x20;   Actividad "1" \*-- "\*" Reserva : contiene



&#x20;   %% 3. Asociaciones -> Flechas simples de flujo de datos

&#x20;   SistemaClub "1" --> "\*" Actividad : gestiona

&#x20;   SistemaClub "1" --> "\*" Socio : gestiona

&#x20;   SistemaClub "1" --> "1" DBConnection : utiliza

&#x20;   

&#x20;   MenuConsola "1" --> "1" SistemaClub : envía datos

&#x20;   MenuVentana "1" --> "1" SistemaClub : envía datos

&#x20;   

&#x20;   %% 4. Dependencias -> Líneas punteadas (Uso temporal)

&#x20;   Main ..> MenuConsola : instancia

&#x20;   Main ..> MenuVentana : instancia

&#x20;   SistemaClub ..> MorosidadException : lanza

```

