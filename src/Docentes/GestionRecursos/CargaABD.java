package Docentes.GestionRecursos;

import Conexion.Conexion_DB;
import Login.Login;
import Docentes.InterfaceDocente.CrearCurso;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CargaABD {
    private ArrayList<String> nombresCursos;
    private final Conexion_DB r = new Conexion_DB();
    private final CrearCurso crear;

    private Login log = Login.getInstance();
    private int ID;
    private int ID_DOCENTE;
    private int ID_CURSO;
    private int ID_CURSOREQUISITO;

    public CargaABD(CrearCurso crear) {

        this.crear = crear;
        cargarCursos();

    }

    private void cargarCursos() {

        r.conectar();

        String sql = "SELECT nombre FROM nombres_cursos";
        PreparedStatement ps;
        ResultSet rs;

        nombresCursos = new ArrayList<>();

        try {
            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String nombres = rs.getString("nombre");
                nombresCursos.add(nombres);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void crearCurso() {

        r.conectar();

        //Consultas necesarias a la base de datos para obtener toda la información necesaria
        String sql = "INSERT INTO nombres_cursos (nombre) VALUES (?);";
        String selectnombre = "SELECT id FROM nombres_cursos WHERE nombre = ?";
        String sql2 = "INSERT INTO cursos_administracion (nombre, descripcion, tope_alumnos) VALUES (?, ?, ?)";
        String selectIDdocente = "SELECT id FROM docentes WHERE usuario = ?";
        String selectIDCurso = "SELECT cursos_administracion.id, cursos_administracion.nombre FROM cursos_administracion  " +
                "INNER JOIN nombres_cursos " +
                "WHERE nombres_cursos.nombre = ? AND cursos_administracion.nombre = nombres_cursos.id";
        String sql3 = "INSERT INTO asignaciones_docentes (id_docente, id_curso) VALUES (?, ?)";
        String sql4 = "INSERT INTO requisitos_curso (id_curso, id_requisitocurso) VALUES (?, ?)";

        PreparedStatement ps, ps1, ps2, ps3, ps4, ps5, ps6;
        ResultSet rs, rs2, rs3;

        try {

            //Introduce el nombre en la tabla de nombres
            ps = r.cc().prepareStatement(sql);
            ps.setString(1, crear.getNombre());
            ps.executeUpdate();

            //Selecciona el ID de la tabla nombres
            ps1 = r.cc().prepareStatement(selectnombre);
            ps1.setString(1, crear.getNombre());
            rs = ps1.executeQuery();

            while (rs.next()) {
                ID = rs.getInt("id"); //Obtiene el ID correspondiente
            }

            //Crea el curso con todos los valores correspondientes
            ps2 = r.cc().prepareStatement(sql2);
            ps2.setInt(1, ID);
            ps2.setString(2, crear.getDescripcion());
            ps2.setInt(3, crear.getTope());
            ps2.executeUpdate();

            //Obtiene el ID del docente que está creando el curso
            ps3 = r.cc().prepareStatement(selectIDdocente);
            ps3.setInt(1, log.getID_USUARIO());
            rs2 = ps3.executeQuery();

            while (rs2.next()) {
                ID_DOCENTE = rs2.getInt("id"); //Obtiene el ID correspondiente
            }

            //Obtiene el ID del curso creado
            ps4 = r.cc().prepareStatement(selectIDCurso);
            ps4.setString(1, crear.getNombre());
            rs3 = ps4.executeQuery();

            while (rs3.next()) {
                ID_CURSO = rs3.getInt("id"); //Obtiene el ID correspondiente
            }

            if (crear.getComboBox().equals("Sin requisitos")) { //SE USA TABLA NO COMBOBOX
                JOptionPane.showMessageDialog(null, "Curso creado sin requisitos");
            } else {
                //Inserta en la tabla de requisitos si es que existen
                ps6 = r.cc().prepareStatement(sql4);
                ps6.setInt(1, ID_CURSO);
                ps6.setInt(2, ID_CURSOREQUISITO);
                ps6.executeUpdate();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al crear el curso");
            throw new RuntimeException(e);
        }

        //Inserta en la tabla de asignaciones docentes el curso
        try {
            ps5 = r.cc().prepareStatement(sql3);
            ps5.setInt(1, ID_DOCENTE);
            ps5.setInt(2, ID_CURSO);
            ps5.executeUpdate();
            JOptionPane.showMessageDialog(null, "Creación de curso exitosa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getNombreCursoRequisito() {

        r.conectar();

        String selectIDCurso = "SELECT cursos_administracion.id, cursos_administracion.nombre FROM cursos_administracion  " +
                "INNER JOIN nombres_cursos " +
                "WHERE nombres_cursos.nombre = ? AND cursos_administracion.nombre = nombres_cursos.id";

        PreparedStatement ps;
        ResultSet rs;

        try {
            ps = r.cc().prepareStatement(selectIDCurso);
            ps.setString(1, crear.getComboBox());
            rs = ps.executeQuery();
            while (rs.next()) {
                ID_CURSOREQUISITO = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword() throws SQLException {

        r.conectar();
        PreparedStatement ps;

        String contraseña = String.valueOf(JOptionPane.showInputDialog("Introduzca una nueva contraseña: "));

        String SQL = "UPDATE usuarios SET contraseña = ? WHERE id = ?";

        ps = r.cc().prepareStatement(SQL);
        ps.setString(1, contraseña);
        ps.setInt(2, log.getID_USUARIO());

        ps.executeUpdate();
    }

    public ArrayList<String> getNombresCursos() {
        return nombresCursos;
    }
}