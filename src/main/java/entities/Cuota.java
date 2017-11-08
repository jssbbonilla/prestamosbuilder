package entities;

import java.io.Serializable;
import java.util.Date;

public class Cuota implements Serializable {

    public int idPrestamo;
    public int numCuota;
    public double valor;
    public double interes;
    public Date fecha;
    public double capital;
    public double saldoAnterior;
    public double saldoActualizado;
    public double mora;

    
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public int getNumCuota() {
        return numCuota;
    }

    public void setNumCuota(int numCuota) {
        this.numCuota = numCuota;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public double getSaldoActualizado() {
        return saldoActualizado;
    }

    public void setSaldoActualizado(double saldoActualizado) {
        this.saldoActualizado = saldoActualizado;
    }
    
    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }
    
    /*---------------End Metodos de la Documentacion ----------*/

    
}
