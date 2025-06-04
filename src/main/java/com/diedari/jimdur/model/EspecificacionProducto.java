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
@Table(name = "EspecificacionProducto",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_producto", "nombre"},
                           name = "uk_producto_especificacion")
       })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EspecificacionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre de la especificación es obligatorio")
    @Size(max = 100, message = "El nombre de la especificación no puede exceder los 100 caracteres")
    private String nombre;

    @Column(name = "valor", nullable = false)
    @NotBlank(message = "El valor de la especificación es obligatorio")
    @Size(max = 255, message = "El valor de la especificación no puede exceder los 255 caracteres")
    private String valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;
}
