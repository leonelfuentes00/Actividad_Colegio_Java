package Usuarios.Docentes.InterfaceDocente;

import Conexion.PasswordChange;
import Usuarios.Docentes.GestionRecursos.CargaABD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Docentes {
    private JPanel panelDocente;
    private JButton crearCursoButton;
    private JButton gestionarCursoButton;
    private JButton cambiarContraseñaButton;
    private final CargaABD carga = new CargaABD(new CrearCurso());
    private final PasswordChange change = new PasswordChange();

    public Docentes() {

        try {
            change.changePassword();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        crearCursoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CrearCurso vent = new CrearCurso();
                vent.setVisibleCurso(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(crearCursoButton);
                frame.dispose();
            }
        });
        gestionarCursoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GestionCursos vent = null;
                vent = new GestionCursos();

                vent.setVisible(true);
                JFrame frame = (JFrame) SwingUtilities.getRoot(gestionarCursoButton);
                frame.dispose();
            }
        });

        cambiarContraseñaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carga.changePassword();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Perfil docente");
        frame.setContentPane(panelDocente);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
