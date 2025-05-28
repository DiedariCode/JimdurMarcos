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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "etiqueta")
    @Size(max = 255, message = "La etiqueta no puede exceder 255 caracteres")
    private String etiqueta;

    @Column(name = "calle")
    @Size(max = 255, message = "La calle no puede exceder 255 caracteres")
    private String calle;

    @Column(name = "distrito")
    @Size(max = 255, message = "El distrito no puede exceder 255 caracteres")
    private String distrito;

    @Column(name = "ciudad")
    @Size(max = 255, message = "La ciudad no puede exceder 255 caracteres")
    private String ciudad;

    @Column(name = "departamento_estado")
    @Size(max = 255, message = "El departamento o estado no puede exceder 255 caracteres")
    private String departamentoEstado;

    @Column(name = "codigo_postal")
    @Size(max = 20, message = "El código postal no puede exceder 20 caracteres")
    private String codigoPostal;

    @Column(name = "pais")
    @Size(max = 255, message = "El país no puede exceder 255 caracteres")
    private String pais;

    @Column(name = "referencia")
    @Size(max = 255, message = "La referencia no puede exceder 255 caracteres")
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_direccion")
    private TipoDireccion tipoDireccion;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    public enum TipoDireccion {
        FISCAL,
        ENVIO,
        FACTURACION,
        DEVOLUCION,
        OTRO
    }

}
