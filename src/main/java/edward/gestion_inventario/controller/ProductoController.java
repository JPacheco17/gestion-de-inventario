package edward.gestion_inventario.controller;

import edward.gestion_inventario.dto.ProductoDTO;
import edward.gestion_inventario.service.ProductoService;
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

@Path("/productos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoController {

    private final ProductoService productoService = new ProductoService();

    @GET
    public Response listar() {
        return Response.ok(productoService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        ProductoDTO producto = productoService.buscarPorId(id);
        return producto == null
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(producto).build();
    }

    @POST
    public Response crear(ProductoDTO productoDTO) {
        try {
            return Response.status(Response.Status.CREATED)
                    .entity(productoService.crear(productoDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id,
            ProductoDTO productoDTO) {
        try {
            productoDTO.setIdProducto(id);
            return Response.ok(productoService.actualizar(productoDTO)).build();
        } catch (IllegalArgumentException ex) {
            return respuestaInvalida(ex);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        productoService.eliminar(id);
        return Response.ok(Map.of("mensaje", "Producto eliminado")).build();
    }

    private Response respuestaInvalida(IllegalArgumentException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("mensaje", ex.getMessage())).build();
    }
}
