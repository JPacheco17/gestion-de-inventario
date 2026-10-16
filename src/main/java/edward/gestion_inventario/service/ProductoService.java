package edward.gestion_inventario.service;

import edward.gestion_inventario.dao.ProductoDAO;
import edward.gestion_inventario.dao.ProveedorDAO;
import edward.gestion_inventario.dto.ProductoDTO;
import edward.gestion_inventario.model.entity.Producto;
import edward.gestion_inventario.model.entity.Proveedor;
import java.math.BigDecimal;
import java.util.List;

public class ProductoService {

    private final ProductoDAO productoDAO;
    private final ProveedorDAO proveedorDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
        this.proveedorDAO = new ProveedorDAO();
    }

    public ProductoDTO crear(ProductoDTO productoDTO) {
        validar(productoDTO);
        Producto producto = convertirAEntidad(productoDTO);
        productoDAO.crear(producto);
        return convertirADTO(producto);
    }

    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoDAO.buscarPorId(id);
        return producto == null ? null : convertirADTO(producto);
    }

    public List<ProductoDTO> listarTodos() {
        return productoDAO.listarTodos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProductoDTO actualizar(ProductoDTO productoDTO) {
        validar(productoDTO);
        if (productoDTO.getIdProducto() == null
                || productoDAO.buscarPorId(productoDTO.getIdProducto()) == null) {
            throw new IllegalArgumentException("El producto no existe");
        }
        Producto producto = convertirAEntidad(productoDTO);
        productoDAO.actualizar(producto);
        return convertirADTO(producto);
    }

    public void eliminar(Long id) {
        productoDAO.eliminar(id);
    }

    private void validar(ProductoDTO productoDTO) {
        if (productoDTO == null
                || productoDTO.getNombre() == null
                || productoDTO.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (productoDTO.getPrecio() == null
                || productoDTO.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a cero");
        }
        if (productoDTO.getStock() == null || productoDTO.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a cero");
        }
        if (productoDTO.getIdProveedor() == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio");
        }
    }

    public ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getProveedor().getIdProveedor()
        );
    }

    private Producto convertirAEntidad(ProductoDTO dto) {
        Proveedor proveedor = proveedorDAO.buscarPorId(dto.getIdProveedor());
        if (proveedor == null) {
            throw new IllegalArgumentException("No existe el proveedor indicado");
        }

        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setNombre(dto.getNombre().trim());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setProveedor(proveedor);
        return producto;
    }
}
