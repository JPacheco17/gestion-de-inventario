package edward.gestion_inventario.service;
import edward.gestion_inventario.dao.*;
import edward.gestion_inventario.dto.*;
import edward.gestion_inventario.model.entity.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.*;
import static edward.gestion_inventario.service.ReglasPedido.*;

public class PedidoService {
    public PedidoDTO crear(PedidoDTO solicitud) {
        if(solicitud==null || solicitud.getDetalles()==null || solicitud.getDetalles().isEmpty() || solicitud.getDetalles().size()>100)throw error(400,"Envíe entre 1 y 100 líneas de pedido");
        id(solicitud.getIdCliente());
        SortedMap<Long,Integer> cantidades=new TreeMap<>();
        for(DetallePedidoDTO linea:solicitud.getDetalles()) {
            if(linea==null || linea.getCantidad()==null || linea.getCantidad()<=0)throw error(400,"La cantidad debe ser un entero positivo");
            id(linea.getIdProducto());long suma=(long)cantidades.getOrDefault(linea.getIdProducto(),0)+linea.getCantidad();
            if(suma>Integer.MAX_VALUE)throw error(400,"Cantidad demasiado grande");
            cantidades.put(linea.getIdProducto(),(int)suma);
        }
        return transaccion(em->{
            PedidoDAO pedidos=new PedidoDAO(em);
            DetallePedidoDAO detalles=new DetallePedidoDAO(em);
            Cliente cliente=existe(em.find(Cliente.class,solicitud.getIdCliente(),jakarta.persistence.LockModeType.PESSIMISTIC_READ));
            Pedido pedido=new Pedido();pedido.setCliente(cliente);pedido.setFechaPedido(LocalDate.now());pedido.setEstado("PENDIENTE");pedidos.crear(pedido);
            // Todos los DAO comparten la transacción y bloquean productos por ID ascendente.
            for(var linea:cantidades.entrySet()) {
                Producto producto=existe(em.find(Producto.class,linea.getKey(),jakarta.persistence.LockModeType.PESSIMISTIC_WRITE));
                if(producto.getStock()<linea.getValue())throw error(409,"Stock insuficiente para "+producto.getNombre());
                producto.setStock(producto.getStock()-linea.getValue());
                DetallePedido detalle=new DetallePedido();detalle.pedido=pedido;detalle.producto=producto;
                detalle.cantidad=linea.getValue();detalle.precioUnitario=producto.getPrecio();detalles.crear(detalle);
            }
            em.flush();return convertirADTO(em,pedido);
        });
    }
    public List<PedidoDTO> listarTodos() {
        return consultar(em->new PedidoDAO(em).listarTodos().stream().map(p->convertirADTO(em,p)).toList());
    }
    public List<PedidoDTO> buscarPorCliente(Long idCliente) {
        id(idCliente);return consultar(em->{existe(em.find(Cliente.class,idCliente));return new PedidoDAO(em).buscarPorCliente(idCliente).stream().map(p->convertirADTO(em,p)).toList();});
    }
    public PedidoDTO buscarPorId(Long identificador) {
        id(identificador);return consultar(em->convertirADTO(em,existe(new PedidoDAO(em).buscarPorId(identificador))));
    }
    private PedidoDTO convertirADTO(EntityManager em,Pedido pedido) {
        PedidoDTO d=new PedidoDTO();d.setIdPedido(pedido.getIdPedido());d.setIdCliente(pedido.getCliente().getIdCliente());
        d.setCliente(pedido.getCliente().getNombre());d.setFechaPedido(pedido.getFechaPedido().toString());d.setEstado(pedido.getEstado());
        List<DetallePedidoDTO> lineas=new DetallePedidoDAO(em).buscarPorPedido(pedido.getIdPedido()).stream().map(DetallePedidoService::convertirADTO).toList();
        d.setDetalles(lineas);d.setTotal(lineas.stream().map(DetallePedidoDTO::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add));return d;
    }
}
