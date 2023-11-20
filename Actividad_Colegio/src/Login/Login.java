package Login;

import Conexion.*;
import Cursos.Curso_Propuesto;
import Formularios_Registro.Registro_Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {
    private JPanel LoginPanel;
    private JTextField UsuarioText;
    private JButton LoginButton;
    private JPasswordField ContraseñaText;
    private JButton registroButton;

    public Login() {

        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idPersona = obtenerIdPersona();

                String sqlUser = "SELECT dp.Numero_Documento, p.contraseña " +
                        "FROM Documentos_Personas dp " +
                        "INNER JOIN Personas p ON dp.ID_Persona = p.ID_Persona " +
                        "WHERE dp.ID_Persona = ?";

                Conexion_DB con = new Conexion_DB();
                con.conectar();

                try {
                    PreparedStatement ps = Conexion_DB.con.prepareStatement(sqlUser);
                    ps.setInt(1, idPersona);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String numeroDocumento = rs.getString("Numero_Documento");
                        String contraseña = rs.getString("contraseña");

                        String contraseñaIngresada = ContraseñaText.getText();

                        String sqlTipoUser = "SELECT FK_ID_Usuario FROM Tipos_Usuarios WHERE FK_ID_Usuario = ?";
                        if (contraseñaIngresada.equals(contraseña)) {
                            try {
                                PreparedStatement ps2 = Conexion_DB.con.prepareStatement(sqlTipoUser);
                                ps2.setInt(1, idPersona);

                                ResultSet rs2 = ps2.executeQuery();
                                if (rs2.next()) {
                                    int tipoUsuario = rs2.getInt("FK_ID_Usuario");
                                    switch (tipoUsuario) {
                                        case 1: // Administrador
                                            //Agregar vista para el admin
                                            break;
                                        case 2: // Profesor
                                            Curso_Propuesto curso = new Curso_Propuesto();
                                            curso.setVisibleCurso();
                                            break;
                                        case 3: // Alumno
                                            //Agregar vista p alumno
                                            break;
                                        default:
                                            System.out.println("Tipo de usuario no identificado: " + tipoUsuario);
                                            break;
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Tipo de usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                rs2.close();
                                ps2.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        private int obtenerIdPersona() {
                String numeroDocumento = UsuarioText.getText();

                String sqlBuscarIdPersona = "SELECT FK_ID_Persona FROM Documentos_Personas WHERE Numero_Documento = ?";

                Conexion_DB con = new Conexion_DB();
                con.conectar();
                try {
                    PreparedStatement ps = Conexion_DB.con.prepareStatement(sqlBuscarIdPersona);
                    ps.setString(1, numeroDocumento);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        int idPersona = rs.getInt("FK_ID_Persona");
                        return idPersona;
                    } else {
                        JOptionPane.showMessageDialog(null, "Número de documento no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    con.desconectar();
                }
                return -1;
            }
        });

        registroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registro_Usuario reg = new Registro_Usuario();
                reg.setVisibleRegistrar();
            }
        });
    }

    public void setVisibleLogin() {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(LoginPanel); // Utiliza el panel directamente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Login login = new Login();
                login.setVisibleLogin();
            }
        });
    }
}
