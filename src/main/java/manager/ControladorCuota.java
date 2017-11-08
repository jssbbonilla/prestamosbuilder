
package manager;

import entities.Cliente;
import entities.Cuota;
import entities.Prestamo;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import managedbeansv3.MensajesFormularios;
import services.Conexion;

public class ControladorCuota extends Conexion implements Serializable{
    
    ErrorHandlerApp ep = new ErrorHandlerApp();
    Conexion cn = new Conexion();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion

    
    public List<Cuota> obtener() {

        List<Cuota> cuotas = new ArrayList<Cuota>();

        try {
            Cuota cuota = new Cuota();
            rst = getValores("SELECT * FROM cuota");
            while (rst.next()) {
                cuota = new Cuota();
                cuota.setIdPrestamo(rst.getInt("id_prestamo"));
                cuota.setNumCuota(rst.getInt("num_cuota"));
                cuota.setValor(rst.getDouble("valor"));
                cuota.setInteres(rst.getDouble("interes"));
                cuota.setFecha(rst.getDate("fecha"));
                cuota.setCapital(rst.getDouble("capital"));
                cuota.setSaldoAnterior(rst.getDouble("saldo_anterior"));
                cuota.setSaldoActualizado(rst.getDouble("saldo_actualizado"));
                cuota.setMora(rst.getDouble("mora"));
                cuotas.add(cuota);
            }
            return cuotas;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }
    
       /*-------------------- Agregar Cuota ------------------------------*/
    public void agregar(Cuota cuota) {

        try {
            UID("INSERT INTO couta (id_prestamo, num_cuota, valor, interes, fecha, capital, saldo_anterior, saldo_actualizado, mora) VALUES ('" + cuota.idPrestamo + "', '" + cuota.numCuota + "', '" + cuota.valor + "', '" + cuota.interes + "', '" + cuota.fecha + "', '" + cuota.capital + "', '" + cuota.saldoAnterior + "', '" + cuota.saldoActualizado + "', '" + cuota.mora + "')");
            mensaje.msgPagoExito();
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlCrear();
        }
    }


    /* ---------------------------  ELIMINAR  ----------------------------*/
    public void eliminar(Cuota cuota) {
        try {
            if (cuota == null) {
                System.err.println("Cuota invalido , verfique los datos");
                mensaje.msgAdvertenciaAlEliminar();
            } else {
                UID("DELETE FROM cuota WHERE num_cuota ='" + cuota.numCuota + "' AND id_prestamo='" + cuota.idPrestamo + "'");
                mensaje.msgEliminacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlEliminar();
        }
    }
    
    /*-------------------------------Buscar Cuota --------------------------------------------------------------*/

        public List<Cuota> buscar(String s) {

        List<Cuota> cuotas = new ArrayList<Cuota>();

        try {
            Cuota cuota = new Cuota();
            rst = getValores("SELECT * FROM cuota WHERE id_prestamo LIKE '"+s+"%' OR num_cuota LIKE '"+s+"%'");
            while (rst.next()) {
                cuota = new Cuota();
                cuota.setIdPrestamo(rst.getInt("id_prestamo"));
                cuota.setNumCuota(rst.getInt("num_cuota"));
                cuota.setValor(rst.getDouble("valor"));
                cuota.setInteres(rst.getDouble("interes"));
                cuota.setFecha(rst.getDate("fecha"));
                cuota.setCapital(rst.getDouble("capital"));
                cuota.setSaldoAnterior(rst.getDouble("saldo_anterior"));
                cuota.setSaldoActualizado(rst.getDouble("saldo_actualizado"));
                cuota.setMora(rst.getDouble("mora"));
                cuotas.add(cuota);
            }
            return cuotas;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }

    }

    
    
    
    
    /*--------------------------AGREGADAS----------------------------------------------------------------*/
    
    
        public void agregarCuotaCalculada(Cuota cuota) {

        int idPrestamo = 0;
        int numCuota = 0;
        
        Calendar cal=Calendar.getInstance(); 
        String fecha= cal.get(cal.YEAR) +"/"+ (cal.get(cal.MONTH)+1) +"/"+ cal.get(cal.DATE);

        idPrestamo = cuota.idPrestamo;
        numCuota = obtenerNumCuota(idPrestamo);

        try {

            UID("INSERT INTO cuota (id_prestamo, num_cuota, valor, interes, fecha, capital, saldo_anterior, saldo_actualizado, mora) VALUES ('" + idPrestamo + "', '" + numCuota + "', '" + cuota.valor + "', '" + cuota.interes + "', '" + fecha + "', '" + cuota.capital + "', '" + cuota.saldoAnterior + "', '" + cuota.saldoActualizado + "', '" + cuota.mora + "')");
        mensaje.msgPagoExito();
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlCrear();
        }
    }
    

//    public double obtenerUltimoSaldoCuota(int idPrestamo) {
//        ResultSet rs = (cn.getValores("SELECT saldo_actualizado FROM cuota WHERE id_prestamo='" + idPrestamo + "'"));
//        double saldo = 0;
//
//        try {
//            while(rs.next()){
//                saldo = rs.getDouble(1);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
//        return saldo;
//    }
    
    public double ultimoSaldoPrestamo(int idPrestamo) {
        ResultSet rs = (cn.getValores("SELECT saldo FROM prestamo WHERE id_prestamo='" + idPrestamo + "'"));
        double saldo = 0;

        try {
            while(rs.next()){
                saldo = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return saldo;
    }
    
      public int obtenerIDPrestamo(String dui){
        int idPrestamo=0;
        ResultSet rs = null;
        rs=getValores("SELECT id_prestamo FROM prestamo WHERE dui='" + dui + "'");
        try {
            while(rs.next()){
                idPrestamo = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return idPrestamo;
    }
    
    public int obtenerNumCuota(int idPrestamo){
        int numCuota=0;
        ResultSet rs = null;
        rs=getValores("SELECT MAX(num_cuota) FROM cuota WHERE id_prestamo='" + idPrestamo + "'");
        try {
            while(rs.next()){
                numCuota = rs.getInt(1);
            }
            numCuota++;
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return numCuota;
    }
    
    public List<Cuota> buscarCuota(String s) {

        List<Cuota> cuotas = new ArrayList<Cuota>();

        try {
            Cuota cuota = new Cuota();
            rst = getValores("SELECT * FROM cuota WHERE id_prestamo='"+s+"'");
            while (rst.next()) {
                cuota = new Cuota();
                cuota.setIdPrestamo(rst.getInt("id_prestamo"));
                cuota.setNumCuota(rst.getInt("num_cuota"));
                cuota.setValor(rst.getDouble("valor"));
                cuota.setInteres(rst.getDouble("interes"));
                cuota.setFecha(rst.getDate("fecha"));
                cuota.setCapital(rst.getDouble("capital"));
                cuota.setSaldoAnterior(rst.getDouble("saldo_anterior"));
                cuota.setSaldoActualizado(rst.getDouble("saldo_actualizado"));
                cuota.setMora(rst.getDouble("mora"));
                cuotas.add(cuota);
            }
            return cuotas;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
        

    }
    public List<Cuota> buscarCuotas(String s) {

        List<Cuota> cuotas = new ArrayList<Cuota>();

        try {
            Cuota cuota = new Cuota();
            rst = getValores("SELECT * FROM cuota WHERE id_prestamo IN (SELECT id_prestamo FROM prestamo WHERE dui='"+s+"')");
            while (rst.next()) {
                cuota = new Cuota();
                cuota.setIdPrestamo(rst.getInt("id_prestamo"));
                cuota.setNumCuota(rst.getInt("num_cuota"));
                cuota.setValor(rst.getDouble("valor"));
                cuota.setInteres(rst.getDouble("interes"));
                cuota.setFecha(rst.getDate("fecha"));
                cuota.setCapital(rst.getDouble("capital"));
                cuota.setSaldoAnterior(rst.getDouble("saldo_anterior"));
                cuota.setSaldoActualizado(rst.getDouble("saldo_actualizado"));
                cuota.setMora(rst.getDouble("mora"));
                cuotas.add(cuota);
            }
            return cuotas;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }}
    
    public String veficacionExisteCuota(String idCuota){
        String idPrestamo ="";
        ResultSet rs = null;
        rs=getValores("SELECT id_prestamo FROM cuota WHERE id_prestamo='" + idCuota + "'");
        try {
            while(rs.next()){
                idPrestamo = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return idPrestamo;
    }
    
    public void eliminarCuotasPrestamo(String idPrestamo) {
        try {
            if (idPrestamo == null) {
                System.err.println("idPrestamo invalido , verfique los datos");
            } else {
                UID("DELETE FROM prestamos.cuota WHERE id_prestamo='" + idPrestamo + "'");
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
        }
    }
    
}
