package Administrativo.GestionDocentes;

import Conexion.Conexion_DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class MetodosGestionDocentes {
    private final Conexion_DB r = new Conexion_DB();
    private final GestionDocentes gestor;

    public MetodosGestionDocentes(GestionDocentes gestor) {
        this.gestor = gestor;
    }
    public void deshabilitarDocente() {

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
                      docentes.id AS docente_id,
                      docentes.nombre AS docente_nombre,
                      cursos_administracion.id AS curso_id,
                      nombres_cursos.nombre AS nombre_curso
                    FROM
                      docentes
                      INNER JOIN asignaciones_docentes ON docentes.id = asignaciones_docentes.id_docente
                      INNER JOIN cursos_administracion ON asignaciones_docentes.id_curso = cursos_administracion.id
                      INNER JOIN nombres_cursos ON cursos_administracion.nombre = nombres_cursos.id
                    WHERE
                      docentes.id = ?;""";

            ResultSet rs;
            PreparedStatement ps;
            ArrayList<String> cursosList = new ArrayList<>(); // Lista para almacenar los cursos

            try {
                ps = r.cc().prepareStatement(getCursos);
                ps.setInt(1, id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String nombreCurso = rs.getString("nombre_curso");
                    cursosList.add(nombreCurso);
                }

                // Mostrar todos los cursos en un solo JOptionPane
                JOptionPane.showMessageDialog(null, cursosList);

            } catch (SQLException d) {
                d.printStackTrace();
            }
        }
    }

    public void generarTablas(){

        DefaultTableModel docentesInscriptos = new DefaultTableModel();

        r.conectar();

        String sql = "SELECT usuarios.id, docentes.nombre, docentes.dni, docentes.telefono, usuarios.habilitado FROM docentes INNER JOIN usuarios WHERE docentes.dni = usuarios.usuario;;";

        ResultSet rs;
        PreparedStatement ps;

        try {

            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData(); // No es necesario hacer casting
            int cantidadColumnas = resul.getColumnCount();

            docentesInscriptos.addColumn("ID");
            docentesInscriptos.addColumn("NOMBRE");
            docentesInscriptos.addColumn("DNI");
            docentesInscriptos.addColumn("TELEFONO");
            docentesInscriptos.addColumn("HABILITADO");

            while (rs.next()) {
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }

                docentesInscriptos.addRow(filas);
            }

            gestor.getTabla().setModel(docentesInscriptos);

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
        return (boolean) gestor.getTabla().getValueAt(row, 4);

    }
}
