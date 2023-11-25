package Administrativo.GestionDocentes;

import Administrativo.Interfaces.Administrativo.Administrativos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
public class GestionDocentes {
    private JPanel LoginPanel;
    private JButton habilitarDocente;
    private JTable TablaDocentes;
    private JButton reiniciarContraseña;
    private JButton Regresar;
    private JButton verCursos;
    private final MetodosGestionDocentes metodos;

    public GestionDocentes() {

        metodos = new MetodosGestionDocentes(this);
        metodos.generarTablas();

        habilitarDocente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((metodos.consultarHabilitado())){
                    metodos.deshabilitarDocente();
                    JOptionPane.showMessageDialog(null, "Docente dado de baja exitosamente");
                }
                else if (!metodos.consultarHabilitado()){
                    metodos.deshabilitarDocente();
                    JOptionPane.showMessageDialog(null, "Docente habilitado exitosamente");
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

        verCursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metodos.listadecursos();
            }
        });

        reiniciarContraseña.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    metodos.reiniciarPassword();
                    JOptionPane.showMessageDialog(null, "Contraseña reiniciada exitosamente");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public JTable getTabla(){
        return TablaDocentes;
    }

    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Gestión de docentes");
        frame.setContentPane(LoginPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
