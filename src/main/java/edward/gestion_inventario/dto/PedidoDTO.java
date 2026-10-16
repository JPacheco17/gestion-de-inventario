package edward.gestion_inventario.dto;
import java.math.BigDecimal;
import java.util.List;
public class PedidoDTO {
    private Long idPedido;
    private Long idCliente;
    private String cliente;
    private String fechaPedido;
    private String estado;
    private List<DetallePedidoDTO> detalles;
    private BigDecimal total;

    public PedidoDTO() {}

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(String fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Long getId() { return idPedido; }
}
