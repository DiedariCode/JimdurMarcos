package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    @Column(name = "nombre")
    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre; // nombreEmpresa

    @Column(name = "nombre_comercial")
    @Size(max = 100, message = "El nombre comercial no puede exceder 100 caracteres")
    private String nombreComercial;

    @Column(name = "ruc", unique = true)
    @NotBlank(message = "El RUC es obligatorio")
    @Size(max = 255, message = "El RUC no puede exceder 255 caracteres")
    private String ruc;

    @Column(name = "tipo_proveedor")
    @Size(max = 50)
    private String tipoProveedor;

    @Column(name = "estado_activo")
    @Size(max = 50)
    private String estadoActivo; // Ej: "Activo", "Inactivo"

    // Información de contacto
    @Column(name = "nombre_contacto_principal")
    @Size(max = 100)
    private String nombreContactoPrincipal;

    @Column(name = "cargo_contacto")
    @Size(max = 100)
    private String cargoContacto;

    @Column(name = "telefono")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15)
    private String telefono;

    @Column(name = "correo", unique = true)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 100)
    private String correo;

    @Column(name = "sitio_web")
    @Size(max = 255)
    private String sitioWebContacto;

    @Column(name = "horario_atencion")
    @Size(max = 255)
    private String horarioAtencionContacto;

    // Categoría de productos que ofrece
    @Column(name = "categoria_productos")
    @Size(max = 255)
    private String categoriaProductosProveedor;

    // Relaciones
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DireccionProveedor> direcciones;

    @ManyToMany(mappedBy = "proveedores", fetch = FetchType.LAZY)
    private List<Producto> productos;
}
