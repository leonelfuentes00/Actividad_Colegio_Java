package Docentes.InterfaceDocente;

import Conexion.Conexion_DB;
import Docentes.GestionRecursos.CargaABD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

public class CrearCurso {
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
    private JSpinner spinnerCupos;
    private JTable ModulosTabla;
    private JScrollPane TableTodosLosModulos;
    private JButton eliminarModuloButton;
    private JTable ModulosTablaAgregados;
    private JScrollPane modulosRequisitoTable;
    private JScrollPane modulosAgregadosTable;
    private JButton AgregarModuloRequerido;
    private JButton EliminarModuloRequerido;
    private JButton regresarButton;
    private DefaultListModel<String> lista;
    private String nombre;
    private String descripcion;
    private int tope;
    private CargaABD carga;

    ButtonGroup Turnos_Cursos_ButtonGroup = new ButtonGroup();
    DefaultTableModel ModulosTable = new DefaultTableModel();
    public CrearCurso()
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
        eliminarModuloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int filaselecionada = ModulosTabla.getSelectedRow();
                ModulosTable.removeRow(filaselecionada);
            }
        });

    carga = new CargaABD(this);
    lista = new DefaultListModel<>();

        agregarCursoButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            carga.crearCurso();
            lista.removeAllElements();
            DescripcionCursoTextArea.setText(null);
            Titulo_CursoText.setText(null);
            spinnerCupos.setValue(null);

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
    }
    private void previsualizar() {

        nombre = Titulo_CursoText.getText();
        descripcion = DescripcionCursoTextArea.getText();
        tope = Integer.parseInt((String) spinnerCupos.getValue());

        lista.addElement("Nombre del curso: " + nombre);
        lista.addElement("Descripción: " + descripcion);
        lista.addElement("Tope de alumnos: " + tope);

    }

    public String getNombre(){
        nombre = Titulo_CursoText.getText();
        return nombre;
    }

    public String getDescripcion(){
        descripcion = DescripcionCursoTextArea.getText();
        return descripcion;
    }
    public int getTope() {
        Number valorSpinner = (Number) spinnerCupos.getValue();

        int tope = valorSpinner.intValue();
        return tope;
    }

    public void setVisibleCurso(boolean b) {
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
        frame.setContentPane(new CrearCurso().Curso_PropuestoJP);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}
