# Documentación del Sistema de Gestión de Club Deportivo

## 1. Descripción general

Este proyecto corresponde al desarrollo de un sistema de gestión para un club deportivo. Su objetivo principal es centralizar en una sola aplicación el manejo de socios, actividades, reservas y procesos básicos de facturación.

El sistema fue desarrollado en Java JDK11 siguiendo una separación por responsabilidades entre modelo, controlador y vista. A lo largo del desarrollo también se incorporó persistencia local mediante SQLite, de forma que la información registrada por el usuario pueda mantenerse entre distintas ejecuciones del programa.

Actualmente la aplicación puede utilizarse desde consola y cuenta con la estructura necesaria para una interfaz gráfica. La versión funcional desarrollada hasta este punto se encuentra principalmente orientada al uso por consola.

## 2. Objetivo del sistema

El sistema busca resolver tareas comunes dentro de la administración de un club deportivo, entre ellas:

- registrar y administrar socios;
- registrar distintos tipos de actividades;
- crear, modificar y eliminar reservas;
- controlar cupos disponibles;
- impedir reservas cuando un socio presenta morosidad;
- gestionar deuda y pagos;
- generar cobros mensuales;
- mantener la información persistida en una base de datos local.

La idea central del proyecto fue mantener la lógica de negocio separada de la interfaz de usuario, evitando que las clases de vista tengan que encargarse directamente de modificar los datos internos del sistema.

## 3. Arquitectura general

El proyecto se encuentra dividido principalmente en los siguientes paquetes.

### `modelo`

Contiene las clases que representan las entidades y reglas principales del dominio:

- `Socio`
- `Actividad`
- `ClaseGrupal`
- `EntrenamientoLibre`
- `Evento`
- `Reserva`
- `EstadoReserva`
- excepciones personalizadas

### `controlador`

Contiene la lógica principal del sistema y la comunicación con la base de datos.

Las clases principales son:

- `SistemaClub`
- `DBConnection`

`SistemaClub` funciona como punto central para las operaciones del programa. La vista solicita acciones al controlador y este se encarga de trabajar con los objetos del modelo.

`DBConnection`, por otra parte, concentra las operaciones relacionadas con SQLite.

### `vista`

Contiene las clases encargadas de interactuar con el usuario:

- `MenuConsola`
- `MenuVentana`

`MenuConsola` contiene la interfaz funcional basada en texto. `MenuVentana` se encuentra preparada para compartir la misma instancia de `SistemaClub`, aunque la lógica de la interfaz gráfica todavía no está implementada en la versión actual.

### `main`

Contiene el punto de entrada de la aplicación.

La clase `Main` crea una única instancia de `SistemaClub`, carga los datos almacenados y permite al usuario escoger entre la interfaz de consola o la interfaz gráfica.

## 4. Modelo de dominio

### 4.1 Socio

La clase `Socio` representa a una persona registrada en el club.

Entre sus principales datos se encuentran:

- RUT;
- nombre;
- edad;
- deuda;
- estado de morosidad;
- estado activo/inactivo;
- lista de reservas.

Cada socio mantiene internamente su propia colección de reservas. Esto permite representar la relación entre un socio y las actividades que ha reservado.

También contiene operaciones relacionadas con su deuda, como el pago total o el abono parcial.

### 4.2 Actividad

`Actividad` es una clase abstracta que reúne la información común de todas las actividades del club:

- identificador;
- nombre;
- cupo máximo;
- edad mínima;
- estado activo/inactivo.

A partir de ella se implementan tres tipos concretos.

#### ClaseGrupal

Representa una actividad dirigida por un profesor.

Su atributo específico es `profesor`.

#### EntrenamientoLibre

Representa una actividad de entrenamiento que no necesariamente requiere un profesor.

Su atributo específico es `requiereAsistencia`.

#### Evento

Representa una actividad especial realizada en una fecha y lugar determinados.

Sus datos específicos son:

- fecha;
- lugar;
- tipo de evento.

## 5. Uso de herencia y polimorfismo

Una de las decisiones importantes del diseño fue trabajar con `Actividad` como tipo general.

Esto permite que `SistemaClub` mantenga una sola colección:

```java
ArrayList<Actividad>
```

Dentro de esta lista pueden coexistir objetos de tipo `ClaseGrupal`, `EntrenamientoLibre` y `Evento`.

Cada subclase redefine los métodos que necesita, por ejemplo `mostrarDetalles()` y `getTipoActividad()`.

Para la persistencia también se utilizan métodos polimórficos como:

```java
getProfesor()
getRequiereAsistencia()
getFecha()
getLugar()
getTipoEvento()
```

Las implementaciones que no utilizan determinado atributo retornan `null`, mientras que la subclase correspondiente devuelve su valor real.

Este enfoque permitió trabajar con distintos tipos de actividad sin utilizar `instanceof`.

## 6. Gestión de socios

El controlador permite realizar las principales operaciones relacionadas con socios:

- agregar socios;
- modificar sus datos;
- buscar por RUT;
- listar socios activos;
- listar socios deudores;
- activar o desactivar socios;
- gestionar pagos.

La eliminación utilizada desde el flujo normal del programa se basa principalmente en un sistema de **soft-delete**.

En lugar de eliminar definitivamente el objeto, se cambia su atributo `activo` a `false`. De esta forma el registro puede mantenerse en memoria y en la base de datos sin aparecer en los listados normales.

## 7. Gestión de actividades

El sistema permite registrar tres tipos distintos de actividades utilizando sobrecarga del método `agregarActividad()`.

Según los parámetros recibidos, se crea una `ClaseGrupal`, un `EntrenamientoLibre` o un `Evento`.

Además se pueden realizar operaciones como:

- modificar una actividad;
- buscar una actividad;
- listar actividades activas;
- listar eventos;
- activar y desactivar actividades.

Al igual que los socios, las actividades utilizan un atributo `activo` para manejar la eliminación lógica dentro del sistema.

## 8. Gestión de reservas

Las reservas representan la relación entre un socio y una actividad.

Cada `Reserva` contiene:

- identificador de reserva;
- fecha;
- estado;
- RUT del socio;
- ID de la actividad.

Los estados disponibles se encuentran definidos mediante el enum `EstadoReserva`:

```text
PENDIENTE
COMPLETADA
CANCELADA
```

Las reservas no se almacenan en una lista global dentro de `SistemaClub`. Cada objeto `Socio` posee su propia lista interna de reservas.

Aun así, el controlador puede recorrer todos los socios para generar un listado global cuando sea necesario.

## 9. Reglas de negocio de las reservas

Al momento de agendar una reserva se aplican distintas validaciones.

### Morosidad

Un socio con deuda activa no puede crear una nueva reserva. Si se intenta realizar la operación, se lanza una `MorosidadException`.

### Cupo máximo

Antes de registrar una reserva se cuentan las reservas vigentes asociadas a la actividad. Las reservas canceladas no se consideran dentro del cupo ocupado.

Si se alcanza el máximo permitido por la actividad, se lanza una `CupoMaximoException`.

### Existencia de datos

Para poder realizar una reserva deben existir tanto el socio como la actividad. Si alguno de ellos no existe, la operación no se realiza.

## 10. Facturación y morosidad

El sistema incorpora una funcionalidad básica de facturación.

El controlador puede generar un cobro mensual para todos los socios activos. En la implementación actual se aplica una tarifa fija de:

```text
$10.000
```

Al generar el cobro se suma el monto a la deuda existente y el socio pasa a estado moroso.

Posteriormente se puede pagar la deuda completa o realizar un abono parcial. Cuando la deuda queda en cero, el socio deja de encontrarse en estado de morosidad.

## 11. Persistencia de datos

La persistencia se implementó utilizando SQLite mediante JDBC.

El objetivo fue que la información del sistema pudiera recuperarse automáticamente al volver a ejecutar el programa.

La conexión se concentra en la clase `DBConnection`.

La base de datos utilizada es:

```text
club_deportivo.db
```

Este archivo se genera localmente y no se almacena en el repositorio Git.

## 12. Estructura de la base de datos

La base de datos contiene tres tablas principales.

### SOCIOS

Almacena:

- `rut`
- `nombre`
- `edad`
- `deuda`
- `es_moroso`
- `activo`

El RUT funciona como clave primaria.

### ACTIVIDADES

Almacena los atributos comunes y específicos de los distintos tipos de actividad:

- `id_actividad`
- `nombre`
- `cupo_maximo`
- `edad_minima`
- `tipo`
- `profesor`
- `requiere_asistencia`
- `fecha_evento`
- `lugar`
- `tipo_evento`
- `activo`

Las columnas específicas que no corresponden a un determinado tipo de actividad quedan almacenadas como `NULL`.

### RESERVAS

Almacena:

- `id_reserva`
- `fecha`
- `estado`
- `rut_socio`
- `id_actividad`

`rut_socio` es una clave foránea hacia `SOCIOS`.

`id_actividad` es una clave foránea hacia `ACTIVIDADES`.

Las claves foráneas se habilitan al establecer la conexión mediante:

```sql
PRAGMA foreign_keys = ON;
```

## 13. Guardado de datos

El sistema utiliza un guardado por lotes desde `SistemaClub`.

El orden utilizado es:

```text
1. Socios
2. Actividades
3. Reservas
```

Los socios y actividades utilizan operaciones de tipo UPSERT. Esto significa que si el registro no existe se inserta, y si ya existe se actualiza.

En el caso de las reservas se realiza primero una limpieza de la tabla `RESERVAS` y luego se vuelven a guardar todas las reservas que existen actualmente dentro de los socios.

Esta decisión es necesaria porque una reserva eliminada se remueve físicamente de la colección del socio. Si solo se utilizara UPSERT, una reserva eliminada en memoria podría permanecer en SQLite y volver a aparecer en la siguiente ejecución.

## 14. Carga de datos

Al iniciar el programa, `Main` crea una instancia de `SistemaClub` y ejecuta la carga de datos antes de mostrar la interfaz.

El orden de reconstrucción es:

```text
1. Socios
2. Actividades
3. Reservas
```

Este orden es importante porque las reservas dependen de los otros dos tipos de registros.

Primero se reconstruye el mapa de socios. Luego se reconstruye la lista de actividades. Finalmente se leen las reservas y cada una se asocia nuevamente con el socio correspondiente mediante su RUT.

Durante esta última etapa también se comprueba que existan tanto el socio como la actividad referenciada. Si la información almacenada resulta inconsistente, el sistema genera una excepción de persistencia.

## 15. Guardado automático al cerrar

Desde la interfaz de consola el usuario dispone de una opción para guardar manualmente los cambios.

Además, al seleccionar la opción de salir se intenta guardar automáticamente toda la información.

Si el guardado falla, el programa no finaliza inmediatamente. En su lugar informa el problema y permite al usuario continuar, evitando cerrar normalmente después de un error de persistencia.

Este comportamiento fue incorporado para reducir el riesgo de perder cambios realizados durante la sesión.

## 16. Manejo de excepciones

El proyecto utiliza excepciones personalizadas para separar distintos tipos de errores.

### Excepciones de negocio

#### MorosidadException

Se utiliza cuando una operación de reserva no puede realizarse porque el socio presenta morosidad.

#### CupoMaximoException

Se utiliza cuando una actividad ha alcanzado su límite de participantes o cuando se detecta un valor de cupo inválido en operaciones que lo validan.

### Excepciones del sistema

#### ConexionBDException

Representa problemas relacionados directamente con la conexión a SQLite.

#### PersistenciaDatosException

Representa problemas ocurridos durante operaciones de lectura o escritura, datos inválidos almacenados o inconsistencias entre reservas, socios y actividades.

Las excepciones SQL no se propagan directamente hasta la interfaz. `DBConnection` las captura y las transforma en excepciones propias del sistema.

## 17. Colecciones utilizadas

El sistema utiliza más de un tipo de colección según la necesidad.

### HashMap de socios

Los socios se mantienen dentro de:

```java
HashMap<String, Socio>
```

El RUT se utiliza como clave, lo que permite realizar búsquedas directas.

### ArrayList de actividades

Las actividades se mantienen en:

```java
ArrayList<Actividad>
```

La lista contiene objetos de distintos subtipos gracias al uso de herencia y polimorfismo.

### ArrayList de reservas dentro de Socio

Cada socio posee:

```java
ArrayList<Reserva>
```

Esta colección representa una relación anidada y permite que cada socio administre directamente sus propias reservas.

## 18. Sobrecarga y sobrescritura de métodos

Durante el desarrollo se utilizaron ambos conceptos.

### Sobrecarga

`SistemaClub` posee distintas versiones de `agregarActividad()` según el tipo de actividad que se desea crear.

También existen dos formas de realizar pagos de deuda:

```java
pagarFacturacion(String rut)
pagarFacturacion(String rut, int abono)
```

En `Socio` también se encuentran dos versiones de `abonarDeuda()`.

### Sobrescritura

Las subclases de `Actividad` redefinen métodos como:

```java
mostrarDetalles()
getTipoActividad()
```

y los getters específicos que correspondan a cada tipo.

Esto permite que el programa trabaje con referencias de tipo `Actividad` y obtenga el comportamiento correcto según el objeto real.

## 19. Flujo general de ejecución

De forma resumida, el funcionamiento es el siguiente:

```text
Main
  |
  v
crea SistemaClub
  |
  v
cargarDatosBatch()
  |
  +--> SOCIOS
  +--> ACTIVIDADES
  +--> RESERVAS
  |
  v
selección de interfaz
  |
  +--> MenuConsola
  |
  +--> MenuVentana
```

En la interfaz de consola, todas las operaciones se realizan sobre la misma instancia de `SistemaClub`. Esto evita que cada menú trabaje con una copia diferente de la información.

## 20. Dependencias y configuración

El proyecto utiliza JDBC y el driver de SQLite.

El módulo Java declara las siguientes dependencias:

```java
requires java.sql;
requires org.xerial.sqlitejdbc;
```

El driver SQLite se mantiene dentro del proyecto en el directorio `lib`.

La base de datos y sus archivos auxiliares se encuentran ignorados por Git:

```text
club_deportivo.db
club_deportivo.db-journal
club_deportivo.db-wal
club_deportivo.db-shm
```

De esta forma cada equipo de ejecución puede mantener su propia base local sin subir datos generados automáticamente al repositorio.

## 21. Diagramas del sistema

Dentro del directorio de planificación se mantienen dos diagramas principales.

### Diagrama de clases UML

Representa las clases principales, atributos, métodos, herencia, relaciones, dependencias de persistencia y excepciones.

### Diagrama Entidad-Relación

Representa la estructura de SQLite y las relaciones entre:

```text
SOCIOS
ACTIVIDADES
RESERVAS
```

Una reserva pertenece a un socio y a una actividad, por lo que funciona como la entidad que conecta ambos registros.

## 22. Estado actual del proyecto

Hasta esta etapa se encuentra implementado y probado:

- gestión de socios;
- gestión de actividades;
- herencia entre tipos de actividad;
- gestión de reservas;
- validación de cupos;
- validación de morosidad;
- pagos y cobro mensual;
- persistencia de socios;
- persistencia de actividades;
- persistencia de reservas;
- carga automática al iniciar;
- guardado manual;
- guardado automático al salir;
- reconstrucción de relaciones desde SQLite;
- manejo de excepciones de negocio y de sistema;
- interfaz de consola.

La estructura de la interfaz gráfica existe y recibe el mismo controlador, pero su comportamiento todavía se encuentra pendiente de implementación en el estado actual del proyecto.

## 23. Mejoras pendientes o posibles

A partir del estado actual del sistema se podrían incorporar varias mejoras:

- completar la interfaz gráfica;
- ejecutar el guardado completo dentro de una transacción SQLite;
- realizar rollback si una operación del guardado por lotes falla;
- agregar más validaciones de entrada;
- mejorar la generación de identificadores de reserva;
- agregar pruebas automatizadas;
- incorporar documentación Javadoc en las clases y métodos principales;
- ampliar los reportes disponibles;
- realizar una prueba integral final antes de la entrega.

Una mejora especialmente importante sería utilizar una transacción durante el guardado completo. Actualmente las operaciones funcionan correctamente, pero una transacción permitiría garantizar que el lote completo se confirme solo si todas las escrituras fueron exitosas.

## 24. Consideraciones de diseño

Durante el desarrollo se intentó mantener una separación clara de responsabilidades.

La vista no accede directamente a SQLite.

El modelo no se encarga de abrir conexiones a la base de datos.

`SistemaClub` concentra la lógica de negocio y coordina los objetos.

`DBConnection` concentra la persistencia.

También se evitó utilizar comprobaciones explícitas de tipo mediante `instanceof` para trabajar con las actividades. En su lugar se utilizaron herencia, sobrescritura y métodos polimórficos.

Esta separación permite que el sistema sea más fácil de modificar, probar y extender sin tener que rehacer completamente las demás capas.

## 25. Conclusión

El proyecto evolucionó desde un conjunto de clases de dominio y operaciones en memoria hasta un sistema capaz de conservar su estado entre ejecuciones.

La incorporación de SQLite permitió persistir las tres partes principales del sistema: socios, actividades y reservas. Además, el uso de excepciones personalizadas, colecciones, herencia, polimorfismo, sobrecarga y sobrescritura permitió integrar distintos conceptos de programación orientada a objetos dentro de una aplicación funcional.

En su estado actual, la aplicación ya posee un flujo completo de trabajo desde consola: puede cargar datos, operar sobre ellos, aplicar reglas de negocio y volver a guardarlos al finalizar. Las siguientes etapas del proyecto se enfocan principalmente en completar la interfaz gráfica, reforzar la robustez del guardado y realizar la revisión final de documentación y pruebas.