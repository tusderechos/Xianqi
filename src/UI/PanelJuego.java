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
import java.util.ArrayList;

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
    
    private JPanel PanelCapturaArriba;
    private JPanel PanelCapturaAbajo;
    private JLabel LblNombreArriba;
    private JLabel LblNombreAbajo;
    
    private JLabel LblTurno;
    private JLabel LblInfo;
    private JButton[][] BtnTablero;
    
    private int FilaSeleccionada = -1;
    private int ColSeleccionada = -1;
    private boolean HaySeleccion = false;
    
    private boolean InicializacionExitosa = false;
    
    private ImageIcon[] CacheImagenes = new ImageIcon[14];
    private String[] CacheKeys = new String[14];
    private int CacheTamano = 0;
    
    public PanelJuego(MemoriaCuentas Memoria, String UsuarioActivo, MenuPrincipal menuPrincipal) {
        this.Memoria = Memoria;
        this.menuPrincipal = menuPrincipal;
        this.UsuarioActivo = (UsuarioActivo == null) ? "" : UsuarioActivo.trim();
        
        if (UsuarioActivo.isEmpty()) {
            MostrarMensaje("Primero inicia sesion", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setTitle("XIANGQI");
        setSize(1000, 820);
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
        btnretirar.addActionListener(e -> onRetirar());
        
        barrainferior.add(btnretirar);
        PanelFondo.add(barrainferior, BorderLayout.SOUTH);
        
        PanelFondo.add(CrearPanelCapturas(), BorderLayout.EAST);
        
        String jugador2 = PedirJugador2();
        if (jugador2 == null)
            return;
        
        Juego = new LogicaXiangqi(UsuarioActivo, jugador2);
        
        RenderizarTablero();
        InicializacionExitosa = true;
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
                } else if (esCeldaPalacio(fila, col)) {
                    boton.setBackground(UIColors.FONDO_PALACIO.getColor());
                } else if (esCeldaRio(fila, col)) {
                    boton.setBackground(UIColors.FONDO_RIO.getColor());
                } else {
                    boton.setBackground(UIColors.FONDO_TABLERO.getColor());
                }
                
                boton.setBorder(BorderFactory.createLineBorder(UIColors.LINEAS.getColor(), 1));
                
                if (pieza == null) {
                    boton.setText("");
                    boton.setIcon(null);
                    continue;
                }
                
                ImageIcon icono = CargarImagenPieza(pieza);
                
                if (icono != null) {
                    boton.setIcon(icono);
                    boton.setText("");
                } else {
                    //Fallback al texto si es que no carga la imagen
                    boton.setIcon(null);
                    boton.setText(pieza.getSimbolo());
                    boton.setForeground(pieza.getColor() == ColorPieza.ROJO ? UIColors.TEXTO_PIEZA_ROJO.getColor() : UIColors.TEXTO_PIEZA_NEGRA.getColor());
                }
            }
        }
        
        DibujarPalacio();
        ActualizarCapturas();
    }
    
    private JPanel CrearPanelCapturas() {
        JPanel panellateral = new JPanel();
        panellateral.setLayout(new BoxLayout(panellateral, BoxLayout.Y_AXIS));
        panellateral.setBackground(UIColors.HEADER_BG.getColor());
        panellateral.setPreferredSize(new Dimension(150, 0));
        panellateral.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 6));
        
        LblNombreArriba = new JLabel("", SwingConstants.CENTER);
        LblNombreArriba.setForeground(new Color(180, 180, 255));
        LblNombreArriba.setFont(new Font("SansSerif", Font.BOLD, 11));
        LblNombreArriba.setAlignmentX(Component.CENTER_ALIGNMENT);
        LblNombreArriba.setMaximumSize(new Dimension(150, 20));
        
        PanelCapturaArriba = new JPanel(new GridLayout(5, 3, 2, 2));
        PanelCapturaArriba.setBackground(new Color(30, 20, 10));
        PanelCapturaArriba.setMaximumSize(new Dimension(150, 280));
        PanelCapturaArriba.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 80, 40), 1), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        
        JLabel lblseparador = new JLabel("-- vs --", SwingConstants.CENTER);
        lblseparador.setForeground(new Color(180, 150, 80));
        lblseparador.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblseparador.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblseparador.setMaximumSize(new Dimension(150, 30));
        
        LblNombreAbajo = new JLabel("", SwingConstants.CENTER);
        LblNombreAbajo.setForeground(UIColors.TEXTO_PIEZA_ROJO.getColor());
        LblNombreAbajo.setFont(new Font("SansSerif", Font.BOLD, 11));
        LblNombreAbajo.setAlignmentX(Component.CENTER_ALIGNMENT);
        LblNombreAbajo.setMaximumSize(new Dimension(150, 20));
        
        PanelCapturaAbajo = new JPanel(new GridLayout(5, 3, 2, 2));
        PanelCapturaAbajo.setBackground(new Color(30, 20, 10));
        PanelCapturaAbajo.setMaximumSize(new Dimension(150, 280));
        PanelCapturaAbajo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 80, 40), 1), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        
        panellateral.add(Box.createVerticalStrut(10));
        panellateral.add(LblNombreArriba);
        panellateral.add(Box.createVerticalStrut(6));
        panellateral.add(PanelCapturaArriba);
        panellateral.add(Box.createVerticalGlue());
        panellateral.add(lblseparador);
        panellateral.add(Box.createVerticalGlue());
        panellateral.add(PanelCapturaAbajo);
        panellateral.add(Box.createVerticalStrut(6));
        panellateral.add(LblNombreAbajo);
        panellateral.add(Box.createVerticalStrut(10));
        
        return panellateral;
    }
    
    private void ActualizarCapturas() {
        Partida partida = Juego.getPartida();
        ColorPieza coloractivo = partida.getColorJugador(UsuarioActivo);
        
        ArrayList<Pieza> capturaarriba = coloractivo == ColorPieza.ROJO ? Juego.getCapturasNegro() : Juego.getCapturasRojo();
        ArrayList<Pieza> capturaabajo = coloractivo == ColorPieza.ROJO ? Juego.getCapturasRojo() : Juego.getCapturasNegro();
        String rival = coloractivo == ColorPieza.ROJO ? partida.getJugador2() : partida.getJugador1();
        
        LblNombreArriba.setText(rival + " capturo: ");
        LblNombreAbajo.setText(UsuarioActivo + " capturo: ");
        
        RefrescarPanelCaptura(PanelCapturaArriba, capturaarriba);
        RefrescarPanelCaptura(PanelCapturaAbajo, capturaabajo);
    }
    
    private void RefrescarPanelCaptura(JPanel panel, ArrayList<Pieza> capturas) {
        panel.removeAll();
        
        for (int i = 0; i < 15; i++) {
            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(50, 35, 15));
            lbl.setBorder(BorderFactory.createLineBorder(new Color(80, 60, 30), 1));
            lbl.setPreferredSize(new Dimension(36, 36));
            
            if (i < capturas.size()) {
                Pieza pieza = capturas.get(i);
                lbl.setText(pieza.getSimbolo());
                lbl.setFont(new Font("Serif", Font.BOLD, 13));
                lbl.setForeground(pieza.getColor() == ColorPieza.ROJO ? UIColors.TEXTO_PIEZA_ROJO.getColor() : UIColors.TEXTO_PIEZA_NEGRA.getColor());
            }
            
            panel.add(lbl);
        }
        
        panel.revalidate();
        panel.repaint();
    }
    
    private boolean esCeldaPalacio(int fila, int col) {
        if (col < 3 || col > 5)
            return false;
        return (fila >= 0 && fila <= 2) || (fila >= 7 && fila <= 9);
    }
    
    private boolean esCeldaRio(int fila, int col) {
        return fila == 4 || fila == 5;
    }
    
    private void DibujarPalacio() {
        //Pinta las dos diagonales del palacio encima de los botones existentes
        PintarDiagonalPalacio(0, 7);
    }
    
    private void PintarDiagonalPalacio(int filainiciopalacionegro, int filainiciopalaciorojo) {
        int[][] palacios = {{filainiciopalacionegro, filainiciopalacionegro + 2}, {filainiciopalaciorojo, filainiciopalaciorojo + 2}};
        
        for (int[] rango : palacios) {
            int finicio = rango[0];
            int ffin = rango[1];
            
            //Diagonal \
            for (int i = 0; i <= 2; i++) {
                MarcarDiagonal(finicio + i, 3 + i);
            }
            
            //Diagonal /
            for (int i = 0; i <= 2; i++) {
                MarcarDiagonal(finicio + i, 5 - i);
            }
        }
    }
    
    private void MarcarDiagonal(int fila, int col) {
        JButton boton = BtnTablero[fila][col];
        
        if (HaySeleccion && fila == FilaSeleccionada && col == ColSeleccionada)
            return;
        if (HaySeleccion && esDestinoValido(fila, col))
            return;
        
        boton.setBackground(UIColors.FONDO_PALACIO_DIAGONAL.getColor());
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
    
    private ImageIcon CargarImagenPieza(Pieza pieza) {
        String llave = pieza.getNOmbreImaen();
        
        for (int i = 0; i < CacheTamano; i++) {
            if (CacheKeys[i].equals(llave))
                return CacheImagenes[i];
        }
        
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/images" + llave));
            Image imagen = icono.getImage().getScaledInstance(52, 52, Image.SCALE_SMOOTH);
            ImageIcon escalado = new ImageIcon(imagen);
            
            if (CacheTamano < 14) {
                CacheKeys[CacheTamano] = llave;
                CacheImagenes[CacheTamano] = escalado;
                CacheTamano++;
            }
            
            return escalado;
        } catch (Exception e) {
            return null;
        }
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