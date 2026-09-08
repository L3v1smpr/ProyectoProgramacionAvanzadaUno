# Documentación del Sistema de Gestión de Club Deportivo

## 1. Descripción general

Este proyecto corresponde al desarrollo de un sistema de gestión para un club deportivo. Su objetivo principal es centralizar en una sola aplicación la administración de socios, actividades, reservas y procesos básicos de facturación.

El sistema fue desarrollado utilizando Java JDK 11 y programación orientada a objetos, buscando mantener una separación clara entre el modelo del dominio, la lógica del sistema, la persistencia de datos y las interfaces utilizadas por el usuario.

La información registrada puede mantenerse entre distintas ejecuciones mediante una base de datos local SQLite conectada a Java mediante JDBC.

El programa dispone de dos formas de interacción:

- interfaz de consola mediante `MenuConsola`;
- interfaz gráfica mediante `MenuVentana`, desarrollada con Java Swing.

Ambas interfaces utilizan una misma instancia de `SistemaClub`, de modo que las reglas de negocio no se encuentran duplicadas dentro de las vistas.

Además, el sistema incorpora una funcionalidad para exportar el registro de socios a un archivo CSV, permitiendo utilizar dicha información posteriormente en herramientas externas.

---

## 2. Objetivo del sistema

El sistema busca resolver tareas comunes dentro de la administración de un club deportivo, entre ellas:

- registrar y administrar socios;
- modificar información de socios existentes;
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
- exportar los datos de los socios a formato CSV;
- almacenar la información en una base de datos local;
- recuperar los datos automáticamente al iniciar una nueva ejecución.

La idea central del proyecto consiste en mantener la lógica de negocio separada de la interfaz de usuario, evitando que las clases de vista modifiquen directamente las colecciones internas o accedan directamente a SQLite.

---

## 3. Arquitectura general

El proyecto se divide principalmente en los paquetes:

```text
main
modelo
controlador
vista
```

### 3.1 Paquete `modelo`

Contiene las clases que representan las entidades y conceptos principales del dominio:

- `Socio`
- `Actividad`
- `ClaseGrupal`
- `EntrenamientoLibre`
- `Evento`
- `Reserva`
- `EstadoReserva`

También contiene las excepciones propias utilizadas por el sistema:

- `MorosidadException`
- `CupoMaximoException`
- `ConexionBDException`
- `PersistenciaDatosException`

---

### 3.2 Paquete `controlador`

Contiene las clases encargadas de coordinar la lógica principal del sistema y la persistencia:

- `SistemaClub`
- `DBConnection`

`SistemaClub` funciona como el punto central mediante el cual las vistas realizan operaciones sobre socios, actividades, reservas, facturación y exportación de información.

`DBConnection` concentra las operaciones relacionadas con SQLite y JDBC.

De esta forma, la vista no necesita conocer la implementación interna de la base de datos.

---

### 3.3 Paquete `vista`

Contiene las dos interfaces disponibles para el usuario:

- `MenuConsola`
- `MenuVentana`

`MenuConsola` presenta las funcionalidades mediante menús de texto.

`MenuVentana` utiliza Java Swing y organiza el sistema mediante diferentes módulos gráficos relacionados con:

- socios;
- actividades;
- reservas;
- facturación.

Las dos vistas reciben una misma instancia de `SistemaClub`.

Por esta razón, una operación realizada desde cualquiera de ellas trabaja sobre la misma estructura de datos y utiliza las mismas reglas de negocio.

---

### 3.4 Paquete `main`

Contiene la clase `Main`, que funciona como punto de entrada de la aplicación.

Sus principales responsabilidades son:

1. crear una instancia de `SistemaClub`;
2. cargar los datos almacenados;
3. permitir al usuario seleccionar la interfaz a utilizar;
4. iniciar `MenuConsola` o `MenuVentana`.

---

## 4. Modelo de dominio

### 4.1 Socio

La clase `Socio` representa a una persona registrada en el club.

Sus principales atributos son:

- RUT;
- nombre;
- edad;
- deuda;
- estado de morosidad;
- estado activo/inactivo;
- lista de reservas.

Cada socio contiene su propia colección:

```java
ArrayList<Reserva>
```

Esta colección representa las reservas asociadas directamente a dicho socio.

La clase también implementa operaciones relacionadas con la deuda, entre ellas el pago total y el abono parcial.

---

### 4.2 Actividad

`Actividad` es una clase abstracta que representa los elementos comunes de las actividades administradas por el club.

Contiene:

- identificador;
- nombre;
- cupo máximo;
- edad mínima;
- estado activo/inactivo.

A partir de esta clase se implementan tres subtipos.

---

### 4.3 ClaseGrupal

`ClaseGrupal` representa una actividad dirigida por un profesor.

Su atributo específico es:

```text
profesor
```

---

### 4.4 EntrenamientoLibre

`EntrenamientoLibre` representa una actividad que puede realizarse con una configuración específica respecto de la asistencia.

Su atributo particular es:

```text
requiereAsistencia
```

---

### 4.5 Evento

`Evento` representa una actividad especial asociada a una fecha y lugar determinados.

Sus atributos específicos son:

- fecha;
- lugar;
- tipo de evento.

---

### 4.6 Reserva

`Reserva` representa la asociación entre un socio y una actividad.

Contiene:

- identificador de reserva;
- fecha;
- estado;
- RUT del socio;
- ID de la actividad.

La reserva almacena los identificadores de socio y actividad en lugar de contener directamente objetos completos de ambas entidades.

---

### 4.7 EstadoReserva

Los estados posibles de una reserva se encuentran definidos mediante el enum:

```text
PENDIENTE
COMPLETADA
CANCELADA
```

Esto evita utilizar cadenas de texto arbitrarias para representar el estado de una reserva.

---

## 5. Uso de herencia y polimorfismo

Una de las decisiones principales del diseño consiste en utilizar `Actividad` como tipo general.

`SistemaClub` mantiene las actividades dentro de:

```java
ArrayList<Actividad>
```

En la misma colección pueden coexistir instancias de:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

Las subclases redefinen métodos de acuerdo con su comportamiento específico.

Entre ellos se encuentran:

```java
mostrarDetalles()
getTipoActividad()
```

También se utilizan métodos polimórficos para obtener datos específicos:

```java
getProfesor()
getRequiereAsistencia()
getFecha()
getLugar()
getTipoEvento()
```

Cada subtipo devuelve los valores que le corresponden.

Gracias a este enfoque, el programa puede tratar los distintos tipos de actividad mediante referencias de tipo `Actividad` sin utilizar comprobaciones con:

```java
instanceof
```

---

## 6. Gestión de socios

`SistemaClub` administra los socios mediante:

```java
HashMap<String, Socio>
```

El RUT se utiliza como clave del mapa.

Las principales operaciones disponibles son:

- registrar un socio;
- modificar sus datos;
- buscarlo mediante su RUT;
- listar socios activos;
- listar socios deudores;
- desactivar socios;
- reactivar socios;
- administrar deuda;
- exportar los registros a CSV.

---

### 6.1 Eliminación lógica de socios

La operación utilizada normalmente por el sistema corresponde a una eliminación lógica o **soft-delete**.

En lugar de eliminar el objeto del mapa, se modifica:

```java
activo = false
```

Esto permite mantener el registro tanto en memoria como en SQLite.

Los listados normales pueden filtrar dichos registros sin perder la información histórica del socio.

El socio puede posteriormente volver a activarse.

---

## 7. Exportación de socios a CSV

Se incorporó una funcionalidad que permite generar un archivo CSV con la información administrativa de todos los socios registrados.

La operación principal se encuentra centralizada en `SistemaClub` mediante:

```java
exportarSociosCSV(String rutaArchivo)
```

La lógica de exportación no utiliza:

```java
obtenerListaSocios()
```

porque dicho método retorna únicamente socios activos.

En su lugar se consideran directamente todos los valores almacenados dentro de:

```java
mapaSocios
```

De esta manera el archivo contiene tanto socios activos como inactivos.

---

### 7.1 Datos exportados

El archivo contiene las siguientes columnas:

```text
RUT
Nombre
Edad
Deuda
Moroso
Estado
CantidadReservas
```

Un ejemplo conceptual corresponde a:

```text
RUT;Nombre;Edad;Deuda;Moroso;Estado;CantidadReservas
"12.345.678-9";"Juan Perez";22;10000;SI;ACTIVO;2
"19.876.543-2";"Ana Soto";25;0;NO;INACTIVO;0
```

El separador utilizado es:

```text
;
```

Esto facilita la apertura del archivo en aplicaciones de hoja de cálculo que utilicen configuraciones regionales donde la coma se reserva como separador decimal.

---

### 7.2 Orden de los registros

Debido a que `HashMap` no garantiza un orden fijo de recorrido, antes de escribir el archivo se genera una lista auxiliar:

```java
ArrayList<Socio>
```

Los socios se ordenan posteriormente según su RUT.

Esto permite obtener un archivo consistente entre diferentes ejecuciones.

---

### 7.3 Codificación

El archivo se genera utilizando UTF-8 para conservar correctamente caracteres como:

```text
á
é
í
ó
ú
ñ
```

También puede utilizarse una marca BOM UTF-8 para mejorar la detección automática de codificación en determinadas aplicaciones de hoja de cálculo.

---

### 7.4 Escape de campos

Los campos de texto se procesan mediante un método auxiliar:

```java
escaparCSV(String valor)
```

Este método permite representar correctamente valores que contengan comillas u otros caracteres especiales.

Por ejemplo:

```text
Juan "Pepe" Perez
```

puede representarse dentro del archivo como:

```text
"Juan ""Pepe"" Perez"
```

evitando alterar la estructura del CSV.

---

### 7.5 Separación de responsabilidades

La generación del contenido se encuentra en:

```text
SistemaClub
```

mientras que las interfaces se encargan únicamente de interactuar con el usuario.

El flujo es:

```text
MenuConsola
     |
     v
SistemaClub.exportarSociosCSV()
     |
     v
archivo CSV
```

Actualmente la exportación se conecta desde la interfaz de consola.

La integración gráfica se realizará desde `MenuVentana` reutilizando la misma operación del controlador, evitando implementar una segunda lógica de exportación.

La futura interacción gráfica puede utilizar un selector de archivos como `JFileChooser` para permitir al usuario escoger la ubicación del CSV.

---

## 8. Gestión de actividades

El sistema permite registrar tres tipos diferentes de actividades.

Para ello se utiliza sobrecarga del método:

```java
agregarActividad()
```

Según los parámetros recibidos se crea una instancia de:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

Las principales operaciones son:

- agregar actividad;
- modificar actividad;
- buscar actividad;
- listar actividades activas;
- listar eventos;
- desactivar actividad;
- reactivar actividad.

---

### 8.1 Eliminación lógica de actividades

Las actividades utilizan la misma estrategia de soft-delete aplicada a los socios.

Al desactivar una actividad se modifica:

```java
activo = false
```

en lugar de eliminar definitivamente el objeto de:

```java
listaActividades
```

Esto permite conservar su información y restaurarla posteriormente.

---

## 9. Gestión de reservas

Las reservas no se almacenan dentro de una colección global permanente en `SistemaClub`.

Cada `Socio` posee internamente:

```java
ArrayList<Reserva>
```

De esta manera las reservas permanecen asociadas al socio propietario.

Las principales operaciones implementadas son:

- agendar una reserva;
- buscar información asociada;
- modificar una reserva;
- eliminar una reserva;
- listar globalmente las reservas.

Para generar el listado global, `SistemaClub` recorre todos los socios y reúne temporalmente sus reservas.

Posteriormente se ordenan cronológicamente según su fecha.

---

## 10. Reglas de negocio de las reservas

### 10.1 Morosidad

Un socio que se encuentra en estado moroso no puede crear una nueva reserva.

Si se intenta realizar la operación se lanza:

```java
MorosidadException
```

La misma regla se aplica en operaciones de modificación que requieran que el socio se encuentre habilitado para reservar.

---

### 10.2 Cupo máximo

Antes de registrar una reserva se cuentan las reservas activas asociadas a la actividad.

Las reservas cuyo estado sea:

```text
CANCELADA
```

no ocupan cupo.

Si el número de participantes alcanza el máximo definido para la actividad se lanza:

```java
CupoMaximoException
```

---

### 10.3 Existencia del socio y actividad

Para agendar una reserva deben existir:

- el socio;
- la actividad.

Si alguno no se encuentra registrado, la operación no continúa.

---

## 11. Facturación y morosidad

El sistema incorpora una funcionalidad básica de facturación.

`SistemaClub` permite generar un cobro mensual para todos los socios activos.

La tarifa utilizada actualmente es:

```text
$10.000 CLP
```

Al realizar el cobro:

1. se obtiene la deuda actual;
2. se agregan $10.000;
3. el socio queda marcado como moroso.

---

### 11.1 Pago total

El sistema puede saldar completamente una deuda.

Cuando la deuda llega a:

```text
0
```

el estado de morosidad se elimina.

---

### 11.2 Abono parcial

También es posible realizar un abono inferior a la deuda total.

Si todavía existe deuda después del pago, el socio continúa en estado moroso.

Si el monto cubre completamente la deuda, el saldo queda en cero.

---

## 12. Persistencia de datos

La persistencia se implementa utilizando:

```text
SQLite
JDBC
```

La comunicación con la base de datos se encuentra concentrada en:

```java
DBConnection
```

La base utilizada es:

```text
club_deportivo.db
```

Este archivo es generado localmente durante la ejecución.

No se almacena normalmente dentro del repositorio Git.

---

## 13. Estructura de la base de datos

La base contiene tres tablas principales:

```text
SOCIOS
ACTIVIDADES
RESERVAS
```

---

### 13.1 Tabla SOCIOS

Almacena:

```text
rut
nombre
edad
deuda
es_moroso
activo
```

El campo:

```text
rut
```

corresponde a la clave primaria.

---

### 13.2 Tabla ACTIVIDADES

Almacena:

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

La tabla permite representar los tres subtipos de `Actividad`.

Los atributos que no correspondan al tipo almacenado quedan como:

```sql
NULL
```

Por ejemplo, una `ClaseGrupal` puede contener `profesor`, mientras que los atributos exclusivos de `Evento` permanecen nulos.

---

### 13.3 Tabla RESERVAS

Almacena:

```text
id_reserva
fecha
estado
rut_socio
id_actividad
```

`rut_socio` corresponde a una clave foránea hacia:

```text
SOCIOS
```

`id_actividad` corresponde a una clave foránea hacia:

```text
ACTIVIDADES
```

---

### 13.4 Claves foráneas

SQLite requiere habilitar explícitamente la validación de claves foráneas.

Al establecer la conexión se ejecuta:

```sql
PRAGMA foreign_keys = ON;
```

Esto permite verificar las relaciones entre reservas, socios y actividades.

---

## 14. Guardado de datos

El sistema utiliza un esquema de guardado por lotes coordinado por:

```java
SistemaClub
```

El orden utilizado es:

```text
1. Socios
2. Actividades
3. Reservas
```

---

### 14.1 Socios

Cada socio se envía a:

```java
DBConnection.guardarSocio()
```

La operación de SQLite utiliza una estrategia equivalente a UPSERT.

Si el registro no existe se crea.

Si ya existe se actualiza.

---

### 14.2 Actividades

Las actividades se guardan mediante:

```java
DBConnection.guardarActividad()
```

La operación trabaja polimórficamente con objetos de tipo `Actividad`.

Los getters específicos permiten recuperar los atributos particulares de cada subtipo.

---

### 14.3 Reservas

Antes de guardar las reservas se limpia su tabla.

Posteriormente se recorren nuevamente las reservas que existen dentro de cada socio.

El proceso conceptual es:

```text
DELETE reservas anteriores

        ↓

recorrer socios

        ↓

recorrer reservas de cada socio

        ↓

guardar reservas actuales
```

Esta estrategia evita que una reserva eliminada de las colecciones Java vuelva a aparecer después de reiniciar la aplicación.

---

## 15. Carga de datos

La carga se realiza antes de iniciar la interfaz del programa.

El orden utilizado es:

```text
1. Socios
2. Actividades
3. Reservas
```

Primero se reconstruye:

```java
HashMap<String, Socio>
```

Después:

```java
ArrayList<Actividad>
```

Finalmente se cargan las reservas.

Cada reserva es asociada nuevamente al socio correspondiente mediante su RUT.

Durante este proceso se comprueba también que las referencias almacenadas sean válidas.

Una inconsistencia puede generar:

```java
PersistenciaDatosException
```

---

## 16. Guardado automático al salir

Desde la interfaz de consola existe una opción para guardar manualmente.

También se realiza un intento de guardado al seleccionar la opción de salida.

Si el guardado falla, la consola no finaliza inmediatamente.

El usuario recibe información del error y puede continuar trabajando o volver a intentar el guardado.

La interfaz gráfica también incorpora una confirmación al cerrar la ventana.

El usuario puede:

- guardar y salir;
- salir sin guardar;
- cancelar el cierre.

Cuando se selecciona guardar se utiliza:

```java
guardarDatosBatch()
```

sobre la misma instancia de `SistemaClub`.

---

## 17. Manejo de excepciones

El proyecto utiliza excepciones de negocio y excepciones relacionadas con el funcionamiento interno del sistema.

---

### 17.1 MorosidadException

Se utiliza cuando una operación relacionada con reservas no puede realizarse debido a la morosidad del socio.

---

### 17.2 CupoMaximoException

Se utiliza cuando:

- una actividad alcanza su máximo de participantes;
- se intenta utilizar un valor inválido para el cupo máximo en operaciones que lo validan.

---

### 17.3 ConexionBDException

Representa errores relacionados directamente con la conexión a SQLite.

Permite evitar que una `SQLException` se propague directamente desde la capa de persistencia hasta la interfaz.

---

### 17.4 PersistenciaDatosException

Representa errores producidos durante:

- lectura de información;
- escritura;
- reconstrucción de datos;
- detección de información inconsistente.

---

### 17.5 IOException en exportación CSV

La exportación de socios utiliza operaciones de entrada y salida de Java.

Si el archivo no puede crearse o escribirse se puede producir:

```java
IOException
```

Esta excepción se propaga desde el método de exportación y se captura en la interfaz mediante:

```java
try-catch
```

De esta manera un error al generar el CSV puede informarse al usuario sin finalizar abruptamente todo el programa.

---

## 18. Colecciones utilizadas

El proyecto utiliza diferentes estructuras pertenecientes al Java Collections Framework.

---

### 18.1 HashMap de socios

Los socios se almacenan en:

```java
HashMap<String, Socio>
```

La clave corresponde al RUT.

Esto permite realizar búsquedas directas utilizando:

```java
mapaSocios.get(rut)
```

---

### 18.2 ArrayList de actividades

Las actividades se almacenan en:

```java
ArrayList<Actividad>
```

Gracias al polimorfismo, la misma lista almacena objetos de diferentes subtipos.

---

### 18.3 ArrayList de reservas

Cada socio mantiene:

```java
ArrayList<Reserva>
```

Esta corresponde a una colección anidada dentro de los objetos de la primera colección de socios.

---

### 18.4 Lista auxiliar para exportación

Para generar el CSV se crea temporalmente:

```java
ArrayList<Socio>
```

a partir de los valores existentes en el mapa.

Su finalidad es ordenar los socios antes de escribir el archivo.

Esta colección no reemplaza al mapa principal y solo se utiliza durante la operación de exportación.

---

## 19. Sobrecarga de métodos

El proyecto utiliza sobrecarga en diferentes clases.

---

### 19.1 SistemaClub

Existen diferentes versiones de:

```java
agregarActividad()
```

para crear:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

También existen:

```java
pagarFacturacion(String rut)
pagarFacturacion(String rut, int abono)
```

---

### 19.2 Socio

La clase `Socio` implementa:

```java
abonarDeuda()
abonarDeuda(int monto)
```

Una versión salda completamente la deuda y la otra permite realizar un pago parcial.

---

## 20. Sobrescritura de métodos

Las subclases de `Actividad` redefinen distintos métodos heredados.

Entre ellos:

```java
mostrarDetalles()
getTipoActividad()
```

También pueden redefinir getters relacionados con información específica.

Por ejemplo:

```text
ClaseGrupal
    -> getProfesor()

EntrenamientoLibre
    -> getRequiereAsistencia()

Evento
    -> getFecha()
    -> getLugar()
    -> getTipoEvento()
```

Esto permite a `SistemaClub` y `DBConnection` trabajar con referencias del tipo general:

```java
Actividad
```

sin comprobar manualmente la clase concreta.

---

## 21. Interfaces de usuario

### 21.1 Consola

`MenuConsola` implementa una interfaz textual.

Su menú principal contiene funcionalidades relacionadas con:

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

Los submenús permiten acceder a operaciones específicas del controlador.

---

### 21.2 Interfaz gráfica

`MenuVentana` utiliza Java Swing.

Su ventana principal contiene un:

```java
JTabbedPane
```

que organiza las funcionalidades en pestañas.

Actualmente existen módulos para:

```text
Socios
Actividades
Reservas
Facturación
```

La interfaz utiliza componentes como:

```text
JFrame
JPanel
JLabel
JTextField
JButton
JTable
JScrollPane
JCheckBox
JComboBox
JOptionPane
```

y administradores de disposición de AWT como:

```text
BorderLayout
GridLayout
FlowLayout
```

La interfaz gráfica continúa en etapa de cierre para garantizar que todas las operaciones exigidas estén disponibles también desde ventana.

La exportación CSV deberá conectarse desde este módulo reutilizando:

```java
SistemaClub.exportarSociosCSV()
```

sin volver a implementar la lógica del archivo dentro de `MenuVentana`.

---

## 22. Flujo general de ejecución

El flujo principal puede representarse como:

```text
Main
 |
 v
SistemaClub
 |
 v
cargarDatosBatch()
 |
 +----> cargar SOCIOS
 |
 +----> cargar ACTIVIDADES
 |
 +----> cargar RESERVAS
 |
 v
seleccionar interfaz
 |
 +------> MenuConsola
 |
 +------> MenuVentana
```

Ambas vistas utilizan:

```text
la misma instancia de SistemaClub
```

por lo que el flujo de operaciones puede representarse como:

```text
MenuConsola ---------+
                     |
                     v
                 SistemaClub
                     ^
                     |
MenuVentana ----------+
```

Para la exportación:

```text
MenuConsola
    |
    v
SistemaClub.exportarSociosCSV()
    |
    v
Archivo CSV
```

Una vez integrada en Swing:

```text
MenuVentana
    |
    v
SistemaClub.exportarSociosCSV()
    |
    v
Archivo CSV
```

---

## 23. Dependencias y configuración

El módulo Java requiere:

```java
requires java.sql;
requires org.xerial.sqlitejdbc;
requires java.desktop;
```

### `java.sql`

Permite utilizar JDBC y las clases relacionadas con bases de datos.

### `org.xerial.sqlitejdbc`

Corresponde al driver utilizado para conectarse a SQLite.

### `java.desktop`

Permite utilizar Java Swing y AWT para la interfaz gráfica.

---

### 23.1 Exportación CSV

La funcionalidad CSV utiliza clases estándar como:

```java
java.io.PrintWriter
java.io.IOException
java.nio.file.Files
java.nio.file.Paths
java.nio.charset.StandardCharsets
```

Estas clases forman parte de los módulos estándar de Java y no requieren incorporar una biblioteca externa al proyecto.

---

### 23.2 Driver SQLite

El driver de SQLite se mantiene en el directorio:

```text
lib
```

del proyecto.

---

### 23.3 Archivos ignorados por Git

La base de datos local y sus archivos auxiliares pueden ignorarse mediante `.gitignore`.

Por ejemplo:

```text
club_deportivo.db
club_deportivo.db-journal
club_deportivo.db-wal
club_deportivo.db-shm
```

De esta forma cada entorno de ejecución puede mantener su propia base local.

Los archivos CSV generados por el usuario corresponden a salidas de la aplicación y no forman parte obligatoria del código fuente.

---

## 24. Diagramas del sistema

Dentro del directorio de planificación se mantienen diagramas que representan distintos aspectos del sistema.

---

### 24.1 Diagrama de clases UML

El archivo:

```text
Diagrama de Clases UML.md
```

representa:

- clases;
- atributos;
- métodos;
- herencia;
- composición;
- dependencias;
- controlador;
- persistencia;
- interfaces;
- excepciones.

La operación:

```java
SistemaClub.exportarSociosCSV(String rutaArchivo)
```

forma parte de las responsabilidades actuales del controlador.

---

### 24.2 Diagrama Entidad-Relación

Representa las entidades persistidas en SQLite:

```text
SOCIOS
ACTIVIDADES
RESERVAS
```

Una reserva contiene claves hacia un socio y una actividad.

Conceptualmente:

```text
SOCIOS 1 -------- N RESERVAS N -------- 1 ACTIVIDADES
```

---

## 25. Estado actual del proyecto

Actualmente se encuentran implementadas las principales funcionalidades del sistema:

- modelo de socios;
- modelo de actividades;
- herencia de actividades;
- polimorfismo;
- modelo de reservas;
- gestión de socios;
- gestión de actividades;
- gestión de reservas;
- control de morosidad;
- control de cupo máximo;
- facturación;
- pago total;
- abono parcial;
- cobro mensual;
- eliminación lógica de socios;
- eliminación lógica de actividades;
- colecciones del Java Collections Framework;
- colecciones anidadas;
- sobrecarga;
- sobrescritura;
- persistencia SQLite;
- carga por lotes;
- guardado por lotes;
- reconstrucción de relaciones;
- claves foráneas;
- excepciones de negocio;
- excepciones de persistencia;
- guardado manual;
- guardado al salir;
- interfaz de consola;
- interfaz gráfica mediante Swing;
- exportación de socios a CSV desde la lógica común de `SistemaClub`;
- acceso a la exportación desde la interfaz de consola.

---

## 26. Elementos pendientes antes del cierre

Antes de considerar la versión final del proyecto terminada, se deben revisar principalmente:

- completar la paridad funcional entre consola y ventana;
- integrar la exportación CSV en `MenuVentana`;
- revisar modificaciones y reactivaciones pendientes de la interfaz gráfica;
- probar todas las acciones gráficas sobre información cargada desde SQLite;
- probar guardado y reapertura después de utilizar la GUI;
- realizar una prueba integral final;
- actualizar el UML después de cualquier modificación restante;
- actualizar esta documentación después de los últimos cambios;
- revisar documentación interna y Javadoc;
- revisar instrucciones de instalación y ejecución.

---

## 27. Mejoras futuras posibles

Más allá de los requerimientos actuales, existen distintas mejoras posibles.

### Transacciones SQLite

Actualmente el guardado por lotes realiza varias operaciones consecutivas.

Podría utilizarse una transacción:

```text
BEGIN
  guardar socios
  guardar actividades
  limpiar reservas
  guardar reservas
COMMIT
```

y ejecutar:

```text
ROLLBACK
```

si alguna operación falla.

Esto permitiría asegurar que el estado completo se guarde de forma atómica.

---

### Generación de identificadores de reserva

Actualmente el identificador se genera a partir del tiempo del sistema.

Una implementación futura podría utilizar:

- un contador persistente;
- UUID;
- autoincremento de SQLite.

Esto reduciría la posibilidad de colisiones.

---

### Exportaciones adicionales

El mismo mecanismo utilizado para los socios podría ampliarse a:

- actividades;
- reservas;
- socios morosos;
- facturación;
- eventos.

---

### Pruebas automatizadas

Se podrían incorporar pruebas unitarias e integrales mediante herramientas como JUnit.

Actualmente la verificación del funcionamiento se realiza principalmente mediante pruebas funcionales del programa.

---

## 28. Consideraciones de diseño

Durante el desarrollo se buscó mantener una separación clara de responsabilidades.

### Vista

Las clases de vista:

```text
MenuConsola
MenuVentana
```

se encargan principalmente de:

- recibir información del usuario;
- mostrar información;
- solicitar acciones al controlador.

No deberían modificar directamente las colecciones internas ni acceder directamente a SQLite.

---

### Controlador

`SistemaClub` coordina:

- socios;
- actividades;
- reservas;
- reglas de negocio;
- facturación;
- carga y guardado;
- exportación de datos.

---

### Persistencia

`DBConnection` concentra las operaciones específicas de SQLite.

De esta manera un cambio en la implementación de persistencia requiere menos modificaciones sobre las vistas y entidades del dominio.

---

### Exportación

La exportación CSV también utiliza esta separación.

La interfaz determina:

```text
qué archivo desea generar el usuario
```

mientras que `SistemaClub` determina:

```text
qué socios se exportan
qué información se escribe
cómo se estructura cada registro
```

Esto permite reutilizar exactamente la misma lógica desde consola y GUI.

---

### Polimorfismo

Para las actividades se evita comprobar explícitamente sus tipos mediante:

```java
instanceof
```

La diferenciación se realiza mediante métodos polimórficos y sobrescritura.

Esto mantiene el código más extensible y coherente con el diseño orientado a objetos.

---

## 29. Conclusión

El proyecto evolucionó desde un conjunto de clases de dominio y operaciones almacenadas únicamente en memoria hasta un sistema capaz de administrar información completa de un club deportivo y conservar su estado entre ejecuciones.

La incorporación de SQLite permitió persistir socios, actividades y reservas.

El uso de colecciones permitió representar tanto registros principales como relaciones anidadas.

La herencia y el polimorfismo permitieron trabajar con diferentes tipos de actividades utilizando una estructura común, evitando comprobaciones explícitas mediante `instanceof`.

Las excepciones permiten diferenciar errores de negocio, problemas de conexión y problemas de persistencia.

El sistema también incorpora dos mecanismos de interacción: una interfaz de consola y una interfaz gráfica mediante Swing, ambas construidas sobre la misma instancia de `SistemaClub`.

Finalmente, la exportación de socios a CSV amplía las funcionalidades administrativas permitiendo obtener una copia externa de los registros del sistema sin acoplar esta operación a una interfaz específica.

El proyecto se encuentra actualmente en su etapa de cierre, concentrándose principalmente en completar la paridad funcional de la interfaz gráfica, integrar en ella la exportación CSV, actualizar la documentación después de los últimos cambios y ejecutar las pruebas integrales previas a la entrega.