package edward.gestion_inventario.service;
import edward.gestion_inventario.dao.*;
import edward.gestion_inventario.dto.HistorialEntregaDTO;
import edward.gestion_inventario.model.entity.*;
import java.time.LocalDate;
import java.util.*;
import static edward.gestion_inventario.service.ReglasPedido.*;

public class HistorialEntregaService {
    private static final List<String> ESTADOS=List.of("PENDIENTE","EN_PROCESO","ENTREGADO");
    public HistorialEntregaDTO registrar(Long idPedido,HistorialEntregaDTO solicitud) {
        id(idPedido);if(solicitud==null)throw error(400,"Envíe los datos de la entrega");
        String estado=texto(solicitud.getEstadoEntrega(),"Estado",20,true).toUpperCase(Locale.ROOT).replace(' ','_');
        if(!ESTADOS.contains(estado))throw error(400,"Estado no permitido");
        LocalDate fecha;
        try{fecha=LocalDate.parse(solicitud.getFechaEntrega());}catch(Exception e){throw error(400,"Use fecha AAAA-MM-DD");}
        if(fecha.isAfter(LocalDate.now()))throw error(400,"No puede registrar una entrega futura");
        return transaccion(em->{
            Pedido pedido=existe(new PedidoDAO(em).bloquear(idPedido));
            HistorialEntregaDAO dao=new HistorialEntregaDAO(em);
            LocalDate ultima=dao.ultimaFecha(idPedido);
            if(fecha.isBefore(pedido.getFechaPedido()) || (ultima!=null && fecha.isBefore(ultima)))throw error(400,"Fecha anterior al pedido o al último evento");
            if("ENTREGADO".equals(pedido.getEstado()) || ESTADOS.indexOf(estado)<ESTADOS.indexOf(pedido.getEstado()))throw error(409,"No se permite retroceder el estado ni modificar pedidos entregados");
            HistorialEntrega h=new HistorialEntrega();h.pedido=pedido;h.fechaEntrega=fecha;h.estadoEntrega=estado;
            dao.crear(h);pedido.setEstado(estado);em.flush();return convertirADTO(h);
        });
    }
    public List<HistorialEntregaDTO> buscarPorPedido(Long idPedido) {
        id(idPedido);return consultar(em->{existe(new PedidoDAO(em).buscarPorId(idPedido));return new HistorialEntregaDAO(em).buscarPorPedido(idPedido).stream().map(this::convertirADTO).toList();});
    }
    public HistorialEntregaDTO buscarPorId(Long identificador) {
        id(identificador);return consultar(em->convertirADTO(existe(new HistorialEntregaDAO(em).buscarPorId(identificador))));
    }
    private HistorialEntregaDTO convertirADTO(HistorialEntrega h) {
        HistorialEntregaDTO d=new HistorialEntregaDTO();d.setIdEntrega(h.id);d.setIdPedido(h.pedido.getIdPedido());
        d.setFechaEntrega(h.fechaEntrega.toString());d.setEstadoEntrega(h.estadoEntrega);return d;
    }
}
