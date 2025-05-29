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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Imagen_Producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;

    @Column(name = "nombre_archivo")
    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    private String nombreArchivo;

    @Column(name = "es_portada")
    private Boolean esPortada; // Indica si es la imagen principal del producto

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    public static class ImagenProductoBuilder {
        public ImagenProductoBuilder nombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
            return this;
        }
    }
}
