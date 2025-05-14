package com.diedari.jimdur.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ImagenProducto")
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idImagen;

    private String urlImagen;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    // Constructor vacío (requerido por JPA)
    public ImagenProducto() {
    }

    // Constructor con todos los campos
    public ImagenProducto(Integer idImagen, String urlImagen, Producto producto) {
        this.idImagen = idImagen;
        this.urlImagen = urlImagen;
        this.producto = producto;
    }

    // Getters y Setters
    public Integer getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Integer idImagen) {
        this.idImagen = idImagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "ImagenProducto{" +
                "idImagen=" + idImagen +
                ", urlImagen='" + urlImagen + '\'' +
                ", producto=" + (producto != null ? producto.getIdProducto(): null) +
                '}';
    }
}
