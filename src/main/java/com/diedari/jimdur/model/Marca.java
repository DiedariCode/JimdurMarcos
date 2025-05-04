package com.diedari.jimdur.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "marca")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_marca", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "descripcion_marca", length = 255)
    private String descripcion;

    @Column(name = "logourl_marca", length = 255)
    private String logoUrl;

    @Column(name = "paisOrigen_marca", length = 100)
    private String paisOrigen;

    @Column(name = "sitioWeb_marca", length = 255)
    private String sitioWeb;

    @Column(name = "estado_marca", nullable = false)
    private boolean activo;

    @OneToMany(mappedBy = "marca")
    @JsonIgnore
    private List<Producto> productos;

    public Marca() {
    }

    public Marca(Long id, String nombre, String descripcion, String logoUrl, String paisOrigen, String sitioWeb,
            boolean activo, List<Producto> productos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.logoUrl = logoUrl;
        this.paisOrigen = paisOrigen;
        this.sitioWeb = sitioWeb;
        this.activo = activo;
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
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Producto> getProductos() {
        return productos;
    }
    
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

}
