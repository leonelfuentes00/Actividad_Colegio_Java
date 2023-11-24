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
        String SQL = "UPDATE cursos_administracion SET estado = 7 WHERE id = ?";
        PreparedStatement ps;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();

        String SQL2 = "DELETE FROM asignaciones_alumnos WHERE id_curso = ?";
        PreparedStatement ps2;

        ps2= r.cc().prepareStatement(SQL2);
        ps2.setInt(1, id);
        ps2.executeUpdate();

        ps.close();
        ps2.close();

    }

    public void habilitarCurso(int id) throws SQLException {

        r.conectar();
        String SQL = "UPDATE cursos_administracion SET estado = 2 WHERE id = ?";
        PreparedStatement ps;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();

        ps.close();

    }

    public void cargarCursos (JTable table, DefaultTableModel model) throws SQLException {

        r.conectar();

        String SQL = "SELECT DISTINCT cursos_administracion.id, nombres_cursos.nombre, cursos_administracion.descripcion, estados.estados, docentes.nombre, cursos_administracion.tope_alumnos FROM cursos_administracion INNER JOIN nombres_cursos, asignaciones_docentes, estados, docentes WHERE cursos_administracion.nombre = nombres_cursos.id AND asignaciones_docentes.id_docente = docentes.id AND cursos_administracion.estado = estados.id AND asignaciones_docentes.id_curso = cursos_administracion.id ORDER BY id;";

        PreparedStatement ps;
        ResultSet rs;
        ps = r.cc().prepareStatement(SQL);
        rs = ps.executeQuery();

        while (rs.next()){
            Object[] filas = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("estados"),
                    rs.getString("nombre"),
                    rs.getInt("tope_alumnos")
            };
            model.addRow(filas);
        }
        table.setModel(model);
    }

    public void reiniciarCurso(int id) throws SQLException {

        r.conectar();
        String SQL2 = "UPDATE cursos_administracion SET estado = 2 WHERE id_curso = ?";

        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL2);
        ps.executeUpdate();

    }
}