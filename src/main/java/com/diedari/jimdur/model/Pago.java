package com.diedari.jimdur.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @Enumerated(EnumType.STRING)
    private Metodo metodo;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Date fechaPago;

    @OneToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    // Constructor vacío (obligatorio para JPA)
    public Pago() {
    }

    // Constructor con todos los campos
    public Pago(Integer idPago, Metodo metodo, Estado estado, Date fechaPago, Pedido pedido) {
        this.idPago = idPago;
        this.metodo = metodo;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.pedido = pedido;
    }

    // Getters y Setters

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public void setMetodo(Metodo metodo) {
        this.metodo = metodo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", metodo=" + metodo +
                ", estado=" + estado +
                ", fechaPago=" + fechaPago +
                ", pedido=" + (pedido != null ? pedido.getIdPedido() : null) +
                '}';
    }

    // Enumeración Metodo
    public enum Metodo {
        yape, transferencia, tarjeta
    }

    // Enumeración Estado
    public enum Estado {
        pendiente, confirmado, fallido
    }
}
