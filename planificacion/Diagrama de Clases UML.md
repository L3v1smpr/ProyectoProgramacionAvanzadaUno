```mermaid
classDiagram
    class Main {
        +main(args: String[]) void
    }
    
    class MenuConsola {
        -controlador : SistemaClub
        +iniciarMenu() void
    }
    
    class MenuVentana {
        -controlador : SistemaClub
        +iniciarVentana() void
    }

    class SistemaClub {
        -mapaSocios : HashMap~String, Socio~
        -listaActividades : ArrayList~Actividad~
        -db : DBConnection
        +agregarSocio(rut: String, nombre: String) boolean
        +buscarActividad(idActividad: String) Actividad
        +buscarActividad(rutSocio: String) Actividad
        +agendarReserva(rut: String, idAct: String) void
        +cargarDatosBatch() void
        +guardarDatosBatch() void
    }
    
    class DBConnection {
        -url : String
        +conectar() void
        +ejecutarQuery(query: String) void
    }

    class Actividad {
        -idActividad : String
        -nombre : String
        -cupoMaximo : int
        -reservasAnidadas : ArrayList~Reserva~
        +mostrarDetalles() void
    }
    
    class ClaseGrupal {
        -profesor : String
        +mostrarDetalles() void
    }
    
    class EntrenamientoLibre {
        -requiereAsistencia : boolean
        +mostrarDetalles() void
    }
    
    class Socio {
        -rut : String
        -nombre : String
        -edad : int
        -deuda : int
        -esMoroso : boolean
        +abonarDeuda(monto: int) void
        +abonarDeuda() void
    }
    
    class Reserva {
        -idReserva : int
        -fecha : Date
        -estado : String
        -rutSocio : String
    }
    
    class MorosidadException {
        +getMessage() String
    }

    Actividad <|-- ClaseGrupal
    Actividad <|-- EntrenamientoLibre

    Actividad "1" *-- "n" Reserva : 1 a Muchos
    SistemaClub "1" --> "n" Actividad : 1 a Muchos
    SistemaClub "1" --> "n" Socio : 1 a Muchos
    SistemaClub "1" --> "1" DBConnection : 1 a 1
    
    MenuConsola "1" --> "1" SistemaClub : 1 a 1
    MenuVentana "1" --> "1" SistemaClub : 1 a 1
    
    Main ..> MenuConsola : Instancia
    Main ..> MenuVentana : Instancia
    SistemaClub ..> MorosidadException : Lanza
```
