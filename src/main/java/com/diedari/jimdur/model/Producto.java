package com.diedari.jimdur.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "imagen_url", length = 255)
    private String imagenURL;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    // NUEVOS CAMPOS PARA DESCUENTO
    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento; // "porcentaje" o "monto"

    @Column(name = "descuento")
    private Double descuento; // puede ser 10.0 (10%) o 20.0 soles

    @Column(name = "precio_oferta")
    private Double precioOferta; // precio con descuento aplicado

    @Column(name = "slug", unique = true, length = 150, nullable = false)
    private String slug;

    // ? ***** Relación con otras entidades *****

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false) // FK categoria_id dentro de la tabla producto
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false) // FK marca_id dentro de la tabla producto"
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false) // FK proveedor
    private Proveedor proveedor;

    // Relación uno a muchos con ImagenProducto
    // Se usa CascadeType.ALL para que al eliminar un producto se eliminen sus
    // imágenes asociadas
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenProducto> imagenes;

    @OneToMany(mappedBy = "producto")
    private List<ItemCarrito> itemsCarrito;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detalles;

    // Constructor vacío
    public Producto() {
    }

    public Producto(Long idProducto, String nombre, String imagenURL, String descripcion, Double precio, int stock,
            boolean activo, String tipoDescuento, Double descuento, Double precioOferta, String slug,
            Categoria categoria, Marca marca, Ubicacion ubicacion, Proveedor proveedor, List<ItemCarrito> itemsCarrito,
            List<DetallePedido> detalles) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.imagenURL = imagenURL;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
        this.tipoDescuento = tipoDescuento;
        this.descuento = descuento;
        this.precioOferta = precioOferta;
        this.slug = slug;
        this.categoria = categoria;
        this.marca = marca;
        this.ubicacion = ubicacion;
        this.proveedor = proveedor;
        this.itemsCarrito = itemsCarrito;
        this.detalles = detalles;
    }

    public Producto(Long idProducto, String nombre, String descripcion, Double precio, int stock, boolean activo,
            String tipoDescuento, Double descuento, Double precioOferta, String slug, Categoria categoria, Marca marca,
            Ubicacion ubicacion, Proveedor proveedor, List<ImagenProducto> imagenes, List<ItemCarrito> itemsCarrito,
            List<DetallePedido> detalles) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
        this.tipoDescuento = tipoDescuento;
        this.descuento = descuento;
        this.precioOferta = precioOferta;
        this.slug = slug;
        this.categoria = categoria;
        this.marca = marca;
        this.ubicacion = ubicacion;
        this.proveedor = proveedor;
        this.imagenes = imagenes;
        this.itemsCarrito = itemsCarrito;
        this.detalles = detalles;
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

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<ImagenProducto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProducto> imagenes) {
        this.imagenes = imagenes;
    }

    public List<ItemCarrito> getItemsCarrito() {
        return itemsCarrito;
    }

    public void setItemsCarrito(List<ItemCarrito> itemsCarrito) {
        this.itemsCarrito = itemsCarrito;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }
}
