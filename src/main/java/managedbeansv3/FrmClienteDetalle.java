/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Cliente;
import entities.Cuota;
import entities.Documento;
import entities.Prestamo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;
import manager.ControladorCliente;
import manager.ControladorCuota;
import manager.ControladorDocumentos;
import manager.ControladorPrestamo;
import org.apache.commons.io.FileUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import services.Conexion;

@Named("frmclientedetalle")
@ViewScoped
public class FrmClienteDetalle implements Serializable {

    ControladorCliente ccliente = new ControladorCliente();
    ControladorPrestamo cprestamo = new ControladorPrestamo();
    private List<Prestamo> lprestamo = new ArrayList<Prestamo>();
    private Cliente scliente = new Cliente();
    private Cliente ecliente = new Cliente();
    ControladorCuota ccuota = new ControladorCuota();
    private List<Cuota> lcuota = new ArrayList<Cuota>();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();
    private Cuota scuota = new Cuota();
    private String duiParam;
     private List<Documento> ldoc = new ArrayList<Documento>();
    private Documento sdoc = new Documento();
    private Documento ndoc = new Documento();
    private String imagen;
    private String nombreArchivo="Vacio";
    ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
            .getExternalContext().getContext();
    String realPath = ctx.getRealPath("/");
    File destFile = new File(realPath + "resources/pdf/", "docSubir.pdf");
    
    private String desc;
    
   private ControladorDocumentos doc = new ControladorDocumentos();
    
    Conexion cn= new Conexion();
    
    
    
    /**
     * @return the duiParam
     */
     
    
     
    public String getDuiParam() {
        return duiParam;
    }
    
    
    public void imagen(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        String nombre = file.getFileName();
        System.out.println(nombre);    

    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException{
        int correlativo = doc.obtenerMaxId(ecliente.getDui());
        if(event.getFile()!=null){
           
            try{
                System.out.println(getDesc());
                cn.UID("INSERT INTO documento(dui, correlativo, nombre_archivo, archivo, descripcion)"
                + " VALUES('" + ecliente.getDui() + "','" + correlativo + "','" +
                event.getFile().getFileName() + "','" +
                event.getFile().getInputstream() + "','" + this.getDesc()+"')");
               
            }catch(Exception e){
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }else{
           System.out.println("El archivo o imagen se encuentra vacio");
       }
     
    }
    
    public StreamedContent traerImagen() throws SQLException, IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        if(context.getCurrentPhaseId()==PhaseId.RENDER_RESPONSE){
           return new DefaultStreamedContent(); 
           
        }else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] image = null;
            try{
                image= new ControladorDocumentos().getProductImage(ecliente.getDui());
                
            }catch (SQLException e){
                
            }
            return new DefaultStreamedContent(new ByteArrayInputStream(image), "image/png");
        }   
    }
    
    
    
    
    
    
    public void upload(FileUploadEvent event) throws IOException {
  if(destFile.exists()){
  }else{
  destFile = new File(realPath + "resources/pdf/", "docSubir.pdf");
  }  
        if (event.getFile() != null) {
            this.nombreArchivo=(event.getFile().getFileName());
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);

            FileUtils.copyInputStreamToFile(event.getFile().getInputstream(), destFile);

        } else {
            FacesMessage message = new FacesMessage("No papu");
            FacesContext.getCurrentInstance().addMessage(null, message);

        }

    }
    public void eliminarDocumento(Documento d){
        getLdoc().remove(getLdoc().indexOf(d));
   
    }

    public void agregarArchivo() {
        if(destFile.exists()){
            Documento addDoc = new Documento();
            addDoc.setArchivo(destFile);
            addDoc.setNombre(getNdoc().getNombre());
            addDoc.setDescripcion(getNdoc().getDescripcion());
            getLdoc().add(addDoc);
            scliente.setDocumentos(getLdoc());
            ndoc = new Documento();
            nombreArchivo = "Vacio";
            destFile.delete();
        }else{
             FacesMessage message = new FacesMessage("Aun no se adjuntan archivos");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
        }

    }
    
    public void modificar() throws ParseException {
        try {
            if (ecliente.Validar()) {
                ecliente.fechaNacimiento = metodo.convertirSqlDatemas1dia(ecliente.getFechaNacimiento().toString());
                ccliente.editar(ecliente);
            } else {
                mensaje.msgFaltanCampos();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void setDuiParam(String duiParam) {
        this.duiParam = duiParam;
    }

    /**
     * @return the scliente
     */
    public Cliente getScliente() {
        this.scliente=ccliente.buscar(duiParam).get(0);
        setEcliente(scliente);
        return scliente;
    }

    /**
     * @param scliente the scliente to set
     */
    public void setScliente(Cliente scliente) {
        
        this.scliente = scliente;
    }

    /**
     * @return the ecliente
     */
    public Cliente getEcliente() {
        return ecliente;
    }

    /**
     * @param ecliente the ecliente to set
     */
    public void setEcliente(Cliente ecliente) {
        this.ecliente = ecliente;
    }

    /**
     * @return the lprestamo
     */
    public List<Prestamo> getLprestamo() {
        lprestamo=cprestamo.buscar(scliente.dui);
        return lprestamo;
    }

    /**
     * @param lprestamo the lprestamo to set
     */
    public void setLprestamo(List<Prestamo> lprestamo) {
        this.lprestamo = lprestamo;
    }

    /**
     * @return the lcuota
     */
    public List<Cuota> getLcuota() {
        lcuota = ccuota.buscarCuotas(scliente.dui);
        return lcuota;
    }

    /**
     * @param lcuota the lcuota to set
     */
    public void setLcuota(List<Cuota> lcuota) {
        this.lcuota = lcuota;
    }

    /**
     * @return the scuota
     */
    public Cuota getScuota() {
        return scuota;
    }

    /**
     * @param scuota the scuota to set
     */
    public void setScuota(Cuota scuota) {
        this.scuota = scuota;
    }

    public ControladorCliente getCcliente() {
        return ccliente;
    }

    public void setCcliente(ControladorCliente ccliente) {
        this.ccliente = ccliente;
    }

    public ControladorPrestamo getCprestamo() {
        return cprestamo;
    }

    public void setCprestamo(ControladorPrestamo cprestamo) {
        this.cprestamo = cprestamo;
    }

    public ControladorCuota getCcuota() {
        return ccuota;
    }

    public void setCcuota(ControladorCuota ccuota) {
        this.ccuota = ccuota;
    }

    /**
     * @return the ldoc
     */
    public List<Documento> getLdoc() {
                this.ldoc = scliente.getDocumentos();

        return ldoc;
    }

    /**
     * @param ldoc the ldoc to set
     */
    public void setLdoc(List<Documento> ldoc) {
        this.ldoc = ldoc;
    }

    /**
     * @return the sdoc
     */
    public Documento getSdoc() {
        return sdoc;
    }

    /**
     * @param sdoc the sdoc to set
     */
    public void setSdoc(Documento sdoc) {
        this.sdoc = sdoc;
    }

    /**
     * @return the ndoc
     */
    public Documento getNdoc() {
        return ndoc;
    }

    /**
     * @param ndoc the ndoc to set
     */
    public void setNdoc(Documento ndoc) {
        this.ndoc = ndoc;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
         return "/images/imagenClienteDetalle.png";
        
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
   
  
}
