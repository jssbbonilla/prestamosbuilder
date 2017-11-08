/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Bitacora;
import entities.Cliente;
import entities.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import manager.ControladorBitacora;
import manager.ControladorUsuario;


/**
 *
 * @author phyroot
 */
@Named(value = "frmBitacora")
@ViewScoped
public class FrmBitacora implements Serializable{
    
        private List<Bitacora> lbitacora = new ArrayList<Bitacora>();
        private ControladorBitacora cbitacora = new ControladorBitacora();
        private Bitacora sbitacora = new Bitacora();

    
        
        
      
   
        
        
        
        
        
        
        
        
        
        
        
        
   public List<Bitacora> getLbitacora() {
       
            lbitacora = cbitacora.obtener();

        
        return lbitacora;
    }

    public void setLbitacora(List<Bitacora> lbitacora) {
        this.lbitacora = lbitacora;
    }

    /**
     * @return the sbitacora
     */
    public Bitacora getSbitacora() {
        return sbitacora;
    }

    /**
     * @param sbitacora the sbitacora to set
     */
    public void setSbitacora(Bitacora sbitacora) {
        this.sbitacora = sbitacora;
    }

   
}
