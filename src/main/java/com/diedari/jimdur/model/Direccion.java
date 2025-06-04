package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Direccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;
    
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
    
    @Column(name = "referencia")
    @Size(max = 255, message = "La referencia no puede exceder 255 caracteres")
    private String referencia;
    
    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
}