package com.diedari.jimdur.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CompatibilidadProducto",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_producto", "modelo_compatible"},
                           name = "uk_producto_modelo_compatible")
       })
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompatibilidadProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modelo_compatible", nullable = false)
    @NotBlank(message = "El modelo compatible es obligatorio")
    @Size(max = 100, message = "El modelo compatible no puede exceder los 100 caracteres")
    private String modeloCompatible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
}