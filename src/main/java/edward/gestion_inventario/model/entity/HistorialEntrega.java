package edward.gestion_inventario.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_entregas")
public class HistorialEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    public Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pedido", nullable = false)
    public Pedido pedido;
    @Column(name = "fecha_entrega", nullable = false)
    public java.time.LocalDate fechaEntrega;
    @Column(name = "estado_entrega", nullable = false, length = 20)
    public String estadoEntrega;
}
