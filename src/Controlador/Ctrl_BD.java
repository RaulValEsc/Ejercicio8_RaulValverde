
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ctrl_BD {
    public static Connection conexion;
    
    public Ctrl_BD(){}
    
    public boolean conectarBD(String servidor,String puerto,String usuario,String contrasenia){
        try{
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@"+servidor+":"+puerto+":xe",usuario,contrasenia);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error : "+ex.getMessage());
            return false;
        }
    }
    
    
}
