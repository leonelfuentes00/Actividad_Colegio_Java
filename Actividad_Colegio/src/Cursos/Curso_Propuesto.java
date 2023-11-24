package Cursos;

import Conexion.Conexion_DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Curso_Propuesto {
    private JPanel Curso_PropuestoJP;
    private JTextField NombreText;
    private JTextField Titulo_CursoText;
    private JTextField Fecha_InicioCursoText;
    private JTextField Fecha_CierreText;
    private JTextField Horario_ComienzoText;
    private JTextField Horario_FinalizadoText;
    private JRadioButton MananaRadioButton;
    private JRadioButton TardeRadioButton;
    private JRadioButton NocheRadioButton;
    private JPanel Radio_ButtonsJP;
    private JPanel JP;
    private JTextField ApellidoText;
    private JTextArea DescripcionCursoTextArea;
    private JPanel Turnos_FechasJP;
    private JPanel Nombre_Apellido_DocenteJP;
    private JTextField ModulosText;
    private JButton Agregar_Modulo_Button;
    private JPanel jpjp;
    private JButton ProponerButton;
    private JButton agregarCursoButton;
    private JSpinner SpinnerLimiteAusencias;
    private JSpinner spinner1;
    private JTable ModulosTabla;
    private JScrollPane TableScrollPane;
    private JButton eliminarButton;

    ButtonGroup Turnos_Cursos_ButtonGroup = new ButtonGroup();
    DefaultTableModel ModulosTable = new DefaultTableModel();
    public Curso_Propuesto ()
    {
        Conexion_DB cone = new Conexion_DB();
        Connection con = cone.conectar();

        ModulosTable.addColumn("Nombre del Modulo");
        ModulosTabla.setModel(ModulosTable);

        Turnos_Cursos_ButtonGroup.add(MananaRadioButton);
        Turnos_Cursos_ButtonGroup.add(TardeRadioButton);
        Turnos_Cursos_ButtonGroup.add(NocheRadioButton);

        ProponerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DefaultTableModel df  = (DefaultTableModel) ModulosTabla.getModel();

                for (int i = 0; i < ModulosTabla.getRowCount(); i++)
                {
                    String mesi = ModulosTabla.getValueAt(i,0).toString();
                    System.out.println(mesi);
                }
            }
        });
        Agregar_Modulo_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement ps = null;

                /*try
                {
                    ps = con.prepareStatement("Call Ingresar_Modulo (?)");
                    ps.setString(1,ModulosText.getText());
                    ps.executeUpdate();*/

                    Object[] Modulos = new Object[1];
                    Modulos[0] = ModulosText.getText();
                    ModulosTable.addRow(Modulos);
                    ModulosTabla.setModel(ModulosTable);
                    System.out.println(Arrays.toString(Modulos));

                /*}
                catch (SQLException throwables)
                {
                    throwables.printStackTrace();
                }*/
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int filaselecionada = ModulosTabla.getSelectedRow();
                ModulosTable.removeRow(filaselecionada);
            }
        });
    }

    public void setVisibleCurso() {
        JFrame frame = new JFrame("Proponer Curso");
        frame.setContentPane(Curso_PropuestoJP);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(800,600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Proponer Curso");
        frame.setContentPane(new Curso_Propuesto().Curso_PropuestoJP);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
