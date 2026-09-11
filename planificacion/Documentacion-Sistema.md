# Documentación del Sistema de Gestión de Club Deportivo

## 1. Descripción general

Este proyecto corresponde al desarrollo de un sistema de gestión para un club deportivo. Su objetivo principal es centralizar en una sola aplicación la administración de socios, actividades, reservas y procesos básicos de facturación.

El sistema fue desarrollado utilizando Java JDK 11 y programación orientada a objetos, manteniendo una separación entre el modelo del dominio, la lógica del sistema, la persistencia de datos y las interfaces de usuario.

La información registrada se conserva entre distintas ejecuciones mediante una base de datos local SQLite conectada a Java mediante JDBC.

El programa dispone de dos formas de interacción:

- interfaz de consola mediante `MenuConsola`;
- interfaz gráfica mediante `MenuVentana`, desarrollada con Java Swing.

Ambas interfaces utilizan una misma instancia de `SistemaClub`, de modo que las reglas de negocio no se encuentran duplicadas dentro de las vistas.

El sistema también permite exportar el registro de socios a un archivo CSV compatible con aplicaciones de hoja de cálculo.

---

## 2. Objetivo del sistema

El sistema permite:

- registrar y administrar socios;
- modificar información de socios;
- activar y desactivar socios;
- identificar socios con deuda;
- registrar distintos tipos de actividades;
- modificar actividades;
- activar y desactivar actividades;
- crear, modificar y eliminar reservas;
- controlar los cupos de las actividades;
- impedir reservas cuando un socio presenta morosidad;
- administrar deuda y pagos;
- generar cobros mensuales;
- listar eventos;
- exportar socios a CSV;
- almacenar la información en SQLite;
- recuperar automáticamente los datos al iniciar.

La lógica de negocio se mantiene separada de la interfaz de usuario. Las vistas no modifican directamente las colecciones internas ni acceden directamente a SQLite.

---

## 3. Arquitectura general

El proyecto se organiza principalmente en los paquetes:

```text
main
modelo
controlador
vista
```

### 3.1 Paquete `modelo`

Contiene las entidades principales:

- `Socio`
- `Actividad`
- `ClaseGrupal`
- `EntrenamientoLibre`
- `Evento`
- `Reserva`
- `EstadoReserva`

También contiene las excepciones personalizadas:

- `MorosidadException`
- `CupoMaximoException`
- `ConexionBDException`
- `PersistenciaDatosException`

### 3.2 Paquete `controlador`

Contiene:

- `SistemaClub`
- `DBConnection`

`SistemaClub` coordina la lógica relacionada con socios, actividades, reservas, facturación, persistencia y exportación.

`DBConnection` concentra las operaciones específicas de SQLite y JDBC.

### 3.3 Paquete `vista`

Contiene:

- `MenuConsola`
- `MenuVentana`

Ambas vistas utilizan `SistemaClub` como punto de acceso a la lógica del sistema.

### 3.4 Paquete `main`

Contiene `Main`, encargado de:

1. crear `SistemaClub`;
2. cargar datos desde SQLite;
3. cargar datos iniciales si el sistema está vacío;
4. permitir al usuario seleccionar consola o GUI;
5. iniciar la interfaz elegida.

---

## 4. Modelo de dominio

### 4.1 Socio

`Socio` representa a una persona registrada en el club.

Contiene:

- RUT;
- nombre;
- edad;
- deuda;
- estado de morosidad;
- estado activo/inactivo;
- colección de reservas.

Cada socio mantiene:

```java
ArrayList<Reserva>
```

La clase también incluye operaciones para pago total y abono parcial de deuda.

### 4.2 Actividad

`Actividad` es una clase abstracta que contiene:

- identificador;
- nombre;
- cupo máximo;
- edad mínima;
- estado activo/inactivo.

De ella heredan:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

### 4.3 ClaseGrupal

Representa una actividad dirigida por un profesor.

Atributo específico:

```text
profesor
```

### 4.4 EntrenamientoLibre

Representa una actividad con configuración específica respecto de asistencia.

Atributo específico:

```text
requiereAsistencia
```

### 4.5 Evento

Representa una actividad especial asociada a:

- fecha;
- lugar;
- tipo de evento.

### 4.6 Reserva

`Reserva` representa la asociación entre un socio y una actividad.

Contiene:

- identificador;
- fecha;
- estado;
- RUT del socio;
- ID de la actividad.

### 4.7 EstadoReserva

Los estados posibles son:

```text
PENDIENTE
COMPLETADA
CANCELADA
```

---

## 5. Herencia y polimorfismo

`SistemaClub` mantiene las actividades dentro de:

```java
ArrayList<Actividad>
```

En esta misma colección pueden coexistir objetos `ClaseGrupal`, `EntrenamientoLibre` y `Evento`.

Las subclases sobrescriben métodos como:

```java
mostrarDetalles()
getTipoActividad()
```

También se utilizan métodos polimórficos como:

```java
getProfesor()
getRequiereAsistencia()
getFecha()
getLugar()
getTipoEvento()
esEvento()
```

Gracias a este diseño, el sistema trabaja con referencias `Actividad` sin utilizar comprobaciones mediante `instanceof`.

---

## 6. Gestión de socios

Los socios se administran mediante:

```java
HashMap<String, Socio>
```

El RUT se utiliza como clave.

Las operaciones principales son:

- registrar;
- modificar;
- buscar por RUT;
- listar;
- filtrar deudores;
- desactivar;
- reactivar;
- administrar deuda;
- exportar a CSV.

### 6.1 Eliminación lógica

La desactivación modifica:

```java
activo = false
```

El socio permanece almacenado y puede reactivarse posteriormente.

---

## 7. Exportación de socios a CSV

La operación se centraliza en:

```java
exportarSociosCSV(String rutaArchivo)
```

La exportación considera socios activos e inactivos.

El archivo contiene:

```text
RUT
Nombre
Edad
Deuda
Moroso
Estado
CantidadReservas
```

Utiliza:

```text
;
```

como separador y codificación UTF-8.

Los socios se ordenan por RUT antes de escribir el archivo.

Los campos de texto se procesan mediante:

```java
escaparCSV(String valor)
```

La exportación está disponible desde:

```text
MenuConsola
MenuVentana
```

En la GUI se utiliza `JFileChooser` para seleccionar la ubicación del archivo.

---

## 8. Gestión de actividades

El sistema permite registrar:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

Para ello se utiliza sobrecarga de:

```java
agregarActividad()
```

Las operaciones principales incluyen:

- agregar;
- modificar;
- buscar;
- listar;
- filtrar eventos;
- desactivar;
- reactivar.

### 8.1 Eliminación lógica

Las actividades se desactivan mediante:

```java
activo = false
```

sin eliminarlas definitivamente de `listaActividades`.

---

## 9. Gestión de reservas

Cada socio contiene:

```java
ArrayList<Reserva>
```

Las reservas se administran mediante operaciones para:

- agendar;
- modificar;
- eliminar o cancelar;
- listar globalmente.

`SistemaClub` puede reunir temporalmente las reservas de todos los socios y ordenarlas cronológicamente.

---

## 10. Reglas de negocio

### 10.1 Morosidad

Un socio moroso no puede registrar una nueva reserva.

En este caso se utiliza:

```java
MorosidadException
```

### 10.2 Cupo máximo

Antes de registrar una reserva se verifica el cupo disponible.

Las reservas `CANCELADA` no ocupan cupo.

Si se alcanza el máximo se utiliza:

```java
CupoMaximoException
```

### 10.3 Existencia del socio y actividad

Para agendar una reserva deben existir tanto el socio como la actividad.

---

## 11. Facturación

El sistema permite generar un cobro mensual para los socios activos.

El valor utilizado actualmente es:

```text
$10.000 CLP
```

También permite:

- consultar deuda;
- pagar completamente;
- realizar abonos parciales.

Cuando la deuda llega a cero, el socio deja de estar marcado como moroso.

---

## 12. Persistencia de datos

La persistencia utiliza:

```text
SQLite
JDBC
```

La clase responsable es:

```java
DBConnection
```

La base local utilizada es:

```text
club_deportivo.db
```

---

## 13. Estructura de la base de datos

Las tablas principales son:

```text
SOCIOS
ACTIVIDADES
RESERVAS
```

### 13.1 SOCIOS

```text
rut
nombre
edad
deuda
es_moroso
activo
```

`rut` es la clave primaria.

### 13.2 ACTIVIDADES

```text
id_actividad
nombre
cupo_maximo
edad_minima
tipo
profesor
requiere_asistencia
fecha_evento
lugar
tipo_evento
activo
```

### 13.3 RESERVAS

```text
id_reserva
fecha
estado
rut_socio
id_actividad
```

`rut_socio` referencia `SOCIOS`.

`id_actividad` referencia `ACTIVIDADES`.

### 13.4 Claves foráneas

Al establecer conexión se ejecuta:

```sql
PRAGMA foreign_keys = ON;
```

---

## 14. Guardado de datos

El guardado por lotes se coordina desde `SistemaClub`.

Orden:

```text
1. Socios
2. Actividades
3. Reservas
```

Los socios se guardan mediante:

```java
DBConnection.guardarSocio()
```

Las actividades mediante:

```java
DBConnection.guardarActividad()
```

Antes de guardar reservas se limpia su tabla y luego se insertan las reservas que existen actualmente en memoria.

Esto evita que una reserva eliminada reaparezca al reiniciar.

---

## 15. Carga de datos

La carga utiliza el orden:

```text
1. Socios
2. Actividades
3. Reservas
```

Primero se reconstruye:

```java
HashMap<String, Socio>
```

Luego:

```java
ArrayList<Actividad>
```

Finalmente las reservas se vuelven a asociar al socio correspondiente.

---

## 16. Datos iniciales

Cuando el sistema se ejecuta sin socios ni actividades almacenados, `Main` utiliza:

```java
cargarDatosIniciales()
```

Después se ejecuta:

```java
guardarDatosBatch()
```

De esta forma los datos iniciales quedan persistidos y no se duplican en ejecuciones posteriores.

---

## 17. Guardado manual y guardado al salir

Las dos interfaces permiten guardar manualmente.

La consola intenta guardar al seleccionar la opción de salida.

La GUI utiliza un control de cierre que permite:

- guardar y salir;
- salir sin guardar;
- cancelar.

El guardado utiliza:

```java
guardarDatosBatch()
```

---

## 18. Manejo de excepciones

El proyecto utiliza:

### 18.1 MorosidadException

Para impedir reservas de socios morosos.

### 18.2 CupoMaximoException

Para validaciones relacionadas con el cupo máximo.

### 18.3 ConexionBDException

Para errores relacionados con la conexión o acceso a SQLite.

### 18.4 PersistenciaDatosException

Para errores durante:

- lectura;
- escritura;
- reconstrucción;
- detección de información inconsistente.

### 18.5 IOException

La exportación CSV puede producir `IOException` si no es posible crear o escribir el archivo.

---

## 19. Colecciones utilizadas

### 19.1 HashMap de socios

```java
HashMap<String, Socio>
```

### 19.2 ArrayList de actividades

```java
ArrayList<Actividad>
```

### 19.3 ArrayList de reservas

Cada socio contiene:

```java
ArrayList<Reserva>
```

Esto constituye una colección anidada.

---

## 20. Sobrecarga de métodos

### 20.1 SistemaClub

Existen distintas versiones de:

```java
agregarActividad()
```

para crear los tres tipos de actividad.

También existen:

```java
pagarFacturacion(String rut)
pagarFacturacion(String rut, int abono)
```

### 20.2 Socio

```java
abonarDeuda()
abonarDeuda(int monto)
```

---

## 21. Sobrescritura de métodos

Las subclases de `Actividad` sobrescriben métodos como:

```java
mostrarDetalles()
getTipoActividad()
```

Además:

```text
ClaseGrupal
    -> getProfesor()

EntrenamientoLibre
    -> getRequiereAsistencia()

Evento
    -> getFecha()
    -> getLugar()
    -> getTipoEvento()
    -> esEvento()
```

---

## 22. Interfaces de usuario

### 22.1 Consola

`MenuConsola` permite administrar:

```text
Actividades
Socios
Reservas
Facturación
Cobro mensual
Exportación de socios
Guardado
Salida
```

### 22.2 Interfaz gráfica

`MenuVentana` utiliza Java Swing y organiza las funciones mediante un:

```java
JTabbedPane
```

con pestañas para:

```text
Socios
Actividades
Reservas
Facturación
```

La GUI permite:

- registrar;
- buscar;
- modificar;
- desactivar;
- reactivar;
- administrar reservas;
- administrar facturación;
- generar cobro mensual;
- guardar;
- exportar CSV.

También utiliza `JFileChooser` para la exportación.

---

## 23. Flujo general de ejecución

```text
Main
 |
 v
SistemaClub
 |
 v
cargarDatosBatch()
 |
 +----> SOCIOS
 |
 +----> ACTIVIDADES
 |
 +----> RESERVAS
 |
 +----> datos iniciales si el sistema está vacío
 |
 v
seleccionar interfaz
 |
 +------> MenuConsola
 |
 +------> MenuVentana
```

---

## 24. Dependencias y configuración

El módulo requiere:

```java
requires java.sql;
requires org.xerial.sqlitejdbc;
requires java.desktop;
```

El driver SQLite incluido en el proyecto es:

```text
lib/sqlite-jdbc-3.53.4.0.jar
```

El `.gitignore` excluye los archivos locales de SQLite, `bin/` y los archivos temporales de LibreOffice.

---

## 25. Diagramas del sistema

Dentro de `planificacion/` se incluyen:

```text
Diagrama de Clases UML.md
Diagrama Entidad-Relacion.md
```

El UML representa clases, atributos, métodos, herencia, composición, dependencias, persistencia, interfaces y excepciones.

El modelo entidad-relación representa:

```text
SOCIOS 1 -------- N RESERVAS N -------- 1 ACTIVIDADES
```

---

## 26. Documentación Javadoc

El proyecto incorpora Javadoc en las clases principales, métodos relevantes, vistas, controlador, persistencia, excepciones y enumeraciones.

Cuando corresponde se utilizan:

```text
@param
@return
@throws
```

---

## 27. Estado final del proyecto

Se encuentran implementadas:

- gestión de socios;
- gestión de actividades;
- gestión de reservas;
- facturación;
- cobro mensual;
- morosidad;
- control de cupo;
- eliminación lógica;
- reactivación;
- herencia;
- polimorfismo;
- sobrecarga;
- sobrescritura;
- colecciones;
- colecciones anidadas;
- persistencia SQLite;
- carga y guardado por lotes;
- claves foráneas;
- excepciones personalizadas;
- consola;
- GUI Swing;
- exportación CSV desde ambas interfaces;
- guardado manual y al salir;
- datos iniciales;
- Javadoc;
- diagramas UML y ER.

El proyecto se encuentra en estado final para entrega.

Las instrucciones de instalación y uso se mantienen separadas en:

```text
README.md
```

---

## 28. Mejoras futuras posibles

Estas mejoras no forman parte de los requisitos actuales:

- utilizar transacciones SQLite;
- reemplazar la generación actual de identificadores de reserva;
- ampliar las exportaciones;
- incorporar pruebas automatizadas con JUnit.

---

## 29. Consideraciones de diseño

### Vista

`MenuConsola` y `MenuVentana` reciben datos del usuario, muestran resultados y solicitan acciones al controlador.

### Controlador

`SistemaClub` coordina socios, actividades, reservas, reglas de negocio, facturación, carga, guardado y exportación.

### Persistencia

`DBConnection` concentra las operaciones específicas de SQLite.

### Polimorfismo

La diferenciación entre tipos de actividad se realiza mediante métodos polimórficos y sobrescritura, evitando `instanceof`.

---

## 30. Conclusión

El proyecto evolucionó desde operaciones mantenidas únicamente en memoria hasta un sistema capaz de administrar socios, actividades, reservas y facturación conservando su estado entre ejecuciones.

SQLite permite persistir la información. Las colecciones representan tanto registros principales como relaciones anidadas. La herencia y el polimorfismo permiten trabajar con los distintos tipos de actividad mediante una estructura común.

Las excepciones diferencian errores de negocio y problemas de persistencia.

El sistema dispone de una interfaz de consola y una interfaz gráfica mediante Swing, ambas construidas sobre la misma instancia de `SistemaClub`.

La exportación de socios a CSV está disponible desde ambas interfaces.

La documentación Javadoc, los diagramas del sistema, el README de uso y el reporte académico complementan el código fuente.

El proyecto se encuentra preparado para su entrega final.
