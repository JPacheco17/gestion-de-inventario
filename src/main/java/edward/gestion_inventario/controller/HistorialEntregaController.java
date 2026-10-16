package edward.gestion_inventario.controller;
import edward.gestion_inventario.dto.HistorialEntregaDTO;
import edward.gestion_inventario.service.HistorialEntregaService;
import static edward.gestion_inventario.service.ReglasPedido.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
@Path("historial-entregas") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
public class HistorialEntregaController {
    private final HistorialEntregaService service=new HistorialEntregaService();
    @GET public Object porPedido(@QueryParam("idPedido") Long id) {return service.buscarPorPedido(id);}
    @GET @Path("{id}") public Object buscar(@PathParam("id") Long id) {return service.buscarPorId(id);}
    @POST public Response registrar(HistorialEntregaDTO d) {
        if(d==null)throw error(400,"Envíe datos JSON");
        return Response.status(201).entity(service.registrar(d.getIdPedido(),d)).build();
    }
}
