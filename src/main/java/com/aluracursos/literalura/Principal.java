package com.aluracursos.literalura;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    // Actualizamos el constructor
    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    === 📚 CATÁLOGO DE LIBROS LITERALURA 📚 ===
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    
                    Ingrese su opción:""";
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroWeb();
                        break;
                    case 2:
                        mostrarLibrosBuscados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresVivosEnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación... ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Error: Por favor, ingrese una opción válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                teclado.nextLine();
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda.resultados().isEmpty()) {
            System.out.println("Libro no encontrado en la API.");
            return; // Salimos del método si no hay resultados
        }

        DatosLibro datosLibro = datosBusqueda.resultados().get(0);

        // 1. Verificamos si el libro ya está guardado
        Optional<Libro> libroExistente = libroRepositorio.findByTituloContainsIgnoreCase(datosLibro.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("\nEl libro ya está registrado en la base de datos.");
            System.out.println(libroExistente.get());
            return; // Salimos del método si el libro ya existe
        }

        // 2. Manejo del Autor
        String nombreAutor = datosLibro.autor().isEmpty() ? "Desconocido" : datosLibro.autor().get(0).nombre();
        Optional<Autor> autorExistente = autorRepositorio.findByNombreIgnoreCase(nombreAutor);

        Autor autor;
        if (autorExistente.isPresent()) {
            // Si existe, lo usamos.
            autor = autorExistente.get();
        } else {
            // Si NO existe, lo creamos.
            if (!datosLibro.autor().isEmpty()) {
                autor = new Autor(datosLibro.autor().get(0));
            } else {
                // Caso borde: El libro no tiene autor en la API
                autor = new Autor();
                autor.setNombre("Desconocido");
            }
            // Guardamos el autor nuevo.
            autor = autorRepositorio.save(autor);
        }

        // 3. Crear y guardar el Libro
        Libro libro = new Libro(datosLibro, autor);
        libroRepositorio.save(libro);

        System.out.println("\n¡Libro guardado exitosamente en la Base de Datos!");
        System.out.println(libro);
    }

    private void mostrarLibrosBuscados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("Aún no has buscado ni guardado ningún libro.");
        } else {
            System.out.println("\n--- LISTA DE LIBROS REGISTRADOS ---");
            libros.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        var idioma = teclado.nextLine();

        // Aquí usamos la Derived Query que ya teníamos
        List<Libro> librosPorIdioma = libroRepositorio.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros registrados en ese idioma.");
        } else {
            System.out.println("\n--- LIBROS EN EL IDIOMA '" + idioma.toUpperCase() + "' ---");
            librosPorIdioma.forEach(System.out::println);

            // ¡NUEVO! Cumpliendo el Paso 10: Usamos Streams para contar la cantidad
            long cantidadDeLibros = librosPorIdioma.stream().count();

            System.out.println("\n📊 *** ESTADÍSTICAS *** 📊");
            System.out.println("Cantidad de libros registrados en el idioma '" + idioma + "': " + cantidadDeLibros);
            System.out.println("**************************\n");
        }
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("Aún no hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n--- LISTA DE AUTORES REGISTRADOS ---");
            autores.forEach(System.out::println);
        }
    }

    private void mostrarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para buscar autores vivos (ej. 1850):");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine(); // Consumir el salto de línea

            // Validación extra para valores ilógicos
            if (anio < 0 || anio > 2026) {
                System.out.println("Por favor, ingrese un año válido.");
                return;
            }

            // Llamamos a la nueva Derived Query (pasamos el año dos veces)
            List<Autor> autores = autorRepositorio.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio, anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores registrados que estuvieran vivos en el año " + anio);
            } else {
                System.out.println("\n--- AUTORES VIVOS EN " + anio + " ---");
                autores.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            // Aquí lidiamos con los valores inválidos (letras en vez de números)
            System.out.println("Error: Formato inválido. Debe ingresar un año en números (ej. 1900).");
            teclado.nextLine(); // Limpiar el buffer para evitar un bucle infinito
        }
    }

}