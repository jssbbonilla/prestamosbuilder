package services;

import com.mysql.jdbc.Driver;
import java.io.Serializable;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion implements Serializable {

    private String url = "jdbc:mysql://localhost:3306/prestamos";
    private String login = "root"; 
    private String password = "12345";
    private Connection cnx = null;
     protected ResultSet rsts = null;
   
    
//Con protecte pueden ser utilizadas sin se hace extendes de esta clase, no sera necesario instanciar objeto (ejmplo controlador cliente)
    protected Statement sttm = null;
    protected ResultSet rst = null;
   

    public Connection conexion() throws SQLException {

        DriverManager.registerDriver(new Driver());
        cnx = DriverManager.getConnection(url, login, password);
        return cnx;

    }

    public void closeconexion() {
        try {
            cnx.close();
            sttm.close();
            rst.close();
        } catch (Exception e) {
        }
    }
 
    // FUNCIONA ################### Obtener un resulset de un SQL ###################################
    public ResultSet getValores(String sql) {
        try {
                
            sttm = conexion().createStatement();
            rst = sttm.executeQuery(sql);  //resultset
            return rst;

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            return null;
//            System.exit(1);
        } finally {
           
        }
    }
    
    // fUNCIONA ############################# UID ###################################################
    public void UID( PreparedStatement pstmt) {
        
        try {
            
            pstmt.execute();
                pstmt.close();
            
        } catch (SQLException e) {
            if (e.getErrorCode()==1451){
                System.out.println( "No se puede eliminar ya que es utilizado para los registros");
         
            }else if (e.getErrorCode()==1062){
                
            
            }else{
            //JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());}
        }
        
    }finally{
        closeconexion();
        }
    
    
  
    
}
  public void UID(String sql) {
        
        try {
            
            sttm  = conexion().createStatement();

            sttm.executeUpdate(sql); //statement
            
        } catch (SQLException e) {
            if (e.getErrorCode()==1451){
                System.out.println( "No se puede eliminar ya que es utilizado para los registros");
         
            }else if (e.getErrorCode()==1062){
                
            
            }else{
            //JOptionPane.showMessageDialog(null, "ERROR: " + e.getMessage());}
        }
        
    }finally{
        closeconexion();
        }
    
    
  }
    
}
