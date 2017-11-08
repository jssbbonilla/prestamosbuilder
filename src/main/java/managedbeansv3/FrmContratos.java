package managedbeansv3;

import entities.Cliente;
import entities.Contratos;
import entities.Prestamo;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import manager.ControladorCliente;
import manager.ControladorPrestamo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import org.jsoup.Jsoup;

@Named(value = "frmContratos")
@ViewScoped
public class FrmContratos implements Serializable {

    public FrmContratos() {
        scontrato.setCiudad("Santa Ana");
        //mostrarDatos();
        mostrarContratoPersonalizado();
    }
    private String duiParam;
    private String prestamoParam;
    private String parametro1 = "forma de pasar paramtros";
    private Contratos scontrato = new Contratos();
    private Prestamo sprestamo = new Prestamo();
    private Cliente scliente = new Cliente();
    ControladorPrestamo cprestamo = new ControladorPrestamo();
    ControladorCliente ccliente = new ControladorCliente();
    private List<Contratos> datos = new ArrayList<Contratos>();
    private String textoContrato;
    private String textoReporte;

    public List<Contratos> getDatos() {

        System.out.println("Esteeeeeee ese el parametro: " + duiParam);

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        String primero = null;
        String segundo = null;
        String tercero = null;
        String cuarto = null;
        String cinco = null;
        String datosPersonales = null;
        String conveniente = null;

        String nombres = scontrato.getNombres();
        String apellidos = scontrato.getApellidos();
        String dui = scontrato.getDui();
        String direccion = scontrato.getDireccion();
        String ciudad = scontrato.getCiudad();
        double monto = scontrato.getMonto();
        double interes = scontrato.getInteres();
        int cantidadCuotas = scontrato.getCantidadCuotas();
        Date fechaInicio = scontrato.getFechaInicio();
        Date fechaFin = scontrato.getFechaFin();
        String fechaActual = String.valueOf(cal.getTime());

        datosPersonales = "Conste por el presente contrato Privado de Préstamo de Dinero que celebramos "
                + "de una parte la Señor/a. " + nombres + ", identificada con DUI. Nro. " + dui + ","
                + "domiciliada en " + direccion + " de esta ciudad de " + ciudad + " a quien en adelante "
                + "sele denominara a quien en adelante se le denominará el PRESTATARIO, y de la otra parte "
                + "el PRESTAMISTA.";
        conveniente = "Ambas partes llegan a los acuerdos siguientes:";

        primero = "LA PRESTAMISTA cede en calidad de PRESTAMO al PRESTATARIO la suma $" + monto + " a la "
                + "Señor/a " + nombres + " " + apellidos + " La de fecha del " + fechaInicio + ".";
        segundo = "El prestatario acepta dicho dinero en calidad deprestamo y asegura haber recibido "
                + "el total del dinero a la firma del presente documento, por lo que se compromete "
                + "a devolver dicha suma de dinero, asimismo ambas partes acuerdan quedicho prestamo "
                + "generará el interes de " + interes + " por ciento.";
        tercero = "El capital prestado ha de devolverse en un plazo máxima de " + cantidadCuotas + " meses. No obstante"
                + "el prestatario podrá amortizar de forma anticipada el capital pendiente en "
                + "cualquier momento. Los intereses se abonrán al efectuar el pago del capital en"
                + "cada uno de los plazos.";
        cuarto = "En caso de incumplimiento de parte del PRESTATARIO A LA PRESTAMISTA queda facultada a"
                + " recurrir a las autoridades pertinentes y hacer valer sus derechos,"
                + " por loque el presente documento es suficiente medio probatorio y vale como RECIBO.";
        cinco = "Ambas partes señalan y aseguran que en lacelebración del mismo no ha mediado error, "
                + "dolo de nulidado anulabilidad que pudiera invalidar el contenido del mismo, por lo "
                + "que proceden a firmar en la ciudad de " + ciudad + ", fecha " + fechaActual + "";

        Contratos info = new Contratos();

        info.setDatosPersonales(datosPersonales);
        info.setPrimero(primero);
        info.setSegundo(segundo);
        info.setTercero(tercero);
        info.setCuarto(cuarto);
        info.setCinco(cinco);
        info.setConveniente(conveniente);
        info.setFechaActual(fechaActual);
        datos.add(info);

        return datos;
    }

    public void setDatos(List<Contratos> datos) {
        this.datos = datos;
    }

    public void mostrarContratoPersonalizado() {
        String rutaFichero = "contrato.txt";
        String texto;

        Ficheros fichero = new Ficheros();
        texto = fichero.leerFichero(rutaFichero);
        System.out.println(texto);

        textoContrato = texto;
    }

    public void guardarContratoPersonalizado() {
        String rutaFichero = "contrato.txt";

        Ficheros fichero = new Ficheros();
        fichero.escribirFichero(rutaFichero, textoContrato);
        //textoReporte =html2text(textoContrato);
    }
    
    public static String html2text(String html) {
    return Jsoup.parse(html).text();
}

    public void verPDF() throws Exception {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtUsuario", parametro1);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratos.jasper"));
        byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(bytes, 0, bytes.length);

        outStream.flush();
        outStream.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarPDF() throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtUsuario", parametro1);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratos.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=contrato-sis_prestamos.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarExcel() throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtUsuario", parametro1);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratos.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=contrato-sis_prestamos.xlsx");
        ServletOutputStream stream = response.getOutputStream();

        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
        exporter.exportReport();

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarPPT() throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtUsuario", parametro1);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratos.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=contrato-sis_prestamos.ppt");
        ServletOutputStream stream = response.getOutputStream();

        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
        exporter.exportReport();

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarDOC() throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtUsuario", parametro1);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratos.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=contrato-sis_prestamos.doc");
        ServletOutputStream stream = response.getOutputStream();

        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
        exporter.exportReport();

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void verPDFEditor() throws Exception {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("contenidoEditor", textoContrato);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratoEditor.jasper"));
        byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(bytes.length);
        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(bytes, 0, bytes.length);

        outStream.flush();
        outStream.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    public void exportarPDFEditor() throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("contenidoEditor", textoContrato);

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/rptContratoEditor.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getDatos()));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=contrato-sis_prestamos.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    /*--------- Metodos para la vista --------------*/
    public void mostrarDatos() {

        scliente = ccliente.buscar(duiParam).get(0);
        sprestamo = cprestamo.Obtenerid(prestamoParam).get(0);

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        String nombres = scliente.nombre;
        String apellidos = scliente.apellido;
        String dui = scliente.dui;
        String direccion = scliente.direcion;
        double monto = sprestamo.monto;
        double interes = sprestamo.tasaInteres;
        int cantidadCuotas = sprestamo.cantidadCuotas;
        Date fechaInicio = sprestamo.fechaInicio;
        Date fechaFin = sprestamo.fechaFin;
        String fechaActual = String.valueOf(cal.getTime());

        scontrato.setNombres(nombres);
        scontrato.setApellidos(apellidos);
        scontrato.setDui(dui);
        scontrato.setDireccion(direccion);
        scontrato.setMonto(monto);
        scontrato.setInteres(interes);
        scontrato.setCantidadCuotas(cantidadCuotas);
        scontrato.setFechaInicio(fechaInicio);
        scontrato.setFechaFin(fechaFin);
        scontrato.setFechaActual(fechaActual);

    }

    /*------ Setter and Getter -------------*/
    public String getDuiParam() {
        return duiParam;
    }

    public void setDuiParam(String duiParam) {
        this.duiParam = duiParam;
    }

    public String getPrestamoParam() {
        return prestamoParam;
    }

    public void setPrestamoParam(String prestamoParam) {
        this.prestamoParam = prestamoParam;
    }

    public Prestamo getSprestamo() {
        if ((duiParam.isEmpty() || duiParam == null || prestamoParam.isEmpty() || prestamoParam == null) != true) {
            this.sprestamo = cprestamo.Obtenerid(prestamoParam).get(0);
        }
        return sprestamo;
    }

    public void setSprestamo(Prestamo sprestamo) {
        this.sprestamo = sprestamo;
    }

    public Cliente getScliente() {
        if ((duiParam.isEmpty() || duiParam == null || prestamoParam.isEmpty() || prestamoParam == null) != true) {
            this.scliente = ccliente.buscar(duiParam).get(0);
        }
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

    public Contratos getScontrato() {
        try {
            if ((duiParam.isEmpty() || duiParam == null || prestamoParam.isEmpty() || prestamoParam == null) != true) {
                mostrarDatos();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return scontrato;
    }

    public void setScontrato(Contratos scontrato) {
        this.scontrato = scontrato;
    }

    public String getTextoContrato() {
        return textoContrato;
    }

    public void setTextoContrato(String textoContrato) {
        this.textoContrato = textoContrato;
    }

    public String getTextoReporte() {
        return textoReporte;
    }

    public void setTextoReporte(String textoReporte) {
        this.textoReporte = textoReporte;
    }
    
    

}
