package edward.gestion_inventario.service;

import edward.gestion_inventario.dao.ClienteDAO;
import edward.gestion_inventario.dto.ClienteDTO;
import edward.gestion_inventario.model.entity.Cliente;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public ClienteDTO crear(ClienteDTO clienteDTO) {
        validar(clienteDTO);
        Cliente cliente = convertirAEntidad(clienteDTO);
        clienteDAO.crear(cliente);
        return convertirADTO(cliente);
    }

    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        return cliente == null ? null : convertirADTO(cliente);
    }

    public List<ClienteDTO> listarTodos() {
        return clienteDAO.listarTodos().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ClienteDTO actualizar(ClienteDTO clienteDTO) {
        validar(clienteDTO);
        if (clienteDTO.getIdCliente() == null
                || clienteDAO.buscarPorId(clienteDTO.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        Cliente cliente = convertirAEntidad(clienteDTO);
        clienteDAO.actualizar(cliente);
        return convertirADTO(cliente);
    }

    public void eliminar(Long id) {
        clienteDAO.eliminar(id);
    }

    private void validar(ClienteDTO clienteDTO) {
        if (clienteDTO == null
                || clienteDTO.getNombre() == null
                || clienteDTO.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
    }

    public ClienteDTO convertirADTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEmail()
        );
    }

    private Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdCliente());
        cliente.setNombre(dto.getNombre().trim());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }
}
