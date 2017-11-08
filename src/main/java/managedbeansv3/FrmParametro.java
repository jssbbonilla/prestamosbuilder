package managedbeansv3;

import entities.Parametro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import manager.ControladorParametro;

@Named(value = "frmParametro")
@ViewScoped
public class FrmParametro implements Serializable {

    ControladorParametro cparametro = new ControladorParametro();
    private Parametro eparametro = new Parametro();
    private List<Parametro> lprametro = new ArrayList<Parametro>();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    MetodosShare metodo = new MetodosShare();

    private double interesMensual;
    private double interesDiario;
    private double mora;

    public FrmParametro() {
        mostrarAlIniciar();
    }

    public class BlockUIController implements Serializable {

        private static final long serialVersionUID = 20130903L;

        public void doSomething() {
            try {
                // simulate a long running request  
                Thread.sleep(3000);
            } catch (Exception e) {
                // ignore  
            }
        }
    }

    public void guardarConfiguracion() {
        if ((interesMensual < 0) != true && (interesMensual < 0) != true
                && (mora < 0) != true) {

            try {
                cparametro.eliminarTodosParametros();

                eparametro.idParametro = 1;
                eparametro.nombre = String.valueOf("interes_mensual");
                eparametro.valor = String.valueOf(interesMensual);

                if (eparametro.validarParametro() == true) {
                    cparametro.agregarParametro(eparametro);
                } else {
                    mensaje.msgErrorValorIncorrecto();
                }
                eparametro = new Parametro();
                eparametro.idParametro = 2;
                eparametro.nombre = String.valueOf("interes_diaria");
                eparametro.valor = String.valueOf(interesDiario);

                if (eparametro.validarParametro() == true) {
                    cparametro.agregarParametro(eparametro);
                } else {
                    mensaje.msgErrorValorIncorrecto();
                }

                eparametro = new Parametro();
                eparametro.idParametro = 3;
                eparametro.nombre = String.valueOf("mora");
                eparametro.valor = String.valueOf(mora);

                if (eparametro.validarParametro() == true) {
                    cparametro.agregarParametro(eparametro);
                } else {
                    mensaje.msgErrorValorIncorrecto();
                }

            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            }
        } else {
            mensaje.msgErrorValorIncorrecto();
        }

    }

    public void mostrarAlIniciar() {
        try {
            Parametro eparametro = new Parametro();
            eparametro = cparametro.buscarPrametro(String.valueOf(1)).get(0);
            interesMensual = Double.parseDouble(eparametro.valor);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        try {
            Parametro eparametro = new Parametro();
            eparametro = cparametro.buscarPrametro(String.valueOf(2)).get(0);
            interesDiario = Double.parseDouble(eparametro.valor);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        try {
            Parametro eparametro = new Parametro();
            eparametro = cparametro.buscarPrametro(String.valueOf(3)).get(0);
            mora = Double.parseDouble(eparametro.valor);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }

    }

    /*------------------- Getter and Setter ------------------------------*/
    public ControladorParametro getCparametro() {
        return cparametro;
    }

    public void setCparametro(ControladorParametro cparametro) {
        this.cparametro = cparametro;
    }

    public Parametro getEparametro() {
        return eparametro;
    }

    public void setEparametro(Parametro eparametro) {
        this.eparametro = eparametro;
    }

    public List<Parametro> getLprametro() {
        return lprametro;
    }

    public void setLprametro(List<Parametro> lprametro) {
        this.lprametro = lprametro;
    }

    public double getInteresMensual() {
        return interesMensual;
    }

    public void setInteresMensual(double interesMensual) {
        this.interesMensual = interesMensual;
    }

    public double getInteresDiario() {
        return interesDiario;
    }

    public void setInteresDiario(double interesDiario) {
        this.interesDiario = interesDiario;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }

}
