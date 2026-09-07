package vista;
import controlador.SistemaClub;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class MenuVentana {

    private SistemaClub controlador;
    private JFrame ventana;
    private JTabbedPane pestanas;

    //Paneles contenedores para cada modulo
    private JPanel panelSocios;
    private JPanel panelActividades;
    private JPanel panelReservas;
    private JPanel panelFacturacion;

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

        //Placeholders temporales
        panelSocios.add(new JLabel("Módulo Socios en construcción", JLabel.CENTER));
        panelActividades.add(new JLabel("Módulo Actividades en construcción", JLabel.CENTER));
        panelReservas.add(new JLabel("Módulo Reservas en construcción", JLabel.CENTER));
        panelFacturacion.add(new JLabel("Módulo Facturación en construcción", JLabel.CENTER));

        //Incorporacion de pestanas al contenedor principal
        pestanas.addTab("Socios", panelSocios);
        pestanas.addTab("Actividades", panelActividades);
        pestanas.addTab("Reservas", panelReservas);
        pestanas.addTab("Facturación", panelFacturacion);

        ventana.add(pestanas, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
}
