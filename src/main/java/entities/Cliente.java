package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Cliente implements Serializable {

    public String dui;
    public String nit;
    public String nombre;
    public String apellido;
    public char sexo;
    public String direcion;
    private List<Documento> documentos;
    public String telefono;
    public Date fechaNacimiento;
    public String observacion;
    public String profesion;

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getDirecion() {
        return direcion;
    }

    public void setDirecion(String direcion) {
        this.direcion = direcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    /*------- Anadido Getter and Setter-----*/
    

    /*---------------Metodos de la Documentacion ----------*/

    public boolean Validar() {
        boolean validacion = false;
        if (dui.isEmpty() != true && nit.isEmpty() != true
                && nombre.isEmpty() != true && apellido.isEmpty() != true
                && direcion.isEmpty() != true && fechaNacimiento == null != true
                && fechaNacimiento.toString().isEmpty() != true) {
            return validacion = true;
        } else {
            return validacion = false;
        }
    }

    /*---------------End Metodos de la Documentacion ----------*/

    /**
     * @return the documentos
     */
    public List<Documento> getDocumentos() {
        return documentos;
    }

    /**
     * @param documentos the documentos to set
     */
    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
}
