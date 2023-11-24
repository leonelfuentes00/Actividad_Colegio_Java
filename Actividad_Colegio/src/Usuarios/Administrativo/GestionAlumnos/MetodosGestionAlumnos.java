package Usuarios.Administrativo.GestionAlumnos;

import Conexion.Conexion_DB;
import Login.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;



public class MetodosGestionAlumnos {
    private final Conexion_DB r = new Conexion_DB();
    private final GestionAlumnos gestor;
    public MetodosGestionAlumnos(GestionAlumnos gestor) {
        this.gestor = gestor;
    }
    public void deshabilitarAlumno() {

        int row = gestor.getTabla().getSelectedRow();
        int id = (int) gestor.getTabla().getValueAt(row, 0);

        r.conectar();

        if (consultarHabilitado()) {

            String update = "UPDATE usuarios SET habilitado = false WHERE id = ?;";

            PreparedStatement ps;

            try {

                ps = r.cc().prepareStatement(update);
                ps.setInt(1, id);
                ps.executeUpdate();

            } catch (SQLException d) {
                d.printStackTrace();
            }
            generarTablas();

        } else if (!consultarHabilitado()) {

            String update = "UPDATE usuarios SET habilitado = true WHERE id = ?;";

            PreparedStatement ps;

            try {

                ps = r.cc().prepareStatement(update);
                ps.setInt(1, id);
                ps.executeUpdate();

            } catch (SQLException d) {
                d.printStackTrace();
            }
            generarTablas();
        }
    }

    public void listadecursos() {
        int row = gestor.getTabla().getSelectedRow();
        int id = (int) gestor.getTabla().getValueAt(row, 0);

        if (row == -1) {
            JOptionPane.showMessageDialog(null, "No ha seleccionado nada");
        } else {
            r.conectar();

            String getCursos = """
                    SELECT
                      alumnos.nombre AS nombre_alumno,
                      nombres_cursos.nombre AS nombre_curso,
                      estados.estados AS estado_curso,
                         CASE
                            WHEN asignaciones_alumnos.calificacion IS NOT NULL THEN asignaciones_alumnos.calificacion
                            ELSE 'Sin calificación'
                            END AS calificacion
                    FROM
                      alumnos
                         INNER JOIN asignaciones_alumnos ON alumnos.id = asignaciones_alumnos.id_alumno
                         INNER JOIN cursos_administracion ON asignaciones_alumnos.id_curso = cursos_administracion.id
                         INNER JOIN estados ON asignaciones_alumnos.estado = estados.id
                         INNER JOIN nombres_cursos ON cursos_administracion.nombre = nombres_cursos.id
                    WHERE
                      alumnos.id = ?;""";

            ResultSet rs;
            PreparedStatement ps;
            ArrayList<String> cursosList = new ArrayList<>(); // Lista para almacenar los cursos

            try {
                ps = r.cc().prepareStatement(getCursos);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String nombreCurso = rs.getString("nombre_curso");
                    String calificacion = rs.getString("calificacion");
                    cursosList.add(nombreCurso);
                    cursosList.add(calificacion);
                }

                // Mostrar todos los cursos en un solo JOptionPane
                JOptionPane.showMessageDialog(null, cursosList);

            } catch (SQLException d) {
                d.printStackTrace();
            }
        }
    }

    public void generarTablas(){

        DefaultTableModel alumnosInscriptos = new DefaultTableModel();

        r.conectar();

        String sql = "SELECT id, nombre, dni, fecha_nac, direccion, telefono FROM alumnos;";

        ResultSet rs;
        PreparedStatement ps;

        try {

            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData(); // No es necesario hacer casting
            int cantidadColumnas = resul.getColumnCount();

            alumnosInscriptos.addColumn("ID");
            alumnosInscriptos.addColumn("NOMBRE");
            alumnosInscriptos.addColumn("DNI");
            alumnosInscriptos.addColumn("FECHA NAC");
            alumnosInscriptos.addColumn("DIRECCION");
            alumnosInscriptos.addColumn("TELEFONO");

            while (rs.next()) {
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                alumnosInscriptos.addRow(filas);
            }

            gestor.getTabla().setModel(alumnosInscriptos);

        } catch (SQLException d) {
            d.printStackTrace();
        }
    }

    public void reiniciarPassword() throws SQLException {

        int id = 0;

        r.conectar();
        String getID = "SELECT usuarios.id FROM usuarios INNER JOIN docentes WHERE docentes.usuario = usuarios.id";
        PreparedStatement pq;
        ResultSet rs;

        pq = r.cc().prepareStatement(getID);
        rs = pq.executeQuery();

        while (rs.next()){
            id = rs.getInt("id");
        }

        PreparedStatement ps;
        String SQL = "UPDATE usuarios SET contraseña = usuario WHERE id = ?";
        ps = r.cc().prepareStatement(SQL);
        ps.setInt(1, id);
        ps.executeUpdate();

    }
    public boolean consultarHabilitado(){
        int row = gestor.getTabla().getSelectedRow();
        return (boolean) gestor.getTabla().getValueAt(row, 6);
    }
}
