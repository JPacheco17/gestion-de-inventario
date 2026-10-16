package edward.gestion_inventario.dao;

import edward.gestion_inventario.model.entity.Producto;

/** Administra el acceso a datos de los productos. */
public class ProductoDAO extends GenericDAO<Producto, Long> {

    public ProductoDAO() {
        super(Producto.class);
    }
}

