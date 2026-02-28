package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdioma(String idioma);

    // Nueva Derived Query para buscar si un libro ya existe
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);
}