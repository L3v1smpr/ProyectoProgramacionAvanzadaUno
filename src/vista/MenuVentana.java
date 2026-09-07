package vista;
import controlador.SistemaClub;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class MenuVentana {

    private SistemaClub controlador;
    private JFrame ventana;
    private JTabbedPane pestanas;

    //Paneles contenedores para cada modulo
    private JPanel panelSocios;
    private JPanel panelActividades;
    private JPanel panelReservas;
    private JPanel panelFacturacion;
    
    //Componentes del formulario de Socios
    private JTextField txtRutSocio;
    private JTextField txtNombreSocio;
    private JTextField txtEdadSocio;
    private JButton btnAgregarSocio;
    private JButton btnLimpiarCamposSocio;

    public MenuVentana(SistemaClub controlador) {
        this.controlador = controlador;
    }

    public void iniciarVentana() {
        ventana = new JFrame("Sistema de Gestión: Club Deportivo");
        ventana.setSize(950, 650);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        pestanas = new JTabbedPane();

        //Inicializacion de paneles base
        panelSocios = new JPanel(new BorderLayout());
        panelActividades = new JPanel(new BorderLayout());
        panelReservas = new JPanel(new BorderLayout());
        panelFacturacion = new JPanel(new BorderLayout());
        
        //Construccion del modulo Socios
        iniciarModuloSocios();

        //Placeholders temporales para los siguientes modulos
        panelActividades.add(new JLabel("Modulo Actividades en construccion", JLabel.CENTER));
        panelReservas.add(new JLabel("Modulo Reservas en construccion", JLabel.CENTER));
        panelFacturacion.add(new JLabel("Modulo Facturacion en construccion", JLabel.CENTER));
        
        //Incorporacion de pestanas al contenedor principal
        pestanas.addTab("Socios", panelSocios);
        pestanas.addTab("Actividades", panelActividades);
        pestanas.addTab("Reservas", panelReservas);
        pestanas.addTab("Facturación", panelFacturacion);

        ventana.add(pestanas, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
    
    private void iniciarModuloSocios() {
        //Formulario de ingreso de datos
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Socio"));

        panelFormulario.add(new JLabel("RUT:"));
        txtRutSocio = new JTextField();
        panelFormulario.add(txtRutSocio);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombreSocio = new JTextField();
        panelFormulario.add(txtNombreSocio);

        panelFormulario.add(new JLabel("Edad:"));
        txtEdadSocio = new JTextField();
        panelFormulario.add(txtEdadSocio);

        //Botones de accion del formulario
        btnAgregarSocio = new JButton("Registrar Socio");
        btnLimpiarCamposSocio = new JButton("Limpiar Campos");

        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesForm.add(btnLimpiarCamposSocio);
        panelBotonesForm.add(btnAgregarSocio);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelFormulario, BorderLayout.CENTER);
        panelNorte.add(panelBotonesForm, BorderLayout.SOUTH);

        panelSocios.add(panelNorte, BorderLayout.NORTH);

        //Placeholder temporal para la tabla
        panelSocios.add(new JLabel("Tabla de socios en construccion", JLabel.CENTER), BorderLayout.CENTER);
    }
    
}
