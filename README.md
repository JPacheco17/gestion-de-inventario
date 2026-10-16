# Gestión de inventario

Backend desarrollado con Java 21, Maven, Jersey, Hibernate/JPA y MariaDB/MySQL.
La organización por capas sigue la arquitectura del proyecto Gestión Veterinaria.

## Arquitectura

```text
config/         Configuración de Jakarta REST
controller/     Endpoints REST
dao/            Acceso a datos y GenericDAO
dto/            Objetos enviados y recibidos como JSON
model/entity/   Entidades JPA
service/        Validaciones y lógica de negocio
util/           Creación del EntityManager con JPAUtil
```

Por ahora se implementan las entidades Producto, Proveedor y Cliente.

## Requisitos

- JDK 21
- Maven 3.9 o superior
- Apache Tomcat 11
- MariaDB o MySQL

## Base de datos

Crear una base de datos llamada `gestion_inventario`. La conexión se configura en:

```text
src/main/resources/META-INF/persistence.xml
```

La configuración inicial usa el usuario `root` sin contraseña, como un XAMPP local típico.

## Compilar

```bash
mvn clean package
```

El archivo generado es `target/gestion-inventario.war`.

## Endpoints

Después de desplegar en Tomcat:

```text
/gestion-inventario/api/productos
/gestion-inventario/api/proveedores
/gestion-inventario/api/clientes
```

Cada recurso cuenta con operaciones GET, POST, PUT y DELETE.
