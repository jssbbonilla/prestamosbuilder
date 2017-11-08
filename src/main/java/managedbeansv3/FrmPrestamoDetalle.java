/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Cliente;
import entities.Prestamo;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.ControladorCliente;
import manager.ControladorPrestamo;

/**
 *
 * @author kevin
 */
@Named(value = "frmPrestamoDetalle")
@ViewScoped
public class FrmPrestamoDetalle implements Serializable{

    private String duiParam;
    private String prestamoParam;
    private Prestamo sprestamo = new Prestamo();
    private Prestamo eprestamo = new Prestamo();

    private Cliente scliente = new Cliente();
    ControladorCliente ccliente = new ControladorCliente();
    ControladorPrestamo cprestamo = new ControladorPrestamo();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();
    
    public FrmPrestamoDetalle() {
    }
    
    public boolean validarPrestamoModificar() {
        boolean validacion = false;
        if ((eprestamo.monto <= 0) != true && (eprestamo.valorCuota <= 0) != true
                && (eprestamo.tasaInteres <= 0) != true && (eprestamo.cantidadCuotas <= 0) != true
                && (eprestamo.saldo < 0) != true && (eprestamo.fechaUltimoPago.toString().isEmpty()) != true
                && (eprestamo.fechaInicio.toString().isEmpty()) != true && eprestamo.fechaFin.toString().isEmpty() != true) {
            return validacion = true;
        } else {
            return validacion = false;
        }
    }
    
    public void modificar(){
        try {
            if (validarPrestamoModificar()== true) {
                eprestamo.fechaInicio = metodo.convertirSqlDatemas1dia(eprestamo.getFechaInicio().toString());
                eprestamo.fechaFin = metodo.convertirSqlDatemas1dia(eprestamo.getFechaFin().toString());
                eprestamo.fechaUltimoPago = metodo.convertirSqlDatemas1dia(eprestamo.getFechaUltimoPago().toString());
                cprestamo.modificar(eprestamo);
            } else {
                mensaje.msgFaltanCampos();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    
    }
    
    
    /*--------------------------Getter and Setter -------------------------*/

    public String getDuiParam() {
        return duiParam;
    }

    public void setDuiParam(String duiParam) {
        this.duiParam = duiParam;
    }

    public String getPrestamoParam() {
        return prestamoParam;
    }

    public void setPrestamoParam(String prestamoParam) {
        this.prestamoParam = prestamoParam;
    }

    public Prestamo getSprestamo() {
        this.sprestamo=cprestamo.Obtenerid(prestamoParam).get(0);
        eprestamo=sprestamo;
        return sprestamo;
    }

    public void setSprestamo(Prestamo sprestamo) {
        this.sprestamo = sprestamo;
    }

    public Prestamo getEprestamo() {
        return eprestamo;
    }

    public void setEprestamo(Prestamo eprestamo) {
        this.eprestamo = eprestamo;
    }

    public Cliente getScliente() {
        this.scliente=ccliente.buscar(duiParam).get(0);
        return scliente;
    }

    public void setScliente(Cliente scliente) {
        this.scliente = scliente;
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
    
}
