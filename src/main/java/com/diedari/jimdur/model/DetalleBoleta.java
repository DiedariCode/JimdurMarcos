package com.diedari.jimdur.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DetalleBoleta")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleBoleta;

    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "id_boleta")
    private Boleta boleta;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    // Constructor vacío (obligatorio para JPA)
    public DetalleBoleta() {
    }

    // Constructor con todos los campos
    public DetalleBoleta(Integer idDetalleBoleta, Integer cantidad, Double precioUnitario, Double subtotal,
            Boleta boleta, Producto producto) {
        this.idDetalleBoleta = idDetalleBoleta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.boleta = boleta;
        this.producto = producto;
    }

    // Getters y Setters

    public Integer getIdDetalleBoleta() {
        return idDetalleBoleta;
    }

    public void setIdDetalleBoleta(Integer idDetalleBoleta) {
        this.idDetalleBoleta = idDetalleBoleta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Boleta getBoleta() {
        return boleta;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
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
        return "DetalleBoleta{" +
                "idDetalleBoleta=" + idDetalleBoleta +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                ", boleta=" + (boleta != null ? boleta.getIdBoleta() : null) +
                ", producto=" + (producto != null ? producto.getIdProducto() : null) +
                '}';
    }
}
