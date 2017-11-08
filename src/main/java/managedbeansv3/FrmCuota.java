/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeansv3;

import entities.Bitacora;
import entities.Cliente;
import entities.Cuota;
import entities.Prestamo;
import entities.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.ControladorBitacora;
import manager.ControladorCliente;
import manager.ControladorCuota;
import manager.ControladorPrestamo;
import org.primefaces.event.SelectEvent;
import services.PrinterOptions;
import services.Ticket;

/**
 *
 * @author kevin
 */
@Named(value = "frmCuota")
@ViewScoped
public class FrmCuota implements Serializable{
    
    private Usuario usuario = new Usuario();
    private Bitacora bit = new Bitacora();
    private ControladorBitacora cbit = new ControladorBitacora();
        private PrinterOptions po = new PrinterOptions();
        private Ticket tk = new Ticket();


    private ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    private Map<String, Object> sessionMap = externalContext.getSessionMap();
           Usuario sesion = (Usuario) sessionMap.get("user");

    
    private String duiParam;
    private String idParam;

    ControladorCuota ccuota = new ControladorCuota();
    private List<Cuota> lcuota = new ArrayList<Cuota>();

    private Cuota scuota = new Cuota();
    private Prestamo sprestamo = new Prestamo();
    private Cliente scliente = new Cliente();

    private ControladorPrestamo cprestamo = new ControladorPrestamo();
    private ControladorCliente ccliente = new ControladorCliente();
    private List<Cliente> lcliente = new ArrayList<Cliente>();

    private double cuotaMinima;
    private String busqueda;
    private boolean busquedaActiva = false;
    private double valorMora;
    private double valorInteres;
    private double cuotaAcumulada;
    private double totalPagar;

    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();
    
    
    public FrmCuota() {
    }
    
    public String redirecionar(String dui) {
        return "clientesdetalles.xhtml?dui="+dui+"&?faces-redirect=true";
}
    public String comprabantePago(String dui, int idPrestamo, int numCuota){
        return "comprobantepago.xhtml?dui="+dui+"&id="+idPrestamo+"&numcuota="+numCuota+"&?faces-redirect=true";
    }
    
    public void nuevo() {
        this.scuota = new Cuota();
    }
    
    public void activarBusqueda() {
        busquedaActiva = true;
    }

    public void crearPagoCuota() throws Exception {

        String dui = duiParam;
        int idPrestamo = Integer.parseInt(idParam);
        this.scuota.idPrestamo = idPrestamo;
        

        if (scuota.getValor() != 0  || scuota.getInteres() !=0 && scuota.getCapital() !=0 
                && scuota.getSaldoAnterior() !=0 && scuota.getSaldoActualizado() !=0 ) {
            try {
                /*----------- Agregar la cuota -------------------*/
                ccuota.agregarCuotaCalculada(scuota);
                generarAccion("Registró un pago para el prestamo con id : ");
                imprmir();

                this.sprestamo.idPrestamo = idPrestamo;
                this.sprestamo.saldo = this.scuota.saldoActualizado;
                /*---------Actualizar Saldo Prestamo---------------------------*/
                cprestamo.actualizarSaldo(sprestamo);
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        } else {
            mensaje.msgFaltanCampos();
        }
    }
    
    public void eliminarCuota() throws Exception {
        if (scuota.getNumCuota() != 0) {
            ccuota.eliminar(scuota);
            generarAccion("Eliminó una cuota para el prestamo con id ");
            scuota = new Cuota();
            mensaje.msgEliminacion();
        }
    }

    public void cambiarSeleccion(SelectEvent e) {
        this.scuota = (Cuota) e.getObject();
    }

    public List<Cliente> listarClientes() {

        try {
            lcliente = ccliente.obtener();
            return lcliente;

        } catch (Exception e) {
            return null;
        }
    }

    public List<Cuota> listaCuotas() {
        try {
            lcuota = ccuota.obtener();
            return lcuota;
        } catch (Exception e) {
            return null;
        }
    }
    
      public PrinterOptions llenarticket(){
    po.setId(scuota.getIdPrestamo());
    po.setCliente(scliente.getNombre());
    po.setValorCuota(scuota.getValor());
    po.setInteres(scuota.getInteres());
    po.setCapital(scuota.getValor());
    po.setMora(scuota.getMora());
    po.setNumeroCuota(scuota.getNumCuota());
    po.setSaldo(scuota.getSaldoActualizado());
    
    return po;
    
    }
    public void imprmir(){
    
    tk.ticket(llenarticket());
    
    }
    
    public void datosCalculados() {

        /*------------El a pagar deber ser diferente de cero-------------------*/
        if (scuota.getValor() != 0) {

            String dui = duiParam;
            int idPrestamo = Integer.parseInt(idParam);
            double saldo = 0;
            double valorcuota = 0;
            int mesActual = 0;
            int mesPago = 0;
            int numMeses = 0;
            int fechaComparacion = 0;
            double resultado = 0;
            double mesesMora=0;
            
            Date fechaPago = null;
            String fechaActual = "";
            Date fechaUltimoPago = null;
            String verificacionCuota = "";
            Date fechaRevision = null;
            
            Calendar cal3 = Calendar.getInstance();
            fechaActual = cal3.get(cal3.YEAR) + "-" + (cal3.get(cal3.MONTH) + 1) + "-" + cal3.get(cal3.DATE);


            /*----------El usuario no pueda ingrear un pago menor a los intereses--------------*/
            if (cuotaMinima <= scuota.valor) {

                saldo = metodo.mascaraDosDigitos(ccuota.ultimoSaldoPrestamo(idPrestamo));

                /*--------------------El usuario no pueda pagar mas del prestamo-------------------*/
                double totalprestamo = saldo + cuotaMinima;
                if (totalprestamo >= scuota.valor) {

                    this.scuota.saldoAnterior = saldo;

                    Prestamo datosprestamo = new Prestamo();
                    datosprestamo = cprestamo.buscarPrestamo(String.valueOf(idParam)).get(0);
                    fechaUltimoPago = datosprestamo.fechaUltimoPago;
//                    

                    /*-------------------No halla error al realizar un pago-----------------------*/
                    verificacionCuota = ccuota.veficacionExisteCuota(idParam);
                    
                    if(sprestamo.capitalizacion == 'M'){
                    
                    if (verificacionCuota.isEmpty() == true || verificacionCuota == null) {

                        try {
                            fechaRevision = datosprestamo.fechaInicio;

                            //Calendar cal = Calendar.getInstance();
                            //mesActual = (cal.get(cal.MONTH) + 1);
                            //mesPago = metodo.obtenerMesesFecha(fechaRevision.toString());
                            //numMeses = mesActual - mesPago;
                            numMeses = metodo.encontrarMeses(fechaRevision.toString(), fechaActual);

                            if (numMeses == 0) {
                                numMeses = 1;
                            }
                            
                            if(numMeses>0){
                                
                                if(numMeses==1){
                                    mesesMora=0;
                                }
                                else{
                                    mesesMora=numMeses-1;
                                }
                            }

                            this.scuota.interes = metodo.mascaraDosDigitos((this.sprestamo.CalcularInteresMensuales()) * mesesMora);
                            this.scuota.capital = metodo.mascaraDosDigitos(this.scuota.valor - this.scuota.interes - this.scuota.mora);
                            this.scuota.saldoActualizado = metodo.mascaraDosDigitos((this.scuota.saldoAnterior - this.scuota.capital));

                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                        }

                    } else {

                        /*---------------No aplicar intereses si el pago es el mismo mes------------*/
                        try {
                            Calendar cal = Calendar.getInstance();
                            fechaActual = cal.get(cal.YEAR) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.DATE);

                            Cuota datoscuotas = new Cuota();
                            datoscuotas = ccuota.buscarCuota(String.valueOf(idPrestamo)).get(0);
                            fechaPago = datoscuotas.fecha;
                            /*
                            mesActual = (cal.get(cal.MONTH) + 1);
                            mesPago = metodo.obtenerMesesFecha(fechaPago.toString());
                            numMeses = mesActual - mesPago;
                            */
                            numMeses = metodo.encontrarMeses(fechaPago.toString(), fechaActual);
                            
                            if(numMeses>0){
                                
                                if(numMeses==1){
                                    mesesMora=0;
                                }
                                else{
                                    mesesMora=numMeses-1;
                                }
                            }
                            

                            if ((mesActual - mesPago) > 0) {
                                this.scuota.mora = metodo.mascaraDosDigitos(this.sprestamo.calcularMora());
                                this.scuota.interes = metodo.mascaraDosDigitos((this.sprestamo.CalcularInteresMensuales()) * mesesMora);
                                this.scuota.capital = metodo.mascaraDosDigitos(this.scuota.valor - this.scuota.interes - this.scuota.mora);
                                this.scuota.saldoActualizado = metodo.mascaraDosDigitos((this.scuota.saldoAnterior - this.scuota.capital));

                            } else {
                                this.scuota.interes = 0;
                                this.scuota.capital = metodo.mascaraDosDigitos(this.scuota.valor);
                                this.scuota.saldoActualizado = metodo.mascaraDosDigitos(this.scuota.saldoAnterior - this.scuota.valor);
                            }
                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    }
                    
                    /*---------------------- CAPITALIZACION DIARIA ------------------------*/
                    else if (sprestamo.capitalizacion == 'D') {
                        this.scuota.mora = metodo.mascaraDosDigitos(this.sprestamo.calcularMora());
                        this.scuota.interes = metodo.mascaraDosDigitos((this.sprestamo.CalcularInteresMensuales()));
                        this.scuota.capital = metodo.mascaraDosDigitos(this.scuota.valor - this.scuota.interes - this.scuota.mora);
                        this.scuota.saldoActualizado = metodo.mascaraDosDigitos((this.scuota.saldoAnterior - this.scuota.capital));
                        
                    }
                    
                    
                } else {
                    mensaje.msgExcederPago();
                }
            } else {
                mensaje.msgValorCuotaMinima();
            }
        } else {
            mensaje.msgFaltanCampos();
        }
    }

    

    

    public void mostrarAlIniciar() {
        String dui = duiParam;
        int idPrestamoParam = Integer.parseInt(idParam);

        this.scliente = ccliente.buscar(dui).get(0);
        this.sprestamo = cprestamo.buscarPrestamo(idParam).get(0);
        this.scuota.numCuota = ccuota.obtenerNumCuota(idPrestamoParam);
        this.scuota.idPrestamo = idPrestamoParam;
        this.sprestamo.idPrestamo = idPrestamoParam;

        Date fechaPago = null;
        String fechaActual = "";
        String fechaUltimoPago = "";
        String verificacionCuota = "";
        Date fechaRevision = null;

        int mesActual = 0;
        int mesPago = 0;
        int numMeses = 0;
        int fechaComparacion = 0;
        
        Calendar cal = Calendar.getInstance();
        fechaActual = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE);

        verificacionCuota = ccuota.veficacionExisteCuota(idParam);
        
        if(sprestamo.capitalizacion == 'M'){
        if (verificacionCuota.isEmpty() == true || verificacionCuota == null) {

            try {
                Prestamo datosprestamo = new Prestamo();
                datosprestamo = cprestamo.buscarPrestamo(idParam).get(0);
                fechaRevision = datosprestamo.fechaInicio;
                
                /*
                Calendar cal = Calendar.getInstance();
                mesActual = (cal.get(cal.MONTH) + 1);
                mesPago = metodo.obtenerMesesFecha(fechaRevision.toString());
                numMeses = mesActual - mesPago;
                */
                numMeses = metodo.encontrarMeses(fechaRevision.toString(), fechaActual);

                if (numMeses == 0) {
                    numMeses = 1;
                }
                
                valorMora = metodo.mascaraDosDigitos(this.sprestamo.calcularMora());
                valorInteres = metodo.mascaraDosDigitos(((this.sprestamo.CalcularInteresMensuales()) * numMeses));
                int valorAnterior= numMeses;
                if (numMeses == 1) {
                    valorAnterior=0;
                }
                cuotaMinima = valorInteres +valorMora + (datosprestamo.valorCuota*(valorAnterior));
                int aumentar= numMeses;
                if (numMeses == 0) {
                    aumentar=1;
                }
                cuotaAcumulada = sprestamo.valorCuota * aumentar;
                totalPagar = cuotaMinima+cuotaAcumulada;

            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }

        } else {

            try {

                try {

                    Cuota datoscuotas = new Cuota();
                    datoscuotas = ccuota.buscarCuota(String.valueOf(idPrestamoParam)).get(0);
                    fechaPago = datoscuotas.fecha;

                    //mesActual = (cal.get(cal.MONTH) + 1);
                    //mesPago = metodo.obtenerMesesFecha(fechaPago.toString());
                    //numMeses = mesActual - mesPago;
                    numMeses = metodo.encontrarMeses(fechaPago.toString(), fechaActual);
                    
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                }
                
                valorMora = metodo.mascaraDosDigitos(this.sprestamo.calcularMora());
                valorInteres = metodo.mascaraDosDigitos((((this.sprestamo.saldo) * (this.sprestamo.tasaInteres / 100)) * numMeses));
                int valorAnterior= numMeses;
                if (numMeses == 1) {
                    valorAnterior=0;
                }
                cuotaMinima = valorInteres +valorMora + (sprestamo.valorCuota*(valorAnterior));
                int aumentar= numMeses;
                if (numMeses == 0) {
                    aumentar=1;
                }
                cuotaAcumulada = sprestamo.valorCuota * aumentar;
                totalPagar = cuotaMinima+cuotaAcumulada;

            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
        }//Fin capitalizacion Mensual
        
        
        /*------------------ CAPITALIZACION DIARIA --------------------------------*/
        else if(sprestamo.capitalizacion == 'D'){
            valorMora = metodo.mascaraDosDigitos(this.sprestamo.calcularMora());
            valorInteres = metodo.mascaraDosDigitos((this.sprestamo.CalcularInteresMensuales()));
            int valorAnterior= numMeses;
                if (numMeses == 1) {
                    valorAnterior=0;
                }
            cuotaMinima = valorInteres +valorMora + (sprestamo.valorCuota*(valorAnterior));
            int aumentar= numMeses;
                if (numMeses == 0) {
                    aumentar=1;
                }
            cuotaAcumulada = sprestamo.valorCuota * aumentar;
            totalPagar = cuotaMinima+cuotaAcumulada;
        }
        
        
    }
    
     public void generarAccion(String accion) {

        bit.setAccion(accion);
        bit.setId_usuario(sesion.getId());
        cbit.agregar(bit);
    }

    
    
    /*-------------------- Getter and Setter -------------------------*/

    public String getDuiParam() {
        return duiParam;
    }

    public void setDuiParam(String duiParam) {
        this.duiParam = duiParam;
    }

    public String getIdParam() {
        return idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
        mostrarAlIniciar();
    }

    public ControladorCuota getCcuota() {
        return ccuota;
    }

    public void setCcuota(ControladorCuota ccuota) {
        this.ccuota = ccuota;
    }

    public List<Cuota> getLcuota() {
        if (busquedaActiva) {
            this.lcuota = ccuota.buscar(busqueda);
            return lcuota;
        } else {
            this.lcuota = ccuota.obtener();
        return lcuota;
        }
    }

    public void setLcuota(List<Cuota> lcuota) {
        this.lcuota = lcuota;
    }

    public Cuota getScuota() {
        return scuota;
    }

    public void setScuota(Cuota scuota) {
        this.scuota = scuota;
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

    public ControladorPrestamo getCprestamo() {
        return cprestamo;
    }

    public void setCprestamo(ControladorPrestamo cprestamo) {
        this.cprestamo = cprestamo;
    }

    public ControladorCliente getCcliente() {
        return ccliente;
    }

    public void setCcliente(ControladorCliente ccliente) {
        this.ccliente = ccliente;
    }

    public List<Cliente> getLcliente() {
        return lcliente;
    }

    public void setLcliente(List<Cliente> lcliente) {
        this.lcliente = lcliente;
    }

    public double getCuotaMinima() {
        return cuotaMinima;
    }

    public void setCuotaMinima(double cuotaMinima) {
        this.cuotaMinima = cuotaMinima;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public boolean isBusquedaActiva() {
        return busquedaActiva;
    }

    public void setBusquedaActiva(boolean busquedaActiva) {
        this.busquedaActiva = busquedaActiva;
    }

    public double getValorMora() {
        return valorMora;
    }

    public void setValorMora(double valorMora) {
        this.valorMora = valorMora;
    }

    public double getValorInteres() {
        return valorInteres;
    }

    public void setValorInteres(double valorInteres) {
        this.valorInteres = valorInteres;
    }

    public double getCuotaAcumulada() {
        return cuotaAcumulada;
    }

    public void setCuotaAcumulada(double cuotaAcumulada) {
        this.cuotaAcumulada = cuotaAcumulada;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }
    
    
    
    
}
