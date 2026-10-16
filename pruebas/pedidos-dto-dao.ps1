param([string]$BaseUrl='http://127.0.0.1:8085/gestion-inventario',[string]$Resultado='resultado-pedidos-dto-dao.json')
$ErrorActionPreference='Stop'
$script:checks=[Collections.Generic.List[object]]::new()
function Verificar($nombre,$ok) {
 $script:checks.Add([pscustomobject]@{prueba=$nombre;correcto=[bool]$ok})
 if(-not $ok){throw "Falló: $nombre"}
}
function Solicitar($ruta,$metodo='GET',$datos=$null,$esperado=200) {
 $args=@{Uri="$BaseUrl/api/$ruta";Method=$metodo;SkipHttpErrorCheck=$true;TimeoutSec=30}
 if($null -ne $datos){$args.ContentType='application/json';$args.Body=$datos|ConvertTo-Json -Depth 8 -Compress}
 $r=Invoke-WebRequest @args
 Verificar "$metodo $ruta HTTP $esperado" ([int]$r.StatusCode -eq $esperado)
 if($r.Content){return $r.Content|ConvertFrom-Json}
}
try {
 $proveedor=Solicitar 'proveedores' POST @{nombre='Proveedor DTO y DAO'} 201
 $cliente=Solicitar 'clientes' POST @{nombre='Cliente DTO y DAO';direccion='Datos ficticios'} 201
 $producto=Solicitar 'productos' POST @{nombre='Producto DTO y DAO';precio=1500.25;stock=4;idProveedor=$proveedor.idProveedor} 201
 $pedido=Solicitar 'pedidos' POST @{idCliente=$cliente.idCliente;detalles=@(@{idProducto=$producto.idProducto;cantidad=2})} 201
 Verificar 'PedidoDTO contiene su clave y su cliente' ($pedido.idPedido -gt 0 -and $pedido.idCliente -eq $cliente.idCliente)
 Verificar 'PedidoDTO contiene fecha y estado inicial' ($pedido.fechaPedido -match '^\d{4}-\d{2}-\d{2}$' -and $pedido.estado -eq 'PENDIENTE')
 $lineas=@(Solicitar "pedidos/$($pedido.idPedido)/detalles")
 Verificar 'DetallePedidoDTO contiene claves y valores de la tabla' ($lineas.Count -eq 1 -and $lineas[0].idDetalle -gt 0 -and $lineas[0].idPedido -eq $pedido.idPedido -and $lineas[0].idProducto -eq $producto.idProducto -and $lineas[0].cantidad -eq 2 -and $lineas[0].precioUnitario -eq 1500.25)
 $detalle=Solicitar "detalles-pedido/$($lineas[0].idDetalle)"
 Verificar 'DetallePedidoDAO consulta por identificador' ($detalle.idDetalle -eq $lineas[0].idDetalle)
 $lista=@(Solicitar "detalles-pedido?idPedido=$($pedido.idPedido)")
 Verificar 'DetallePedidoDAO filtra por pedido' ($lista.Count -eq 1 -and $lista[0].idPedido -eq $pedido.idPedido)
 $porCliente=@(Solicitar "pedidos?idCliente=$($cliente.idCliente)")
 Verificar 'PedidoDAO filtra por cliente' ($porCliente.Count -eq 1 -and $porCliente[0].idPedido -eq $pedido.idPedido)
 $evento=Solicitar 'historial-entregas' POST @{idPedido=$pedido.idPedido;fechaEntrega=(Get-Date -Format 'yyyy-MM-dd');estadoEntrega='en proceso'} 201
 Verificar 'HistorialEntregaDTO contiene claves, fecha y estado' ($evento.idEntrega -gt 0 -and $evento.idPedido -eq $pedido.idPedido -and $evento.estadoEntrega -eq 'EN_PROCESO')
 $guardado=Solicitar "historial-entregas/$($evento.idEntrega)"
 Verificar 'HistorialEntregaDAO consulta por identificador' ($guardado.idEntrega -eq $evento.idEntrega)
 $eventos=@(Solicitar "historial-entregas?idPedido=$($pedido.idPedido)")
 Verificar 'HistorialEntregaDAO filtra por pedido' ($eventos.Count -eq 1 -and $eventos[0].idPedido -eq $pedido.idPedido)
 $consultado=Solicitar "pedidos/$($pedido.idPedido)"
 Verificar 'Pedido mantiene total y estado tras registrar evento' ($consultado.total -eq 3000.50 -and $consultado.estado -eq 'EN_PROCESO')
 $null=Solicitar 'detalles-pedido' GET $null 400
 $null=Solicitar 'historial-entregas' GET $null 400
 $null=Solicitar 'detalles-pedido/99999999' GET $null 404
 $null=Solicitar 'historial-entregas/99999999' GET $null 404
 $null=Solicitar 'pedidos/99999999/detalles' GET $null 404
 $null=Solicitar 'pedidos?idCliente=99999999' GET $null 404
} finally {
 ConvertTo-Json -InputObject @($script:checks) -Depth 6 | Set-Content -LiteralPath $Resultado -Encoding utf8
}
Write-Output ("Pruebas específicas correctas: "+$script:checks.Count)
