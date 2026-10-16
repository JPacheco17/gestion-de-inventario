package edward.gestion_inventario.service;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.*;
import java.util.*;
import java.util.function.Function;
import edward.gestion_inventario.util.JPAUtil;
public final class ReglasPedido {
 public static WebApplicationException error(int status,String mensaje) {
  return new WebApplicationException(Response.status(status).entity(Map.of("mensaje",mensaje)).build());
 }
 public static String texto(String value,String campo,int max,boolean obligatorio) {
  String s=value==null?"":value.trim();
  if(obligatorio && s.isEmpty()) throw error(400,campo+" es obligatorio");
  if(s.length()>max) throw error(400,campo+" supera "+max+" caracteres");
  return s;
 }
 public static Long id(Long id) { if(id==null || id<1) throw error(400,"Identificador inválido"); return id; }
 public static <T> T existe(T valor) { if(valor==null) throw error(404,"Registro no encontrado"); return valor; }
 public static <T> T consultar(Function<EntityManager,T> accion) {
  try(EntityManager em=JPAUtil.crearEntityManager()) { return accion.apply(em); }
 }
 public static <T> T transaccion(Function<EntityManager,T> accion) {
  try(EntityManager em=JPAUtil.crearEntityManager()) {
   EntityTransaction tx=em.getTransaction();
   try { tx.begin(); T resultado=accion.apply(em); tx.commit(); return resultado; }
   catch(RuntimeException ex) { if(tx.isActive()) tx.rollback(); throw ex; }
  }
 }
 public static String email(String value) {
  String s=texto(value,"Correo",150,false);
  if(!s.isEmpty() && !s.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw error(400,"Correo inválido");
  return s;
 }
 private ReglasPedido() {}
}
