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
    // URLS amigables para la categoría
    private String slug;

    @Column(name = "descripcion_categoria", nullable = false)
    private String descripcion;

    @Column(name = "estado_activa", nullable = false)
    private boolean activa; // true = activa, false = inactiva

    @Column(name = "icono_categoria", nullable = false)
    private String iconoCategoria; // Icono de la categoría para la vista de index, agregado el 8 de mayo :V

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL) // Persistencia en cascada para productos
    @JsonIgnore // ! infinito
    private List<Producto> productos;

    public Categoria() {
    }

    public Categoria(Long id, String nombre, String slug, String descripcion, boolean estado, String iconoCategoria,
            List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.slug = slug;
        this.descripcion = descripcion;
        this.activa = estado;
        this.iconoCategoria = iconoCategoria;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.slug = generarSlug(nombre);
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

    public String getIconoCategoria() {
        return iconoCategoria;
    }

    public void setIconoCategoria(String iconoCategoria) {
        this.iconoCategoria = iconoCategoria;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    // Método para generar el slug automáticamente desde el nombre
    private String generarSlug(String nombre) {
        return nombre.toLowerCase().replace(" ", "-").replaceAll("[^a-z0-9-]", "");
    }

}
