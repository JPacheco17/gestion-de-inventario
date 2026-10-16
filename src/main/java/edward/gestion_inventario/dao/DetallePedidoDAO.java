package edward.gestion_inventario.dao;
import edward.gestion_inventario.model.entity.DetallePedido;
import jakarta.persistence.EntityManager;
public class DetallePedidoDAO extends PedidosDAOBase<DetallePedido> {
    public DetallePedidoDAO() { super(DetallePedido.class); }
    public DetallePedidoDAO(EntityManager em) { super(DetallePedido.class, em); }

    public java.util.List<DetallePedido> buscarPorPedido(Long idPedido) {
        return entityManager.createQuery("select d from DetallePedido d where d.pedido.id=:id order by d.id",DetallePedido.class).setParameter("id",idPedido).getResultList();
    }
}
