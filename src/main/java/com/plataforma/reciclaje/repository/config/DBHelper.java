package com.plataforma.reciclaje.repository.config;
import com.plataforma.reciclaje.repository.entity.Premio;
import com.plataforma.reciclaje.repository.entity.RegistroReciclaje;
import com.plataforma.reciclaje.repository.entity.Usuario;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper {

    public static Boolean createDatabase() {
        try {
            Class.forName(SQLConstantes.DRIVER);
             Connection conn = DriverManager.getConnection(
                    SQLConstantes.RUTA);
             // Aplica la clave de cifrado
            String sqlKey = "PRAGMA key = '" + SQLConstantes.PASSWORD + "';";
             conn.createStatement().execute(sqlKey);
            if (conn != null) {
                System.out.println("Base de datos creada o abierta correctamente.");
                DatabaseMetaData meta = conn.getMetaData();
                DBHelper.onCreate();
                conn.close();
                return Boolean.TRUE;
            } else {
                System.out.println("No creado database.");
            } 
        } catch (SQLException e) {//Error en la conexion de B.D. 
            System.out.println("ERROR DE CONEXION " + e.getMessage());
        } catch (ClassNotFoundException ex) {//Error en el driver
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR DRIVER ." + ex);
        }
        return Boolean.FALSE;
    }

    public static Connection openDatabase() {
        Connection conn = null;
        try {
            Class.forName(SQLConstantes.DRIVER);
            conn = DriverManager.getConnection(
                    SQLConstantes.RUTA);
            String sqlKey = "PRAGMA key = '" + SQLConstantes.PASSWORD + "';";
            conn.createStatement().execute(sqlKey);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                // System.out.println("Conexion a BD sqlite.");
            }
            return conn;
        } catch (SQLException e) {//Error en la conexion de B.D. 
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {//Error en el driver
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void createTable(String sqlConsulta) {
        try {
            Connection conn = DBHelper.openDatabase();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlConsulta);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void onCreate() {
        try {
            DBHelper.createTable(Usuario.CREATE_TABLE_USUARIO);
            DBHelper.createTable(Premio.CREATE_TABLE_PREMIO);
            DBHelper.createTable(RegistroReciclaje.CREATE_TABLE_PREMIO);
        } catch (Exception e) {
            // System.out.println( "Exception onCreate "+e );
        }
    }
}