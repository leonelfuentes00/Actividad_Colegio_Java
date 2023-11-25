package Docentes.InterfaceDocente;

import Conexion.Conexion_DB;
import Docentes.GestionRecursos.ServicioModulo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Modulo_Propuesto {
    private JPanel Curso_PropuestoJP;
    private JTextArea DescripcionModuloTextArea;
    private JTextField Titulo_ModuloText;
    private JButton ProponerButton;
    private JTextField TiempoEstimadoModulo;

    public Modulo_Propuesto() {

        ProponerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloModulo = Titulo_ModuloText.getText();
                String descripcionModulo = DescripcionModuloTextArea.getText();
                String tiempoModulo = TiempoEstimadoModulo.getText();

                if (ServicioModulo.NombreModuloExiste(tituloModulo)) {
                    JOptionPane.showMessageDialog(null, "Este nombre de Modulo ya esta en uso. Por favor, elija otro.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO Modulos (Modulo, Detalle_Modulo, Tiempo_Estimado) VALUES (?, ?, ?)";
                Conexion_DB conexion = new Conexion_DB();
                conexion.getConexion();

                try {
                    PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
                    ps.setString(1, tituloModulo);
                    ps.setString(2, descripcionModulo);
                    ps.setString(3, tiempoModulo);

                    int filasAfectadas = ps.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(null, "Módulo propuesto insertado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo insertar el módulo propuesto", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    ps.close(); // Cerrar PreparedStatement
                    conexion.desconectar();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al intentar insertar el módulo propuesto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

