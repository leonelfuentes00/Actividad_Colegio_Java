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

    public void generarTablas() {

        DefaultTableModel docentesInscriptos = new DefaultTableModel();

        r.conectar();

        String sql = "SELECT DISTINCT " +
                "NP.FK_ID_Persona, " +
                "N.Nombre, " +
                "A.Apellido, " +
                "TD.Tipo_Documento, " +
                "DP.Numero_Documento, " +
                "SP.FK_ID_Sexo, " +
                "T.Numero_Telefono, " +
                "CE.Correo_Electronico, " +
                "P.Habilitado " +  // Agregada la columna "Habilitado"
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
                "JOIN Personas P ON P.ID_Persona = NP.FK_ID_Persona " + // Agregada la tabla Personas
                "WHERE " +
                "TU.FK_ID_Usuario = 2;";

        ResultSet rs;
        PreparedStatement ps;

        try {

            ps = r.cc().prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData resul = rs.getMetaData(); // No es necesario hacer casting
            int cantidadColumnas = resul.getColumnCount();

            docentesInscriptos.addColumn("ID");
            docentesInscriptos.addColumn("NOMBRE");
            docentesInscriptos.addColumn("APELLIDO");
            docentesInscriptos.addColumn("TIPO DOC");
            docentesInscriptos.addColumn("NUM DOC");
            docentesInscriptos.addColumn("SEXO");
            docentesInscriptos.addColumn("TELÉFONO");
            docentesInscriptos.addColumn("CORREO ELECTRÓNICO");
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

        String getID = "SELECT personas.ID_Persona, personas.Contrasena " +
                "FROM personas " +
                "INNER JOIN docentes ON docentes.FK_ID_Persona = personas.ID_Persona " +
                "INNER JOIN usuarios ON usuarios.ID_Usuario = personas.FK_ID_Usuario " +
                "WHERE usuarios.Habilitado = 1 AND usuarios.ID_Usuario = 2";

        PreparedStatement pq;
        ResultSet rs;

        pq = r.cc().prepareStatement(getID);
        rs = pq.executeQuery();

        if (rs.next()) {
            id = rs.getInt("ID_Persona");
            String contrasena = rs.getString("contrasena");


            PreparedStatement ps;
            String SQL = "UPDATE personas SET Contrasena = ? WHERE ID_Persona = ?";
            ps = r.cc().prepareStatement(SQL);
            ps.setString(1, contrasena);
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