package manager;

import entities.Bitacora;
import entities.Cliente;
import entities.Usuario;
import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import managedbeansv3.MensajesFormularios;
import services.Conexion;

public class ControladorBitacora extends Conexion implements Serializable {

    private ErrorHandlerApp ep = new ErrorHandlerApp();
    private Conexion cn = new Conexion();
    private Bitacora bitacora = new Bitacora();
    private MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    
    public List<Bitacora> obtener() {
        List<Bitacora> bitacoras = new ArrayList<Bitacora>();
        try {
            bitacora = new Bitacora();
            rst = getValores("SELECT * FROM bitacora");
            while (rst.next()) {
                bitacora = new Bitacora();
                bitacora.setId_bitacora(rst.getInt("id_bitacora"));
                bitacora.setId_usuario(rst.getInt("id_usuario"));;
                bitacora.setFecha(rst.getDate("fecha"));
                bitacora.setAccion(rst.getString("accion"));
                bitacoras.add(bitacora);
            }
            return bitacoras;
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }

    }
     public void agregar(Bitacora bit) {

        try{
                PreparedStatement pstmt = conexion().prepareStatement("INSERT INTO bitacora (id_usuario,fecha,accion) VALUES (?,now(),?)");
                pstmt.setInt(1, bit.getId_usuario());
                pstmt.setString(2, bit.getAccion());
             

                String sql = pstmt.toString();
                System.out.println("Succesful "+ sql);
                UID(pstmt);
                pstmt.close();
                
            
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            
            mensaje.msgErrorAlCrear();
        }

    }
    
    
    
}
