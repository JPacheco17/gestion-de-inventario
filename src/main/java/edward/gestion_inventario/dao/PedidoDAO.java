package edward.gestion_inventario.dao;
import edward.gestion_inventario.model.entity.Pedido;
import jakarta.persistence.EntityManager;
public class PedidoDAO extends PedidosDAOBase<Pedido> {
    public PedidoDAO() { super(Pedido.class); }
    public PedidoDAO(EntityManager em) { super(Pedido.class, em); }

    public java.util.List<Pedido> buscarPorCliente(Long idCliente) {
        return entityManager.createQuery("select p from Pedido p where p.cliente.idCliente=:id order by p.id desc",Pedido.class).setParameter("id",idCliente).getResultList();
    }
    @Override public java.util.List<Pedido> listarTodos() {
        return entityManager.createQuery("select p from Pedido p order by p.id desc",Pedido.class).getResultList();
    }
}
