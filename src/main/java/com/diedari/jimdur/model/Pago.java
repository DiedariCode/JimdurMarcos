package com.diedari.jimdur.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo")
    private MetodoPago metodo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPago estado;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    // Relaciones
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", unique = true)
    private Pedido pedido;
    
    public enum MetodoPago {
        tarjeta, transferencia, yape
    }
    
    public enum EstadoPago {
        confirmado, fallido, pendiente
    }
}