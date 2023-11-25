package Docentes.InterfaceDocente;

import Docentes.GestionRecursos.CargaGestionCursos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GestionCursos {
    private final CargaGestionCursos carga;
    {
        try {
            carga = new CargaGestionCursos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private JTable tablaMisCursos;
    private JTable tablaAlumnos;
    private JButton APROBARCURSADAButton;
    private JButton DESAPROBARCURSADAButton;
    private JButton EXPULSARDELCURSOButton;
    private JButton ALTERARCALIFICACIONButton;
    private JButton CERRARINSCRIPCIONESButton;
    private JButton FINALIZARCURSOButton;
    private JPanel panel1;
    private JButton regresarButton;
    private JButton CARGARALUMNOSButton;
    private final DefaultTableModel tablaAlumnosmod = new DefaultTableModel();
    private final DefaultTableModel tablaCursos = new DefaultTableModel();
    public GestionCursos() {

        tablaAlumnosmod.addColumn("ID");
        tablaAlumnosmod.addColumn("NOMBRE");
        tablaAlumnosmod.addColumn("ESTADO");
        tablaAlumnosmod.addColumn("CALIFICACION");

        tablaCursos.addColumn("ID");
        tablaCursos.addColumn("NOMBRE");
        tablaCursos.addColumn("DESCRIPCION");
        tablaCursos.addColumn("ESTADO");
        tablaCursos.addColumn("TOPE ALUMNOS");

        try {
            carga.ArmarTablas(tablaMisCursos, tablaCursos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        CERRARINSCRIPCIONESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    carga.setEstadoCurso(getIDCurso(), 3);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                tablaCursos.setRowCount(0);
                JOptionPane.showMessageDialog(null, "Inscripciones cerradas");

                try {
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        FINALIZARCURSOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    carga.setEstadoCurso(getIDCurso(), 8);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                tablaCursos.setRowCount(0);
                JOptionPane.showMessageDialog(null, "Curso finalizado exitosamente");

                try {
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        ALTERARCALIFICACIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga.setCalificacion(tablaAlumnos, tablaAlumnosmod, getIDCurso());
                    tablaAlumnosmod.setRowCount(0);
                    tablaCursos.setRowCount(0);
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                    JOptionPane.showMessageDialog(null, "Calificación modificada. Cargue nuevamente los alumnos para ver los cambios.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        APROBARCURSADAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga.aprobarCursada(getIDCurso(), tablaAlumnos, tablaAlumnosmod);
                    tablaAlumnosmod.setRowCount(0);
                    tablaCursos.setRowCount(0);
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(null, "Cursada aprobada");
            }
        });

        DESAPROBARCURSADAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga.desaprobarCursada(getIDCurso(), tablaAlumnos, tablaAlumnosmod);
                    tablaAlumnosmod.setRowCount(0);
                    tablaCursos.setRowCount(0);
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                    JOptionPane.showMessageDialog(null, "Cursada desaprobada");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        EXPULSARDELCURSOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = tablaAlumnos.getSelectedRow();
                int alumno = (int) tablaAlumnosmod.getValueAt(row, 0);

                try {
                    carga.expulsar(alumno, getIDCurso());
                    JOptionPane.showMessageDialog(null, "Alumno expulsado del curso");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                tablaAlumnosmod.setRowCount(0);
                tablaCursos.setRowCount(0);

                try {
                    carga.ArmarTablas(tablaMisCursos, tablaCursos);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Docentes vent = new Docentes();
                vent.setVisible(true);

                JFrame frame = (JFrame) SwingUtilities.getRoot(regresarButton);
                frame.dispose();
            }
        });

        CARGARALUMNOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tablaAlumnosmod.setRowCount(0);
                showAlumnos();
            }
        });

    }
    private void showAlumnos(){
        if (getIDCurso() > 0){
            try {
                carga.cargarAlumnos(getIDCurso(), tablaAlumnos, tablaAlumnosmod);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    private int getIDCurso(){
        int row = tablaMisCursos.getSelectedRow();
        if (row == -1){
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningún curso");
        } else {
            return (int) tablaMisCursos.getValueAt(row, 0);
        }
        return 0;
    }

    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("GESTIÓN DE CURSOS");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}