package manager;

import java.io.Serializable;


public class ErrorHandlerApp extends Exception implements Serializable{
    
    String salida;

    public void nuevo(String titulo,String ubicacion, String mensaje){
        String title = titulo;
        String direc = ubicacion;
        String msg = mensaje;
        
        salida = title+"\n"+msg+"\t"+"Se originó en: "+"\t"+direc;
        
        System.out.println(salida);
    }
    
}
