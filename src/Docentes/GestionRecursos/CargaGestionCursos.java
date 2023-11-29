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

        String SQL = "SELECT " +
                "c.ID_Curso, c.Nombre_Curso, c.Turno, dc.Nombre_Docente, dc.Apellido_Docente, dc.Detalle_Curso," +
                "e.Estado_Curso, c.Cupo, dc.Limite_Ausencia, c.Desde, c.Hasta" +
                "FROM Cursos c " +
                "INNER JOIN Detalles_Cursos dc ON c.FK_ID_Detalle_Curso = dc.ID_Detalle_Curso " +
                "INNER JOIN Cursos_Estados ce ON c.ID_Curso = ce.FK_ID_Curso " +
                "INNER JOIN Estados_Cursos e ON ce.FK_ID_Estado = e.ID_Estado_Curso";

        ResultSet rs = null;
        PreparedStatement ps = null;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, ID_DOCENTE);

        rs = ps.executeQuery();

        while (rs.next()) {

            Object[] fila = {
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso"),
                    rs.getString("turno"),
                    rs.getString("nombre_docente"),
                    rs.getString("apellido_docente"),
                    rs.getString("detalle_curso"),
                    rs.getString("estado_curso"),
                    rs.getInt("cupo"),
                    rs.getInt("limite_ausencia"),
                    rs.getDate("desde"),
                    rs.getDate("hasta"),
            };
            t.addRow(fila);
        }
        tabla.setModel(t);
    }
    private void setID_DOCENTE() throws SQLException {

        r.conectar();

        String SQL = "SELECT p.id_persona " +
                "FROM personas p " +
                "INNER JOIN tipos_usuarios tc ON p.id_persona = tc.fk_id_persona " +
                "INNER JOIN usuarios u ON tc.fk_id_usuario = u.id_usuario " +
                "WHERE u.id_usuario = 2";

        PreparedStatement ps;
        ResultSet rs;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, ID_USUARIO);

        rs = ps.executeQuery();

        while(rs.next()){
            ID_DOCENTE = rs.getInt("id_usuario");
        }

    }
    public void setEstadoCurso(int idCursoEstado, int nuevoEstado) throws SQLException {
        r.conectar();
        String SQL = "UPDATE Colegio_CtrZ.Cursos_Estados SET FK_ID_Estado = ? WHERE ID_Curso_Estado = ?";
        PreparedStatement ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, nuevoEstado);
        ps.setInt(2, idCursoEstado);
        ps.executeUpdate();
        ps.close();
    }

    public void cargarAlumnos(int id_curso, JTable table, DefaultTableModel model) throws SQLException {
        r.conectar();

        String SQL = "SELECT a.id, a.nombre, ec.Estado_Curso, c.Calificacion " +
                "FROM Colegio_CtrZ.Inscripciones i " +
                "INNER JOIN Colegio_CtrZ.Personas a ON i.FK_ID_Persona = a.ID_Persona " +
                "INNER JOIN Colegio_CtrZ.Estados_Inscripciones ei ON i.FK_ID_Estado_Inscripcion = ei.ID_Estado_Inscripcion " +
                "INNER JOIN Colegio_CtrZ.Calificaciones c ON i.FK_ID_Persona = c.FK_ID_Persona AND i.FK_ID_Curso = c.FK_ID_Modulo " +
                "WHERE i.FK_ID_Curso = ? AND ei.Estado_Inscripcion = 'Inscripto'";

        PreparedStatement ps;
        ResultSet rs;
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id_curso);
        rs = ps.executeQuery();

        while (rs.next()){
            Object[] filas = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("Estado_Curso"),
                    rs.getFloat("Calificacion")
            };
            model.addRow(filas);
        }
        table.setModel(model);
    }

    public void setCalificacion(JTable tabla, DefaultTableModel model, int id_curso) throws SQLException {
        int row = tabla.getSelectedRow();

        if (row == -1){
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            int id_alumno = (int) model.getValueAt(row, 0);
            float calificacion = Float.parseFloat(JOptionPane.showInputDialog("Introduzca la calificación"));

            r.conectar();

            String SQL = "UPDATE Colegio_CtrZ.Calificaciones SET Calificacion = ? " +
                    "WHERE FK_ID_Modulo = ? AND FK_ID_Persona = ?";
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

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            int id_alumno = (int) model.getValueAt(row, 0);
            r.conectar();
            String SQL = "UPDATE Colegio_CtrZ.Inscripciones SET FK_ID_Estado_Inscripcion = 4 " +
                    "WHERE FK_ID_Curso = ? AND FK_ID_Persona = ?";
            PreparedStatement ps;
            ps = r.cc().prepareStatement(SQL);
            ps.setInt(1, id_curso);
            ps.setInt(2, id_alumno);
            ps.executeUpdate();
        }
    }

    public void desaprobarCursada(int id_curso, JTable tabla, DefaultTableModel model) throws SQLException {
        int row = tabla.getSelectedRow();

        if (row == -1){
            JOptionPane.showMessageDialog(null, "No seleccionó ningún alumno");
        } else {
            int id_alumno = (int) model.getValueAt(row, 0);
            r.conectar();
            String SQL = "UPDATE Colegio_CtrZ.Inscripciones SET FK_ID_Estado_Inscripcion = 5 " +
                    "WHERE FK_ID_Curso = ? AND FK_ID_Persona = ?";
            PreparedStatement ps;
            ps = r.cc().prepareStatement(SQL);
            ps.setInt(1, id_curso);
            ps.setInt(2, id_alumno);
            ps.executeUpdate();
        }
    }

    public void expulsar(int id_alumno, int id_curso) throws SQLException {
        r.conectar();

        String SQL = "DELETE FROM Colegio_CtrZ.Inscripciones WHERE FK_ID_Curso = ? AND FK_ID_Persona = ?";
        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL);

        ps.setInt(1, id_curso);
        ps.setInt(2, id_alumno);

        ps.executeUpdate();
    }
}
