package vista;

import controlador.SistemaClub;
import modelo.Socio;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

    //Componentes de la tabla y controles de Socios
    private JTable tablaSocios;
    private DefaultTableModel modeloTablaSocios;
    private JButton btnModificarSocio;
    private JButton btnDesactivarSocio;
    private JCheckBox chkSoloDeudores;

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
        pestanas.addTab("Facturacion", panelFacturacion);

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

        //Panel inferior con filtro y acciones sobre la seleccion
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkSoloDeudores = new JCheckBox("Mostrar solo socios con deuda");
        panelFiltro.add(chkSoloDeudores);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnModificarSocio = new JButton("Modificar Socio Seleccionado");
        btnDesactivarSocio = new JButton("Desactivar Socio Seleccionado");
        panelAccionesTabla.add(btnModificarSocio);
        panelAccionesTabla.add(btnDesactivarSocio);

        panelSur.add(panelFiltro, BorderLayout.WEST);
        panelSur.add(panelAccionesTabla, BorderLayout.EAST);
        panelSocios.add(panelSur, BorderLayout.SOUTH);

        //Configuracion de eventos para socios
        configurarEventosSocios();

        //Carga inicial de datos en la tabla
        refrescarTablaSocios();
    }

    private void configurarEventosSocios() {
        //Evento para limpiar campos
        btnLimpiarCamposSocio.addActionListener(e -> limpiarCamposSocio());

        //Evento para alternar filtro de socios morosos
        chkSoloDeudores.addActionListener(e -> refrescarTablaSocios());

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

        //Evento para modificar socio seleccionado
        btnModificarSocio.addActionListener(e -> {
            int filaSeleccionada = tablaSocios.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione un socio de la tabla para modificar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String rut = (String) modeloTablaSocios.getValueAt(filaSeleccionada, 0);
            Socio socioActual = controlador.buscarSocio(rut);

            if (socioActual == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error: No se encontro el socio seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            //Formulario modal de edicion
            JTextField txtModNombre = new JTextField(socioActual.getNombre());
            JTextField txtModEdad = new JTextField(String.valueOf(socioActual.getEdad()));
            JTextField txtModDeuda = new JTextField(String.valueOf(socioActual.getDeuda()));
            JCheckBox chkModMoroso = new JCheckBox("Es Moroso", socioActual.getEsMoroso());

            JPanel panelEdicion = new JPanel(new GridLayout(4, 2, 6, 6));
            panelEdicion.add(new JLabel("Nombre:"));
            panelEdicion.add(txtModNombre);
            panelEdicion.add(new JLabel("Edad:"));
            panelEdicion.add(txtModEdad);
            panelEdicion.add(new JLabel("Deuda:"));
            panelEdicion.add(txtModDeuda);
            panelEdicion.add(new JLabel("Estado:"));
            panelEdicion.add(chkModMoroso);

            int resultado = JOptionPane.showConfirmDialog(
                ventana,
                panelEdicion,
                "Modificar Socio: " + rut,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (resultado == JOptionPane.OK_OPTION) {
                String nuevoNombre = txtModNombre.getText().trim();
                String nuevaEdadTxt = txtModEdad.getText().trim();
                String nuevaDeudaTxt = txtModDeuda.getText().trim();
                boolean nuevoMoroso = chkModMoroso.isSelected();

                if (nuevoNombre.isEmpty() || nuevaEdadTxt.isEmpty() || nuevaDeudaTxt.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Todos los campos deben contener datos validos.",
                        "Campos vacios",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int nuevaEdad;
                int nuevaDeuda;

                try {
                    nuevaEdad = Integer.parseInt(nuevaEdadTxt);
                    nuevaDeuda = Integer.parseInt(nuevaDeudaTxt);

                    if (nuevaEdad <= 0 || nuevaDeuda < 0) {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "La edad debe ser mayor a 0 y la deuda no puede ser negativa.",
                            "Valores invalidos",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Edad y Deuda deben ser valores numericos enteros.",
                        "Formato invalido",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                boolean modificado = controlador.modificarSocio(rut, nuevoNombre, nuevaEdad, nuevaDeuda, nuevoMoroso);

                if (modificado) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Socio modificado correctamente.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refrescarTablaSocios();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Error al intentar modificar el socio.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
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
            java.util.ArrayList<Socio> lista = chkSoloDeudores != null && chkSoloDeudores.isSelected()
                ? controlador.obtenerListaSociosDeudores()
                : controlador.obtenerListaSocios();

            for (Socio socio : lista) {
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