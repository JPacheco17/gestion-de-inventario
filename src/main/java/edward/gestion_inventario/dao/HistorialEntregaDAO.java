package edward.gestion_inventario.dao;
import edward.gestion_inventario.model.entity.HistorialEntrega;
import jakarta.persistence.EntityManager;
public class HistorialEntregaDAO extends PedidosDAOBase<HistorialEntrega> {
    public HistorialEntregaDAO() { super(HistorialEntrega.class); }
    public HistorialEntregaDAO(EntityManager em) { super(HistorialEntrega.class, em); }

    public java.util.List<HistorialEntrega> buscarPorPedido(Long idPedido) {
        return entityManager.createQuery("select h from HistorialEntrega h where h.pedido.id=:id order by h.id",HistorialEntrega.class).setParameter("id",idPedido).getResultList();
    }
    public java.time.LocalDate ultimaFecha(Long idPedido) {
        return entityManager.createQuery("select max(h.fechaEntrega) from HistorialEntrega h where h.pedido.id=:id",java.time.LocalDate.class).setParameter("id",idPedido).getSingleResult();
    }
}
