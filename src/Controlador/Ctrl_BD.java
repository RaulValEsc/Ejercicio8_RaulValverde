package Controlador;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ctrl_BD {

    public Connection conexion;

    ResultSet rs;

    public Ctrl_BD() {
    }

    public boolean conectarBD(String servidor, String puerto, String usuario, String contrasenia) {
        try {
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@" + servidor + ":" + puerto + ":xe", usuario, contrasenia);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error : " + ex.getMessage());
            return false;
        }
    }

    public ArrayList<String> devolverTablas(DatabaseMetaData metadata) {
        ArrayList<String> listaTablas = new ArrayList();
        try {
            rs = metadata.getTables(null, metadata.getUserName(), "%", new String[]{"TABLE"});
            while (rs.next()) {
                listaTablas.add(rs.getString(3));
            }
        } catch (SQLException ex) {

        }
        return listaTablas;
    }

    public ArrayList<String> devolverColumnas(DatabaseMetaData metadata, String tabla) {
        ArrayList<String> listaColumnas = new ArrayList();
        try {
            rs = metadata.getColumns(null, metadata.getUserName(), tabla, "%");
            while (rs.next()) {
                listaColumnas.add(rs.getString(4));
            }
        } catch (SQLException ex) {

        }
        return listaColumnas;
    }

    /*
     * 1-> VARCHAR2
     * 2-> NUMBER
     * 3-> BOOLEAN
     * 4-> DATE
     */
    public int conocerTipoColumna(DatabaseMetaData metadata, String tabla, String columna) {
        String tipo = null;
        try {
            rs = metadata.getColumns(null, metadata.getUserName(), tabla, columna);
            rs.next();
            tipo = rs.getString(6);
        } catch (SQLException ex) {
            Logger.getLogger(Ctrl_BD.class.getName()).log(Level.SEVERE, null, ex);
        }
        switch (tipo) {
            case "VARCHAR2":
                return 1;
            case "NUMBER":
                return 2;
            case "DATE":
                return 3;
            case "BOOLEAN":
                return 4;
            default:
                return -1;
        }
    }

    public String redactarSelect(String tabla, String columna, String operador, String valor, boolean LIKE) {
        String select = "";
        if (LIKE) {
            select = ("SELECT * FROM " + tabla + " WHERE " + columna + " " + operador + " \'%" + valor + "%\'");
        } else {
            select = ("SELECT * FROM " + tabla + " WHERE " + columna + " " + operador + " \'" + valor + "\'");
        }
        return select;
    }

    public ArrayList<String> nombresColumnas(String select) {
        ArrayList<String> listaColumnas = new ArrayList();
        int numColumnas = 0;
        Statement s = null;
        try {
            s = conexion.createStatement();
            ResultSet rs = s.executeQuery(select);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                listaColumnas.add(rsmd.getColumnName(i));
            }
            s.close();

        } catch (SQLException ex) {
            Logger.getLogger(Ctrl_BD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaColumnas;
    }
    
    public ArrayList<String[]> devolverRegistros(String select){
        ArrayList<String[]> listaRegistros = new ArrayList();
        String[] registro;
        ArrayList<String> auxiliar = new ArrayList();
        int numColumnas = 0;
        Statement s = null;
        try {
            s = conexion.createStatement();
            ResultSet rs = s.executeQuery(select);
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()){
                auxiliar.clear();
                registro = new String[rsmd.getColumnCount()];
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    auxiliar.add(rs.getString(rsmd.getColumnName(i)));
                }
                auxiliar.toArray(registro);
                listaRegistros.add(registro);
            }
            s.close();

        } catch (SQLException ex) {
            Logger.getLogger(Ctrl_BD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRegistros;
    }
}
