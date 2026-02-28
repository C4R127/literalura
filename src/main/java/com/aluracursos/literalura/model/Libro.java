package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    private Autor autor;

    private String idioma;
    private Double numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();

        // Aplicamos la regla del desafío: guardar solo el primer idioma
        if (datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()) {
            this.idioma = datosLibro.idiomas().get(0);
        }

        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        this.autor = autor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Double getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Double numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }

    @Override
    public String toString() {
        return "\n--- LIBRO ---" +
                "\nTítulo: " + titulo +
                "\nAutor: " + (autor != null ? autor.getNombre() : "Desconocido") +
                "\nIdioma: " + idioma +
                "\nDescargas: " + numeroDeDescargas +
                "\n-------------";
    }
}