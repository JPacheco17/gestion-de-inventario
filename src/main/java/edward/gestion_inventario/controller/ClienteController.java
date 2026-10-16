package edward.gestion_inventario.controller;

import edward.gestion_inventario.dto.ClienteDTO;
import edward.gestion_inventario.service.ClienteService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteController {

    private final ClienteService clienteService = new ClienteService();

    @GET
    public Response listar() {
        return Response.ok(clienteService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        ClienteDTO cliente = clienteService.buscarPorId(id);
        return cliente == null
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(cliente).build();
    }

    @POST
    public Response crear(ClienteDTO clienteDTO) {
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(clienteService.crear(clienteDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id,
            ClienteDTO clienteDTO) {
        try {
            clienteDTO.setIdCliente(id);
            return Response.ok(clienteService.actualizar(clienteDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        clienteService.eliminar(id);
        return Response.ok(Map.of("mensaje", "Cliente eliminado")).build();
    }

    private Response respuestaInvalida(IllegalArgumentException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("mensaje", ex.getMessage())).build();
    }
}
