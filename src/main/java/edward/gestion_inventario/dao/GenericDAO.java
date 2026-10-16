package edward.gestion_inventario.dao;

import edward.gestion_inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public abstract class GenericDAO<T, ID> {

    protected final EntityManager entityManager;
    protected final Class<T> entityClass;

    protected GenericDAO(Class<T> entityClass) {
        this.entityManager = JPAUtil.crearEntityManager();
        this.entityClass = entityClass;
    }

    public void crear(T entidad) {
        entityManager.getTransaction().begin();
        entityManager.persist(entidad);
        entityManager.getTransaction().commit();
    }

    public T buscarPorId(ID id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> listarTodos() {
        String consulta = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(consulta, entityClass).getResultList();
    }

    public void actualizar(T entidad) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidad);
        entityManager.getTransaction().commit();
    }

    public void eliminar(ID id) {
        T entidad = buscarPorId(id);
        entityManager.getTransaction().begin();
        if (entidad != null) {
            entityManager.remove(entidad);
        }
        entityManager.getTransaction().commit();
    }
}
