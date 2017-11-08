package manager;

import entities.Prestamo;
import entities.Cliente;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import managedbeansv3.MensajesFormularios;
import manager.ErrorHandlerApp;
import services.Conexion;

public class ControladorPrestamo extends Conexion implements Serializable {

    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion
    Prestamo p = new Prestamo();
    ErrorHandlerApp ep = new ErrorHandlerApp();
    Conexion cn = new Conexion();
    Cliente c = new Cliente();

    public void agregar(Prestamo p) {

        Calendar cal = Calendar.getInstance();
        String fechaInicio = cal.get(cal.YEAR) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.DATE);
        int idPrestamo = obtenerIDPrestamo();

        String fechaUltimoPago = "1111-11-11";

        try {
            cn.conexion();
            cn.UID("INSERT INTO prestamo (id_prestamo,dui,monto,valor_cuota,tasa_interes,cantidad_cuotas,fecha_inicio,fecha_fin,fecha_ultimo_pago,saldo,estado,observaciones, capitalizacion, tasa_mora) VALUES ('" + idPrestamo + "','" + p.dui + "','" + p.monto + "','" + p.valorCuota + "','" + p.tasaInteres + "','" + p.cantidadCuotas + "','" + fechaInicio + "','" + p.fechaFin + "','" + fechaUltimoPago + "','" + p.monto + "','" + p.estado + "','" + p.observaciones + "','" + p.capitalizacion +"','" + p.tasaMora +"');");
            mensaje.msgCreadoExito();
        } catch (Exception e) {
            ep.nuevo("Advertencia: ", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlCrear();
        } finally {
            cn.closeconexion();
        }
    }

    public List<Prestamo> ObtenerActivos() {
        List<Prestamo> salida = new ArrayList<Prestamo>();
        try {
            rst = getValores("SELECT * FROM prestamos.prestamo WHERE estado='A'");
            while (rst.next()) {
                p = new Prestamo();

                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                salida.add(p);

            }
            return salida;

        } catch (Exception e) {
            ep.nuevo("Advertencia", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }

    }

    public void modificar(Prestamo p) {
        try {
            cn.conexion();
            cn.UID("UPDATE prestamos.prestamo SET monto='" + p.monto + "', valor_cuota='" + p.valorCuota + "', tasa_interes='" + p.tasaInteres + "', cantidad_cuotas='" + p.cantidadCuotas + "', fecha_inicio='" + p.fechaInicio + "', fecha_fin='" + p.fechaFin + "', fecha_ultimo_pago='" + p.fechaUltimoPago + "', saldo='" + p.saldo + "', observaciones='" + p.observaciones + "', capitalizacion='"+ p.capitalizacion+"', tasa_mora='"+ p.tasaMora+"' WHERE id_prestamo='" + p.idPrestamo + "'");
            mensaje.msgModificacion();
        } catch (Exception e) {
            ep.nuevo("Advertencia", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlModificar();
        } finally {
            cn.closeconexion();
        }
    }

    public void eliminarprestamo(Prestamo prestamo) {
        try {
            if (prestamo == null) {
                System.err.println("Prestamo invalido , verfique los datos");
                mensaje.msgAdvertenciaAlEliminar();
            } else {
                UID("DELETE FROM prestamo WHERE id_prestamo ='" + prestamo.getIdPrestamo() + "'");
                mensaje.msgEliminacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlEliminar();
        }
    }
    
    public List<Prestamo> buscar(String s) {

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try {
            rst = getValores("SELECT * FROM prestamo WHERE dui LIKE '" + s + "%'");
            while (rst.next()) {
               p = new Prestamo();
                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                prestamos.add(p);
            }
            return prestamos;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }
    
        public int obtenerIDPrestamo() {
        int idPrestamo = 0;
        ResultSet rs = null;
        rs = getValores("SELECT MAX(id_prestamo) FROM prestamo");
        try {
            while (rs.next()) {
                idPrestamo = rs.getInt(1);
            }
            idPrestamo++;
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return idPrestamo;
    }
    

    /*---------------------- Agregados -------------------------------------*/
        
        
                
        public List<Prestamo> buscarPrestamo(String s) {

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try {
            p = new Prestamo();
            rst = getValores("SELECT * FROM prestamos.prestamo WHERE id_prestamo='" + s + "'");
            while (rst.next()) {
                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                prestamos.add(p);
            }
            return prestamos;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }        
    
        public List<Prestamo> buscarPrestamos(String s) {

        List<Prestamo> prestamos = new ArrayList<Prestamo>();

        try {
            p = new Prestamo();
            rst = getValores("SELECT * FROM prestamo WHERE dui LIKE '" + s + "%'");
            while (rst.next()) {
                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                prestamos.add(p);
            }
            return prestamos;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }
    }
    
    public void actualizarSaldo(Prestamo p) {
        
        Calendar cal = Calendar.getInstance();
        String fechaUltimoPago = cal.get(cal.YEAR) + "/" + (cal.get(cal.MONTH) + 1) + "/" + cal.get(cal.DATE);
        
        try {
            cn.conexion();
            cn.UID("UPDATE prestamos.prestamo SET saldo='" + p.saldo + "', fecha_ultimo_pago='"+fechaUltimoPago+"' WHERE id_prestamo='" + p.idPrestamo + "'");
        } catch (Exception e) {
            ep.nuevo("Advertencia", e.getStackTrace().toString(), e.getMessage());
        } finally {
            cn.closeconexion();
        }
    }
      public List<Prestamo> Obtener(String s) {
        List<Prestamo> salida = new ArrayList<Prestamo>();
        try {
            rst = getValores("SELECT * FROM prestamo WHERE id_prestamo LIKE '"+s+"%' OR monto LIKE '"+s+"%'OR valor_cuota LIKE '"+s+"%'");
            while (rst.next()) {
                p = new Prestamo();

                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                salida.add(p);
            }
            return salida;

        } catch (Exception e) {
            ep.nuevo("Advertencia", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }}
      
      public List<Prestamo> Obtenerid(String s) {
        List<Prestamo> salida = new ArrayList<Prestamo>();
        try {
            rst = getValores("SELECT * FROM prestamos.prestamo WHERE id_prestamo LIKE '"+s+"%'");
            while (rst.next()) {
                p = new Prestamo();

                p.setIdPrestamo(rst.getInt("id_prestamo"));
                p.setDui(rst.getString("dui"));
                p.setMonto(rst.getDouble("monto"));
                p.setValorCuota(rst.getDouble("valor_cuota"));
                p.setTasaInteres(rst.getDouble("tasa_interes"));
                p.setFechaInicio(rst.getDate("fecha_inicio"));
                p.setCantidadCuotas(rst.getInt("cantidad_cuotas"));
                p.setFechaFin(rst.getDate("fecha_fin"));
                p.setFechaUltimoPago(rst.getDate("fecha_ultimo_pago"));
                p.setSaldo(rst.getDouble("saldo"));
                p.setEstado(rst.getString("estado").charAt(0));
                p.setObservaciones(rst.getString("observaciones"));
                p.setCapitalizacion(rst.getString("capitalizacion").charAt(0));
                p.setTasaMora(rst.getDouble("tasa_mora"));
                salida.add(p);
            }
            return salida;

        } catch (Exception e) {
            ep.nuevo("Advertencia", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }}
      
      public double obtenerTasaMoraPrestamo(int idPrestamo){
        double tasaMora=0;
        ResultSet rs = null;
        rs=getValores("SELECT tasa_mora FROM prestamos.prestamo WHERE id_prestamo='" + idPrestamo + "'");
        try {
            while(rs.next()){
                tasaMora = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return tasaMora;
    }
    
}
