package Administrativo.Interfaces.Formularios_Registro;

import Conexion.Conexion_DB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class Registro_Usuario {
    private JPanel FormulariosJP;
    private JPanel Formulario_IdentidadJP;
    private JPanel Titulo_Paso1JP;
    private JPanel Ingresar_Datos_IdentidadJP;
    private JComboBox Tipo_DocumentoCombobox;
    private JTextField NombreText;
    private JTextField ApellidoText;
    private JTextField Fecha_NacimientoText;
    private JTextField NacionalidadText;
    private JTextField Numero_DocumentoText;
    private JComboBox SexoCombobox;
    private JButton SiguienteButton1;
    private JPanel Formulario_UbicacionJP;
    private JPanel Titulo_Paso2JP;
    private JPanel Ingresar_Datos_UbicacionJP;
    private JTextField PartidoText;
    private JTextField PaisText;
    private JTextField ProvinciaText;
    private JTextField CiudadText;
    private JRadioButton Casa_RadioButton;
    private JRadioButton Departamento_RadioButton;
    private JTextField DepartamentoText;
    private JTextField AlturaText;
    private JTextField CalleText;
    private JTextField PisoText;
    private JButton SiguienteButton2;
    private JPanel Formulario_AfiliadosJP;
    private JTextField Correo_ElectronicoText;
    private JTextField TelefonoText;
    private JPanel Titulo_Paso3JP;
    private JPanel Ingresar_Datos_AfiliacionJP;
    private JRadioButton Afiliado_Si_RadioButton;
    private JRadioButton Afiliado_No_RadioButton;
    private JTextField Numero_AfiliadoText;
    private JTextField Nombre_Empresa_AfiliadoText;
    private JTextField Cuil_Empresa_AfiliadoText;
    private JButton VolverButton;
    private JButton VolverButton2;
    private JButton FinalizarButton;
    private JComboBox Nombre_Institucion_comboBox;
    private JComboBox Ciudad_Sede_comboBox;
    private JComboBox Calle_Sede_ComboBox;
    private JTextField Altura_Calle_SedeText;
    private JTextField Fecha_Ingreso_Text;
    ButtonGroup Grupo_Botones_Radio_Afiliado = new ButtonGroup();
    ButtonGroup Grupo_Botones_Radio_Tipo_Domicilio = new ButtonGroup();
    private int idInst;
    private int idCiud;

    private Connection con;


    //---------------------------------------------------------------------------------------------------------------------------------------------------
    //                       TODOS LOS BOTONES (EXCEPTO "FINALIZAR")
    //---------------------------------------------------------------------------------------------------------------------------------------------------

    public Registro_Usuario () {

        LocalDate Fecha = LocalDate.now();
        Fecha_Ingreso_Text.setText(String.valueOf(Fecha));

        Conexion_DB cone = new Conexion_DB();
        con = cone.conectar();

        // ---------------------------------------------------------------------------------------------------------------------------------------------------
        //   DATOS COMBOBOX
        // ---------------------------------------------------------------------------------------------------------------------------------------------------

        // TIPO DE DOCUMENTO. ---

        try
        {
            PreparedStatement ps = con.prepareStatement("Select Tipo_Documento from tipos_Documentos");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String Tipos_Doc = rs.getString("Tipo_Documento");
                Tipo_DocumentoCombobox.addItem(Tipos_Doc);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        // ---------------------------------------------------------------------------------------------------------------------------------------------------

        // SEXOS. ----
        try
        {
            PreparedStatement ps = con.prepareStatement("Select Sexo from Sexos");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String Sexos = rs.getString("Sexo");
                SexoCombobox.addItem(Sexos);
            }

        }catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        // ---------------------------------------------------------------------------------------------------------------------------------------------------

        // NOMBRE INSTITUCION. -----
        try
        {
            PreparedStatement ps = con.prepareStatement("Select Nombre_Institucion from Instituciones");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                String Instituciones = rs.getString("Nombre_Institucion");
                Nombre_Institucion_comboBox.addItem(Instituciones);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        // ---------------------------------------------------------------------------------------------------------------------------------------------------

        Nombre_Institucion_comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                String institucion = (String) Nombre_Institucion_comboBox.getSelectedItem();
                System.out.println(institucion);


                try {
                    PreparedStatement ps = con.prepareStatement("Select ID_Institucion FROM Instituciones WHERE nombre_institucion = ?");
                    ps.setString(1, institucion);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        idInst = rs.getInt("ID_Institucion");

                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            // ---------------------------------------------------------------------------------------------------------------------------------------
                Ciudad_Sede_comboBox.removeAllItems();
                Ciudad_Sede_comboBox.addItem("<Seleccionar>");

                try {
                    PreparedStatement ps2 = con.prepareStatement(
                            "Select Distinct Ciudad from ciudades" +
                                    " inner join domicilios_Instituciones on domicilios_Instituciones.FK_ID_Ciudad = Ciudades.ID_Ciudad" +
                                    " Where FK_ID_Institucion = ?");
                    ps2.setInt(1,idInst);
                    ResultSet rs = ps2.executeQuery();

                    while (rs.next()){
                        String ciudades = rs.getString("Ciudad");
                        Ciudad_Sede_comboBox.addItem(ciudades);
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Ciudad_Sede_comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciudad = (String) Ciudad_Sede_comboBox.getSelectedItem();

                try {
                    PreparedStatement ps = con.prepareStatement("Select ID_Ciudad FROM Ciudades WHERE Ciudad = ?");
                    ps.setString(1, ciudad);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        idCiud = rs.getInt("ID_Ciudad");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            //-------------------------------------------------------------------------------------------------------------------------------
                Calle_Sede_ComboBox.removeAllItems();
                Calle_Sede_ComboBox.addItem("<Seleccionar>");

                try {
                    PreparedStatement ps2 = con.prepareStatement(
                            "Select distinct Calle from Calles " +
                                    "inner join Domicilios_Instituciones on Domicilios_Instituciones.FK_ID_Calle = Calles.ID_Calle " +
                                    "Where FK_ID_Ciudad = ?");
                    ps2.setInt(1, idCiud);
                    ResultSet rs2 = ps2.executeQuery();

                    while(rs2.next()){
                        String Calles = rs2.getString("Calle");
                        Calle_Sede_ComboBox.addItem(Calles);
                    }
                }catch (SQLException throwables){
                    throwables.printStackTrace();
                }
            }
        });

        Calle_Sede_ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                    PreparedStatement ps = null;
                    ResultSet rs = null;

                    Conexion_DB cone = new Conexion_DB();
                    con = cone.conectar();
                    ps = con.prepareStatement("call Select_Altura " + /*Nombre de La Calle*/ "(?)");
                    ps.setString(1, (String) Calle_Sede_ComboBox.getSelectedItem());
                    rs = ps.executeQuery();

                    if (rs.next())
                    {
                        Altura_Calle_SedeText.setText(rs.getString("Altura"));
                    }
                    }
                    catch (Exception a)
                    {
                    System.out.println(a);
                    }

            }
        });

        Altura_Calle_SedeText.setEnabled(false);

        Grupo_Botones_Radio_Afiliado.add(Afiliado_Si_RadioButton);
        Grupo_Botones_Radio_Afiliado.add(Afiliado_No_RadioButton);

        Grupo_Botones_Radio_Tipo_Domicilio.add(Casa_RadioButton);
        Grupo_Botones_Radio_Tipo_Domicilio.add(Departamento_RadioButton);


        FormulariosJP.setVisible(true);
        Formulario_IdentidadJP.setVisible(true);
        Formulario_UbicacionJP.setVisible(false);
        Formulario_AfiliadosJP.setVisible(false);

        SiguienteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Formulario_IdentidadJP.setVisible(false);
                Formulario_UbicacionJP.setVisible(true);
                Formulario_AfiliadosJP.setVisible(false);
            }
        });
        SiguienteButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Formulario_IdentidadJP.setVisible(false);
                Formulario_UbicacionJP.setVisible(false);
                Formulario_AfiliadosJP.setVisible(true);
            }
        });
        VolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Formulario_IdentidadJP.setVisible(true);
                Formulario_UbicacionJP.setVisible(false);
                Formulario_AfiliadosJP.setVisible(false);

            }
        });
        VolverButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Formulario_IdentidadJP.setVisible(false);
                Formulario_UbicacionJP.setVisible(true);
                Formulario_AfiliadosJP.setVisible(false);
            }
        });
        // ---------------------------------------------------------------------------------------------------------------------------------------------------
        //  INSERT DATOS PARA REGISTRAR PERSONA AL SISTEMA
        // ---------------------------------------------------------------------------------------------------------------------------------------------------


        FinalizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement ps = null;
                //-----------------------------------------------------------

                String Domicilio = null;
                if (Casa_RadioButton.isSelected())
                {
                     Domicilio = "Casa";
                }
                else if (Departamento_RadioButton.isSelected())
                {
                     Domicilio = "Departamento";
                }
                //-----------------------------------------------------------

                String Afiliado = null;
                if (Afiliado_Si_RadioButton.isSelected())
                {
                    Afiliado = "Si";
                }
                else if (Afiliado_No_RadioButton.isSelected())
                {
                    Afiliado = "No";
                }
                //------------------------------------------------------------

                try
                {
                    Conexion_DB cone = new Conexion_DB();
                    con = cone.getConexion();

                    ps = con.prepareStatement
                            ("Call Ingresar_Datos_Completos_Persona" +
                                    // Contraseña 1
                                    "(?," +
                                    // Nombre 2, Apellido 3
                                    "?, ?," +
                                    // Tipo Documento 4, Numero Documento 5, Fecha Nacimiento 6
                                    "?, ?, ?," +
                                    // Numero de Telefono 7, Correo Electronico 8
                                    "?, ?," +
                                    // Sexo 9
                                    "?," +
                                    // Nacionalidad 10
                                    "?," +
                                    // Pais 11, Provincia 12, Partido 13, Ciudad 14
                                    "?, ?, ?, ?," +
                                    // Tipo Domicilio 15, Calle 16, Altura 17, Piso 18, Departamento 19
                                    "?, ?, ?, ?, ?," +
                                    // Afiliado (si, no) 20, Numero de Afiliado 21, Nombre Empresa 22, Cuil Empresa 23
                                    "?, ?, ?, ?," +
                                    // Fecha de Ingreso Al Sistema 24
                                    "?);");

                    ps.setString(1, Numero_DocumentoText.getText());
                    ps.setString(2, NombreText.getText());
                    ps.setString(3, ApellidoText.getText());
                    ps.setString(4, (String) Tipo_DocumentoCombobox.getSelectedItem());
                    ps.setString(5, Numero_DocumentoText.getText());
                    ps.setDate(6, Date.valueOf(Fecha_NacimientoText.getText()));
                    ps.setString(7, TelefonoText.getText());
                    ps.setString(8, Correo_ElectronicoText.getText());
                    ps.setString(9, (String) SexoCombobox.getSelectedItem());
                    ps.setString(10, NacionalidadText.getText());
                    ps.setString(11, PaisText.getText());
                    ps.setString(12, ProvinciaText.getText());
                    ps.setString(13, PartidoText.getText());
                    ps.setString(14, CiudadText.getText());
                    ps.setString(15, Domicilio);
                    ps.setString(16, CalleText.getText());
                    ps.setString(17, Altura_Calle_SedeText.getText());
                    ps.setString(18, PisoText.getText());
                    ps.setString(19, DepartamentoText.getText());
                    ps.setString(20, Afiliado);
                    ps.setString(21, Numero_AfiliadoText.getText());
                    ps.setString(22, Nombre_Empresa_AfiliadoText.getText());
                    ps.setString(23, Cuil_Empresa_AfiliadoText.getText());
                    ps.setDate(24, Date.valueOf(Fecha_Ingreso_Text.getText()));

                    ps.executeUpdate();

                }
                catch (Exception a)
                {
                    System.out.println(a);
                }
            }
        });

        // -----------------------------------------------------------------------------------------------------
        // BOTONES DE RADIO TIPO DE DOMICILIO
        // -----------------------------------------------------------------------------------------------------
        Casa_RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                PisoText.setText("-");
                PisoText.setEnabled(false);
                DepartamentoText.setText("-");
                DepartamentoText.setEnabled(false);
            }
        });

        Departamento_RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                PisoText.setText("");
                PisoText.setEnabled(true);
                DepartamentoText.setText("");
                DepartamentoText.setEnabled(true);
            }
        });

        // -----------------------------------------------------------------------------------------------------
        // BOTONES DE RADIO AFILIACION
        // -----------------------------------------------------------------------------------------------------
        Afiliado_No_RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                Numero_AfiliadoText.setText("-");
                Numero_AfiliadoText.setEnabled(false);
                Nombre_Empresa_AfiliadoText.setText("-");
                Nombre_Empresa_AfiliadoText.setEnabled(false);
                Cuil_Empresa_AfiliadoText.setText("-");
                Cuil_Empresa_AfiliadoText.setEnabled(false);
            }
        });
        Afiliado_Si_RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Numero_AfiliadoText.setText("");
                Numero_AfiliadoText.setEnabled(true);
                Nombre_Empresa_AfiliadoText.setText("");
                Nombre_Empresa_AfiliadoText.setEnabled(true);
                Cuil_Empresa_AfiliadoText.setText("");
                Cuil_Empresa_AfiliadoText.setEnabled(true);
            }
        });

    }

    public static void setVisibleRegistrar(boolean b)
    {
        JFrame frame = new JFrame("Registrar");
        frame.setContentPane(new Registro_Usuario().FormulariosJP);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(800,400);
    }


}
