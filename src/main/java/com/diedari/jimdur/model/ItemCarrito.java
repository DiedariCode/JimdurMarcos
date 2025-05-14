package com.diedari.jimdur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ItemCarrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idItem;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    // Constructor vacío (obligatorio para JPA)
    public ItemCarrito() {
    }

    // Constructor con todos los campos
    public ItemCarrito(Integer idItem, Integer cantidad, Carrito carrito, Producto producto) {
        this.idItem = idItem;
        this.cantidad = cantidad;
        this.carrito = carrito;
        this.producto = producto;
    }

    // Getters y Setters

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "ItemCarrito{" +
                "idItem=" + idItem +
                ", cantidad=" + cantidad +
                ", carrito=" + (carrito != null ? carrito.getIdCarrito() : null) +
                ", producto=" + (producto != null ? producto.getIdProducto() : null) +
                '}';
    }
}
