package com.diedari.jimdur.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_categoria", nullable = false, unique = true)
    private String nombre;

    @Column(name = "slug_categoria", nullable = false, unique = true)
    private String slug;

    @Column(name = "descripcion_categoria", nullable = false)
    private String descripcion;

    @Column(name = "estado_activa", nullable = false)
    private boolean activa; // true = activa, false = inactiva

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Producto> productos;

    public Categoria() {
    }

    public Categoria(Long id, String nombreCategoria, String slug, String descripcion, boolean estado,
            List<Producto> productos) {
        this.id = id;
        this.nombre = nombreCategoria;
        this.slug = slug;
        this.descripcion = descripcion;
        this.activa = estado;
        this.productos = productos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombreCategoria) {
        this.nombre = nombreCategoria;
        // Al cambiar el nombre, también actualizamos el slug
        this.slug = generarSlug(nombreCategoria);
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    // Método para generar el slug automáticamente desde el nombre
    private String generarSlug(String nombreCategoria) {
        return nombreCategoria.toLowerCase().replace(" ", "-").replaceAll("[^a-z0-9-]", "");
    }
}
