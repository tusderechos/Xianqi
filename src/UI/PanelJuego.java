/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

/**
 *
 * @author USUARIO
 */

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import ManejoCuentas.MemoriaCuentas;
import LogicaJuego.LogicaXiangqi;
import LogicaJuego.Partida;
import LogicaJuego.Pieza;
import enums.UIColors;
import enums.ColorPieza;
import enums.ResultadoMovimiento;

public class PanelJuego extends JFrame {
    
    private final MemoriaCuentas Memoria;
    private final MenuPrincipal menuPrincipal;
    private final String UsuarioActivo;
    
    private LogicaXiangqi Juego;
    
    private JLabel LblTurno;
    private JLabel LblInfo;
    private JButton[][] BtnTablero;
    
    private int FilaSeleccionada = -1;
    private int ColSeleccionada = -1;
    private boolean HaySeleccion = false;
    
    private boolean InicializacionExitosa = false;
    
    public PanelJuego(MemoriaCuentas Memoria, String UsuarioActivo, MenuPrincipal menuPrincipal) {
        this.Memoria = Memoria;
        this.menuPrincipal = menuPrincipal;
        this.UsuarioActivo = (UsuarioActivo == null) ? "" : UsuarioActivo.trim();
        
        if (UsuarioActivo.isEmpty()) {
            MostrarMensaje("Primero inicia sesion", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String jugador2 = PedirJugador2();
        if (jugador2 == null)
            return;
        
        Juego = new LogicaXiangqi(UsuarioActivo, jugador2);
        
        setTitle("XIANGQI");
        setSize(720, 900);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(menuPrincipal);
        
        JPanel PanelFondo = new JPanel(new BorderLayout());
        PanelFondo.setBackground(UIColors.FONDO_PANEL.getColor());
        
        setContentPane(PanelFondo);
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIColors.HEADER_BG.getColor());
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        
        JLabel lbltitulo = new JLabel("XIANGQI", SwingConstants.CENTER);
        lbltitulo.setForeground(UIColors.TEXTO_HEADER.getColor());
        lbltitulo.setFont(new Font("Serif", Font.BOLD, 40));
        lbltitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        LblTurno = new JLabel("", SwingConstants.CENTER);
        LblTurno.setForeground(Color.WHITE);
        LblTurno.setFont(new Font("Serif", Font.BOLD, 18));
        LblTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        LblInfo = new JLabel(" ", SwingConstants.CENTER);
        LblInfo.setForeground(new Color(200, 200, 200));
        LblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        LblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(lbltitulo);
        header.add(Box.createVerticalStrut(6));
        header.add(LblTurno);
        header.add(Box.createVerticalStrut(4));
        header.add(LblInfo);
        
        PanelFondo.add(header, BorderLayout.NORTH);
        
        JPanel PanelTablero = new JPanel(new GridLayout(10, 9, 2, 2));
        PanelTablero.setBackground(UIColors.LINEAS.getColor());
        PanelTablero.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.LINEAS.getColor(), 3), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        
        BtnTablero = new JButton[10][9];
        
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 9; col++) {
                JButton boton = new JButton();
                boton.setFont(new Font("Serif", Font.BOLD, 18));
                boton.setMargin(new Insets(0, 0, 0, 0));
                boton.setFocusPainted(false);
                boton.setOpaque(true);
                boton.setBackground(UIColors.FONDO_TABLERO.getColor());
                boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                final int FF = fila;
                final int FC = col;
                boton.addActionListener(e -> onClickCelda(FF, FC));
                
                BtnTablero[fila][col] = boton;
                PanelTablero.add(boton);
            }
        }
        
        JPanel tablerowrap = new JPanel(new BorderLayout());
        tablerowrap.setBackground(UIColors.FONDO_PANEL.getColor());
        tablerowrap.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        tablerowrap.add(PanelTablero, BorderLayout.CENTER);
        
        PanelFondo.add(tablerowrap, BorderLayout.CENTER);
        
        JPanel barrainferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10));
        barrainferior.setBackground(UIColors.HEADER_BG.getColor());
        
        JButton btnretirar = new JButton("RETIRAR");
        EstilizarBoton(btnretirar);
        
        barrainferior.add(btnretirar);
        PanelFondo.add(barrainferior, BorderLayout.SOUTH);
        
        RenderizarTablero();
    }
    
    private void RenderizarTablero() {
        ActualizarLabels();
        
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 9; col++) {
                JButton boton = BtnTablero[fila][col];
                Pieza pieza = Juego.getPieza(fila, col);
                
                //Color de fondo de celda
                if (HaySeleccion && fila == FilaSeleccionada && col == ColSeleccionada) {
                    boton.setBackground(UIColors.CELDA_SELECCION.getColor());
                } else if (HaySeleccion && esDestinoValido(fila, col)) {
                    boton.setBackground(UIColors.CELDA_DESTINO.getColor());
                } else {
                    boton.setBackground(UIColors.FONDO_TABLERO.getColor());
                }
                
                //Lo del rio
                if (fila == 4 || fila == 5) {
                    boton.setBorder(BorderFactory.createMatteBorder(fila == 4 ? 1 : 2, 1, fila == 4 ? 2 : 1, 1, new Color(40, 80, 160, 120)));
                } else {
                    boton.setBorder(BorderFactory.createLineBorder(UIColors.LINEAS.getColor(), 1));
                }
                
                if (pieza == null) {
                    boton.setText("");
                    continue;
                }
                
                boton.setText(pieza.getSimbolo());
                boton.setForeground(pieza.getColor() == ColorPieza.ROJO ? UIColors.TEXTO_PIEZA_ROJO.getColor() : UIColors.TEXTO_PIEZA_ROJO.getColor());
            }
        }
    }
    
    private boolean esDestinoValido(int fdestino, int cdestino) {
        if (!HaySeleccion)
            return false;
        
        Pieza pieza = Juego.getPieza(FilaSeleccionada, ColSeleccionada);
        
        if (pieza == null)
            return false;
        
        return pieza.esMovimientoValido(fdestino, cdestino, Juego.getTablero());
    }
    
    private void ActualizarLabels() {
        Partida partida = Juego.getPartida();
        String turno = Juego.getJugadorTurno();
        ColorPieza color = partida.getColorJugador(turno);
        String colorstr = color == ColorPieza.ROJO ? "ROJO" : "NEGRO";
        
        LblTurno.setText("Turno: " + turno + " (" + colorstr + ")");
        LblTurno.setForeground(color == ColorPieza.ROJO ? UIColors.PIEZA_ROJO.getColor() : new Color(180, 180, 255));
    }
    
    private void setInfo(String mensaje) {
        LblInfo.setText(mensaje);
    }
    
    private void LimpiarSeleccion() {
        FilaSeleccionada = -1;
        ColSeleccionada = -1;
        HaySeleccion = false;
        setInfo(" ");
    }
    
    private void RegistrarResultado(String ganador, String perdedor) {
        Memoria.SumarPuntos(ganador, 3);
        
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        
        Memoria.AgregarLog(ganador, fecha, perdedor, Juego.getResultadoFinal());
        Memoria.AgregarLog(perdedor, fecha, ganador, Juego.getResultadoFinal());
    }
    
    private void onClickCelda(int fila, int col) {
        if (!Juego.isActiva())
            return;
        
        Pieza piezadestino = Juego.getPieza(fila, col);
        ColorPieza coloractivo = Juego.getPartida().getColorJugador(UsuarioActivo);
        String turnoactual = Juego.getJugadorTurno();
        
        if (!HaySeleccion) {
            //Seleccionar pieza
            if (piezadestino == null)
                return;
            if (piezadestino.getColor() != Juego.getPartida().getColorJugador(turnoactual)) {
                setInfo("No es tu pieza");
                return;
            }
            
            FilaSeleccionada = fila;
            ColSeleccionada = col;
            HaySeleccion = true;
            RenderizarTablero();
            return;
        }
        
        //Ya hay seleccion - intentar mover
        if (fila == FilaSeleccionada && col == ColSeleccionada) {
            //Deseleccionar
            LimpiarSeleccion();
            RenderizarTablero();
            return;
        }
        
        if (piezadestino != null && piezadestino.getColor() == Juego.getPartida().getColorJugador(turnoactual)) {
            FilaSeleccionada = fila;
            ColSeleccionada = col;
            RenderizarTablero();
            return;
        }
        
        //Intentar mover
        ResultadoMovimiento resultado = Juego.Mover(turnoactual, FilaSeleccionada, ColSeleccionada, fila, col);
        LimpiarSeleccion();
        
        switch (resultado) {
            case OK:
                RenderizarTablero();
                break;
            case GANO:
                RenderizarTablero();
                onGanar();
                break;
            case MOVIMIENTO_INVALIDO:
                setInfo("Movimiento invalido");
                RenderizarTablero();
                break;
            default:
                RenderizarTablero();
                break;
        }
    }
    
    private void onRetirar() {
        if (!Juego.isActiva())
            return;
        
        int opcion = MostrarConfirmacion("Seguro que " + Juego.getJugadorTurno() + " quiere retirarse?\nEl rival ganara", "Retiro");
        
        if (opcion != JOptionPane.YES_OPTION)
            return;
        
        ResultadoMovimiento resultado = Juego.Retirar(Juego.getJugadorTurno());
        
        if (resultado == ResultadoMovimiento.RETIRO) {
            String ganador = Juego.getGanador();
            String perdedor = Juego.getPerdedor();
            
            MostrarMensaje(perdedor + " SE HA RETIRADO\n" + ganador + " GANA\n\n(+3 puntos)", "Fin de Partida", JOptionPane.INFORMATION_MESSAGE);
            
            RegistrarResultado(ganador, perdedor);
            Volver();
        }
    }
    
    private void onGanar() {
        String ganador = Juego.getGanador();
        String perdedor = Juego.getPerdedor();
        
        MostrarMensaje(ganador + " VENCIO A " + perdedor + "\nFELICIDADES!\n\n(+3 puntos)", "Fin de Partida", JOptionPane.INFORMATION_MESSAGE);
        
        RegistrarResultado(ganador, perdedor);
        Volver();
    }
    
    private String PedirJugador2() {
        String[] activos = Memoria.getUsuariosActivos();
        
        //Filtrar al usuario activo de la lista
        int conteo = 0;
        
        for (String usu : activos) {
            if (!usu.equals(UsuarioActivo))
                conteo++;
        }
        
        if (conteo == 0) {
            MostrarMensaje("No hay otros jugadores registrados", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        String[] opciones = new String[conteo];
        int conteo2 = 0;
        
        for (String usu : activos) {
            if (!usu.equals(UsuarioActivo))
                opciones[conteo2++] = usu;
        }
        
        JComboBox<String> combo = new JComboBox<>(opciones);
        EstilizarBoton(combo);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIColors.HEADER_BG.getColor());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        JLabel lbl = new JLabel("Selecciona al jugador 2:");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        combo.setMaximumSize(new Dimension(260, 36));
        
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(12));
        panel.add(combo);
        
        UIManager.put("OptionPane.background", UIColors.HEADER_BG.getColor());
        UIManager.put("Panel.background", UIColors.HEADER_BG.getColor());
        
        int respuesta = JOptionPane.showConfirmDialog(this, panel, "Nueva Partida", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        
        if (respuesta != JOptionPane.OK_OPTION) {
            return null;
        }
        
        return (String) combo.getSelectedItem();
    }
    
    private void Volver() {
        dispose();
        
        if (menuPrincipal != null)
            menuPrincipal.setVisible(true);
    }
    
    private void EstilizarBoton(JComponent compo) {
        compo.setFont(new Font("SansSerif", Font.BOLD, 14));
        compo.setBackground(UIColors.FONDO_PANEL.getColor());
        compo.setForeground(UIColors.TEXTO_HEADER.getColor());
        compo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.TEXTO_HEADER.getColor(), 1), BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        compo.setOpaque(true);
        compo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void MostrarMensaje(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    private int MostrarConfirmacion(String msg, String titulo) {
        return JOptionPane.showConfirmDialog(this, msg, titulo, JOptionPane.YES_NO_OPTION);
    }
    
    public boolean isInicializacionExitosa() {
        return InicializacionExitosa;
    }

}
