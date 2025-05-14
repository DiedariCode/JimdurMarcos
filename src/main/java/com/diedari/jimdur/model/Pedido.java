package com.diedari.jimdur.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    private Date fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pago pago;

    @OneToOne(mappedBy = "pedido")
    private Boleta boleta;

    // Constructor vacío (obligatorio para JPA)
    public Pedido() {
    }

    // Constructor con todos los campos
    public Pedido(Integer idPedido, Date fecha, Estado estado, Double total, Usuario usuario,
            Direccion direccion, List<DetallePedido> detalles, Pago pago, Boleta boleta) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.usuario = usuario;
        this.direccion = direccion;
        this.detalles = detalles;
        this.pago = pago;
        this.boleta = boleta;
    }

    // Getters y Setters

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Boleta getBoleta() {
        return boleta;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", total=" + total +
                ", usuario=" + (usuario != null ? usuario.getId() : null) +
                ", direccion=" + (direccion != null ? direccion.getIdDireccion() : null) +
                ", detalles=" + (detalles != null ? detalles.size() : 0) +
                ", pago=" + (pago != null ? pago.getIdPago() : null) +
                ", boleta=" + (boleta != null ? boleta.getIdBoleta() : null) +
                '}';
    }

    // Enumeración Estado
    public enum Estado {
        pendiente, pagado, enviado, entregado, cancelado
    }
}
