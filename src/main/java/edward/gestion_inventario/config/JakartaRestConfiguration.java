package edward.gestion_inventario.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("aplication")
//configura el llamado de la ruta para los diferentes sevcios 
//ruta base http://localhost:8080/gestion-inventario/api
public class JakartaRestConfiguration extends Application {
}
