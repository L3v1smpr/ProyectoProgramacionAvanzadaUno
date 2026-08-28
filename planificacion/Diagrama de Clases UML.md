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
        +iniciarConsola() void
    }
    
    class MenuVentana {
        -controlador : SistemaClub
        +iniciarVentana() void
    }

    class SistemaClub {
        -mapaSocios : HashMap~String, Socio~
        -listaActividades : ArrayList~Actividad~
        -connection : DBConnection
        +agregarSocio(rut: String, nombre: String, edad: int) boolean
	    +modificarSocio(rut: String, nombre: String, edad: int, deuda: int, esMoroso: boolean) boolean
	    +eliminarSocio(rut: String) boolean
	    +desactivarSocio(rut: String) boolean
	    +activarSocio(rut: String) boolean
	    +obtenerListaSocios() ArrayList~Socio~
	    +obtenerListaSociosDeudores() ArrayList~Socio~
	    +buscarSocio(rut: String) Socio
        +agregarActividad(idActividad: String, nombre: String, cupoMaximo: int, edadMinima : int, profesor: String) boolean
        +agregarActividad(idActividad: String, nombre: String, cupoMaximo: int, edadMinima : int, requiereAsistencia: boolean) boolean
        +modificarActividad(idActividad: String, nuevoNombre: String, nuevoCupoMaximo: int, nuevaEdadMinima : int) boolean
        +eliminarActividad(idActividad: String) boolean
        +desactivarActividad(idActividad: String) boolean
        +activarActividad(idActividad: String) boolean
        +buscarActividad(idActividad: String) Actividad
        +buscarActividad(idReserva: int) Actividad
        +obtenerActividades() Arraylist~Actividad~
        +agendarReserva(rut: String, idAct: String, fecha: Date) boolean
        +modificarReserva(idReserva: int, fecha: Date, estado: EstadoReserva, rutSocio: String, idActividad: String) boolean
        +eliminarReserva(idReserva: int) boolean
        +listarReservasGlobales() ArrayList~Reserva~
        +pagarFacturacion(rut: String) boolean
        +pagarFacturacion(rut: String, abono: int) boolean
        +generarCobroMensual() boolean   
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
    		<<abstract>>
        -idActividad : String
        -nombre : String
        -cupoMaximo : int
        -edadMinima : int
        -activo : boolean
        +Actividad(idActividad: String, nombre: String, cupoMaximo: int, edadMinima: int)
        +getIdActividad() String
        +getNombre() String
        +getCupoMaximo() int
        +getEdadMinima() int
        +getActivo() boolean
        +setIdActividad(id: String) void
        +setNombre(nombre: String) void
        +setCupoMaximo(cupo: int) void
        +setEdadMinima(edad: int) void
        +setActivo(activo: boolean) void 
        +mostrarDetalles() String
        +mostrarDetalles(formatoCorto: boolean) String
    }
        
    class ClaseGrupal {
        -profesor : String
        +ClaseGrupal(idActividad: String, nombre: String, cupoMaximo: int, edadMinima: int, profesor: String)
        +getProfesor() String
        +setProfesor(profesor: String) void
        +mostrarDetalles() String
    }
    
    class EntrenamientoLibre {
        -requiereAsistencia : boolean
        +EntrenamientoLibre(idActividad: String, nombre: String, cupoMaximo: int, edadMinima: int, requiereAsistencia: boolean)
        +isRequiereAsistencia() boolean
        +setRequiereAsistencia(requiere: boolean) void
        +mostrarDetalles() String
    }
    
    class Socio {
        -rut : String
        -nombre : String
        -edad : int
        -deuda : int
        -esMoroso : boolean
        -listaReservas : ArrayList~Reserva~
        -activo : boolean
        +Socio(rut: String, nombre: String, edad: int, deuda: int, esMoroso: boolean)
        +getRut() String
        +getNombre() String
        +getEdad() int
        +getDeuda() int
        +getEsMoroso() boolean
        +getActivo() boolean
        +getListaReservas() ArrayList~Reserva~
        +setRut(rut: String) void
        +setNombre(nombre: String) void
        +setEdad(edad: int) void
        +setDeuda(deuda: int) void
        +setEsMoroso(estado: boolean) void
        +setActivo(activo: boolean) void
        +abonarDeuda(monto: int) void
        +abonarDeuda() void
        +agregarReserva(reserva: Reserva) boolean
        +buscarReserva(idReserva: int) Reserva
        +eliminarReserva(idReserva: int) boolean
    }
    
    class Reserva {
        -idReserva : int
        -fecha : Date
        -estado : EstadoReserva
        -rutSocio : String
		-idActividad : String
        +getIdReserva() int
        +getFecha() Date
        +getEstado() EstadoReserva
        +getRutSocio() String
        +getIdActividadEnReserva() String
        +setIdReserva(id: int) void
        +setFecha(fecha: Date) void
        +setEstado(estado: EstadoReserva) void
        +setRutSocio(rut: String) void
		+setIdActividadEnReserva (rut: String) void
    }
    
    class EstadoReserva {
    		<<enumeration>>
    		PENDIENTE
    		COMPLETADA
    		CANCELADA
    		
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

	Reserva ..> EstadoReserva : usa
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