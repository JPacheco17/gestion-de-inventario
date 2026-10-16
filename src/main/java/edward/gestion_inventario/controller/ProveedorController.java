package edward.gestion_inventario.controller;

import edward.gestion_inventario.dto.ProveedorDTO;
import edward.gestion_inventario.service.ProveedorService;
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

@Path("/proveedores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProveedorController {

    private final ProveedorService proveedorService = new ProveedorService();

    @GET
    public Response listar() {
        return Response.ok(proveedorService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        ProveedorDTO proveedor = proveedorService.buscarPorId(id);
        return proveedor == null
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(proveedor).build();
    }

    @POST
    public Response crear(ProveedorDTO proveedorDTO) {
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(proveedorService.crear(proveedorDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id,
            ProveedorDTO proveedorDTO) {
        try {
            proveedorDTO.setIdProveedor(id);
            return Response.ok(proveedorService.actualizar(proveedorDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        proveedorService.eliminar(id);
        return Response.ok(Map.of("mensaje", "Proveedor eliminado")).build();
    }

    private Response respuestaInvalida(IllegalArgumentException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("mensaje", ex.getMessage())).build();
    }
}
