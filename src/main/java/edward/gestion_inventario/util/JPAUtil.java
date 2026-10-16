package edward.gestion_inventario.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY
            = Persistence.createEntityManagerFactory("InventarioPU");

    private JPAUtil() {
    }

    public static EntityManager crearEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void cerrar() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
