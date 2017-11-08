package managedbeansv3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import static org.omnifaces.util.Faces.getServletContext;

public class Ficheros implements Serializable {

    MensajesFormularios msg = new MensajesFormularios(); //Mensajes de validacion

    public String leerFichero(String ruta) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        String texto = "";

        try {
            archivo = new File(ruta);
            
            if(archivo.exists()){
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea = "";
            while ((linea = br.readLine()) != null) {
                texto += linea + "\n";
            }
            br.close();
            }
            else{
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            texto= leerFicheroResource("/resources/ficheros/contrato.txt");
            bw.write(texto);
            bw.close();
            
            }
            return texto;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            msg.msgErrorAlLeer();
            return texto = "";
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e2);
            }

        }
    }

    public void escribirFichero(String ruta, String texto) {
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(texto);
            pw.close();
            msg.msgCreadoExito();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            msg.msgErrorAlGuardar();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e2);
            }
        }
    }

    public String saberRuta(String ruta) {
        String absolutePath = "";
        try {
            File file = new File(ruta);
            absolutePath = file.getAbsolutePath();
            System.out.println("LA RUTAAAAAAAAAAAA:---->" + absolutePath);

        } catch (Exception e) {
        }
        return absolutePath;
    }

    public String leerFicheroResource(String ruta) {
        BufferedReader br = null;
        String texto = "";

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            InputStream entrada = externalContext.getResourceAsStream(ruta);
            br = new BufferedReader(new InputStreamReader(entrada));

            // Lectura del fichero
            String linea = "";
            while ((linea = br.readLine()) != null) {
                texto += linea + "\n";
            }
            entrada.close();
            return texto;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return texto = "";
        } finally {
        }
    }

    @Deprecated
    public String abrirTextoResource(String ruta) {
        String contenido = "";
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            InputStream input = externalContext.getResourceAsStream(ruta);
            //entrada = new FileInputStream(ruta);
            int ascci;
            while ((ascci = input.read()) != -1) {
                char carcater = (char) ascci;
                contenido += carcater;
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return contenido;
    }

    @Deprecated
    public String leerTextoRealPath(String rutaResources) {
        BufferedReader br = null;
        String texto = "";

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String realPath = externalContext.getRealPath(rutaResources);
            System.out.println("LA RUTA LECTURA ES: -->" + realPath);
            InputStream entrada = externalContext.getResourceAsStream(realPath);
            br = new BufferedReader(new InputStreamReader(entrada));

            // Lectura del fichero
            String linea = "";
            while ((linea = br.readLine()) != null) {
                texto += linea + "\n";
            }

            return texto;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            return texto = "";
        } finally {
        }
    }

    @Deprecated
    public void guardarTextoRealPath(String rutaResources, String contenido) {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String realPath = externalContext.getRealPath(rutaResources);
            System.out.println("LA RUTA ESCRITURA ES: ------>" + realPath);
            FileOutputStream salida = new FileOutputStream(realPath);
            byte[] bytesTxt = contenido.getBytes();
            salida.write(bytesTxt);
            msg.msgGuardadoExito();
            System.out.println("\nSe guardo con exito el archivo");
            salida.flush();
            salida.close();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            msg.msgErrorAlModificar();
        }
    }

}
