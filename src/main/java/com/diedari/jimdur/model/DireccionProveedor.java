package com.diedari.jimdur.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Direccion_Proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion_proveedor")
    private Long idDireccionProveedor;

    @Column(name = "etiqueta", nullable = false)
    @NotBlank(message = "La etiqueta es obligatoria")
    @Size(max = 255, message = "La etiqueta no puede exceder 255 caracteres")
    private String etiqueta;

    @Column(name = "calle", nullable = false)
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 255, message = "La calle no puede exceder 255 caracteres")
    private String calle;

    @Column(name = "distrito", nullable = false)
    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 255, message = "El distrito no puede exceder 255 caracteres")
    private String distrito;

    @Column(name = "ciudad", nullable = false)
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 255, message = "La ciudad no puede exceder 255 caracteres")
    private String ciudad;

    @Column(name = "departamento_estado", nullable = false)
    @NotBlank(message = "El departamento o estado es obligatorio")
    @Size(max = 255, message = "El departamento o estado no puede exceder 255 caracteres")
    private String departamentoEstado;

    @Column(name = "codigo_postal", nullable = false)
    @NotBlank(message = "El código postal es obligatorio")
    @Size(max = 20, message = "El código postal no puede exceder 20 caracteres")
    private String codigoPostal;

    @Column(name = "pais", nullable = false)
    @NotBlank(message = "El país es obligatorio")
    @Size(max = 255, message = "El país no puede exceder 255 caracteres")
    private String pais;

    @Column(name = "referencia")
    @Size(max = 255, message = "La referencia no puede exceder 255 caracteres")
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_direccion", nullable = false)
    @NotNull(message = "El tipo de dirección es obligatorio")
    private TipoDireccion tipoDireccion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    @NotNull(message = "El proveedor es obligatorio")
    @ToString.Exclude
    private Proveedor proveedor;

    public enum TipoDireccion {
        FISCAL,
        ENVIO,
        FACTURACION,
        DEVOLUCION,
        OTRO
    }

}
