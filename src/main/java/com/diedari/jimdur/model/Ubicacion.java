package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUbicacion;

    @Column(unique = true)
    private String codigo;

    private String nombre;

    @Column(name = "capacidad")
    private Integer capacidad;

    private String descripcion;

    @OneToMany(mappedBy = "ubicacion")
    private List<Producto> productos;

    public Ubicacion() {
    }

    public Ubicacion(Integer idUbicacion, String nombre, String descripcion, List<Producto> productos, String codigo, Integer capacidad) {
        this.idUbicacion = idUbicacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = productos;
        this.codigo = codigo;
        this.capacidad = capacidad;
    }

    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
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

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    
}
