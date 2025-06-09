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
@Table(name = "Imagen_Producto",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_producto", "nombre_archivo"},
                           name = "uk_producto_imagen")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;

    @Column(name = "nombre_archivo", nullable = false)
    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 255, message = "El nombre del archivo no puede exceder 255 caracteres")
    private String nombreArchivo;

    @Column(name = "es_portada", nullable = false)
    @NotNull(message = "Debe especificar si es la imagen de portada")
    private Boolean esPortada; // Indica si es la imagen principal del producto

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto es obligatorio")
    private Producto producto;

    public static class ImagenProductoBuilder {
        public ImagenProductoBuilder nombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
            return this;
        }
    }
}
