/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Bitacora;
import entities.Cliente;
import entities.Cuota;
import entities.Parametro;
import entities.Prestamo;
import entities.Usuario;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.ControladorBitacora;
import manager.ControladorCuota;
import manager.ControladorDocumentos;
import manager.ControladorParametro;
import manager.ControladorPrestamo;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author kevin
 */
@Named(value = "frmPrestamos")
@ViewScoped
public class FrmPrestamos implements Serializable {

    private Usuario usuario = new Usuario();
    private Bitacora bit = new Bitacora();
    private ControladorBitacora cbit = new ControladorBitacora();

    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();
    Usuario sesion = (Usuario) sessionMap.get("user");

    private ControladorPrestamo cprestamo = new ControladorPrestamo();
    private ControladorCuota ccuota = new ControladorCuota();
    private ControladorParametro cparametro = new ControladorParametro();
    private ControladorDocumentos cdocumento = new ControladorDocumentos();
    private List<Prestamo> lprestamo = new ArrayList<Prestamo>();
    private Prestamo sprestamo = new Prestamo();
    private Cliente scliente = new Cliente();
    private Cuota scuota = new Cuota();
    private Parametro parametro = new Parametro();
    private boolean busquedaActiva = false;
    private String busqueda = "";
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();

    Calendar cal = Calendar.getInstance();
    String fechaDiaHoy = cal.get(cal.YEAR) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.DATE);

    public FrmPrestamos() {
        sprestamo.capitalizacion = 'M';
        mostrarAlCargar();
    }

    public void activarBusqueda() {
        busquedaActiva = true;
    }

    public void crearPrestamo() throws Exception {
        if (sprestamo.validarPrestamo() == true) {

            sprestamo.fechaInicio = metodo.utilDatetoSqlDate(sprestamo.getFechaInicio().toString());
            sprestamo.fechaFin = metodo.utilDatetoSqlDate(sprestamo.getFechaFin().toString());
            sprestamo.estado = 'A';

            cprestamo.agregar(sprestamo);
            generarAccion("Genero un prestamos para el cliente con el dui : " + sprestamo.getDui());
            nuevo();
        } else {
            mensaje.msgFaltanCampos();
        }
    }

    public void cambiarSeleccion(SelectEvent e) {
        this.sprestamo = (Prestamo) e.getObject();

    }

    public void nuevo() {
        this.sprestamo = new Prestamo();
    }

    public void eliminarPrestamo() throws Exception {
        if (sprestamo.getIdPrestamo() != 0) {
            ccuota.eliminarCuotasPrestamo(String.valueOf(sprestamo.idPrestamo));
            cdocumento.eliminarDocumetosPrestamo(sprestamo.dui);
            cprestamo.eliminarprestamo(sprestamo);
            generarAccion("Elimino un prestamos para el cliente con el dui : " + sprestamo.getDui());
            sprestamo = new Prestamo();
            nuevo();
        }
    }

    public void mostraDatosPrestamo() throws ParseException {
        if (sprestamo.getCantidadCuotas() != 0 && sprestamo.getMonto() != 0) {

            double valor = 0;
            String fechaFinal = "";

            valor = sprestamo.calcularCuotaMensual();
            sprestamo.valorCuota = valor;
            sprestamo.setFechaInicio(metodo.stringToSqlDate(fechaDiaHoy));

            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.MONTH, this.sprestamo.cantidadCuotas);
            fechaFinal = cal2.get(cal2.YEAR) + "/" + (cal2.get(cal2.MONTH) + 1) + "/" + cal2.getActualMaximum(Calendar.DAY_OF_MONTH);
            sprestamo.setFechaFin(metodo.stringToSqlDate(fechaFinal));
        } else {
            mensaje.msgFaltanCampos();
        }
    }

    public void mostrarInteresPrestamo() {
        try {
            if (sprestamo.capitalizacion == 'M') {
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(1)).get(0);
                sprestamo.tasaInteres = Double.parseDouble(eparametro.valor);
            } else if (sprestamo.capitalizacion == 'D') {
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(2)).get(0);
                sprestamo.tasaInteres = Double.parseDouble(eparametro.valor);
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void mostrarAlCargar() {
        try {
            sprestamo.tasaMora = cparametro.obtenerTasaMora();
            mostrarInteresPrestamo();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void generarAccion(String accion) {

        bit.setAccion(accion);
        bit.setId_usuario(sesion.getId());
        cbit.agregar(bit);
    }

    /*---------------Getter and Setter -------------------------*/
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

    public List<Prestamo> getLprestamo() {
        if (busquedaActiva) {
            lprestamo = cprestamo.Obtener(busqueda);
        } else {
            lprestamo = cprestamo.ObtenerActivos();
        }
        return lprestamo;
    }

    public void setLprestamo(List<Prestamo> lprestamo) {
        this.lprestamo = lprestamo;
    }

    public Prestamo getSprestamo() {
        return sprestamo;
    }

    public void setSprestamo(Prestamo sprestamo) {
        this.sprestamo = sprestamo;
    }

    public Cliente getScliente() {
        return scliente;
    }

    public void setScliente(Cliente scliente) {
        this.scliente = scliente;
    }

    public Cuota getScuota() {
        return scuota;
    }

    public void setScuota(Cuota scuota) {
        this.scuota = scuota;
    }

    public boolean isBusquedaActiva() {
        return busquedaActiva;
    }

    public void setBusquedaActiva(boolean busquedaActiva) {
        this.busquedaActiva = busquedaActiva;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public String getFechaDiaHoy() {
        return fechaDiaHoy;
    }

    public void setFechaDiaHoy(String fechaDiaHoy) {
        this.fechaDiaHoy = fechaDiaHoy;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public ControladorParametro getCparametro() {
        return cparametro;
    }

    public void setCparametro(ControladorParametro cparametro) {
        this.cparametro = cparametro;
    }

}
