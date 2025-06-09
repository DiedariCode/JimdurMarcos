package com.diedari.jimdur.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedorId implements Serializable {

    private Long idProducto;
    private Long idProveedor;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductoProveedorId that = (ProductoProveedorId) o;
        return Objects.equals(idProducto, that.idProducto) &&
                Objects.equals(idProveedor, that.idProveedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idProveedor);
    }
}
