package Docentes.GestionRecursos;

import Conexion.Conexion_DB;
import Login.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CargaGestionCursos {
    private final Login log = Login.getInstance();
    private final Conexion_DB r = new Conexion_DB();
    private int ID_DOCENTE;
    private final int ID_USUARIO = log.getID_USUARIO();
    public CargaGestionCursos() throws SQLException {

        try {
            setID_DOCENTE();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void ArmarTablas(JTable tabla, DefaultTableModel t) throws SQLException {

        tabla.setModel(t);
        t.setRowCount(0);

        r.conectar();

        String SQL = "SELECT DISTINCT cursos_administracion.id, nombres_cursos.nombre, cursos_administracion.descripcion, estados.estados, cursos_administracion.tope_alumnos " +
                "FROM cursos_administracion INNER JOIN nombres_cursos, asignaciones_docentes, estados " +
                "WHERE cursos_administracion.nombre = nombres_cursos.id AND asignaciones_docentes.id_docente = ? AND cursos_administracion.estado = estados.id;";

        ResultSet rs = null;
        PreparedStatement ps = null;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, ID_DOCENTE);

        rs = ps.executeQuery();

        while (rs.next()) {

            Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("estados"),
                    rs.getInt("tope_alumnos")
            };
            t.addRow(fila);
        }
        tabla.setModel(t);
    }
    private void setID_DOCENTE() throws SQLException {

        r.conectar();

        String SQL = "SELECT id FROM docentes WHERE usuario = ?";

        PreparedStatement ps;
        ResultSet rs;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, ID_USUARIO);

        rs = ps.executeQuery();

        while(rs.next()){
            ID_DOCENTE = rs.getInt("id");
        }

    }
    public void setEstadoCurso(int id, int estado) throws SQLException {

        r.conectar();
        String SQL = "UPDATE cursos_administracion SET estado = ? WHERE id = ?";
        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, estado);
        ps.setInt(2, id);
        ps.executeUpdate();

        ps.close();

    }
    public void cargarAlumnos(int id_curso, JTable table, DefaultTableModel model) throws  SQLException {

        r.conectar();

        String SQL = "SELECT DISTINCT alumnos.id, alumnos.nombre, estados.estados, asignaciones_alumnos.calificacion FROM alumnos INNER JOIN estados, asignaciones_alumnos WHERE asignaciones_alumnos.id_curso = ? AND"
                + " asignaciones_alumnos.estado = 6 and estados.id = asignaciones_alumnos.estado AND asignaciones_alumnos.id_alumno = alumnos.id";
        PreparedStatement ps;
        ResultSet rs;
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id_curso);
        rs = ps.executeQuery();

        while (rs.next()){
            Object[] filas = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("estados"),
                    rs.getFloat("calificacion")
            };
            model.addRow(filas);
        }
        table.setModel(model);
    }
    public void setCalificacion(JTable tabla, DefaultTableModel model, int id_curso) throws SQLException {

        int row = tabla.getSelectedRow();

        int id_alumno = (int) model.getValueAt(row, 0);
        if (id_alumno == -1){
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            float calificacion = Float.parseFloat(JOptionPane.showInputDialog("Introduzca la calificacion"));
            r.conectar();

            String SQL = "UPDATE asignaciones_alumnos SET calificacion = ? WHERE id_curso = ? AND id_alumno = ?";
            PreparedStatement ps;
            ps = r.cc().prepareStatement(SQL);
            ps.setFloat(1, calificacion);
            ps.setInt(2, id_curso);
            ps.setInt(3, id_alumno);
            ps.executeUpdate();
        }
    }
    public void aprobarCursada(int id_curso, JTable tabla, DefaultTableModel model) throws SQLException {

        int row = tabla.getSelectedRow();

        int id_alumno = (int) model.getValueAt(row, 0);
        if (id_alumno == -1) {
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            r.conectar();
            String SQL = "UPDATE asignaciones_alumnos SET estado = 4 WHERE id_curso = ? AND id_alumno = ?";
            PreparedStatement ps;
            ps = r.cc().prepareStatement(SQL);
            ps.setInt(1, id_curso);
            ps.setInt(2, id_alumno);
            ps.executeUpdate();
        }
    }
    public void desaprobarCursada(int id_curso, JTable tabla, DefaultTableModel model) throws SQLException {

        int row = tabla.getSelectedRow();

        int id_alumno = (int) model.getValueAt(row, 0);
        if (id_alumno == -1){
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            r.conectar();
            String SQL = "UPDATE asignaciones_alumnos SET estado = 5 WHERE id_curso = ? AND id_alumno = ?";
            PreparedStatement ps;
            ps = r.cc().prepareStatement(SQL);
            ps.setInt(1, id_curso);
            ps.setInt(2, id_alumno);
            ps.executeUpdate();
        }
    }
    public void expulsar(int id_alumno, int id_curso) throws SQLException {

        r.conectar();

        String SQL = "DELETE FROM asignaciones_alumnos WHERE id_curso = ? AND id_alumno = ?";
        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL);

        ps.setInt(1, id_curso);
        ps.setInt(2, id_alumno);

        ps.executeUpdate();
    }
}
