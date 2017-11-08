package services;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

/**
 *
 * @author henri
 */
public class Ticket {
    
    public void ticket(PrinterOptions p){
        
        p.resetAll();
        p.initialize();
        p.feedBack((byte) 2);
        p.chooseFont(1);
        p.color(0);
        p.alignLeft();
        p.setText("Sistema de Prestamos ABC\nUniversidad de El Salvador\nFacultad Multidisciplinaria de Occidente\nIngenieria y Arquitectura");
        p.newLine();
        p.setText(fecha());
        p.newLine();
        p.setText("Pago de prestamo");
        p.newLine();
        p.addLineSeperator();
       
        p.newLine();
        p.alignLeft();
        p.setText("Id Prestamo: " + p.getId());
        p.newLine();
        p.setText("Cliente: "+p.getCliente());
        p.newLine();
        p.setText("Cuota n: "+p.getNumeroCuota());
        p.newLine();
        p.addLineSeperator();
        
        p.newLine();
        p.setText("Detalles");
        p.newLine();
        p.setText("\tCuota: $" + p.getValorCuota());
        p.newLine();
        p.setText("\tCapital: $" + p.getCapital());
        p.newLine();
        p.setText("\tIntereses: $" + p.getInteres());
        p.newLine();
        p.setText("\tMora: $" + p.getMora());
        p.newLine();
        p.setText("\tSaldo: $" + p.getSaldo());
        p.newLine();
        p.addLineSeperator();
   
        p.newLine();
        p.setText("Ante cualquier duda la mas tetuda :v");
        p.newLine();
        
        p.feed((byte) 3);
        
        p.finit();

        //Esta onda es la que imprime
        feedPrinter(p.finalCommandSet().getBytes());
    }
    
    public static String fecha() {
      SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      Date now = new Date();
      String strDate = sdfDate.format(now);
    return strDate;
}
    
    private static boolean feedPrinter(byte[] b) {
        try {
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName("pepe", null)); 

            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
         
   
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);

            job.print(doc, null);

        } catch (javax.print.PrintException pex) {
            System.out.println("Printer Error " + pex.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
}
