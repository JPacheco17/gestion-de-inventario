package edward.gestion_inventario.dao;

import edward.gestion_inventario.model.entity.Proveedor;

public class ProveedorDAO extends GenericDAO<Proveedor, Long> {

    public ProveedorDAO() {
        super(Proveedor.class);
    }
}

