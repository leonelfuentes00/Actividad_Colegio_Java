package Administrativo.GestionAlumnos;

import Administrativo.Interfaces.Administrativo.Administrativos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GestionAlumnos {
    private JTable tablaAlumnadoInscripto;
    private final MetodosGestionAlumnos metodos;
    private JPanel LoginPanel;
    private JButton GestionarCursos;
    private JButton Habilitado;
    private JButton ReiniciarContraseña;
    private JButton Regresar;

    public GestionAlumnos() {

        metodos = new MetodosGestionAlumnos(this);
        metodos.generarTablas();

        GestionarCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metodos.listadecursos();
            }
        });
        Habilitado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((metodos.consultarHabilitado())){
                    metodos.deshabilitarAlumno();
                    JOptionPane.showMessageDialog(null, "Alumno dado de baja exitosamente");
                }
                else if (!metodos.consultarHabilitado()){
                    metodos.deshabilitarAlumno();
                    JOptionPane.showMessageDialog(null, "Alumno habilitado exitosamente");
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
        ReiniciarContraseña.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    metodos.reiniciarPassword();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public JTable getTabla(){
        return tablaAlumnadoInscripto;
    }

    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Gestión de alumnos");
        frame.setContentPane(LoginPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
