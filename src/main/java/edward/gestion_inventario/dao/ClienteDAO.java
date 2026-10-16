package edward.gestion_inventario.dao;

import edward.gestion_inventario.model.entity.Cliente;

public class ClienteDAO extends GenericDAO<Cliente, Long> {

    public ClienteDAO() {
        super(Cliente.class);
    }
}

