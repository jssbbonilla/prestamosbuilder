package entities;

import java.io.Serializable;
import net.sf.jasperreports.engine.util.DigestUtils;
import org.bouncycastle.crypto.Digest;

public class Usuario implements Serializable{
    
    private int id;
    private String login;
    private String nombre;
    private String apellido;
    private String clave;
    private char rol;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        
        this.clave = clave;
    }

    /**
     * @return the rol
     */
    public char getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(char rol) {
        this.rol = rol;
    }
    
    
    
}
