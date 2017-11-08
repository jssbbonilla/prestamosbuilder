
package managedbeansv3;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author kevin
 */
public class MensajesFormularios {
 
    
    public void msgCreadoExito() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Creado!", "Creado con Exito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void msgFaltanCampos() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Existen campos vacios"));
    }
    
    public void msgModificacion() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardado!", "Modificación exitosa"));
    }
    
    public void msgErrorAlModificar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Ocurrio un error al modificar"));
    }
    
    public void msgEliminacion() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado!", "Borrado con exito"));
    }
    
    public void msgValorCuotaMinima() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error!", "El pago es menor a la cuota minima"));
    }
    
    public void msgPagoExito() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago!", "Realizado con exito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void msgExcederPago() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El pago supera al saldo restante"));
    }
    public void msgErrorAlCrear() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo crear el registro"));
    }
    public void msgErrorAlEliminar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo eliminar el registro"));
    }
    public void msgAdvertenciaAlEliminar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cliente invalido" , "Porfavor seleccione un cliente"));
    }
    public void msgGuardadoExito() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardado!", "Realizado con exito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void msgErrorValorIncorrecto() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "valor incorrecto"));
    }
    
    public void msgErrorAlLeer() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo leer el archivo"));
    }
    public void msgErrorAlGuardar() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "No se pudo guardar el archivo"));
    }
}
