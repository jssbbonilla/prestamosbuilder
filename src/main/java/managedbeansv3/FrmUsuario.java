package managedbeansv3;

import entities.Bitacora;
import entities.Cliente;
import entities.Usuario;
import manager.ControladorBitacora;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import javax.inject.Named;
import manager.ControladorUsuario;

@Named(value = "frmUsuarios")
@RequestScoped
public class FrmUsuario implements Serializable {

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();
    private Usuario usuario = new Usuario();
    
    private Usuario usuarionuevo = new Usuario();
    private ControladorUsuario cusuario = new ControladorUsuario();
    private ControladorUsuario userman = new ControladorUsuario();
    private ControladorBitacora cbit = new ControladorBitacora();
    private Bitacora bit = new Bitacora();
    private List<Usuario> lsuarios = new ArrayList<Usuario>();
    private Usuario susario = new Usuario();
    private boolean editarActivo = true;

    public void initialize(ComponentSystemEvent cse) throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("user")) {

        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

        }
    }

    public void login() throws IOException{
        usuario = userman.autenticar(usuario);
        if ((usuario) != null) {
           sessionMap.put("user", usuario);
           generarAccion("El usuario inicio Sesión");
           externalContext.redirect("../index.xhtml");
           

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso denegado", "Usuario o contraseña incorrecta");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void logout() throws IOException {
                generarAccion("El usuario termino sesíon");

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

    }
    
    public void editar(){
        try {

                userman.editar(susario);
                generarAccion("Se editó al usuario con el id: "+susario.getId());
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
       

    public void crearCliente() throws Exception {
        try {
            if (validadObjeto()) {
                cusuario.agregar(usuarionuevo);
                generarAccion("EL usuario creo el siguiente usuario : "+usuarionuevo.getNombre() + " con el id "+ usuarionuevo.getId());
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Campos sin llenar");
                FacesContext.getCurrentInstance().addMessage(null, message);
            };

        } catch (Exception e) {

            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean validadObjeto() {

        if (usuarionuevo.getNombre().isEmpty() || usuarionuevo.getApellido().isEmpty()
                || usuarionuevo.getClave().isEmpty() || usuarionuevo.getLogin().isEmpty()
                || String.valueOf(usuarionuevo.getRol()).isEmpty()) {
            return false;
        }

        return true;
    }
    
    public void generarAccion(String accion){
        
      
            Usuario sesion = (Usuario) sessionMap.get("user");
            bit.setAccion(accion);
            bit.setId_usuario(sesion.getId());
            cbit.agregar(bit);
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the lsuarios
     */
    public List<Usuario> getLsuarios() {
        this.lsuarios = userman.obtener();
        return lsuarios;
    }

    /**
     * @param lsuarios the lsuarios to set
     */
    public void setLsuarios(List<Usuario> lsuarios) {
        this.lsuarios = lsuarios;
    }

    /**
     * @return the susario
     */
    public Usuario getSusario() {
        return susario;
    }

    /**
     * @param susario the susario to set
     */
    public void setSusario(Usuario susario) {
        this.susario = susario;
    }

    /**
     * @return the usuarionuevo
     */
    public Usuario getUsuarionuevo() {
        return usuarionuevo;
    }

    /**
     * @param usuarionuevo the usuarionuevo to set
     */
    public void setUsuarionuevo(Usuario usuarionuevo) {
        this.usuarionuevo = usuarionuevo;
    }

    /**
     * @return the busquedaActiva
     */
    public boolean isEditarActivo() {
        return editarActivo;
    }

    /**
     * @param busquedaActiva the busquedaActiva to set
     */
    public void setEditarActivo(boolean busquedaActiva) {
        this.editarActivo = busquedaActiva;
    }

}
