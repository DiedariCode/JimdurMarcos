package com.diedari.jimdur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "Favorito", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "producto_id"}, name = "uk_favorito_usuario_producto")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime fechaAgregado;
} 