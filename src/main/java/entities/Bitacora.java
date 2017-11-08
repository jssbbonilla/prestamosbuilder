package entities;

import java.io.Serializable;
import java.util.Date;
import net.sf.jasperreports.engine.util.DigestUtils;
import org.bouncycastle.crypto.Digest;

public class Bitacora implements Serializable{
    
    private int id_bitacora;
    private int id_usuario;
    private Date fecha;
    private String accion;

    /**
     * @return the id_bitacora
     */
    public int getId_bitacora() {
        return id_bitacora;
    }

    /**
     * @param id_bitacora the id_bitacora to set
     */
    public void setId_bitacora(int id_bitacora) {
        this.id_bitacora = id_bitacora;
    }

    /**
     * @return the id_usuario
     */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
     * @param id_usuario the id_usuario to set
     */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the accion
     */
    public String getAccion() {
        return accion;
    }

    /**
     * @param accion the accion to set
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
   
    
    
}
