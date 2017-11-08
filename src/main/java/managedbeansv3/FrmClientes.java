/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Bitacora;
import entities.Cliente;
import entities.Usuario;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import manager.ControladorBitacora;
import manager.ControladorCliente;
import manager.ControladorDocumentos;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import services.Conexion;

/**
 *
 * @author kevin
 */
@Named(value = "frmClientes")
@ViewScoped
public class FrmClientes implements Serializable {

    private Usuario usuario = new Usuario();
    private Bitacora bit = new Bitacora();
    private ControladorBitacora cbit = new ControladorBitacora();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();
    Usuario sesion = (Usuario) sessionMap.get("user");

    ControladorCliente ccliente = new ControladorCliente();
    DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
    private List<Cliente> lcliente = new ArrayList<Cliente>();
    private Cliente scliente = new Cliente();
    private Cliente nuevoCliente = new Cliente();
    private String busqueda;
    private boolean busquedaActiva = false;
    private String imagenNuevo;
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();
    private ControladorDocumentos doc = new ControladorDocumentos();

    Conexion cn = new Conexion();

    public void imagen(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        String nombre = file.getFileName();
        System.out.println(nombre);

    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        int correlativo = doc.obtenerMaxId(nuevoCliente.getDui());
        if (event.getFile() != null) {

            try {
                cn.UID("INSERT INTO documento(dui, correlativo, nombre_archivo, archivo, descripcion)"
                        + " VALUES('" + nuevoCliente.getDui() + "','" + correlativo + "','"
                        + event.getFile().getFileName() + "','"
                        + event.getFile().getInputstream() + "','" + "IMAGEN" + "')");
                nuevo();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        } else {
            System.out.println("El archivo o imagen se encuentra vacio");
        }

    }

    public FrmClientes() {
    }

    public void activarBusqueda() {
        busquedaActiva = true;
    }

    public void crearCliente() throws Exception {
        try {
            if (nuevoCliente.Validar()) {
                nuevoCliente.fechaNacimiento = metodo.utilDatetoSqlDate(nuevoCliente.getFechaNacimiento().toString());
                generarAccion("Se creo el cliente con dui :" + nuevoCliente.getDui());
                ccliente.agregar(nuevoCliente);
            } else {
                mensaje.msgFaltanCampos();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void eliminarCliente() throws Exception {
        if (scliente.getDui().isEmpty() != true) {
            ccliente.eliminar(scliente);
            generarAccion("Se elimino el cliente con el dui : " + scliente.getDui());
            scliente = new Cliente();
        }
    }

    public void cambiarSeleccion(SelectEvent e) {
        this.scliente = (Cliente) e.getObject();

    }

    public void nuevo() {
        this.nuevoCliente = new Cliente();
    }

    public Cliente buscar(String esta) {
        return ccliente.buscar(esta).get(0);
    }
    
     public void generarAccion(String accion) {

        bit.setAccion(accion);
        bit.setId_usuario(sesion.getId());
        cbit.agregar(bit);
        
        
    }

    /*-------------------------Getter and Setter----------------------------*/
    public List<Cliente> getLcliente() {
        if (busquedaActiva) {
            lcliente = ccliente.buscar(busqueda);

        } else {
            lcliente = ccliente.obtener();

        }
        return lcliente;
    }

    public void setLcliente(List<Cliente> lcliente) {
        this.lcliente = lcliente;
    }

    /**
     * @return the scliente
     */
    public Cliente getScliente() {
        return scliente;
    }

    /**
     * @param scliente the scliente to set
     */
    public void setScliente(Cliente scliente) {
        this.scliente = scliente;
    }

    /**
     * @return the busqueda
     */
    public String getBusqueda() {
        return busqueda;
    }

    /**
     * @param busqueda the busqueda to set
     */
    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    /**
     * @return the nuevoCliente
     */
    public Cliente getNuevoCliente() {
        return nuevoCliente;
    }

    /**
     * @param nuevoCliente the nuevoCliente to set
     */
    public void setNuevoCliente(Cliente nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }

    public ControladorCliente getCcliente() {
        return ccliente;
    }

    public void setCcliente(ControladorCliente ccliente) {
        this.ccliente = ccliente;
    }

    public boolean isBusquedaActiva() {
        return busquedaActiva;
    }

    public void setBusquedaActiva(boolean busquedaActiva) {
        this.busquedaActiva = busquedaActiva;
    }

    public String getImagenNuevo() {
        return imagenNuevo;
    }

    public void setImagenNuevo(String imagenNuevo) {
        this.imagenNuevo = imagenNuevo;
    }

    public Conexion getCn() {
        return cn;
    }

    public void setCn(Conexion cn) {
        this.cn = cn;
    }

}
