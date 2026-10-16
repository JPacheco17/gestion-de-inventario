package edward.gestion_inventario.dao;

import edward.gestion_inventario.model.entity.Producto;

public class ProductoDAO extends GenericDAO<Producto, Long> {

    public ProductoDAO() {
        super(Producto.class);
    }
}

