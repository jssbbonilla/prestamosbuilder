package manager;

import entities.Cuota;
import entities.Parametro;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import managedbeansv3.MensajesFormularios;
import services.Conexion;


public class ControladorParametro extends Conexion implements Serializable{
    
    ErrorHandlerApp ep = new ErrorHandlerApp();
    Conexion cn = new Conexion();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    Parametro param = new Parametro();
    
    public List<Parametro> buscarPrametro(String s) {
        
        List<Parametro> parametros = new ArrayList<Parametro>();
        
        try {
            param = new Parametro();
            rst = getValores("SELECT * FROM prestamos.parametro WHERE id_parametro='" + s + "'");
            while (rst.next()) {
                param.setIdParametro(rst.getInt("id_parametro"));
                param.setNombre(rst.getString("nombre"));
                param.setValor(rst.getString("valor"));
                parametros.add(param);
            }
            return parametros;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }
    
    public List<Parametro> obtenerParametros() {

        List<Parametro> parametros = new ArrayList<Parametro>();

        try {
            param = new Parametro();
            rst = getValores("SELECT * FROM parametro");
            while (rst.next()) {
                param = new Parametro();
                param.setIdParametro(rst.getInt("id_parametro"));
                param.setNombre(rst.getString("nombre"));
                param.setValor(rst.getString("valor"));
                parametros.add(param);
            }
            return parametros;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }
    
    public void modificarParametro(Parametro p) {
        try {
            if (p == null) {
                System.err.println("Parametro invalido , verfique los datos");
                
            } else {
                UID("UPDATE prestamos.parametro SET nombre='"+p.nombre+"', valor='"+p.valor+"' WHERE id='"+p.idParametro+"'");
                mensaje.msgModificacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlModificar();
        }
    }
    
    public void eliminarParamentro(Parametro param) {
        try {
            if (param == null) {
                System.err.println("Prestamo invalido , verfique los datos");
                mensaje.msgAdvertenciaAlEliminar();
            } else {
                UID("DELETE FROM parametro WHERE id='" + param.idParametro + "'");
                mensaje.msgEliminacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlEliminar();
        }
    }
    
    public void agregarParametro(Parametro param) {
        try {
            UID("INSERT INTO prestamos.parametro (id_parametro, nombre, valor) VALUES ('" + param.idParametro + "', '" + param.nombre + "', '" + param.valor + "')");
            mensaje.msgGuardadoExito();
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlCrear();
        }
    }
    
    public void eliminarTodosParametros(){
        try {
            UID("TRUNCATE TABLE prestamos.parametro;");
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlEliminar();
        }
    }
    
    public double obtenerTasaMora(){
        double tasaMora=0;
        ResultSet rs = null;
        rs=getValores("SELECT valor FROM prestamos.parametro WHERE id_parametro='3'");
        try {
            while(rs.next()){
                tasaMora = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return tasaMora;
    }
   
    
}
