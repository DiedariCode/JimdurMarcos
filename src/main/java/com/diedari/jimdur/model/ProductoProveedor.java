package com.diedari.jimdur.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos_proveedores")
@IdClass(ProductoProveedorId.class)
public class ProductoProveedor {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    private Double precioCompra;
    private Date fechaAdquisicion;

    public ProductoProveedor() {
    }

    public ProductoProveedor(Producto producto, Proveedor proveedor, Double precioCompra, Date fechaAdquisicion) {
        this.producto = producto;
        this.proveedor = proveedor;
        this.precioCompra = precioCompra;
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Date getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(Date fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    @Override
    public String toString() {
        return "ProductoProveedor{" +
                "producto=" + producto +
                ", proveedor=" + proveedor +
                ", precioCompra=" + precioCompra +
                ", fechaAdquisicion=" + fechaAdquisicion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductoProveedor))
            return false;

        ProductoProveedor that = (ProductoProveedor) o;

        if (!producto.equals(that.producto))
            return false;
        return proveedor.equals(that.proveedor);
    }

    @Override
    public int hashCode() {
        int result = producto.hashCode();
        result = 31 * result + proveedor.hashCode();
        return result;
    }

    

}
