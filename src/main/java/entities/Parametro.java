package entities;

import java.io.Serializable;

public class Parametro implements Serializable{
    
    public int idParametro;
    public String nombre;
    public String valor;
    
    /*---------------- Getter and Setter ----------------------------*/

    public int getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    
    /*------------------Metodos de la Documentacion-----------------------*/
    
    public boolean validarParametro() {
        boolean validacion = false;
        if ((idParametro <= 0) != true && (nombre.isEmpty()) != true
                && (Double.parseDouble(valor) < 0) != true) {
            validacion = true;
        } else {
            validacion = false;
        }
        return validacion;
    }
    
}
