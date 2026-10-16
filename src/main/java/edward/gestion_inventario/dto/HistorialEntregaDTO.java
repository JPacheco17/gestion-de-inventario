package edward.gestion_inventario.dto;

public class HistorialEntregaDTO {
    private Long idEntrega;
    private Long idPedido;
    private String fechaEntrega;
    private String estadoEntrega;

    public HistorialEntregaDTO() {}

    public Long getIdEntrega() { return idEntrega; }
    public void setIdEntrega(Long idEntrega) { this.idEntrega = idEntrega; }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { this.estadoEntrega = estadoEntrega; }

    public Long getId() { return idEntrega; }
}
