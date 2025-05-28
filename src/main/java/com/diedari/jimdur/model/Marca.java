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
@Table(name = "Marca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_marca", unique = true)
    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 100, message = "El nombre de la marca no puede exceder 100 caracteres")
    private String nombreMarca;

    @Column(name = "descripcion_marca")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcionMarca;

    @Column(name = "pais_origen_marca")
    @Size(max = 100, message = "El país de origen no puede exceder 100 caracteres")
    private String paisOrigenMarca;

    @Column(name = "sitio_web_marca")
    @Size(max = 255, message = "El sitio web no puede exceder 255 caracteres")
    private String sitioWebMarca;

    @Column(name = "logourl_marca")
    @Size(max = 255, message = "La URL del logo no puede exceder 255 caracteres")
    private String logourlMarca;

    @Column(name = "slug_marca", unique = true)
    @NotBlank(message = "El slug de la marca es obligatorio")
    @Size(max = 255, message = "El slug no puede exceder 255 caracteres")
    private String slugMarca;

    @Column(name = "estado_marca")
    @NotNull(message = "El estado de la marca es obligatorio")
    private Boolean estadoMarca;

    // Relaciones
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;
}
