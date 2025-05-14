package com.diedari.jimdur.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Boleta")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBoleta;

    private String serie;
    private Integer numero;
    private Date fechaEmision;
    private Double totalBruto;
    private Double igv;
    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estadoEnvio;

    @OneToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles;

    // Constructor vacío (obligatorio para JPA)
    public Boleta() {
    }

    // Constructor con todos los campos
    public Boleta(Integer idBoleta, String serie, Integer numero, Date fechaEmision, Double totalBruto, Double igv,
            Double total, EstadoEnvio estadoEnvio, Pedido pedido, List<DetalleBoleta> detalles) {
        this.idBoleta = idBoleta;
        this.serie = serie;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.totalBruto = totalBruto;
        this.igv = igv;
        this.total = total;
        this.estadoEnvio = estadoEnvio;
        this.pedido = pedido;
        this.detalles = detalles;
    }

    // Getters y Setters

    public Integer getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(Integer idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(Double totalBruto) {
        this.totalBruto = totalBruto;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(EstadoEnvio estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<DetalleBoleta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleBoleta> detalles) {
        this.detalles = detalles;
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "Boleta{" +
                "idBoleta=" + idBoleta +
                ", serie='" + serie + '\'' +
                ", numero=" + numero +
                ", fechaEmision=" + fechaEmision +
                ", totalBruto=" + totalBruto +
                ", igv=" + igv +
                ", total=" + total +
                ", estadoEnvio=" + estadoEnvio +
                ", pedido=" + (pedido != null ? pedido.getIdPedido() : null) +
                '}';
    }

    // Enumeración EstadoEnvio
    public enum EstadoEnvio {
        pendiente, enviada, rechazada
    }
}
