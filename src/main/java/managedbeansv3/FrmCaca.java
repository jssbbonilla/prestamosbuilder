package managedbeansv3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import services.Conexion;

@Named(value = "frmCaca")
@RequestScoped
public class FrmCaca implements Serializable {

    private Conexion con = new Conexion();
    
    private String duiParam;
    private String idprestamoParam;
    private int numcuotaParam;

    public void verPDF(Map<String, Object> parametros, String filePath ) throws Exception {
        JasperPrint jp = JasperFillManager.fillReport(filePath, parametros, con.conexion());
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream outStream = response.getOutputStream()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jp, byteArrayOutputStream);
            response.setContentType("application/pdf");
            outStream.write(byteArrayOutputStream.toByteArray());
            response.setHeader("Cache-Control", "max-age=0");
            //response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
            response.setContentLength(byteArrayOutputStream.toByteArray().length);
            outStream.flush();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void rptEstado(String dui){
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("dui", dui);
            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reporteCliente.jasper"));
            String fileName = jasper.getPath();
            verPDF(parametros, fileName);
        } catch (Exception ex) {
            Logger.getLogger(FrmCaca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void rptComprobante(int num_couta, String id_prestamo){
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("num_cuota", num_couta);
            parametros.put("id_prestamo", id_prestamo);
            File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/comprobantePago.jasper"));
            String fileName = jasper.getPath();
            verPDF(parametros, fileName);
        } catch (Exception ex) {
            Logger.getLogger(FrmCaca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public String getDuiParam() {
        return duiParam;
    }

    public void setDuiParam(String duiParam) {
        this.duiParam = duiParam;
    }

    public String getIdprestamoParam() {
        return idprestamoParam;
    }

    public void setIdprestamoParam(String idprestamoParam) {
        this.idprestamoParam = idprestamoParam;
    }

    public int getNumcuotaParam() {
        return numcuotaParam;
    }

    public void setNumcuotaParam(int numcuotaParam) {
        this.numcuotaParam = numcuotaParam;
    }


     
     
}



