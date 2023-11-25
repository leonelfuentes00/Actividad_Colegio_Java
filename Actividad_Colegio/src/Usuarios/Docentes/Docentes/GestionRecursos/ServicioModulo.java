package Docentes.GestionRecursos;

import Conexion.Conexion_DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicioModulo {

    public static boolean NombreModuloExiste(String TituloModulo) {
        Conexion_DB conexion = new Conexion_DB();
        conexion.getConexion();
        boolean existe = false;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            String sql = "SELECT COUNT(*) AS cantidad FROM Modulos WHERE Modulo = ?";
            stmt = conexion.con.prepareStatement(sql);
            stmt.setString(1, TituloModulo);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                existe = (cantidad > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return existe;
    }
}
