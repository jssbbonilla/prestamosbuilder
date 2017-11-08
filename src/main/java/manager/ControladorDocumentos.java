
package manager;

import entities.Documento;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import managedbeansv3.MensajesFormularios;
import services.Conexion;


public class ControladorDocumentos extends Conexion implements Serializable {
    
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    ErrorHandlerApp ep = new ErrorHandlerApp();
    Conexion cn = new Conexion();

    public int obtenerMaxId(String DUI) {
        int id = 0;
        ResultSet resultado;
        try {
            Conexion conexion = new Conexion();
            resultado = conexion.getValores("SELECT correlativo From documento WHERE dui='" + DUI + "'");
            
            if (resultado.first()) {
                resultado.last();
                id = resultado.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorPrestamo.class.getName()).log(Level.SEVERE, null, ex);
        }

        id = id + 1;
        return id;
    }
    
    public void eliminarDocumentos(Documento doc) {
        try {
            if (doc == null) {
                System.err.println("Documento invalido , verfique los datos");
                mensaje.msgAdvertenciaAlEliminar();
            } else {
                UID("DELETE FROM prestamos.documento WHERE dui='" + doc.getDui() + "'");
                mensaje.msgEliminacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlEliminar();
        }
    }
    
    public void eliminarDocumetosPrestamo(String idPrestamo) {
        try {
            if (idPrestamo == null) {
                System.err.println("idPrestamo invalido , verfique los datos");
            } else {
                UID("DELETE FROM prestamos.documento WHERE dui='" + idPrestamo + "'");
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
        }
    }
    
    public byte[] getProductImage(String dui) throws IOException, SQLException{
        Conexion cn=null;
        byte[] imagen = null;
        
        ResultSet rs;
        rs = cn.getValores("SELECT archivo FROM documento WHERE dui='"+dui+"' AND correlativo= 1");
        if(rs.first()){
            imagen = rs.getBytes("archivo");
        }
        return imagen;
    }
}
