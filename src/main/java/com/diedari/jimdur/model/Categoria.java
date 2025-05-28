package com.diedari.jimdur.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_categoria", unique = true)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 255, message = "El nombre de la categoría no puede exceder 255 caracteres")
    private String nombreCategoria;

    @Column(name = "descripcion_categoria")
    @NotBlank(message = "La descripción de la categoría es obligatoria")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcionCategoria;

    @Column(name = "icono_categoria")
    @NotBlank(message = "El icono de la categoría es obligatorio")
    @Size(max = 255, message = "El icono no puede exceder 255 caracteres")
    private String iconoCategoria;

    @Column(name = "slug_categoria", unique = true)
    @NotBlank(message = "El slug de la categoría es obligatorio")
    @Size(max = 255, message = "El slug no puede exceder 255 caracteres")
    private String slugCategoria;

    @Column(name = "estado_activa")
    @NotNull(message = "El estado de la categoría es obligatorio")
    private Boolean estadoActiva;

    // Relaciones
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;
}