package Administrativo.Interfaces.Administrativo;

import Administrativo.GestionAlumnos.GestionAlumnos;
import Administrativo.GestionCursos.GestorCursos;
import Administrativo.GestionDocentes.GestionDocentes;
import Administrativo.Interfaces.Formularios_Registro.Registro_Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Administrativos {
    private JPanel Curso_PropuestoJP;
    private JButton IngresarUsuario;
    private JButton GestionarCursos;
    private JButton GestionarAlumnos;
    private JButton GestionarPersonal;

    public Administrativos() {
        GestionarPersonal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionDocentes vent = new GestionDocentes();
                vent.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(GestionarPersonal);
                frame.dispose();
            }
        });

        GestionarAlumnos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestionAlumnos vent = new GestionAlumnos();
                vent.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(GestionarAlumnos);
                frame.dispose();
            }
        });

        GestionarCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestorCursos curso = null;
                try {
                    curso = new GestorCursos();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                curso.setVisible(true);
            }
        });
        IngresarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registro_Usuario vent = new Registro_Usuario();
                vent.setVisibleRegistrar(true);
            }
        });

    }
    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Perfil administrativo");
        frame.setContentPane(Curso_PropuestoJP);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}