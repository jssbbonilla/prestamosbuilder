package entities;

import entities.Cliente;
import java.io.Serializable;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import managedbeansv3.MetodosShare;
import manager.ControladorCuota;
import manager.ControladorParametro;
import manager.ControladorPrestamo;
import manager.ErrorHandlerApp;

public class Prestamo implements Serializable {

    public int idPrestamo;
    public String dui;
    public double monto;
    public double valorCuota;
    public double tasaInteres;
    public int cantidadCuotas;
    public Date fechaInicio;
    public Date fechaFin;
    public Date fechaUltimoPago;
    public double saldo;
    public char estado;
    public String observaciones;
    public char capitalizacion;
    public double tasaMora;

    Cliente ecliente = new Cliente();
    Cuota ecuota = new Cuota();
    ControladorCuota ccuota = new ControladorCuota();
    ControladorParametro cparametro = new ControladorParametro();
    ErrorHandlerApp ep = new ErrorHandlerApp();
    MetodosShare metodo = new MetodosShare();

    /*-----Getter and Setter ----------*/
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public char getCapitalizacion() {
        return capitalizacion;
    }

    public void setCapitalizacion(char capitalizacion) {
        this.capitalizacion = capitalizacion;
    }

    public Cliente getEcliente() {
        return ecliente;
    }

    public void setEcliente(Cliente ecliente) {
        this.ecliente = ecliente;
    }

    public Cuota getEcuota() {
        return ecuota;
    }

    public void setEcuota(Cuota ecuota) {
        this.ecuota = ecuota;
    }

    public ErrorHandlerApp getEp() {
        return ep;
    }

    public void setEp(ErrorHandlerApp ep) {
        this.ep = ep;
    }

    public ControladorParametro getCparametro() {
        return cparametro;
    }

    public void setCparametro(ControladorParametro cparametro) {
        this.cparametro = cparametro;
    }

    public double getTasaMora() {
        return tasaMora;
    }

    public void setTasaMora(double tasaMora) {
        this.tasaMora = tasaMora;
    }

    /*------End Getter ans Setter ---------*/
 /*-----------Metodo para Calcular Cuota---------------------*/
    public double calcularCuotaMensual() {

        Calendar cal = Calendar.getInstance();
        double cuotaMensual = 0;
        double interes = 0;
        int conversion = 1;
        int diasDelMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        try {
            if (this.capitalizacion == 'M') {
                conversion = 1;
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(1)).get(0);
                tasaInteres = Double.parseDouble(eparametro.valor);

                interes = tasaInteres / 100;
                double prestamo = monto;

                double resultado = (prestamo) * ((interes / conversion) / (1 - (Math.pow(1 + (interes / conversion), (-cantidadCuotas)))));
                cuotaMensual = metodo.mascaraDosDigitos(resultado);

            } else if (this.capitalizacion == 'D') {
                conversion = diasDelMes;
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(2)).get(0);
                tasaInteres = Double.parseDouble(eparametro.valor);

                interes = (tasaInteres / (100)) / (30 * cantidadCuotas);
                double resultado = (this.monto) * (interes) / (1 - Math.pow(1 + interes, -cantidadCuotas));
                cuotaMensual = metodo.mascaraDosDigitos(resultado);

            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

        return cuotaMensual;
    }

    /*-------------------Crear Nueva Cuota---------------------------------*/
    public Cuota crearNuevaCuota() {
        this.ecuota = new Cuota();
        return this.ecuota;
    }

    /*-----------------Calcular Intereses Mensuales-------------------------------*/
    public double CalcularInteresMensuales() {
        double intereses = 0;
        double porcentajeInteres = tasaInteres / 100;
        double saldoActual = saldo;
        Calendar cal = Calendar.getInstance();
        int diasDelMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        double mul = 0;
        double mulmeses = 0;
        int mesActual = 0;
        int mesPago = 0;
        int numMeses = 0;
        String fechaActual = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE);
        

        if (this.capitalizacion == 'M') {
            intereses = 0;
            porcentajeInteres = tasaInteres / 100;
            saldoActual = saldo;
            mul = 1;
            numMeses = 1;

        } else if (this.capitalizacion == 'D') {
            try {
                String verificacionCuota = "";

                verificacionCuota = ccuota.veficacionExisteCuota(String.valueOf(this.idPrestamo));
                if (verificacionCuota.isEmpty() != true) {
                    String fechaDiaHoy = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE);

                    Cuota datoscuotas = new Cuota();
                    datoscuotas = ccuota.buscarCuota(String.valueOf(this.idPrestamo)).get(0);
                    Date fechaPagada = datoscuotas.fecha;

                    int d = metodo.encontrarDias(fechaPagada.toString(), fechaDiaHoy);
                    int m = metodo.encontrarMesesDiarios(fechaPagada.toString(), fechaDiaHoy);

                    double dias = Double.parseDouble(String.valueOf(d));
                    double meses = Double.parseDouble(String.valueOf(m));

                    if (meses == 0) {
                        meses = diasDelMes;
                        mul = (dias / (diasDelMes));
                    }else{
                        mul = (dias / (meses*diasDelMes));
                    }
                    /*
                    mesActual = (cal.get(cal.MONTH) + 1);
                    mesPago = metodo.obtenerMesesFecha(fechaPagada.toString());
                    numMeses = mesActual - mesPago;
                    */
                    numMeses = metodo.encontrarMeses(fechaPagada.toString(), fechaActual);

                } else {
                    String fechaDiaHoy = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE);

                    Prestamo datosprestamo = new Prestamo();
                    ControladorPrestamo cprestamo = new ControladorPrestamo();
                    datosprestamo = cprestamo.buscarPrestamo(String.valueOf(this.idPrestamo)).get(0);
                    Date fechaPagada = datosprestamo.fechaInicio;

                    int d = metodo.encontrarDias(fechaPagada.toString(), fechaDiaHoy);
                    int m = metodo.encontrarMesesDiarios(fechaPagada.toString(), fechaDiaHoy);

                    double dias = Double.parseDouble(String.valueOf(d));
                    double meses = Double.parseDouble(String.valueOf(m));

                    if (meses == 0) {
                        meses = diasDelMes;
                        mul = (dias / (diasDelMes));
                    }else{
                        mul = (dias / (meses*diasDelMes));
                    }
                    /*
                    mesActual = (cal.get(cal.MONTH) + 1);
                    mesPago = metodo.obtenerMesesFecha(fechaPagada.toString());
                    numMeses = mesActual - mesPago;
                    */
                    numMeses = metodo.encontrarMeses(fechaPagada.toString(), fechaActual);

                    if (numMeses == 0) {
                        numMeses = 1;
                    }

                }

                intereses = 0;
                porcentajeInteres = (tasaInteres / 100) * (mul);
                saldoActual = saldo;

            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        }

        try {
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator('.');
            DecimalFormat decimales = new DecimalFormat("#.##", simbolo);
            double resultado = (saldoActual) * (porcentajeInteres);
            String covertidor = decimales.format(resultado);
            intereses = Double.valueOf(covertidor) * (numMeses);

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

        return intereses;
    }

    public double calcularMora() {
        double mora = 0;

        int mesActual = 0;
        int mesPago = 0;
        int numMeses = 0;
        Calendar cal = Calendar.getInstance();
        double valorPrestamo = 0;
        double tasaMora = 0;
        double mesesMora = 0;
        String fechaActual = cal.get(cal.YEAR) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.DATE);
        
        try {
            String verificacionCuota = "";
            verificacionCuota = ccuota.veficacionExisteCuota(String.valueOf(this.idPrestamo));
            if (verificacionCuota.isEmpty() != true) {

                Cuota datoscuotas = new Cuota();
                datoscuotas = ccuota.buscarCuota(String.valueOf(this.idPrestamo)).get(0);
                Date fechaPagada = datoscuotas.fecha;
                /*
                mesActual = (cal.get(cal.MONTH) + 1);
                mesPago = metodo.obtenerMesesFecha(fechaPagada.toString());
                numMeses = mesActual - mesPago;
                */
                numMeses = metodo.encontrarMeses(fechaPagada.toString(), fechaActual);

                ControladorParametro cparametro = new ControladorParametro();
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(3)).get(0);

                Prestamo datosprestamo = new Prestamo();
                ControladorPrestamo cprestamo = new ControladorPrestamo();
                datosprestamo = cprestamo.buscarPrestamo(String.valueOf(this.idPrestamo)).get(0);

                if (numMeses > 0) {

                    if (numMeses == 1) {
                        mesesMora = 0;
                    } else {
                        mesesMora = numMeses - 1;
                    }
                }

                valorPrestamo = datosprestamo.monto;
                tasaMora = Double.parseDouble(eparametro.valor);
                mora = (valorPrestamo * (tasaMora / 100)) * (mesesMora);
            } else {

                Prestamo datosprestamo = new Prestamo();
                ControladorPrestamo cprestamo = new ControladorPrestamo();
                datosprestamo = cprestamo.buscarPrestamo(String.valueOf(this.idPrestamo)).get(0);
                Date fechaPagada = datosprestamo.fechaInicio;
                /*
                mesActual = (cal.get(cal.MONTH) + 1);
                mesPago = metodo.obtenerMesesFecha(fechaPagada.toString());
                numMeses = mesActual - mesPago;
                */
                numMeses = metodo.encontrarMeses(fechaPagada.toString(), fechaActual);

                ControladorParametro cparametro = new ControladorParametro();
                Parametro eparametro = new Parametro();
                eparametro = cparametro.buscarPrametro(String.valueOf(3)).get(0);

                if (numMeses > 0) {

                    if (numMeses == 1) {
                        mesesMora = 0;
                    } else {
                        mesesMora = numMeses - 1;
                    }
                }

                valorPrestamo = datosprestamo.monto;
                tasaMora = Double.parseDouble(eparametro.valor);
                mora = (valorPrestamo * (tasaMora / 100)) * (mesesMora);

            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

        return mora;
    }


    /*----------------Validar Campos Prestamos-------------------------------------*/
    public boolean validarPrestamo() {
        boolean validacion = false;
        if ((dui.isEmpty() && dui == null) != true && (monto <= 0) != true && (valorCuota <= 0) != true
                && (tasaInteres <= 0) != true && (cantidadCuotas <= 0) != true
                && (fechaInicio == null) != true && fechaFin == null != true
                && (fechaInicio.toString().isEmpty()) != true && fechaFin.toString().isEmpty() != true) {
            return validacion = true;
        } else {
            return validacion = false;
        }
    }
}
