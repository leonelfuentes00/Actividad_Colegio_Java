package Alumnos.InterfaceAlumno;

import Conexion.PasswordChange;
import Alumnos.GestionRecursos.CargaAlumnos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Alumnos {
    private JPanel panel1;
    private JTable tablaCursosDisponibles;
    private JTable tablaMisCursos;
    private JButton INSCRIBIRSEENCURSOButton;
    private JButton DARSEDEBAJADEButton;
    private JButton AJUSTESDELPERFILButton;
    private JButton SALIRButton;
    private PasswordChange cambio = new PasswordChange();

    private final CargaAlumnos carga = new CargaAlumnos();

    public Alumnos() {

        try {
            cambio.changePassword();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DefaultTableModel miscursos = new DefaultTableModel();
        DefaultTableModel cursosdisponibles = new DefaultTableModel();

        miscursos.addColumn("ID");
        miscursos.addColumn("NOMBRE");
        miscursos.addColumn("DESCRIPCION");
        miscursos.addColumn("ESTADO");
        miscursos.addColumn("CALIFICACIÓN");

        cursosdisponibles.addColumn("ID");
        cursosdisponibles.addColumn("NOMBRE");
        cursosdisponibles.addColumn("DESCRIPCIÓN");
        cursosdisponibles.addColumn("ESTADO");
        cursosdisponibles.addColumn("TOPE DE CÁTEDRA");

        carga.conectarTablaCursos(tablaCursosDisponibles, cursosdisponibles);
        carga.conectarTablas(tablaMisCursos, miscursos);

        INSCRIBIRSEENCURSOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Este grupo busca si hay requisitos, verifica si el usuario los cumple y lo inscribe si
                // es así. En su defecto (si el curso no tiene requisitos) también inscribe al alumno.

                try {
                    if (carga.hasRequisitos(getID_CURSODisponible())){
                        if(!carga.requisitosCurso(getID_CURSODisponible())){
                            carga.inscripcion(getID_CURSODisponible());
                            JOptionPane.showMessageDialog(null, "Inscripción exitosa");
                            limpiartablas(cursosdisponibles, miscursos);
                        } else if (carga.requisitosCurso(getID_CURSODisponible())){
                            JOptionPane.showMessageDialog(null, "No cumple requisitos de inscripción");
                        }
                    } else if (!carga.alertaInscripto(tablaCursosDisponibles, cursosdisponibles)){
                        carga.inscripcion(getID_CURSODisponible());
                        JOptionPane.showMessageDialog(null, "Inscripción exitosa");
                        limpiartablas(cursosdisponibles, miscursos);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ya inscripto en el curso");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        DARSEDEBAJADEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    carga.darsedeBaja(getID_CURSOalumno());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                cursosdisponibles.setRowCount(0);
                miscursos.setRowCount(0);

                JOptionPane.showMessageDialog(null, "Dado de baja con éxito");
                carga.conectarTablaCursos(tablaCursosDisponibles, cursosdisponibles);
                carga.conectarTablas(tablaMisCursos, miscursos);
            }
        });

        AJUSTESDELPERFILButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga.changePassword();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        SALIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public int getID_CURSODisponible(){

        int row = tablaCursosDisponibles.getSelectedRow();
        if (row == - 1) {
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningún curso");
        }
        return (int) tablaCursosDisponibles.getValueAt(row, 0);
    }

    public int getID_CURSOalumno(){
        int row = tablaMisCursos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "No ha seleccionado ningún curso");
        }
        return (int) tablaMisCursos.getValueAt(row, 0);
    }

    private void limpiartablas(DefaultTableModel model1, DefaultTableModel model2){

        model1.setRowCount(0);
        model2.setRowCount(0);
        carga.conectarTablaCursos(tablaCursosDisponibles, model1);
        carga.conectarTablas(tablaMisCursos, model2);

    }
    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("ALUMNO: ");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
