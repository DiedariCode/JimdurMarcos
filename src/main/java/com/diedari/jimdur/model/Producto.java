package com.diedari.jimdur.model;

import jakarta.persistence.*;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false) // FK categoria_id dentro de la tabla producto
    private Categoria categoria;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "proveedor", nullable = false, length = 100)
    private String proveedor;

    @Column(name = "imagen_url", nullable = true, length = 1000)
    private String imagenURL;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    // NUEVOS CAMPOS PARA DESCUENTO
    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento; // "porcentaje" o "monto"

    @Column(name = "descuento")
    private Double descuento; // puede ser 10.0 (10%) o 20.0 soles

    @Column(name = "precio_oferta")
    private Double precioOferta; // precio con descuento aplicado

    @Column(name = "slug", unique = true, length = 150, nullable = false)
    private String slug;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con todos los campos (puedes omitir si no lo usas)
    public Producto(Long id, String nombre, String descripcion, Categoria categoria, Double precio, int stock,
            String proveedor, String imagenURL, boolean activo, Marca marca,
            String tipoDescuento, Double descuento, Double precioOferta, String slug) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
        this.imagenURL = imagenURL;
        this.activo = activo;
        this.marca = marca;
        this.tipoDescuento = tipoDescuento;
        this.descuento = descuento;
        this.precioOferta = precioOferta;
        this.slug = slug;
    }

    // * Método para calcular el precio de oferta basado en tipo y valor del
    // * descuento
    public void calcularPrecioOferta() {
        if (tipoDescuento != null && descuento != null && descuento > 0) {
            if (tipoDescuento.equalsIgnoreCase("porcentaje")) {
                this.precioOferta = precio - (precio * descuento / 100);
            } else if (tipoDescuento.equalsIgnoreCase("monto")) {
                this.precioOferta = precio - descuento;
            }
            // Validar que no sea negativo
            if (this.precioOferta < 0)
                this.precioOferta = 0.0;
        } else {
            this.precioOferta = precio;
        }
    }
    // * Método para generar el slug a partir del nombre
    private String generarSlug(String nombre) {
        return nombre.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.slug = generarSlug(nombre);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(Double precioOferta) {
        this.precioOferta = precioOferta;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", categoria=" + categoria
                + ", precio=" + precio + ", stock=" + stock + ", proveedor=" + proveedor + ", imagenURL=" + imagenURL
                + ", activo=" + activo + ", tipoDescuento=" + tipoDescuento + ", descuento=" + descuento
                + ", precioOferta=" + precioOferta + "]";
    }
}
