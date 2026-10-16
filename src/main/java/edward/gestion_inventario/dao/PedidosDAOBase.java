package edward.gestion_inventario.dao;
import edward.gestion_inventario.util.JPAUtil;
import jakarta.persistence.*;
import java.util.List;
import java.util.function.Supplier;

/** DAO para el módulo de pedidos, sin modificar el GenericDAO existente. */
public abstract class PedidosDAOBase<T> implements AutoCloseable {
    protected final EntityManager entityManager;
    private final Class<T> clase;
    private final boolean propio;

    protected PedidosDAOBase(Class<T> clase) { this(clase,JPAUtil.crearEntityManager(),true); }
    protected PedidosDAOBase(Class<T> clase,EntityManager em) { this(clase,em,false); }
    private PedidosDAOBase(Class<T> clase,EntityManager em,boolean propio) {
        this.clase=clase;this.entityManager=em;this.propio=propio;
    }
    private <R> R escribir(Supplier<R> accion) {
        EntityTransaction tx=entityManager.getTransaction();boolean inicia=!tx.isActive();
        try {if(inicia)tx.begin();R resultado=accion.get();if(inicia)tx.commit();return resultado;}
        catch(RuntimeException ex){if(inicia && tx.isActive())tx.rollback();throw ex;}
    }
    public T buscarPorId(Long id) {return entityManager.find(clase,id);}
    public T bloquear(Long id) {return entityManager.find(clase,id,LockModeType.PESSIMISTIC_WRITE);}
    public List<T> listarTodos() {return entityManager.createQuery("select e from "+clase.getSimpleName()+" e",clase).getResultList();}
    public void crear(T entidad) {escribir(()->{entityManager.persist(entidad);return null;});}
    public void actualizar(T entidad) {escribir(()->entityManager.merge(entidad));}
    public void eliminar(Long id) {escribir(()->{T x=buscarPorId(id);if(x!=null)entityManager.remove(x);return null;});}
    @Override public void close() {if(propio && entityManager.isOpen())entityManager.close();}
}
