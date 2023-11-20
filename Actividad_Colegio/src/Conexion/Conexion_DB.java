package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion_DB
{
    public static final String url = "jdbc:mysql://localhost:3306/";
    public static final String DB = "colegio_ctrz?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    public static final String username = "root";
    public static final String password = "root";
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    public static Connection con;

    public Connection getConexion ()
    {
        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url+DB,username,password);
            System.out.println("Conexion Exitosa");
        }
        catch (SQLException a)
        {
            System.out.println(a);
        } catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return con;
    }

    public Connection conectar() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url+DB, username, password);
            if (!con.isClosed()) {
                System.out.println("Conexión exitosa");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return con;
    }
    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
