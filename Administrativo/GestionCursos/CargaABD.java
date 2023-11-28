package Usuarios.Administrativo.GestionCursos;

import Conexion.Conexion_DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CargaABD {

    private final Conexion_DB r = new Conexion_DB();

    public void cancelarCurso(int id) throws SQLException {
        r.conectar();
        String SQL = "UPDATE Cursos_Estados SET FK_ID_Estado = 4 WHERE FK_ID_Curso = ?";
        PreparedStatement ps;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();

        String SQL2 = "DELETE FROM Asignaciones_Alumnos WHERE FK_ID_Curso = ?";
        PreparedStatement ps2;

        ps2 = r.cc().prepareStatement(SQL2);
        ps2.setInt(1, id);
        ps2.executeUpdate();

        ps.close();
        ps2.close();
    }

    public void habilitarCurso(int id) throws SQLException {
        r.conectar();
        String SQL = "UPDATE Cursos_Estados SET FK_ID_Estado = 2 WHERE FK_ID_Curso = ?";
        PreparedStatement ps;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();

        ps.close();
    }

    public void cargarCursos(JTable table, DefaultTableModel model) throws SQLException {
        r.conectar();

        String SQL = "SELECT DISTINCT c.ID_Curso, c.Nombre_Curso, ec.Estado_Curso, c.Cupo " +
                "FROM Cursos c " +
                "INNER JOIN Cursos_Estados ce " +
                "INNER JOIN Detalles_Cursos dc ON c.FK_ID_Detalle_Curso = dc.ID_Detalle_Curso " +
                "INNER JOIN Estados_Cursos ec ON c.ID_Curso = ce.FK_ID_Curso " +
                "ORDER BY c.ID_Curso";

        PreparedStatement ps;
        ResultSet rs;
        ps = r.cc().prepareStatement(SQL);
        rs = ps.executeQuery();

        while (rs.next()) {
            Object[] filas = {
                    rs.getInt("ID_Curso"),
                    rs.getString("Nombre_Curso"),
                    rs.getString("Estado_Curso"),
                    rs.getInt("Cupo")
            };
            model.addRow(filas);
        }
        table.setModel(model);
    }


    public void reiniciarCurso(int id) throws SQLException {
        r.conectar();
        String SQL = "UPDATE Cursos_Estados SET FK_ID_Estado = 2 WHERE FK_ID_Curso = ?";

        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
