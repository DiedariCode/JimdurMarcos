package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubicaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @Column(name = "nombre")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nombre;

    @Column(name = "descripcion")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @Column(name = "codigo", unique = true)
    @Size(max = 255, message = "El código no puede exceder 255 caracteres")
    private String codigo;

    @Column(name = "capacidad")
    @Min(value = 0, message = "La capacidad no puede ser negativa")
    private Integer capacidad;

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios;

}
