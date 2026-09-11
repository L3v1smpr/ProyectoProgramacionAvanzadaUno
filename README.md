# Sistema de Gestión de Club Deportivo

Proyecto desarrollado para la asignatura **Programación Avanzada**.

El sistema permite administrar socios, actividades, reservas y procesos básicos de facturación dentro de un club deportivo.

La aplicación fue desarrollada en **Java JDK 11**, utiliza **SQLite** para la persistencia de datos y dispone de dos interfaces de usuario:

- interfaz por consola;
- interfaz gráfica mediante Java Swing.

---

## Integrantes

- Cristian Ávalos
- Alex Reyes
- Elvis Peñaloza

---

# 1. Requisitos

Para ejecutar el proyecto se recomienda disponer de:

- **Java JDK 11**;
- **Eclipse IDE** o un entorno compatible con proyectos Java;
- un sistema operativo con soporte para Java Desktop/Swing.

El driver JDBC de SQLite ya se encuentra incluido en el proyecto:

```text
lib/sqlite-jdbc-3.53.4.0.jar
```

Por esta razón no es necesario descargar SQLite JDBC por separado.

---

# 2. Descargar el proyecto

Desde el repositorio de GitHub:

1. Presionar el botón **Code**.
2. Seleccionar **Download ZIP**.
3. Extraer el archivo ZIP en una carpeta local.

La carpeta extraída debe contener una estructura similar a:

```text
ProyectoProgramacionAvanzadaUno/
├── src/
├── lib/
├── planificacion/
├── .project
├── .classpath
├── .gitignore
├── README.md
└── Reporte Proyecto Club Actividades.docx
```

---

# 3. Importar el proyecto en Eclipse

1. Abrir Eclipse.
2. Seleccionar:

```text
File -> Import
```

3. Elegir:

```text
General -> Existing Projects into Workspace
```

4. Presionar **Next**.
5. En **Select root directory**, elegir la carpeta donde se extrajo el proyecto.
6. Eclipse debería detectar automáticamente:

```text
ProyectoProgramacionAvanzadaUno
```

7. Seleccionar el proyecto.
8. Presionar **Finish**.

---

# 4. Verificar Java JDK 11

El proyecto fue desarrollado utilizando:

```text
Java JDK 11
```

Para verificar la versión utilizada por Eclipse:

```text
Project -> Properties -> Java Build Path
```

También puede revisarse en:

```text
Project -> Properties -> Java Compiler
```

Si Eclipse utiliza una versión distinta y aparecen errores de compatibilidad, debe configurarse un JDK 11 instalado en el sistema.

---

# 5. Verificar la dependencia SQLite

El proyecto utiliza el driver:

```text
lib/sqlite-jdbc-3.53.4.0.jar
```

El archivo `.classpath` ya incorpora esta biblioteca.

Si Eclipse no reconoce automáticamente el driver:

1. Clic derecho sobre el proyecto.
2. Seleccionar:

```text
Properties -> Java Build Path -> Libraries
```

3. Agregar el archivo:

```text
lib/sqlite-jdbc-3.53.4.0.jar
```

4. Aplicar los cambios.

El módulo Java utiliza:

```java
requires java.sql;
requires org.xerial.sqlitejdbc;
requires java.desktop;
```

---

# 6. Ejecutar el programa

La clase principal se encuentra en:

```text
src/main/Main.java
```

Para ejecutar el programa:

1. Abrir `Main.java`.
2. Hacer clic derecho dentro del archivo.
3. Seleccionar:

```text
Run As -> Java Application
```

Al iniciar se mostrará:

```text
=== BIENVENIDO AL SISTEMA DEL CLUB ===

Que interfaz desea utilizar?
1. Modo Consola (Texto)
2. Modo Ventana (Grafico)
```

Ingrese:

```text
1
```

para utilizar la interfaz por consola.

Ingrese:

```text
2
```

para utilizar la interfaz gráfica.

---

# 7. Primera ejecución

El sistema utiliza una base de datos local llamada:

```text
club_deportivo.db
```

Al iniciar:

1. `Main` crea una instancia de `SistemaClub`.
2. Se ejecuta la carga de datos desde SQLite.
3. Si no existen socios ni actividades almacenados, el sistema carga automáticamente datos iniciales.
4. Estos datos se guardan inmediatamente en la base de datos.
5. Finalmente se solicita al usuario elegir entre consola o interfaz gráfica.

Los datos iniciales permiten probar las funcionalidades principales desde la primera ejecución.

En ejecuciones posteriores, los datos se recuperan desde SQLite y no vuelven a duplicarse.

---

# 8. Base de datos local

La aplicación utiliza SQLite y genera localmente:

```text
club_deportivo.db
```

La base contiene tres tablas principales:

```text
SOCIOS
ACTIVIDADES
RESERVAS
```

Los archivos de la base de datos están excluidos del repositorio mediante `.gitignore`:

```text
club_deportivo.db
club_deportivo.db-journal
club_deportivo.db-wal
club_deportivo.db-shm
```

Por esta razón cada copia del proyecto mantiene su propia base local.

---

# 9. Probar nuevamente una primera ejecución

Si se desea reiniciar completamente los datos locales:

1. Cerrar el programa.
2. Eliminar el archivo:

```text
club_deportivo.db
```

3. Ejecutar nuevamente `Main.java`.

El sistema detectará una base vacía y volverá a generar los datos iniciales.

> Esta operación elimina los datos guardados localmente en la base de datos actual.

---

# 10. Uso del modo consola

Después de seleccionar:

```text
1. Modo Consola
```

se muestran menús de texto desde los cuales se pueden administrar:

- actividades;
- socios;
- reservas;
- facturación;
- cobro mensual;
- exportación CSV;
- guardado manual;
- salida del programa.

La navegación se realiza ingresando el número correspondiente a cada opción.

## 10.1 Gestión de socios

Desde el módulo de socios se pueden realizar operaciones como:

- registrar un socio;
- buscar un socio por RUT;
- modificar sus datos;
- desactivar un socio;
- reactivar un socio;
- listar socios;
- visualizar socios con deuda.

El RUT funciona como identificador principal del socio.

La desactivación corresponde a una baja lógica, por lo que el socio permanece almacenado y puede reactivarse posteriormente.

## 10.2 Gestión de actividades

El sistema permite registrar tres tipos de actividades:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

Las operaciones disponibles incluyen:

- registrar actividad;
- buscar actividad;
- modificar actividad;
- desactivar actividad;
- reactivar actividad;
- listar actividades;
- visualizar eventos.

Cada actividad posee un identificador propio.

## 10.3 Gestión de reservas

Desde el módulo de reservas se puede:

- agendar una reserva;
- modificar una reserva;
- cancelar o eliminar una reserva;
- visualizar las reservas registradas.

Para realizar una reserva deben existir previamente:

- el socio;
- la actividad.

Además, el sistema aplica reglas de negocio relacionadas con morosidad y cupo máximo.

## 10.4 Facturación

Las funciones de facturación permiten:

- consultar la deuda de un socio;
- pagar la deuda completa;
- realizar un abono parcial;
- generar el cobro mensual.

El cobro mensual utilizado actualmente es:

```text
$10.000 CLP
```

y se aplica a los socios activos.

## 10.5 Guardado manual en consola

El menú de consola dispone de una opción para guardar manualmente.

El guardado utiliza:

```java
SistemaClub.guardarDatosBatch()
```

La información se almacena en SQLite.

## 10.6 Salir desde consola

Al seleccionar la opción de salida, el sistema intenta guardar los datos antes de finalizar.

Si ocurre un problema de persistencia, se informa el error al usuario.

---

# 11. Uso del modo ventana

Después de seleccionar:

```text
2. Modo Ventana
```

se inicia la interfaz gráfica desarrollada con Java Swing.

La ventana principal utiliza un:

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

Cuando se encuentra disponible, la interfaz utiliza el estilo visual **Nimbus**.

---

# 12. Pestaña Socios

Desde la pestaña de socios se puede:

- registrar nuevos socios;
- buscar por RUT;
- modificar información;
- desactivar;
- reactivar;
- filtrar socios deudores;
- exportar socios a CSV.

La tabla permite visualizar la información administrativa almacenada.

---

# 13. Pestaña Actividades

Desde la pestaña de actividades se pueden registrar:

```text
ClaseGrupal
EntrenamientoLibre
Evento
```

También permite:

- buscar;
- modificar;
- desactivar;
- reactivar;
- listar actividades;
- filtrar eventos.

Los campos mostrados cambian según el tipo de actividad ingresada.

---

# 14. Pestaña Reservas

Desde la pestaña de reservas se puede:

- ingresar el RUT del socio;
- indicar la actividad;
- registrar una fecha;
- agendar una reserva;
- modificar una reserva;
- cancelar una reserva;
- visualizar las reservas existentes.

Antes de agendar, el sistema valida las reglas de negocio correspondientes.

---

# 15. Pestaña Facturación

Desde la pestaña de facturación se puede:

- buscar un socio por RUT;
- consultar su deuda;
- visualizar su estado de morosidad;
- pagar la deuda completa;
- realizar un abono parcial;
- ejecutar el cobro mensual.

---

# 16. Guardado manual en la interfaz gráfica

En la zona inferior de la ventana se encuentra el botón:

```text
Guardar modificaciones
```

Al presionarlo, se ejecuta el guardado del estado actual en SQLite.

Si el proceso termina correctamente, se muestra un mensaje de confirmación.

---

# 17. Cerrar la interfaz gráfica

Al intentar cerrar la ventana se muestra una confirmación.

El usuario puede:

```text
Guardar y salir
Salir sin guardar
Cancelar
```

Si se elige guardar, la aplicación utiliza:

```java
guardarDatosBatch()
```

antes de cerrar.

---

# 18. Reglas de negocio importantes

## 18.1 Morosidad

Un socio marcado como moroso no puede realizar una nueva reserva.

Si se intenta registrar una, el sistema utiliza:

```java
MorosidadException
```

para impedir la operación.

## 18.2 Cupo máximo

Cada actividad posee un cupo máximo.

Si la cantidad de reservas que ocupan cupo alcanza ese límite, no pueden registrarse nuevos participantes.

En este caso se utiliza:

```java
CupoMaximoException
```

Las reservas canceladas no ocupan cupo.

## 18.3 Socios y actividades inactivas

Socios y actividades pueden desactivarse sin eliminarse físicamente.

Esto permite conservar la información y reactivarla posteriormente.

---

# 19. Exportación de socios a CSV

La exportación está disponible tanto en:

```text
MenuConsola
MenuVentana
```

La lógica se encuentra centralizada en:

```java
SistemaClub.exportarSociosCSV(String rutaArchivo)
```

El archivo generado contiene:

```text
RUT
Nombre
Edad
Deuda
Moroso
Estado
CantidadReservas
```

El separador utilizado es:

```text
;
```

El archivo utiliza codificación UTF-8 y puede abrirse en aplicaciones de hoja de cálculo.

## 19.1 Exportar desde consola

Desde el menú correspondiente:

1. seleccionar la opción de exportación;
2. ingresar la ruta o nombre solicitado por el programa;
3. confirmar la operación;
4. abrir el archivo generado con una aplicación compatible con CSV.

## 19.2 Exportar desde la interfaz gráfica

Desde la pestaña de socios:

1. seleccionar la opción de exportación;
2. se abrirá un `JFileChooser`;
3. elegir la ubicación del archivo;
4. confirmar el guardado.

El contenido se genera utilizando la misma lógica que la interfaz de consola.

---

# 20. Datos incluidos en la primera ejecución

La primera carga incorpora información de ejemplo para facilitar las pruebas.

Incluye:

- varios socios;
- al menos un socio moroso;
- un socio inactivo;
- distintos tipos de actividades;
- una actividad inactiva;
- reservas con diferentes estados.

Esto permite comprobar funciones de búsqueda, reactivación, morosidad, reservas y persistencia sin ingresar toda la información manualmente.

---

# 21. Persistencia y reinicio

Para comprobar que la persistencia funciona:

1. ejecutar el programa;
2. crear o modificar información;
3. guardar;
4. cerrar la aplicación;
5. ejecutar nuevamente `Main.java`;
6. verificar que los datos continúan disponibles.

La carga se realiza automáticamente al inicio.

---

# 22. Estructura del proyecto

```text
ProyectoProgramacionAvanzadaUno/
│
├── src/
│   ├── main/
│   │   └── Main.java
│   │
│   ├── modelo/
│   │   ├── Socio.java
│   │   ├── Actividad.java
│   │   ├── ClaseGrupal.java
│   │   ├── EntrenamientoLibre.java
│   │   ├── Evento.java
│   │   ├── Reserva.java
│   │   ├── EstadoReserva.java
│   │   ├── MorosidadException.java
│   │   ├── CupoMaximoException.java
│   │   ├── ConexionBDException.java
│   │   └── PersistenciaDatosException.java
│   │
│   ├── controlador/
│   │   ├── SistemaClub.java
│   │   └── DBConnection.java
│   │
│   ├── vista/
│   │   ├── MenuConsola.java
│   │   └── MenuVentana.java
│   │
│   └── module-info.java
│
├── lib/
│   └── sqlite-jdbc-3.53.4.0.jar
│
├── planificacion/
│   ├── Diagrama de Clases UML.md
│   ├── Diagrama Entidad-Relacion.md
│   └── Documentacion-Sistema.md
│
├── Reporte Proyecto Club Actividades.docx
├── README.md
├── .classpath
├── .project
└── .gitignore
```

---

# 23. Arquitectura general

El proyecto mantiene una separación entre:

```text
modelo
controlador
vista
main
```

Las vistas interactúan principalmente con:

```java
SistemaClub
```

y no acceden directamente a SQLite.

Las operaciones de persistencia se concentran en:

```java
DBConnection
```

---

# 24. Documentación interna

Dentro del directorio:

```text
planificacion/
```

se incluyen documentos relacionados con el diseño y la evolución del proyecto.

Entre ellos:

```text
Diagrama de Clases UML.md
Diagrama Entidad-Relacion.md
Documentacion-Sistema.md
```

El código fuente también incorpora documentación Javadoc.

---

# 25. Solución de problemas

## 25.1 Eclipse no reconoce el proyecto

Verificar que la carpeta seleccionada al importar contenga:

```text
.project
.classpath
src/
```

Utilizar:

```text
File -> Import -> General -> Existing Projects into Workspace
```

## 25.2 Error relacionado con SQLite JDBC

Verificar que exista:

```text
lib/sqlite-jdbc-3.53.4.0.jar
```

y que Eclipse lo tenga agregado al Build Path.

## 25.3 Error con `org.xerial.sqlitejdbc`

Revisar:

```text
src/module-info.java
```

y verificar que contenga:

```java
requires org.xerial.sqlitejdbc;
```

También verificar que el JAR esté configurado como módulo dentro del Build Path.

## 25.4 No aparece la interfaz gráfica

Verificar que el entorno utilizado tenga soporte para Java Desktop/Swing.

El módulo requiere:

```java
requires java.desktop;
```

## 25.5 Los datos antiguos siguen apareciendo

El sistema guarda la información en:

```text
club_deportivo.db
```

como separador si la aplicación solicita configurarlo manualmente.

El archivo utiliza UTF-8.

---

# 26. Recomendaciones para probar el proyecto

Una prueba rápida del sistema puede realizarse siguiendo este orden:

1. ejecutar `Main.java`;
2. comprobar que se cargan los datos iniciales;
3. entrar al modo consola;
4. listar socios y actividades;
5. probar búsqueda y modificación;
6. probar una reserva;
7. probar facturación;
8. guardar y salir;
9. volver a ejecutar y comprobar persistencia;
10. iniciar el modo ventana;
11. probar búsqueda, modificación, reactivación y reservas;
12. exportar los socios a CSV;
13. guardar desde la interfaz gráfica;
14. cerrar y volver a ejecutar;
15. comprobar nuevamente la persistencia.

---

# 27. Consideraciones finales

El proyecto utiliza:

- Java JDK 11;
- programación orientada a objetos;
- encapsulamiento;
- herencia;
- polimorfismo;
- sobrecarga;
- sobrescritura;
- Java Collections Framework;
- colecciones anidadas;
- excepciones personalizadas;
- SQLite;
- JDBC;
- Java Swing;
- persistencia por lotes;
- exportación CSV;
- Javadoc;
- Git y GitHub.

La lógica relacionada con actividades utiliza polimorfismo y sobrescritura, evitando comprobaciones explícitas con `instanceof`.
