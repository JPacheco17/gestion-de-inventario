package edward.gestion_inventario.service;

import edward.gestion_inventario.dao.ProveedorDAO;
import edward.gestion_inventario.dto.ProveedorDTO;
import edward.gestion_inventario.model.entity.Proveedor;
import java.util.List;

public class ProveedorService {

    private final ProveedorDAO proveedorDAO;

    public ProveedorService() {
        this.proveedorDAO = new ProveedorDAO();
    }

    public ProveedorDTO crear(ProveedorDTO proveedorDTO) {
        validar(proveedorDTO);
        Proveedor proveedor = convertirAEntidad(proveedorDTO);
        proveedorDAO.crear(proveedor);
        return convertirADTO(proveedor);
    }

    public ProveedorDTO buscarPorId(Long id) {
        Proveedor proveedor = proveedorDAO.buscarPorId(id);
        return proveedor == null ? null : convertirADTO(proveedor);
    }

    public List<ProveedorDTO> listarTodos() {
        return proveedorDAO.listarTodos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProveedorDTO actualizar(ProveedorDTO proveedorDTO) {
        validar(proveedorDTO);
        if (proveedorDTO.getIdProveedor() == null
                || proveedorDAO.buscarPorId(proveedorDTO.getIdProveedor()) == null) {
            throw new IllegalArgumentException("El proveedor no existe");
        }
        Proveedor proveedor = convertirAEntidad(proveedorDTO);
        proveedorDAO.actualizar(proveedor);
        return convertirADTO(proveedor);
    }

    public void eliminar(Long id) {
        proveedorDAO.eliminar(id);
    }

    private void validar(ProveedorDTO proveedorDTO) {
        if (proveedorDTO == null
                || proveedorDTO.getNombre() == null
                || proveedorDTO.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio");
        }
    }

    public ProveedorDTO convertirADTO(Proveedor proveedor) {
        return new ProveedorDTO(
                proveedor.getIdProveedor(),
                proveedor.getNombre(),
                proveedor.getContacto(),
                proveedor.getTelefono(),
                proveedor.getEmail()
        );
    }

    private Proveedor convertirAEntidad(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(dto.getIdProveedor());
        proveedor.setNombre(dto.getNombre().trim());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return proveedor;
    }
}
