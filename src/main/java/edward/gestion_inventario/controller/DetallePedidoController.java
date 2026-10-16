package edward.gestion_inventario.controller;
import edward.gestion_inventario.service.DetallePedidoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
@Path("detalles-pedido") @Produces(MediaType.APPLICATION_JSON)
public class DetallePedidoController {
    private final DetallePedidoService service=new DetallePedidoService();
    @GET public Object porPedido(@QueryParam("idPedido") Long id) {return service.buscarPorPedido(id);}
    @GET @Path("{id}") public Object buscar(@PathParam("id") Long id) {return service.buscarPorId(id);}
}
