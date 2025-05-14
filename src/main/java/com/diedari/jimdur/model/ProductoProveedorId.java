package com.diedari.jimdur.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductoProveedorId implements Serializable {

    private Long producto; // Cambié Integer a Long
    private Long proveedor; // Cambié Integer a Long

    public ProductoProveedorId() {}

    public ProductoProveedorId(Long producto, Long proveedor) { // Cambié Integer a Long en el constructor
        this.producto = producto;
        this.proveedor = proveedor;
    }

    public Long getProducto() {
        return producto;
    }

    public void setProducto(Long producto) {
        this.producto = producto;
    }

    public Long getProveedor() {
        return proveedor;
    }

    public void setProveedor(Long proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoProveedorId)) return false;

        ProductoProveedorId that = (ProductoProveedorId) o;

        if (!Objects.equals(producto, that.producto)) return false;
        return Objects.equals(proveedor, that.proveedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, proveedor);
    }
}
