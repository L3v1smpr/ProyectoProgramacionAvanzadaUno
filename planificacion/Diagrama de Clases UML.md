```mermaid
classDiagram
    %% ==========================================
    %% CAPA VISTA Y CONTROLADOR (Resumida para enfoque en Modelo)
    %% ==========================================
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
        +agendarReserva(rut: String, idAct: String) void
        +cargarDatosBatch() void
        +guardarDatosBatch() void
    }
    
    class DBConnection {
        -url : String
        +conectar() void
        +ejecutarQuery(query: String) void
    }

    %% ==========================================
    %% CAPA MODELO (Con Getters, Setters y Métodos de Negocio)
    %% ==========================================
    class Actividad {
        -idActividad : String
        -nombre : String
        -cupoMaximo : int
        +getIdActividad() String
        +setIdActividad(id: String) void
        +getNombre() String
        +setNombre(nombre: String) void
        +getCupoMaximo() int
        +setCupoMaximo(cupo: int) void
        +mostrarDetalles() void
        +mostrarDetalles(formatoCorto: boolean) void
    }
    
    class ClaseGrupal {
        -profesor : String
        +getProfesor() String
        +setProfesor(profesor: String) void
        +mostrarDetalles() void
    }
    
    class EntrenamientoLibre {
        -requiereAsistencia : boolean
        +isRequiereAsistencia() boolean
        +setRequiereAsistencia(requiere: boolean) void
        +mostrarDetalles() void
    }
    
    class Socio {
        -rut : String
        -nombre : String
        -edad : int
        -deuda : int
        -esMoroso : boolean
        -arrayReservas : ArrayList~Reserva~
        +getRut() String
        +setRut(rut: String) void
        +getNombre() String
        +setNombre(nombre: String) void
        +getEdad() int
        +setEdad(edad: int) void
        +getDeuda() int
        +setDeuda(deuda: int) void
        +isEsMoroso() boolean
        +setEsMoroso(estado: boolean) void
        +abonarDeuda(monto: int) void
        +abonarDeuda() void
    }
    
    class Reserva {
        -idReserva : int
        -fecha : Date
        -estado : String
        -rutSocio : String
	-idActividad : String
        +getIdReserva() int
        +setIdReserva(id: int) void
        +getFecha() Date
        +setFecha(fecha: Date) void
        +getEstado() String
        +setEstado(estado: String) void
        +getRutSocio() String
        +setRutSocio(rut: String) void
	+getIdActividad() String
	+setIdActividad(rut: String) void
    }
    
    class MorosidadException {
        +getMessageMorosidad() String
    }

    class CupoMaximoException {
	+getMessageCupoMaximo() String
    }

    %% ==========================================
    %% RELACIONES 
    %% ==========================================
    Actividad <|-- ClaseGrupal
    Actividad <|-- EntrenamientoLibre

    Socio "1" *-- "n" Reserva : 1 a Muchos
    SistemaClub "1" --> "n" Actividad : 1 a Muchos
    SistemaClub "1" --> "n" Socio : 1 a Muchos
    SistemaClub "1" --> "1" DBConnection : 1 a 1
    
    MenuConsola "1" --> "1" SistemaClub : 1 a 1
    MenuVentana "1" --> "1" SistemaClub : 1 a 1
    
    Main ..> MenuConsola : Instancia
    Main ..> MenuVentana : Instancia
    SistemaClub ..> MorosidadException : Lanza
    SistemaClub ..> CupoMaximoException : Lanza

```