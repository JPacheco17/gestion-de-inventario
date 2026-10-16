CREATE TABLE IF NOT EXISTS pedidos (
 id_pedido BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 id_cliente BIGINT NOT NULL, fecha_pedido DATE NOT NULL, estado VARCHAR(20) NOT NULL,
 CONSTRAINT fk_pedido_cliente FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente),
 CONSTRAINT ck_estado_pedido CHECK(estado IN ('PENDIENTE','EN_PROCESO','ENTREGADO'))
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS detalles_pedido (
 id_detalle BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 id_pedido BIGINT NOT NULL, id_producto BIGINT NOT NULL, cantidad INT NOT NULL,
 precio_unitario DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_detalle_pedido FOREIGN KEY(id_pedido) REFERENCES pedidos(id_pedido),
 CONSTRAINT fk_detalle_producto FOREIGN KEY(id_producto) REFERENCES productos(id_producto),
 CONSTRAINT uq_pedido_producto UNIQUE(id_pedido,id_producto),
 CONSTRAINT ck_cantidad CHECK(cantidad>0), CONSTRAINT ck_precio_unitario CHECK(precio_unitario>=0)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS historial_entregas (
 id_entrega BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 id_pedido BIGINT NOT NULL, fecha_entrega DATE NOT NULL, estado_entrega VARCHAR(20) NOT NULL,
 CONSTRAINT fk_entrega_pedido FOREIGN KEY(id_pedido) REFERENCES pedidos(id_pedido),
 CONSTRAINT ck_estado_entrega CHECK(estado_entrega IN ('PENDIENTE','EN_PROCESO','ENTREGADO'))
) ENGINE=InnoDB;
