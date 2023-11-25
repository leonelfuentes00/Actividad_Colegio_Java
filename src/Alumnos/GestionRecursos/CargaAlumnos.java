package Alumnos.GestionRecursos;

import Conexion.Conexion_DB;
import Login.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CargaAlumnos {

    private int ID_ALUMNO;
    private final Login log = Login.getInstance();
    private final Conexion_DB r = new Conexion_DB();

    public void conectarTablas(JTable table, DefaultTableModel model){

        r.conectar();

        try{

            PreparedStatement ps;
            ResultSet rs = null;
            String sql = "SELECT DISTINCT cursos_administracion.id, nombres_cursos.nombre, cursos_administracion.descripcion, estados.estados, asignaciones_alumnos.calificacion FROM cursos_administracion INNER JOIN estados, nombres_cursos, asignaciones_alumnos" +
                    " WHERE cursos_administracion.estado <> 7 AND cursos_administracion.estado <> 1 AND asignaciones_alumnos.id_alumno = ? AND asignaciones_alumnos.estado = estados.id AND asignaciones_alumnos.id_curso = cursos_administracion.id AND nombres_cursos.id = cursos_administracion.nombre ORDER BY id ASC";

            ps = r.cc().prepareStatement(sql);
            ps.setInt(1, getID_ALUMNO());
            rs = ps.executeQuery();

            while (rs.next()){
                Object[] filas = {
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estados"),
                        rs.getString("calificacion"),
                };
                model.addRow(filas);
            }
            table.setModel(model);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void conectarTablaCursos(JTable table, DefaultTableModel model){

        r.conectar();

        try{

            PreparedStatement ps;
            ResultSet rs = null;

            String sql = "SELECT DISTINCT cursos_administracion.id, nombres_cursos.nombre, cursos_administracion.descripcion, estados.estados, cursos_administracion.tope_alumnos " +
                    "FROM cursos_administracion " +
                    "INNER JOIN nombres_cursos, asignaciones_docentes, estados " +
                    "WHERE cursos_administracion.nombre = nombres_cursos.id " +
                    "AND cursos_administracion.estado = 2 AND estados.id = cursos_administracion.estado ORDER BY id ASC";

            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()){
                Object[] filas = {

                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estados"),
                        rs.getInt("tope_alumnos"),
                };

                model.addRow(filas);
            }
            table.setModel(model);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void darsedeBaja(int id_curso) throws SQLException {

        r.conectar();
        String SQL = "DELETE FROM asignaciones_alumnos WHERE id_curso = ? AND id_alumno = ?";

        PreparedStatement ps;
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id_curso);
        ps.setInt(2, getID_ALUMNO());
        ps.executeUpdate();
    }

    public void inscripcion(int id_curso) throws  SQLException {

        r.conectar();
        String SQL1 = "INSERT INTO asignaciones_alumnos (id_alumno, id_curso) VALUES (?, ?)";
        String SQL2 = "UPDATE asignaciones_alumnos SET estado = 6 WHERE id_curso = ?";

        PreparedStatement pc;
        PreparedStatement pq;
        PreparedStatement ps;

        pq = r.cc().prepareStatement(SQL1);
        pq.setInt(1, getID_ALUMNO());
        pq.setInt(2, id_curso);
        pq.executeUpdate();
        pc = r.cc().prepareStatement(SQL2);
        pc.setInt(1, id_curso);
        pc.executeUpdate();
    }

    public boolean alertaInscripto(JTable table, DefaultTableModel model) throws SQLException {

        int row = table.getSelectedRow();
        int i = (int) model.getValueAt(row, 0);

        r.conectar();
        PreparedStatement ps;
        ResultSet rs;
        String SQL = "SELECT id_curso, estado FROM asignaciones_alumnos WHERE id_alumno = ?";
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, getID_ALUMNO());

        rs = ps.executeQuery();

        while(rs.next()){
            if (rs.getInt("id_curso") == i){
                return true;
            }
        }

        return false;
    }

    public boolean hasRequisitos(int id_curso) throws SQLException {

        r.conectar();
        PreparedStatement ps;
        ResultSet rs;
        String SQL = "SELECT * FROM requisitos_curso";
        ps = r.cc().prepareStatement(SQL);
        rs = ps.executeQuery();

        while(rs.next()){
            int curso = rs.getInt("id_curso");
            if (curso == id_curso){
                return true;
            }
        }
        return false;
    }

    public boolean requisitosCurso(int id_curso) throws SQLException {

        r.conectar();
        PreparedStatement ps;
        ResultSet rs;
        String SQL = "SELECT requisitos_curso.id_curso, asignaciones_alumnos.id_alumno, " +
                "asignaciones_alumnos.id_curso, estados.estados FROM " +
                "requisitos_curso INNER JOIN asignaciones_alumnos, estados WHERE " +
                "asignaciones_alumnos.estado = estados.id AND asignaciones_alumnos.id_alumno = ? AND requisitos_curso.id_curso = ?";

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, getID_ALUMNO());
        ps.setInt(2, id_curso);
        rs = ps.executeQuery();

        while (rs.next()){

            int reqCurso = rs.getInt("id_curso");
            String estado = rs.getString("estados");

            if (estado.equals("Aprobado") && reqCurso == id_curso){
                return false;
            }
        }

        return true;
    }

    public void changePassword() throws SQLException {

        r.conectar();
        PreparedStatement ps;
        String contraseña = String.valueOf(JOptionPane.showInputDialog("Introduzca una nueva contraseña: "));
        String SQL2 = "UPDATE usuarios SET contraseña = ? WHERE id = ?";
        ps = r.cc().prepareStatement(SQL2);
        ps.setString(1, contraseña);
        ps.setInt(2, log.getID_USUARIO());
        ps.executeUpdate();

    }

    private int getID_ALUMNO() throws SQLException {

        r.conectar();
        String SQL = "SELECT id FROM alumnos WHERE usuario = ?";

        PreparedStatement ps;
        ResultSet rs;

        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, log.getID_USUARIO());
        rs = ps.executeQuery();

        while (rs.next()){
            ID_ALUMNO = rs.getInt("id");
        }

        return ID_ALUMNO;
    }
}
