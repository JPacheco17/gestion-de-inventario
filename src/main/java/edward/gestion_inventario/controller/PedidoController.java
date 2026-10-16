package edward.gestion_inventario.controller;
import edward.gestion_inventario.dto.*;
import edward.gestion_inventario.service.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
@Path("pedidos") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
public class PedidoController {
    private final PedidoService service=new PedidoService();
    @GET public Object listar(@QueryParam("idCliente") Long cliente) {return cliente==null?service.listarTodos():service.buscarPorCliente(cliente);}
    @GET @Path("{id}") public Object buscar(@PathParam("id") Long id) {return service.buscarPorId(id);}
    @POST public Response crear(PedidoDTO d) {return Response.status(201).entity(service.crear(d)).build();}
    @GET @Path("{id}/detalles") public Object detalles(@PathParam("id") Long id) {return new DetallePedidoService().buscarPorPedido(id);}
    @GET @Path("{id}/entregas") public Object entregas(@PathParam("id") Long id) {return new HistorialEntregaService().buscarPorPedido(id);}
    @POST @Path("{id}/entregas") public Response entregar(@PathParam("id") Long id,HistorialEntregaDTO d) {
        return Response.status(201).entity(new HistorialEntregaService().registrar(id,d)).build();
    }
}
