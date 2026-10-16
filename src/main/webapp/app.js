"use strict";

let productos = [];
let detalles = [];

function elemento(id) {
    return document.getElementById(id);
}

function informar(texto, error = false) {
    elemento("mensaje").textContent = texto;
    elemento("mensaje").style.color = error ? "#a00000" : "#245024";
}

// Todas las consultas utilizan la API REST del mismo proyecto.
async function peticion(ruta, metodo = "GET", datos) {
    const opciones = { method: metodo };
    if (datos !== undefined) {
        opciones.headers = { "Content-Type": "application/json" };
        opciones.body = JSON.stringify(datos);
    }
    const respuesta = await fetch("aplication/" + ruta, opciones);
    const texto = await respuesta.text();
    let resultado = null;
    try {
        resultado = texto ? JSON.parse(texto) : null;
    } catch (error) {
        throw new Error("El servidor devolvió una respuesta inesperada.");
    }
    if (!respuesta.ok) {
        throw new Error(resultado?.mensaje || resultado?.error || "No se pudo completar la operación.");
    }
    return resultado;
}

function celda(fila, texto) {
    const td = document.createElement("td");
    td.textContent = texto ?? "";
    fila.appendChild(td);
    return td;
}

function dinero(valor) {
    return Number(valor || 0).toLocaleString("es-CO", { style: "currency", currency: "COP" });
}

function opcion(lista, valor, texto) {
    lista.add(new Option(texto, valor));
}

async function cargarDatos() {
    try {
        const clientes = await peticion("clientes");
        productos = await peticion("productos");
        const clienteActual = elemento("cliente").value;
        elemento("cliente").length = 1;
        for (const cliente of clientes) {
            opcion(elemento("cliente"), cliente.idCliente, cliente.nombre);
        }
        elemento("cliente").value = clienteActual;
        elemento("producto").length = 1;
        for (const producto of productos) {
            opcion(elemento("producto"), producto.idProducto,
                producto.nombre + " — Stock: " + producto.stock + " — " + dinero(producto.precio));
        }
        await cargarPedidos();
        informar("Datos actualizados. Los clientes y productos deben estar registrados en el sistema.");
    } catch (error) {
        informar(error.message, true);
    }
}

function agregarProducto() {
    const idProducto = Number(elemento("producto").value);
    const cantidad = Number(elemento("cantidad").value);
    const producto = productos.find(p => p.idProducto === idProducto);
    if (!producto || !Number.isSafeInteger(cantidad) || cantidad < 1) {
        informar("Seleccione un producto y una cantidad entera mayor que cero.", true);
        return;
    }
    const existente = detalles.find(d => d.idProducto === idProducto);
    const total = cantidad + (existente ? existente.cantidad : 0);
    if (total > producto.stock) {
        informar("La cantidad supera el stock disponible.", true);
        return;
    }
    if (existente) existente.cantidad = total;
    else detalles.push({ idProducto, cantidad });
    mostrarDetalleNuevo();
    informar("Producto agregado al pedido.");
}

function mostrarDetalleNuevo() {
    elemento("detalle-nuevo").replaceChildren();
    for (const detalle of detalles) {
        const fila = document.createElement("tr");
        const producto = productos.find(p => p.idProducto === detalle.idProducto);
        celda(fila, producto ? producto.nombre : detalle.idProducto);
        celda(fila, detalle.cantidad);
        const boton = document.createElement("button");
        boton.type = "button";
        boton.textContent = "Quitar";
        boton.onclick = function () {
            detalles = detalles.filter(d => d.idProducto !== detalle.idProducto);
            mostrarDetalleNuevo();
        };
        celda(fila, "").appendChild(boton);
        elemento("detalle-nuevo").appendChild(fila);
    }
}

async function guardarPedido(evento) {
    evento.preventDefault();
    if (detalles.length === 0) {
        informar("Agregue por lo menos un producto.", true);
        return;
    }
    elemento("guardar").disabled = true;
    try {
        const pedido = await peticion("pedidos", "POST", {
            idCliente: Number(elemento("cliente").value), detalles
        });
        detalles = [];
        mostrarDetalleNuevo();
        await cargarDatos();
        informar("Pedido " + pedido.idPedido + " registrado. El servidor actualizó el stock.");
    } catch (error) {
        informar(error.message, true);
    } finally {
        elemento("guardar").disabled = false;
    }
}

async function cargarPedidos() {
    const pedidos = await peticion("pedidos");
    elemento("pedidos").replaceChildren();
    const seleccionado = elemento("pedido-entrega").value;
    elemento("pedido-entrega").length = 1;
    for (const pedido of pedidos) {
        const fila = document.createElement("tr");
        celda(fila, pedido.idPedido);
        celda(fila, pedido.cliente);
        celda(fila, pedido.fechaPedido);
        celda(fila, pedido.estado);
        celda(fila, dinero(pedido.total));
        const boton = document.createElement("button");
        boton.type = "button";
        boton.textContent = "Ver detalle";
        boton.onclick = () => consultarPedido(pedido.idPedido);
        celda(fila, "").appendChild(boton);
        elemento("pedidos").appendChild(fila);
        opcion(elemento("pedido-entrega"), pedido.idPedido,
            "Pedido " + pedido.idPedido + " — " + pedido.estado);
    }
    elemento("pedido-entrega").value = seleccionado;
    await cargarHistorial();
}

async function consultarPedido(id) {
    try {
        const detallesPedido = await peticion("pedidos/" + id + "/detalles");
        elemento("titulo-detalle").textContent = "Detalle del pedido " + id;
        elemento("detalle-consulta").replaceChildren();
        for (const detalle of detallesPedido) {
            const fila = document.createElement("tr");
            celda(fila, detalle.producto);
            celda(fila, detalle.cantidad);
            celda(fila, dinero(detalle.precioUnitario));
            celda(fila, dinero(detalle.subtotal));
            elemento("detalle-consulta").appendChild(fila);
        }
        elemento("pedido-entrega").value = String(id);
        await cargarHistorial();
    } catch (error) {
        informar(error.message, true);
    }
}

async function cargarHistorial() {
    elemento("historial").replaceChildren();
    const id = elemento("pedido-entrega").value;
    if (!id) return;
    const entregas = await peticion("pedidos/" + id + "/entregas");
    for (const entrega of entregas) {
        const fila = document.createElement("tr");
        celda(fila, entrega.idEntrega);
        celda(fila, entrega.fechaEntrega);
        celda(fila, entrega.estadoEntrega);
        elemento("historial").appendChild(fila);
    }
}

async function guardarEntrega(evento) {
    evento.preventDefault();
    elemento("guardar-entrega").disabled = true;
    try {
        await peticion("pedidos/" + elemento("pedido-entrega").value + "/entregas", "POST", {
            fechaEntrega: elemento("fecha").value,
            estadoEntrega: elemento("estado").value
        });
        await cargarPedidos();
        informar("Entrega registrada e historial actualizado.");
    } catch (error) {
        informar(error.message, true);
    } finally {
        elemento("guardar-entrega").disabled = false;
    }
}

elemento("actualizar").onclick = cargarDatos;
elemento("agregar").onclick = agregarProducto;
elemento("form-pedido").onsubmit = guardarPedido;
elemento("form-entrega").onsubmit = guardarEntrega;
elemento("pedido-entrega").onchange = async function () {
    try { await cargarHistorial(); }
    catch (error) { informar(error.message, true); }
};
const hoy = new Date();
const fechaLocal = hoy.getFullYear() + "-" + String(hoy.getMonth() + 1).padStart(2, "0")
    + "-" + String(hoy.getDate()).padStart(2, "0");
elemento("fecha").value = fechaLocal;
elemento("fecha").max = fechaLocal;
cargarDatos();
