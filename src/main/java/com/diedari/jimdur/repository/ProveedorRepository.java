package com.diedari.jimdur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.diedari.jimdur.model.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByEstadoActivoOrderByNombreAsc(String estado);

    List<Proveedor> findByEstadoActivo(String estadoActivo);

    /**
     * Busca proveedores aplicando filtros opcionales por nombre, tipo y estado.
     *
     * <p>
     * Esta consulta permite filtrar por:
     * <ul>
     *     <li><b>nombreProveedor:</b> busca coincidencias parciales en los campos 'nombre' o 'nombreComercial'.</li>
     *     <li><b>tipoProveedor:</b> filtra por el tipo exacto de proveedor si se proporciona.</li>
     *     <li><b>estadoActivo:</b> filtra por el estado activo (activo/inactivo) si se proporciona.</li>
     * </ul>
     * Si algún filtro es null, no se aplica en la consulta.
     * </p>
     *
     * @param nombreProveedor nombre o parte del nombre del proveedor a buscar (opcional).
     * @param tipoProveedor tipo de proveedor a filtrar (opcional).
     * @param estadoActivo estado activo/inactivo a filtrar (opcional).
     * @param pageable objeto Pageable para paginar los resultados.
     * @return página de proveedores que cumplen con los filtros.
     */
    @Query(value = "SELECT DISTINCT p FROM Proveedor p " +
           "WHERE (:nombreProveedor IS NULL OR " +
           "      LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombreProveedor, '%')) OR " +
           "      LOWER(p.nombreComercial) LIKE LOWER(CONCAT('%', :nombreProveedor, '%'))) " +
           "AND (:tipoProveedor IS NULL OR p.tipoProveedor = :tipoProveedor) " +
           "AND (:estadoActivo IS NULL OR p.estadoActivo = :estadoActivo)")
    Page<Proveedor> findByFiltros(
        @Param("nombreProveedor") String nombreProveedor,
        @Param("tipoProveedor") String tipoProveedor,
        @Param("estadoActivo") String estadoActivo,
        Pageable pageable
    );

    /**
     * Obtiene la lista de proveedores con sus productos asociados, incluyendo:
     * <ul>
     *     <li>Relación productoProveedor</li>
     *     <li>Producto</li>
     *     <li>Categoria del producto</li>
     * </ul>
     *
     * <p>
     * Utiliza LEFT JOIN FETCH para traer las relaciones sin generar múltiples consultas N+1.
     * </p>
     *
     * @param proveedores lista de proveedores a consultar.
     * @return lista de proveedores con sus productos y categorías cargados.
     */
    @Query("SELECT p FROM Proveedor p " +
           "LEFT JOIN FETCH p.productoProveedores pp " +
           "LEFT JOIN FETCH pp.producto prod " +
           "LEFT JOIN FETCH prod.categoria " +
           "WHERE p IN :proveedores")
    List<Proveedor> findProveedoresWithProductos(@Param("proveedores") List<Proveedor> proveedores);
}
