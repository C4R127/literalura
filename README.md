# Literalura - Catálogo de Libros
## Descripción del Proyecto
Literalura es una aplicación de consola desarrollada en Java que permite gestionar un catálogo de libros y autores. El sistema consume datos de la API de Gutendex y los almacena de forma persistente en una base de datos PostgreSQL utilizando Spring Data JPA.

## Funcionalidades Obligatorias
El proyecto incluye las siguientes capacidades de consulta y gestión:

Búsqueda de libro por título: Realiza consultas a la API externa para obtener y registrar el primer resultado coincidente.

Lista de todos los libros: Muestra un listado completo de todas las obras que han sido buscadas y registradas previamente.

Lista de autores: Presenta la información de todos los autores cuyos libros están en la base de datos.

Listar autores vivos en determinado año: Permite filtrar autores que se encontraban con vida en un año específico ingresado por el usuario.

Listar libros por idioma: Exhibe la cantidad de libros registrados en un idioma seleccionado (ej. español, inglés, francés, portugués).

## Tecnologías y Herramientas
Para el desarrollo de este desafío se utilizaron:

Java: Lenguaje de programación principal.

Spring Boot: Framework para la configuración y ejecución de la aplicación.

Spring Data JPA (Hibernate): Para el mapeo de entidades y la persistencia de datos.

PostgreSQL: Sistema de gestión de bases de datos relacionales.

Jackson: Utilizado para la conversión de atributos JSON a objetos Java.

Streams de Java: Implementados para procesar y filtrar colecciones de datos de forma eficiente.

## Detalles Técnicos Relevantes
Persistencia: Se crearon entidades de modelo para Libro y Autor, manteniendo una relación entre ambos mediante sus identificadores (ID).

Consultas Inteligentes: Uso de derived queries para recuperar datos específicos de la base de datos, como el filtrado por idioma o años de vida.

Robustez: El sistema está preparado para lidiar con valores inválidos que el usuario pueda ingresar en las consultas de año.

## Instalación y Configuración
Sigue estos pasos para ejecutar Literalura en tu entorno local:

1. Prerrequisitos
Asegúrate de tener instalados los siguientes componentes:

Java JDK 17 o superior.

Maven (gestor de dependencias).

PostgreSQL (base de datos relacional).

Un IDE como IntelliJ IDEA o Eclipse.

2. Configuración de la Base de Datos
Abre tu terminal de PostgreSQL o pgAdmin 4.

Crea una nueva base de datos ejecutando el siguiente comando SQL:

SQL

```
CREATE DATABASE literalura;
```

3. Configuración del Proyecto
Navega hasta el archivo src/main/resources/application.properties y configura tus credenciales locales de PostgreSQL para que Spring Data JPA pueda conectarse:
```
Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario_aqui
spring.datasource.password=tu_contraseña_aqui
spring.jpa.hibernate.ddl-auto=update
```
4. Ejecución de la Aplicación
Clona este repositorio en tu máquina local.

Importa el proyecto en tu IDE como un proyecto Maven existente.

Ejecuta la clase principal LiteraluraApplication.java.

¡Listo! El menú interactivo aparecerá en tu consola de comandos.
