package com.diedari.jimdur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MedioPago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedioPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoMedioPago tipo;

    @Column(name = "datos", nullable = false)
    private String datos;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public enum TipoMedioPago {
        TARJETA, TRANSFERENCIA, YAPE, EFECTIVO, OTRO
    }
} 