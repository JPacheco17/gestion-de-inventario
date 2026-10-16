package edward.gestion_inventario.service;
import edward.gestion_inventario.dao.*;
import edward.gestion_inventario.dto.DetallePedidoDTO;
import edward.gestion_inventario.model.entity.DetallePedido;
import java.math.BigDecimal;
import java.util.List;
import static edward.gestion_inventario.service.ReglasPedido.*;
public class DetallePedidoService {
    public List<DetallePedidoDTO> buscarPorPedido(Long idPedido) {
        id(idPedido);return consultar(em->{existe(new PedidoDAO(em).buscarPorId(idPedido));return new DetallePedidoDAO(em).buscarPorPedido(idPedido).stream().map(DetallePedidoService::convertirADTO).toList();});
    }
    public DetallePedidoDTO buscarPorId(Long identificador) {
        id(identificador);return consultar(em->convertirADTO(existe(new DetallePedidoDAO(em).buscarPorId(identificador))));
    }
    public static DetallePedidoDTO convertirADTO(DetallePedido x) {
        DetallePedidoDTO d=new DetallePedidoDTO();
        d.setIdDetalle(x.id);d.setIdPedido(x.pedido.getIdPedido());
        d.setIdProducto(x.producto.getIdProducto());d.setProducto(x.producto.getNombre());
        d.setCantidad(x.cantidad);d.setPrecioUnitario(x.precioUnitario);
        d.setSubtotal(x.precioUnitario.multiply(BigDecimal.valueOf(x.cantidad)));return d;
    }
}
