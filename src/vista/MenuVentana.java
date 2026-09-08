package vista;

import controlador.SistemaClub;
import modelo.Socio;
import modelo.Actividad;
import modelo.Reserva;
import modelo.EstadoReserva;
import modelo.MorosidadException;
import modelo.CupoMaximoException;
import modelo.ConexionBDException;
import modelo.PersistenciaDatosException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ArrayList;

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
    private JButton btnReactivarSocio;
    private JCheckBox chkSoloDeudores;

    //Componentes del formulario de Actividades
    private JTextField txtIdActividad;
    private JTextField txtNombreActividad;
    private JTextField txtCupoActividad;
    private JTextField txtEdadMinActividad;
    private JComboBox<String> cmbTipoActividad;

    //Campos especificos de subclases de Actividad
    private JTextField txtProfesorActividad;
    private JCheckBox chkRequiereAsistencia;
    private JTextField txtFechaEvento;
    private JTextField txtLugarEvento;
    private JTextField txtTipoEvento;
    private JButton btnAgregarActividad;
    private JButton btnLimpiarCamposActividad;

    //Componentes de la tabla y controles de Actividades
    private JTable tablaActividades;
    private DefaultTableModel modeloTablaActividades;
    private JCheckBox chkSoloEventos;
    private JButton btnModificarActividad;
    private JButton btnDesactivarActividad;
    private JButton btnReactivarActividad;

    //Componentes del formulario de Reservas
    private JTextField txtRutReserva;
    private JTextField txtIdActividadReserva;
    private JTextField txtFechaReserva;
    private JButton btnAgendarReserva;
    private JButton btnLimpiarCamposReserva;

    //Componentes de la tabla y acciones de Reservas
    private JTable tablaReservas;
    private DefaultTableModel modeloTablaReservas;
    private JButton btnModificarReserva;
    private JButton btnCancelarReserva;

    //Componentes del modulo de Facturacion
    private JTextField txtRutFacturacion;
    private JButton btnBuscarFacturacion;
    private JLabel lblNombreFacturacion;
    private JLabel lblDeudaFacturacion;
    private JLabel lblEstadoFacturacion;
    private JTextField txtMontoAbono;
    private JButton btnPagarTotal;
    private JButton btnAbonar;
    private JButton btnCobroMensual;

    public MenuVentana(SistemaClub controlador) {
        this.controlador = controlador;
    }

    public void iniciarVentana() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si falla, mantiene el tema por defecto
        }

        ventana = new JFrame("Sistema de Gestion: Club Deportivo");
        ventana.setSize(950, 650);
        ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        //Control de cierre con guardado automatico en BD
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalidaYGuardar();
            }
        });

        pestanas = new JTabbedPane();

        //Inicializacion de paneles base
        panelSocios = new JPanel(new BorderLayout());
        panelActividades = new JPanel(new BorderLayout());
        panelReservas = new JPanel(new BorderLayout());
        panelFacturacion = new JPanel(new BorderLayout());

        //Construccion de modulos
        iniciarModuloSocios();
        iniciarModuloActividades();
        iniciarModuloReservas();
        iniciarModuloFacturacion();

        //Incorporacion de pestanas al contenedor principal
        pestanas.addTab("Socios", panelSocios);
        pestanas.addTab("Actividades", panelActividades);
        pestanas.addTab("Reservas", panelReservas);
        pestanas.addTab("Facturacion", panelFacturacion);

        ventana.add(pestanas, BorderLayout.CENTER);
        ventana.setVisible(true);
    }

    private void confirmarSalidaYGuardar() {
        int confirmacion = JOptionPane.showConfirmDialog(
            ventana,
            "Desea guardar los cambios en la base de datos y salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (controlador != null) {
                    controlador.guardarDatosBatch();
                }
                JOptionPane.showMessageDialog(
                    ventana,
                    "Datos guardados exitosamente en SQLite.",
                    "Guardado exitoso",
                    JOptionPane.INFORMATION_MESSAGE
                );
                ventana.dispose();
                System.exit(0);
            } catch (ConexionBDException | PersistenciaDatosException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error de base de datos al guardar: " + ex.getMessage(),
                    "Error de persistencia",
                    JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error inesperado al guardar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (confirmacion == JOptionPane.NO_OPTION) {
            ventana.dispose();
            System.exit(0);
        }
    }

    private void iniciarModuloSocios() {
        panelSocios.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

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

        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
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
        tablaSocios.setRowHeight(22);
        tablaSocios.setShowGrid(true);

        JScrollPane scrollTabla = new JScrollPane(tablaSocios);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Socios Activos"));

        panelSocios.add(scrollTabla, BorderLayout.CENTER);

        //Panel inferior con filtro y acciones sobre la seleccion
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        chkSoloDeudores = new JCheckBox("Mostrar solo socios con deuda");
        panelFiltro.add(chkSoloDeudores);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        btnModificarSocio = new JButton("Modificar Socio Seleccionado");
        btnDesactivarSocio = new JButton("Desactivar Socio Seleccionado");
        btnReactivarSocio = new JButton("Reactivar Socio");

        panelAccionesTabla.add(btnModificarSocio);
        panelAccionesTabla.add(btnDesactivarSocio);
        panelAccionesTabla.add(btnReactivarSocio);

        panelSur.add(panelFiltro, BorderLayout.WEST);
        panelSur.add(panelAccionesTabla, BorderLayout.EAST);
        panelSocios.add(panelSur, BorderLayout.SOUTH);

        //Configuracion de eventos para socios
        configurarEventosSocios();

        //Carga inicial de datos en la tabla
        refrescarTablaSocios();
    }

    private void iniciarModuloActividades() {
        panelActividades.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        //Panel superior que contiene los formularios de actividades
        JPanel panelNorteActividades = new JPanel(new BorderLayout(8, 8));

        //Formulario de atributos comunes
        JPanel panelComun = new JPanel(new GridLayout(5, 2, 6, 6));
        panelComun.setBorder(BorderFactory.createTitledBorder("Datos Generales"));

        panelComun.add(new JLabel("ID Actividad:"));
        txtIdActividad = new JTextField();
        panelComun.add(txtIdActividad);

        panelComun.add(new JLabel("Nombre:"));
        txtNombreActividad = new JTextField();
        panelComun.add(txtNombreActividad);

        panelComun.add(new JLabel("Cupo Maximo:"));
        txtCupoActividad = new JTextField();
        panelComun.add(txtCupoActividad);

        panelComun.add(new JLabel("Edad Minima:"));
        txtEdadMinActividad = new JTextField();
        panelComun.add(txtEdadMinActividad);

        panelComun.add(new JLabel("Tipo de Actividad:"));
        String[] tipos = {"Clase Grupal", "Entrenamiento Libre", "Evento"};
        cmbTipoActividad = new JComboBox<>(tipos);
        panelComun.add(cmbTipoActividad);

        //Formulario de atributos especificos
        JPanel panelEspecifico = new JPanel(new GridLayout(5, 2, 6, 6));
        panelEspecifico.setBorder(BorderFactory.createTitledBorder("Detalles Segun Tipo"));

        panelEspecifico.add(new JLabel("Profesor:"));
        txtProfesorActividad = new JTextField();
        panelEspecifico.add(txtProfesorActividad);

        panelEspecifico.add(new JLabel("Asistencia Obligatoria:"));
        chkRequiereAsistencia = new JCheckBox("Requiere asistencia");
        panelEspecifico.add(chkRequiereAsistencia);

        panelEspecifico.add(new JLabel("Fecha Evento (dd-MM-yyyy):"));
        txtFechaEvento = new JTextField();
        panelEspecifico.add(txtFechaEvento);

        panelEspecifico.add(new JLabel("Lugar Evento:"));
        txtLugarEvento = new JTextField();
        panelEspecifico.add(txtLugarEvento);

        panelEspecifico.add(new JLabel("Tipo de Evento:"));
        txtTipoEvento = new JTextField();
        panelEspecifico.add(txtTipoEvento);

        //Contenedor horizontal para ambos subformularios
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 8, 8));
        panelFormularios.add(panelComun);
        panelFormularios.add(panelEspecifico);

        //Barra de botones del formulario de actividades
        btnAgregarActividad = new JButton("Registrar Actividad");
        btnLimpiarCamposActividad = new JButton("Limpiar Campos");

        JPanel panelBotonesAct = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        panelBotonesAct.add(btnLimpiarCamposActividad);
        panelBotonesAct.add(btnAgregarActividad);

        panelNorteActividades.add(panelFormularios, BorderLayout.CENTER);
        panelNorteActividades.add(panelBotonesAct, BorderLayout.SOUTH);

        panelActividades.add(panelNorteActividades, BorderLayout.NORTH);

        //Configuracion de tabla y modelo de datos para actividades
        String[] columnasAct = {"ID", "Nombre", "Tipo", "Cupo", "Edad Min", "Detalles"};
        modeloTablaActividades = new DefaultTableModel(columnasAct, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaActividades = new JTable(modeloTablaActividades);
        tablaActividades.setRowHeight(22);
        tablaActividades.setShowGrid(true);

        JScrollPane scrollTablaAct = new JScrollPane(tablaActividades);
        scrollTablaAct.setBorder(BorderFactory.createTitledBorder("Listado de Actividades Activas"));

        panelActividades.add(scrollTablaAct, BorderLayout.CENTER);

        //Panel inferior con filtro de eventos y botones de accion
        JPanel panelSurAct = new JPanel(new BorderLayout());
        panelSurAct.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        JPanel panelFiltroAct = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        chkSoloEventos = new JCheckBox("Mostrar solo eventos");
        panelFiltroAct.add(chkSoloEventos);

        JPanel panelAccionesAct = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        btnModificarActividad = new JButton("Modificar Actividad Seleccionada");
        btnDesactivarActividad = new JButton("Desactivar Actividad Seleccionada");
        btnReactivarActividad = new JButton("Reactivar Actividad");

        panelAccionesAct.add(btnModificarActividad);
        panelAccionesAct.add(btnDesactivarActividad);
        panelAccionesAct.add(btnReactivarActividad);

        panelSurAct.add(panelFiltroAct, BorderLayout.WEST);
        panelSurAct.add(panelAccionesAct, BorderLayout.EAST);
        panelActividades.add(panelSurAct, BorderLayout.SOUTH);

        //Configuracion de eventos de actividades
        configurarEventosActividades();

        //Estado visual inicial segun la seleccion por defecto
        actualizarCamposSegunTipoActividad();

        //Carga inicial de datos en la tabla de actividades
        refrescarTablaActividades();
    }

    private void iniciarModuloReservas() {
        panelReservas.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        //Panel superior para formulario de agendar reserva
        JPanel panelNorteReservas = new JPanel(new BorderLayout(8, 8));

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 6, 6));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agendar Nueva Reserva"));

        panelFormulario.add(new JLabel("RUT Socio:"));
        txtRutReserva = new JTextField();
        panelFormulario.add(txtRutReserva);

        panelFormulario.add(new JLabel("ID Actividad:"));
        txtIdActividadReserva = new JTextField();
        panelFormulario.add(txtIdActividadReserva);

        panelFormulario.add(new JLabel("Fecha (dd-MM-yyyy):"));
        txtFechaReserva = new JTextField();
        panelFormulario.add(txtFechaReserva);

        //Botones de accion del formulario de reservas
        btnAgendarReserva = new JButton("Agendar Reserva");
        btnLimpiarCamposReserva = new JButton("Limpiar Campos");

        JPanel panelBotonesRes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        panelBotonesRes.add(btnLimpiarCamposReserva);
        panelBotonesRes.add(btnAgendarReserva);

        panelNorteReservas.add(panelFormulario, BorderLayout.CENTER);
        panelNorteReservas.add(panelBotonesRes, BorderLayout.SOUTH);

        panelReservas.add(panelNorteReservas, BorderLayout.NORTH);

        //Configuracion de tabla y modelo de datos para reservas
        String[] columnasRes = {"ID Reserva", "RUT Socio", "ID Actividad", "Fecha", "Estado"};
        modeloTablaReservas = new DefaultTableModel(columnasRes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloTablaReservas);
        tablaReservas.setRowHeight(22);
        tablaReservas.setShowGrid(true);

        JScrollPane scrollTablaRes = new JScrollPane(tablaReservas);
        scrollTablaRes.setBorder(BorderFactory.createTitledBorder("Listado Global de Reservas (Orden Cronologico)"));

        panelReservas.add(scrollTablaRes, BorderLayout.CENTER);

        //Panel inferior con botones de accion sobre reservas
        JPanel panelSurRes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        panelSurRes.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));

        btnModificarReserva = new JButton("Modificar Reserva Seleccionada");
        btnCancelarReserva = new JButton("Cancelar Reserva Seleccionada");
        panelSurRes.add(btnModificarReserva);
        panelSurRes.add(btnCancelarReserva);

        panelReservas.add(panelSurRes, BorderLayout.SOUTH);

        //Configuracion de eventos de reservas
        configurarEventosReservas();

        //Carga inicial de datos en la tabla de reservas
        refrescarTablaReservas();
    }

    private void iniciarModuloFacturacion() {
        panelFacturacion.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        //Panel central contenedor de las secciones de facturacion
        JPanel panelContenedorFacturacion = new JPanel(new GridLayout(3, 1, 10, 10));
        panelContenedorFacturacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //1. Seccion de busqueda y estado de cuenta del socio
        JPanel panelConsulta = new JPanel(new BorderLayout(8, 8));
        panelConsulta.setBorder(BorderFactory.createTitledBorder("Consulta de Estado de Cuenta"));

        JPanel panelBusquedaRut = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panelBusquedaRut.add(new JLabel("RUT Socio:"));
        txtRutFacturacion = new JTextField(12);
        panelBusquedaRut.add(txtRutFacturacion);
        btnBuscarFacturacion = new JButton("Consultar Deuda");
        panelBusquedaRut.add(btnBuscarFacturacion);

        JPanel panelDatosCuenta = new JPanel(new GridLayout(3, 1, 4, 4));
        panelDatosCuenta.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        lblNombreFacturacion = new JLabel("Nombre Socio: -");
        lblDeudaFacturacion = new JLabel("Deuda Pendiente: -");
        lblEstadoFacturacion = new JLabel("Condicion de Pago: -");

        panelDatosCuenta.add(lblNombreFacturacion);
        panelDatosCuenta.add(lblDeudaFacturacion);
        panelDatosCuenta.add(lblEstadoFacturacion);

        panelConsulta.add(panelBusquedaRut, BorderLayout.NORTH);
        panelConsulta.add(panelDatosCuenta, BorderLayout.CENTER);

        //2. Seccion de gestion de pagos
        JPanel panelPagos = new JPanel(new BorderLayout(8, 8));
        panelPagos.setBorder(BorderFactory.createTitledBorder("Procesar Pagos y Abonos"));

        JPanel panelFormPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panelFormPago.add(new JLabel("Monto a Abonar ($):"));
        txtMontoAbono = new JTextField(10);
        panelFormPago.add(txtMontoAbono);
        btnAbonar = new JButton("Realizar Abono Parcial");
        panelFormPago.add(btnAbonar);

        JPanel panelPagoTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        btnPagarTotal = new JButton("Pagar Deuda Total");
        panelPagoTotal.add(btnPagarTotal);

        panelPagos.add(panelFormPago, BorderLayout.NORTH);
        panelPagos.add(panelPagoTotal, BorderLayout.CENTER);

        //3. Seccion de cobro mensual administrativo
        JPanel panelCobroMensual = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        panelCobroMensual.setBorder(BorderFactory.createTitledBorder("Cobro Mensual del Club"));
        btnCobroMensual = new JButton("Generar Cobro Mensual a Todos los Socios");
        panelCobroMensual.add(btnCobroMensual);

        panelContenedorFacturacion.add(panelConsulta);
        panelContenedorFacturacion.add(panelPagos);
        panelContenedorFacturacion.add(panelCobroMensual);

        panelFacturacion.add(panelContenedorFacturacion, BorderLayout.NORTH);

        //Configuracion inicial de eventos de facturacion
        configurarEventosFacturacion();
    }

    private void configurarEventosFacturacion() {
        //Evento de consulta de socio por RUT
        btnBuscarFacturacion.addActionListener(e -> consultarSocioFacturacion());

        //Evento para pagar la deuda total
        btnPagarTotal.addActionListener(e -> {
            String rut = txtRutFacturacion.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Consulte primero el socio ingresando su RUT.",
                    "RUT requerido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Socio socio = controlador.buscarSocio(rut);
            if (socio == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "No se encontro el socio para procesar el pago.",
                    "Socio no encontrado",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (socio.getDeuda() <= 0) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "El socio no mantiene deuda pendiente.",
                    "Sin saldo pendiente",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Desea cancelar el monto total de $" + socio.getDeuda() + " para " + socio.getNombre() + "?",
                "Confirmar pago total",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean pagado = controlador.pagarFacturacion(rut);
                if (pagado) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Pago total registrado exitosamente. Deuda saldada.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    consultarSocioFacturacion();
                    refrescarTablaSocios();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Error al registrar el pago en el sistema.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        //Evento para realizar abono parcial
        btnAbonar.addActionListener(e -> {
            String rut = txtRutFacturacion.getText().trim();
            String montoTxt = txtMontoAbono.getText().trim();

            if (rut.isEmpty() || montoTxt.isEmpty()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Consulte un socio e ingrese el monto a abonar.",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Socio socio = controlador.buscarSocio(rut);
            if (socio == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "No se encontro el socio para procesar el abono.",
                    "Socio no encontrado",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int monto;
            try {
                monto = Integer.parseInt(montoTxt);
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "El abono debe ser mayor a cero.",
                        "Monto invalido",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "El monto debe ser un valor numerico entero.",
                    "Formato invalido",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean abonado = controlador.pagarFacturacion(rut, monto);
            if (abonado) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Abono de $" + monto + " registrado correctamente.",
                    "Operacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                txtMontoAbono.setText("");
                consultarSocioFacturacion();
                refrescarTablaSocios();
            } else {
                JOptionPane.showMessageDialog(
                    ventana,
                    "No fue posible procesar el abono. Verifique el saldo adeudado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        //Evento para cobro mensual general
        btnCobroMensual.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Esta accion aplicara el cobro de la cuota mensual a todos los socios registrados.\nDesea continuar?",
                "Confirmar cobro general",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                controlador.generarCobroMensual();
                JOptionPane.showMessageDialog(
                    ventana,
                    "Cobro mensual aplicado correctamente a la totalidad del padron.",
                    "Cobro completado",
                    JOptionPane.INFORMATION_MESSAGE
                );
                if (!txtRutFacturacion.getText().trim().isEmpty()) {
                    consultarSocioFacturacion();
                }
                refrescarTablaSocios();
            }
        });
    }

    private void consultarSocioFacturacion() {
        String rut = txtRutFacturacion.getText().trim();
        if (rut.isEmpty()) {
            JOptionPane.showMessageDialog(
                ventana,
                "Ingrese un RUT para consultar el estado de cuenta.",
                "RUT requerido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Socio socio = controlador.buscarSocio(rut);
        if (socio != null) {
            lblNombreFacturacion.setText("Nombre Socio: " + socio.getNombre());
            lblDeudaFacturacion.setText("Deuda Pendiente: $" + socio.getDeuda());
            lblEstadoFacturacion.setText("Condicion de Pago: " + (socio.getEsMoroso() ? "MOROSO" : "AL DIA"));
        } else {
            JOptionPane.showMessageDialog(
                ventana,
                "No se encontro un socio registrado con el RUT ingresado.",
                "Socio no encontrado",
                JOptionPane.ERROR_MESSAGE
            );
            limpiarInformacionFacturacion();
        }
    }

    private void limpiarInformacionFacturacion() {
        lblNombreFacturacion.setText("Nombre Socio: -");
        lblDeudaFacturacion.setText("Deuda Pendiente: -");
        lblEstadoFacturacion.setText("Condicion de Pago: -");
        txtMontoAbono.setText("");
    }

    private void configurarEventosReservas() {
        //Evento para limpiar campos de reserva
        btnLimpiarCamposReserva.addActionListener(e -> limpiarCamposReserva());

        //Evento para agendar reserva con captura especifica de excepciones
        btnAgendarReserva.addActionListener(e -> {
            String rut = txtRutReserva.getText().trim();
            String idActividad = txtIdActividadReserva.getText().trim();
            String fechaTexto = txtFechaReserva.getText().trim();

            if (rut.isEmpty() || idActividad.isEmpty() || fechaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Complete todos los campos para agendar la reserva.",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            Date fechaReserva;

            try {
                fechaReserva = sdf.parse(fechaTexto);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Formato de fecha invalido. Utilice el formato dd-MM-yyyy.",
                    "Fecha invalida",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                boolean agendada = controlador.agendarReserva(rut, idActividad, fechaReserva);
                if (agendada) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Reserva agendada exitosamente.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    limpiarCamposReserva();
                    refrescarTablaReservas();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "No se pudo agendar. Verifique que el socio y la actividad existan.",
                        "Datos no encontrados",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (MorosidadException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    ex.getMessage(),
                    "Socio Moroso",
                    JOptionPane.WARNING_MESSAGE
                );
            } catch (CupoMaximoException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    ex.getMessage(),
                    "Cupo Maximo Excedido",
                    JOptionPane.WARNING_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error inesperado al procesar la reserva: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        //Evento para modificar reserva seleccionada
        btnModificarReserva.addActionListener(e -> {
            int fila = tablaReservas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione una reserva de la tabla para modificar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int idReserva = (int) modeloTablaReservas.getValueAt(fila, 0);
            String rutSocio = (String) modeloTablaReservas.getValueAt(fila, 1);
            String idActActual = (String) modeloTablaReservas.getValueAt(fila, 2);
            String fechaActual = (String) modeloTablaReservas.getValueAt(fila, 3);
            String estadoActualStr = (String) modeloTablaReservas.getValueAt(fila, 4);

            //Formulario modal de edicion de reserva
            JTextField txtModFecha = new JTextField(fechaActual);
            JTextField txtModIdAct = new JTextField(idActActual);
            JComboBox<EstadoReserva> cmbModEstado = new JComboBox<>(EstadoReserva.values());
            try {
                cmbModEstado.setSelectedItem(EstadoReserva.valueOf(estadoActualStr));
            } catch (Exception ex) {
                cmbModEstado.setSelectedIndex(0);
            }

            JPanel panelModal = new JPanel(new GridLayout(3, 2, 6, 6));
            panelModal.add(new JLabel("Fecha (dd-MM-yyyy):"));
            panelModal.add(txtModFecha);
            panelModal.add(new JLabel("ID Actividad:"));
            panelModal.add(txtModIdAct);
            panelModal.add(new JLabel("Estado:"));
            panelModal.add(cmbModEstado);

            int opcion = JOptionPane.showConfirmDialog(
                ventana,
                panelModal,
                "Modificar Reserva ID: " + idReserva,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String nuevaFechaTxt = txtModFecha.getText().trim();
                String nuevaAct = txtModIdAct.getText().trim();
                EstadoReserva nuevoEstado = (EstadoReserva) cmbModEstado.getSelectedItem();

                if (nuevaFechaTxt.isEmpty() || nuevaAct.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Todos los campos deben contener datos.",
                        "Campos vacios",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false);
                Date nuevaFecha;

                try {
                    nuevaFecha = sdf.parse(nuevaFechaTxt);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Formato de fecha invalido. Use dd-MM-yyyy.",
                        "Fecha invalida",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    boolean modificada = controlador.modificarReserva(idReserva, nuevaFecha, nuevoEstado, rutSocio, nuevaAct);
                    if (modificada) {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "Reserva modificada correctamente.",
                            "Operacion exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        refrescarTablaReservas();
                    } else {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "No fue posible modificar la reserva. Compruebe los datos ingresados.",
                            "Error de modificacion",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (MorosidadException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        ex.getMessage(),
                        "Socio Moroso",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        //Evento para cancelar o eliminar reserva seleccionada
        btnCancelarReserva.addActionListener(e -> {
            int fila = tablaReservas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione una reserva de la tabla para cancelar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int idReserva = (int) modeloTablaReservas.getValueAt(fila, 0);

            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Esta seguro de que desea cancelar la reserva con ID: " + idReserva + "?",
                "Confirmar cancelacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminada = controlador.eliminarReserva(idReserva);
                if (eliminada) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Reserva eliminada exitosamente del sistema.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refrescarTablaReservas();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "No se encontro la reserva seleccionada.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void limpiarCamposReserva() {
        txtRutReserva.setText("");
        txtIdActividadReserva.setText("");
        txtFechaReserva.setText("");
    }

    private void refrescarTablaReservas() {
        modeloTablaReservas.setRowCount(0);
        if (controlador != null) {
            ArrayList<Reserva> lista = controlador.listarReservasGlobales();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (Reserva r : lista) {
                String fechaFmt = (r.getFecha() != null) ? sdf.format(r.getFecha()) : "N/A";
                Object[] fila = {
                    r.getIdReserva(),
                    r.getRutSocio(),
                    r.getIdActividadEnReserva(),
                    fechaFmt,
                    r.getEstado() != null ? r.getEstado().name() : "N/A"
                };
                modeloTablaReservas.addRow(fila);
            }
        }
    }

    private void configurarEventosActividades() {
        //Evento de cambio de tipo en el selector
        cmbTipoActividad.addActionListener(e -> actualizarCamposSegunTipoActividad());

        //Evento para limpiar campos de actividad
        btnLimpiarCamposActividad.addActionListener(e -> limpiarCamposActividad());

        //Evento para filtrar solo eventos en la tabla
        chkSoloEventos.addActionListener(e -> refrescarTablaActividades());

        //Evento para registrar actividad segun polimorfismo
        btnAgregarActividad.addActionListener(e -> {
            String id = txtIdActividad.getText().trim();
            String nombre = txtNombreActividad.getText().trim();
            String cupoTxt = txtCupoActividad.getText().trim();
            String edadMinTxt = txtEdadMinActividad.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || cupoTxt.isEmpty() || edadMinTxt.isEmpty()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Complete todos los campos generales de la actividad.",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int cupo;
            int edadMin;
            try {
                cupo = Integer.parseInt(cupoTxt);
                edadMin = Integer.parseInt(edadMinTxt);

                if (cupo <= 0 || edadMin < 0) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "El cupo debe ser mayor a cero y la edad minima no puede ser negativa.",
                        "Valores invalidos",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Cupo y Edad Minima deben ser valores numericos enteros.",
                    "Formato invalido",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String tipo = (String) cmbTipoActividad.getSelectedItem();
            boolean exito = false;

            if (tipo.equals("Clase Grupal")) {
                String profesor = txtProfesorActividad.getText().trim();
                if (profesor.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Debe ingresar el nombre del profesor para la clase grupal.",
                        "Profesor requerido",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                exito = controlador.agregarActividad(id, nombre, cupo, edadMin, profesor);

            } else if (tipo.equals("Entrenamiento Libre")) {
                boolean requiereAsistencia = chkRequiereAsistencia.isSelected();
                exito = controlador.agregarActividad(id, nombre, cupo, edadMin, requiereAsistencia);

            } else if (tipo.equals("Evento")) {
                String fechaTxt = txtFechaEvento.getText().trim();
                String lugar = txtLugarEvento.getText().trim();
                String tipoEvt = txtTipoEvento.getText().trim();

                if (fechaTxt.isEmpty() || lugar.isEmpty() || tipoEvt.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Complete fecha, lugar y tipo de evento para registrar el evento.",
                        "Campos requeridos",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false);
                Date fechaEvento;

                try {
                    fechaEvento = sdf.parse(fechaTxt);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Formato de fecha invalido. Utilice el formato dd-MM-yyyy.",
                        "Fecha invalida",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                exito = controlador.agregarActividad(id, nombre, cupo, edadMin, fechaEvento, lugar, tipoEvt);
            }

            if (exito) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Actividad registrada exitosamente.",
                    "Operacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                limpiarCamposActividad();
                refrescarTablaActividades();
            } else {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error: Ya existe una actividad registrada con ese ID.",
                    "ID duplicado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        //Evento para modificar actividad seleccionada
        btnModificarActividad.addActionListener(e -> {
            int filaSeleccionada = tablaActividades.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione una actividad de la tabla para modificar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String id = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 0);
            Actividad actActual = controlador.buscarActividad(id);

            if (actActual == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error: No se encontro la actividad seleccionada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            //Formulario modal de edicion de actividad
            JTextField txtModNombre = new JTextField(actActual.getNombre());
            JTextField txtModCupo = new JTextField(String.valueOf(actActual.getCupoMaximo()));
            JTextField txtModEdadMin = new JTextField(String.valueOf(actActual.getEdadMinima()));

            JPanel panelEdicion = new JPanel(new GridLayout(3, 2, 6, 6));
            panelEdicion.add(new JLabel("Nombre:"));
            panelEdicion.add(txtModNombre);
            panelEdicion.add(new JLabel("Cupo Maximo:"));
            panelEdicion.add(txtModCupo);
            panelEdicion.add(new JLabel("Edad Minima:"));
            panelEdicion.add(txtModEdadMin);

            int resultado = JOptionPane.showConfirmDialog(
                ventana,
                panelEdicion,
                "Modificar Actividad: " + id,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (resultado == JOptionPane.OK_OPTION) {
                String nuevoNombre = txtModNombre.getText().trim();
                String nuevoCupoTxt = txtModCupo.getText().trim();
                String nuevaEdadMinTxt = txtModEdadMin.getText().trim();

                if (nuevoNombre.isEmpty() || nuevoCupoTxt.isEmpty() || nuevaEdadMinTxt.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Todos los campos deben contener datos validos.",
                        "Campos vacios",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int nuevoCupo;
                int nuevaEdadMin;

                try {
                    nuevoCupo = Integer.parseInt(nuevoCupoTxt);
                    nuevaEdadMin = Integer.parseInt(nuevaEdadMinTxt);

                    if (nuevoCupo <= 0 || nuevaEdadMin < 0) {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "El cupo debe ser mayor a 0 y la edad minima no puede ser negativa.",
                            "Valores invalidos",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Cupo y Edad Minima deben ser valores numericos enteros.",
                        "Formato invalido",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    boolean modificada = controlador.modificarActividad(id, nuevoNombre, nuevoCupo, nuevaEdadMin);

                    if (modificada) {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "Actividad modificada correctamente.",
                            "Operacion exitosa",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        refrescarTablaActividades();
                    } else {
                        JOptionPane.showMessageDialog(
                            ventana,
                            "Error al intentar modificar la actividad.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (CupoMaximoException ex) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        ex.getMessage(),
                        "Cupo Invalido",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        //Evento para desactivar actividad seleccionada (baja logica)
        btnDesactivarActividad.addActionListener(e -> {
            int filaSeleccionada = tablaActividades.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Seleccione una actividad de la tabla para desactivar.",
                    "Seleccion requerida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String id = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTablaActividades.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Esta seguro de que desea desactivar la actividad " + nombre + " (ID: " + id + ")?\n"
                + "La actividad no admitira nuevas reservas ni figurara en los catalogos activos.",
                "Confirmar desactivacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean desactivada = controlador.desactivarActividad(id);
                if (desactivada) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Actividad \"" + nombre + "\" desactivada exitosamente del catalogo activo.",
                        "Operacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refrescarTablaActividades();
                } else {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Error al desactivar la actividad seleccionada.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        //Evento para reactivar actividad inactiva por ID con confirmacion detallada
        btnReactivarActividad.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(
                ventana,
                "Ingrese el ID de la actividad inactiva a reactivar:",
                "Reactivar Actividad",
                JOptionPane.QUESTION_MESSAGE
            );

            if (id == null || id.trim().isEmpty()) {
                return;
            }

            id = id.trim();
            Actividad act = controlador.buscarActividad(id);

            if (act == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "No se encontro ninguna actividad registrada con el ID: " + id,
                    "Actividad no encontrada",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (act.getActivo()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "La actividad \"" + act.getNombre() + "\" ya se encuentra activa en el sistema.",
                    "Actividad ya activa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            //Dialogo de confirmacion con resumen de datos de la actividad encontrada
            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Se encontro la actividad inactiva:\n"
                + "• ID: " + act.getIdActividad() + "\n"
                + "• Nombre: " + act.getNombre() + "\n"
                + "• Tipo: " + act.getTipoActividad() + "\n"
                + "• Cupo Maximo: " + act.getCupoMaximo() + "\n"
                + "• Edad Minima: " + act.getEdadMinima() + "\n\n"
                + "Desea reactivar esta actividad en el sistema?",
                "Confirmar Reactivacion de Actividad",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean reactivada = controlador.activarActividad(id);
            if (reactivada) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Actividad \"" + act.getNombre() + "\" reactivada exitosamente.",
                    "Operacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                refrescarTablaActividades();
            } else {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error al intentar reactivar la actividad.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void actualizarCamposSegunTipoActividad() {
        String tipoSeleccionado = (String) cmbTipoActividad.getSelectedItem();
        if (tipoSeleccionado == null) {
            return;
        }

        boolean esClase = tipoSeleccionado.equals("Clase Grupal");
        boolean esEntrenamiento = tipoSeleccionado.equals("Entrenamiento Libre");
        boolean esEvento = tipoSeleccionado.equals("Evento");

        txtProfesorActividad.setEnabled(esClase);
        chkRequiereAsistencia.setEnabled(esEntrenamiento);
        txtFechaEvento.setEnabled(esEvento);
        txtLugarEvento.setEnabled(esEvento);
        txtTipoEvento.setEnabled(esEvento);

        //Limpieza de valores en los campos que quedan deshabilitados
        if (!esClase) {
            txtProfesorActividad.setText("");
        }
        if (!esEntrenamiento) {
            chkRequiereAsistencia.setSelected(false);
        }
        if (!esEvento) {
            txtFechaEvento.setText("");
            txtLugarEvento.setText("");
            txtTipoEvento.setText("");
        }
    }

    private void limpiarCamposActividad() {
        txtIdActividad.setText("");
        txtNombreActividad.setText("");
        txtCupoActividad.setText("");
        txtEdadMinActividad.setText("");
        cmbTipoActividad.setSelectedIndex(0);
        actualizarCamposSegunTipoActividad();
    }

    private void refrescarTablaActividades() {
        modeloTablaActividades.setRowCount(0);
        if (controlador != null) {
            ArrayList<Actividad> lista = (chkSoloEventos != null && chkSoloEventos.isSelected())
                ? controlador.obtenerEventos()
                : controlador.obtenerActividades();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            for (Actividad a : lista) {
                String detalles = "";
                if (a.getProfesor() != null) {
                    detalles = "Profesor: " + a.getProfesor();
                } else if (a.getRequiereAsistencia() != null) {
                    detalles = "Asistencia: " + (a.getRequiereAsistencia() ? "Obligatoria" : "Opcional");
                } else if (a.esEvento()) {
                    String fechaFmt = (a.getFecha() != null) ? sdf.format(a.getFecha()) : "N/A";
                    detalles = "Tipo: " + a.getTipoEvento() + " | Lugar: " + a.getLugar() + " | Fecha: " + fechaFmt;
                }

                Object[] fila = {
                    a.getIdActividad(),
                    a.getNombre(),
                    a.getTipoActividad(),
                    a.getCupoMaximo(),
                    a.getEdadMinima(),
                    detalles
                };
                modeloTablaActividades.addRow(fila);
            }
        }
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
                "Esta seguro de que desea desactivar al socio " + nombre + " (RUT: " + rut + ")?\n"
                + "El socio no podra realizar nuevas reservas ni figurara en los catalogos activos.",
                "Confirmar desactivacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean desactivado = controlador.desactivarSocio(rut);
                if (desactivado) {
                    JOptionPane.showMessageDialog(
                        ventana,
                        "Socio \"" + nombre + "\" desactivado exitosamente del catalogo activo.",
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

        //Evento para reactivar socio inactivo por RUT con confirmacion detallada
        btnReactivarSocio.addActionListener(e -> {
            String rut = JOptionPane.showInputDialog(
                ventana,
                "Ingrese el RUT del socio inactivo a reactivar:",
                "Reactivar Socio",
                JOptionPane.QUESTION_MESSAGE
            );

            if (rut == null || rut.trim().isEmpty()) {
                return;
            }

            rut = rut.trim();
            Socio socio = controlador.buscarSocio(rut);

            if (socio == null) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "No se encontro ningun socio registrado con el RUT: " + rut,
                    "Socio no encontrado",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (socio.getActivo()) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "El socio " + socio.getNombre() + " ya se encuentra activo en el sistema.",
                    "Socio ya activo",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            //Dialogo de confirmacion con resumen de datos del socio encontrado
            int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "Se encontro el socio inactivo:\n"
                + "• RUT: " + socio.getRut() + "\n"
                + "• Nombre: " + socio.getNombre() + "\n"
                + "• Edad: " + socio.getEdad() + "\n"
                + "• Deuda Pendiente: $" + socio.getDeuda() + "\n"
                + "• Condicion: " + (socio.getEsMoroso() ? "MOROSO" : "AL DIA") + "\n\n"
                + "Desea reactivar a este socio en el sistema?",
                "Confirmar Reactivacion de Socio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            boolean reactivado = controlador.activarSocio(rut);
            if (reactivado) {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Socio " + socio.getNombre() + " reactivado exitosamente.",
                    "Operacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                refrescarTablaSocios();
            } else {
                JOptionPane.showMessageDialog(
                    ventana,
                    "Error al intentar reactivar al socio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
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
            ArrayList<Socio> lista = chkSoloDeudores != null && chkSoloDeudores.isSelected()
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