package Usuarios.Administrativo.GestionCursos;

import Usuarios.Administrativo.Interfaces.Administrativo.Administrativos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GestorCursos {

    private JPanel LoginPanel;
    private JButton HabilitarCurso;
    private JTable TablaCursos;
    private JButton CancelarCurso;
    private JButton ReiniciarCurso;
    private JButton AgregarCurso;
    private JButton Regresar;
    private final CargaABD carga = new CargaABD();

    public GestorCursos() throws SQLException {

        DefaultTableModel cursos = new DefaultTableModel();

        cursos.addColumn("ID");
        cursos.addColumn("NOMBRE");
        cursos.addColumn("DESCRIPCION");
        cursos.addColumn("ESTADOS");
        cursos.addColumn("RESPONSABLE");
        cursos.addColumn("TOPE ALUMNOS");

        carga.cargarCursos(TablaCursos, cursos);

        CancelarCurso.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = TablaCursos.getSelectedRow();
                int id = (int) TablaCursos.getValueAt(row,0);

                try {
                    carga.cancelarCurso(id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                cursos.setRowCount(0);
                JOptionPane.showMessageDialog(null, "Curso cancelado exitosamente");

                try {
                    carga.cargarCursos(TablaCursos, cursos); //Reinicia el trámite
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        HabilitarCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = TablaCursos.getSelectedRow();
                int id = (int) TablaCursos.getValueAt(row,0);

                try {
                    carga.habilitarCurso(id);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, "Curso habilitado exitosamente");
                cursos.setRowCount(0);
                try {
                    carga.cargarCursos(TablaCursos, cursos); //Reinicia el trámite
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        Regresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Administrativos vent = new Administrativos();
                vent.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(Regresar);
                frame.dispose();
            }
        });

        ReiniciarCurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row =  TablaCursos.getSelectedRow();
                int id = (int) TablaCursos.getValueAt(row,0);

                try {
                    carga.reiniciarCurso(id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                JOptionPane.showMessageDialog(null, "Curso reiniciado");
                cursos.setRowCount(0);

                try {
                    carga.cargarCursos(TablaCursos, cursos); //Reinicia el trámite
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
    }
    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Perfil administrativo");
        frame.setContentPane(LoginPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
