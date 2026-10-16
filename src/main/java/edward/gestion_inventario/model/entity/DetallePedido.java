/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edward.gestion_inventario.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    public Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pedido", nullable = false)
    public Pedido pedido;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    public Producto producto;
    @Column(nullable = false)
    public Integer cantidad;
    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    public java.math.BigDecimal precioUnitario;
}
