# Gestión de inventario

Proyecto web base desarrollado con Java 17, Maven y Jakarta EE 11.

## Requisitos

- JDK 17
- Maven 3.9 o superior
- Apache NetBeans 30 (opcional)
- Un servidor compatible con Jakarta EE 11

## Compilar

```bash
mvn clean package
```

El archivo WAR se genera en `target/gestion_inventario-1.0-SNAPSHOT.war`.

## Abrir en NetBeans

1. Clona o descarga este repositorio.
2. En NetBeans, selecciona **File > Open Project**.
3. Abre la carpeta del proyecto.
4. Configura un servidor compatible con Jakarta EE y ejecuta el proyecto.

## Endpoint de prueba

Después de desplegar la aplicación, el recurso REST de prueba queda disponible en:

```text
/gestion_inventario/resources/jakartaee11
```
