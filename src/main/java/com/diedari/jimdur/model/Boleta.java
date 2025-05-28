package com.diedari.jimdur.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private Long idBoleta;
    
    @Column(name = "serie")
    @Size(max = 255, message = "La serie no puede exceder 255 caracteres")
    private String serie;
    
    @Column(name = "numero")
    private Integer numero;
    
    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;
    
    @Column(name = "total_bruto")
    @DecimalMin(value = "0.0", message = "El total bruto no puede ser negativo")
    private Double totalBruto;
    
    @Column(name = "igv")
    @DecimalMin(value = "0.0", message = "El IGV no puede ser negativo")
    private Double igv;
    
    @Column(name = "total")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private Double total;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio")
    private EstadoEnvio estadoEnvio;
    
    // Relaciones
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", unique = true)
    private Pedido pedido;
    
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleBoleta> detalles;
    
    public enum EstadoEnvio {
        enviada, pendiente, rechazada
    }
}