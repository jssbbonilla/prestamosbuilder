package manager;

import entities.Cliente;
import entities.Documento;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import managedbeansv3.MensajesFormularios;
import org.apache.commons.io.FileUtils;
import services.Conexion;

public class ControladorCliente extends Conexion implements Serializable {

    ErrorHandlerApp ep = new ErrorHandlerApp();
    Conexion cn = new Conexion();
    MensajesFormularios mensaje = new MensajesFormularios(); //Mensajes de validacion

    public List<Cliente> obtener() {

        List<Cliente> clientes = new ArrayList<Cliente>();

        try {
            Cliente cliente = new Cliente();
            rst = getValores("SELECT * FROM cliente");
            while (rst.next()) {
                cliente = new Cliente();
                cliente.setApellido(rst.getString("apellidos"));
                cliente.setDirecion(rst.getString("direccion"));
                cliente.setDui(rst.getString("dui"));
                cliente.setNit(rst.getString("nit"));
                cliente.setNombre(rst.getString("nombres"));
                cliente.setFechaNacimiento(rst.getDate("fecha_nacimiento"));
                cliente.setObservacion(rst.getString("observaciones"));
                cliente.setSexo(rst.getString("sexo").charAt(0));
                cliente.setTelefono(rst.getString("telefonos"));
                cliente.setProfesion(rst.getString("profesion"));
                clientes.add(cliente);
            }
            return clientes;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }

    }

    // ###### FUNCIONA- AGREGAR CLIENTE ########################## 
    public void agregar(Cliente cli) {

        try {

            UID("INSERT INTO cliente (dui, nit, nombres, apellidos, sexo, direccion, telefonos, observaciones, fecha_nacimiento, profesion) VALUES ('" + cli.dui + "', '" + cli.nit + "', '" + cli.nombre + "', '" + cli.apellido + "', '" + cli.sexo + "', '" + cli.direcion + "', '" + cli.telefono + "', '" + cli.observacion + "', '" + cli.fechaNacimiento + "', '" + cli.profesion + "')");
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext();
            String realPath = ctx.getRealPath("/");

            File destFile = new File(realPath + "resources/images/", "somefile.png");

            if (destFile.exists()) {
                InputStream fis = new FileInputStream(destFile);
                PreparedStatement pstmt = conexion().prepareStatement("INSERT INTO documento (dui,nombre_archivo,archivo,descripcion) VALUES (?,?,?,?)");
                pstmt.setString(1, cli.getDui());
                pstmt.setString(2, "IMAGEN");
                pstmt.setBinaryStream(3, fis, (int) destFile.length());
                pstmt.setString(4, "ES UNA IMAGEN");
                String sql = pstmt.toString();
                FacesMessage z = new FacesMessage("Succesful", sql);
                FacesContext.getCurrentInstance().addMessage(null, z);
                UID(pstmt);
                pstmt.close();

            }
            
            mensaje.msgCreadoExito();
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            
            mensaje.msgErrorAlCrear();
        }

    }

    // ###############################    ELIMINAR    ############################################
    public boolean eliminar(Cliente cli) {
        try {
            if (cli == null || cli.dui == null || cli.dui.isEmpty()) {
                mensaje.msgAdvertenciaAlEliminar();
                System.err.println("Cliente invalido , verfique los datos");
                return false;
            } else {
                UID("DELETE FROM documento WHERE dui ='" + cli.getDui() + "'");
                UID("DELETE FROM cliente WHERE dui ='" + cli.getDui() + "'");
                mensaje.msgEliminacion();
                return true;
            }
        } catch (Exception e) {
            mensaje.msgErrorAlEliminar();
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return false;
        }
    }

    public void editar(Cliente cli) {
        try {
            if (cli == null) {
                System.err.println("Cliente invalido , verfique los datos");

            } else {
                UID("UPDATE prestamos.cliente SET nombres='" + cli.nombre + "', apellidos='" + cli.apellido + "', sexo='" + cli.sexo + "', direccion='" + cli.direcion + "', telefonos='" + cli.telefono + "', fecha_nacimiento='" + cli.fechaNacimiento + "', observaciones='" + cli.observacion + "', profesion='" + cli.profesion + "' WHERE dui='" + cli.dui + "'");
                mensaje.msgModificacion();
            }
        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            mensaje.msgErrorAlModificar();
        }
    }

    // ################################ BUSQUEDA   ################################################  REHACER en base a getValores
    // DEBE Buscar y devuelve los clientes que coincidan con el parámetro enviado. El parámetro puede ser dui, nit, nombres o apellidos.
    public List<Cliente> buscar(String s) {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        String realPath = ctx.getRealPath("/");
        List<Cliente> clientes = new ArrayList<Cliente>();
        List<Documento> documentos = new ArrayList<Documento>();
        InputStream blob;
        File docCli = new File(realPath + "resources/images/", "imagenClienteDetalle.png");

        try {
            Cliente cliente = new Cliente();
            rst = getValores("SELECT * FROM cliente WHERE nombres LIKE '" + s + "%' OR apellidos LIKE '" + s + "%'OR dui LIKE '" + s + "%'OR nit LIKE '" + s + "%'");
            while (rst.next()) {
                cliente = new Cliente();
                cliente.setApellido(rst.getString("apellidos"));
                cliente.setDirecion(rst.getString("direccion"));
                cliente.setDui(rst.getString("dui"));
                cliente.setNit(rst.getString("nit"));
                cliente.setNombre(rst.getString("nombres"));
                cliente.setFechaNacimiento(rst.getDate("fecha_nacimiento"));
                cliente.setObservacion(rst.getString("observaciones"));
                cliente.setSexo(rst.getString("sexo").charAt(0));
                cliente.setTelefono(rst.getString("telefonos"));
                cliente.setProfesion(rst.getString("profesion"));

                Documento documento = new Documento();

                rsts = getValores("SELECT * FROM prestamos.documento WHERE dui='" + cliente.dui + "'");

                while (rsts.next()) {
                    documento = new Documento();
                    documento.setNombre(rsts.getString(3));
                    blob = rsts.getBinaryStream(4);
                    FileUtils.copyInputStreamToFile((InputStream) blob, docCli);
                    documento.setArchivo(docCli);
                    documentos.add(documento);
                    documento.getArchivo().length();
                    System.out.println(documento.getArchivo().length() + "pepe");


                }
                cliente.setDocumentos(documentos);
                
                clientes.add(cliente);

            }

            return clientes;

        } catch (Exception e) {
            ep.nuevo("Error", e.getStackTrace().toString(), e.getMessage());
            return null;
        } finally {
            closeconexion();
        }

    }

}
