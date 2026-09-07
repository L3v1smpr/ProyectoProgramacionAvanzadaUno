package vista;

import controlador.SistemaClub;
import modelo.Socio;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
    
    //Componentes de la tabla de Socios
    private JTable tablaSocios;
    private DefaultTableModel modeloTablaSocios;
    private JButton btnDesactivarSocio;

    public MenuVentana(SistemaClub controlador) {
        this.controlador = controlador;
    }

    public void iniciarVentana() {
    	ventana = new JFrame("Sistema de Gestion: Club Deportivo");
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

        //Configuracion de tabla y modelo de datos
        String[] columnas = {"RUT", "Nombre", "Edad", "Deuda", "Moroso"};
        modeloTablaSocios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSocios = new JTable(modeloTablaSocios);
        JScrollPane scrollTabla = new JScrollPane(tablaSocios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Socios Activos"));

        panelSocios.add(scrollTabla, BorderLayout.CENTER);
        
        //Panel inferior para acciones sobre la seleccion de la tabla
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDesactivarSocio = new JButton("Desactivar Socio Seleccionado");
        panelSur.add(btnDesactivarSocio);
        panelSocios.add(panelSur, BorderLayout.SOUTH);

        //Configuracion de eventos para socios
        configurarEventosSocios();

        //Carga inicial de datos en la tabla
        refrescarTablaSocios();
    }
    
    private void configurarEventosSocios() {
        //Evento para limpiar campos
        btnLimpiarCamposSocio.addActionListener(e -> limpiarCamposSocio());

        //Evento para agregar socio
        btnAgregarSocio.addActionListener(e -> {
            String rut = txtRutSocio.getText().trim();
            String nombre = txtNombreSocio.getText().trim();
            String edadTexto = txtEdadSocio.getText().trim();

            if (rut.isEmpty() || nombre.isEmpty() || edadTexto.isEmpty()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Por favor, complete todos los campos del formulario.",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int edad;
            try {
                edad = Integer.parseInt(edadTexto);
                if (edad <= 0) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "La edad debe ser un numero positivo mayor a cero.",
                        "Edad invalida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "La edad debe ser un valor numerico entero.",
                    "Formato invalido",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean agregado = controlador.agregarSocio(rut, nombre, edad);

            if (agregado) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Socio registrado exitosamente.",
                    "Operacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCamposSocio();
                refrescarTablaSocios();
            } else {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error: Ya existe un socio registrado con el RUT ingresado.",
                    "RUT duplicado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        //Evento para desactivar socio seleccionado (baja logica)
        btnDesactivarSocio.addActionListener(e -> {
            int filaSeleccionada = tablaSocios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione un socio de la tabla para desactivar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String rut = (String) modeloTablaSocios.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTablaSocios.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Esta seguro de que desea desactivar al socio " + nombre + " (RUT: " + rut + ")?",
                "Confirmar desactivacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean desactivado = controlador.desactivarSocio(rut);
                if (desactivado) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Socio desactivado exitosamente del catalogo activo.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refrescarTablaSocios();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Error al desactivar el socio seleccionado.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void limpiarCamposSocio() {
        txtRutSocio.setText("");
        txtNombreSocio.setText("");
        txtEdadSocio.setText("");
    }

    private void refrescarTablaSocios() {
        modeloTablaSocios.setRowCount(0);
        if (controlador != null) {
            for (Socio socio : controlador.obtenerListaSocios()) {
                Object[] fila = {
                    socio.getRut(),
                    socio.getNombre(),
                    socio.getEdad(),
                    "$" + socio.getDeuda(),
                    socio.getEsMoroso() ? "Si" : "No"
                };
                modeloTablaSocios.addRow(fila);
            }
        }
    }
}
