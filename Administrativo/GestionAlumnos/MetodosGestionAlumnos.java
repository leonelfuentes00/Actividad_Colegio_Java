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

        String sql = "SELECT DISTINCT " +
                "NP.FK_ID_Persona, " +
                "N.Nombre, " +
                "A.Apellido, " +
                "TD.Tipo_Documento, " +
                "DP.Numero_Documento, " +
                "SP.FK_ID_Sexo, " +
                "T.Numero_Telefono, " +
                "CE.Correo_Electronico " +
                "P.Habilitado " +
                "FROM " +
                "Nombres_Personas NP " +
                "JOIN Nombres N ON NP.FK_ID_Nombre = N.ID_Nombre " +
                "JOIN Apellidos_Personas AP ON NP.FK_ID_Persona = AP.FK_ID_Persona " +
                "JOIN Apellidos A ON AP.FK_ID_Apellido = A.ID_Apellido " +
                "JOIN Documentos_Personas DP ON NP.FK_ID_Persona = DP.FK_ID_Persona " +
                "JOIN Tipos_Documentos TD ON DP.FK_ID_Tipo_Documento = TD.ID_Tipo_Documento " +
                "JOIN Sexos_Personas SP ON NP.FK_ID_Persona = SP.FK_ID_Persona " +
                "JOIN Telefonos_Personas TP ON NP.FK_ID_Persona = TP.FK_ID_Persona " +
                "JOIN Telefonos T ON TP.FK_ID_Telefono = T.ID_Telefono " +
                "JOIN Correos_Electronicos_Personas CEP ON NP.FK_ID_Persona = CEP.FK_ID_Persona " +
                "JOIN Correos_Electronicos CE ON CEP.FK_ID_Correo_Electronico = CE.ID_Correo_Electronico " +
                "JOIN Tipos_Usuarios TU ON TU.FK_ID_Persona = NP.FK_ID_Persona " +
                "JOIN Personas P ON P.ID_Persona = NP.FK_ID_Persona " +
                "WHERE " +
                "TU.FK_ID_Usuario = 3;";

        ResultSet rs;
        PreparedStatement ps;

        try {

            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData(); // No es necesario hacer casting
            int cantidadColumnas = resul.getColumnCount();

            alumnosInscriptos.addColumn("ID");
            alumnosInscriptos.addColumn("NOMBRE");
            alumnosInscriptos.addColumn("APELLIDO");
            alumnosInscriptos.addColumn("TIPO DOC");
            alumnosInscriptos.addColumn("NUM DOC");
            alumnosInscriptos.addColumn("SEXO");
            alumnosInscriptos.addColumn("TELÉFONO");
            alumnosInscriptos.addColumn("CORREO ELECTRÓNICO");
            alumnosInscriptos.addColumn("HABILITADO");

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

    public void reiniciarPasswordAlumnos() throws SQLException {

        int id = 0;

        r.conectar();

        // Obtener el ID del usuario que es alumno
        String getID = "SELECT personas.ID_Persona, personas.Contrasena FROM personas INNER JOIN alumnos ON alumnos.FK_ID_Persona = personas.ID_Persona INNER JOIN usuarios ON usuarios.FK_ID_Persona = personas.ID_Persona WHERE usuarios.Habilitado = 1";
        PreparedStatement pq;
        ResultSet rs;

        pq = r.cc().prepareStatement(getID);
        rs = pq.executeQuery();

        if (rs.next()) {
            id = rs.getInt("ID_Persona");
            String contrasena = rs.getString("Contrasena");


            // Actualizar la contraseña al valor del ID del usuario
            PreparedStatement ps;
            String SQL = "UPDATE usuarios SET Contrasena = ? WHERE FK_ID_Persona = ?";
            ps = r.cc().prepareStatement(SQL);
            ps.setString(1, contrasena);  // Usando el ID como contraseña
            ps.setInt(2, id);
            ps.executeUpdate();
        }

        rs.close();
        pq.close();
    }
    public boolean consultarHabilitado(){
        int row = gestor.getTabla().getSelectedRow();
        return (boolean) gestor.getTabla().getValueAt(row, 8);
    }
}
