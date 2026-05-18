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
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setSize(1180, 860);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(menuPrincipal);
        
        JPanel PanelFondo = new JPanel(new BorderLayout());
        PanelFondo.setBackground(UIColors.FONDO_PANEL.getColor());
        setContentPane(PanelFondo);
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(UIColors.HEADER_BG.getColor());
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, UIColors.ORO_DECORATIVO.getColor()), BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        
        JLabel lbltitulo = new JLabel("XIANGQI", SwingConstants.CENTER);
        lbltitulo.setForeground(UIColors.TEXTO_HEADER.getColor());
        lbltitulo.setFont(new Font("Palatino Linotype", Font.BOLD, 44));
        lbltitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        LblTurno = new JLabel("", SwingConstants.CENTER);
        LblTurno.setForeground(Color.WHITE);
        LblTurno.setFont(new Font("Palatino Linotype", Font.BOLD, 20));
        LblTurno.setOpaque(true);
        LblTurno.setBackground(new Color(70, 40, 12));
        LblTurno.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.ORO_DECORATIVO.getColor(), 1), BorderFactory.createEmptyBorder(5, 18, 5, 18)));
        LblTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        LblInfo = new JLabel("Selecciona una pieza para mover", SwingConstants.CENTER);
        LblInfo.setForeground(new Color(225, 200, 140));
        LblInfo.setFont(new Font("Palatino Linotype", Font.PLAIN, 15));
        LblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(lbltitulo);
        header.add(Box.createVerticalStrut(8));
        header.add(LblTurno);
        header.add(Box.createVerticalStrut(6));
        header.add(LblInfo);
        
        PanelFondo.add(header, BorderLayout.NORTH);
        
        JPanel PanelTablero = new JPanel(new GridLayout(10, 9, 2, 2));
        PanelTablero.setBackground(UIColors.LINEAS.getColor());
        PanelTablero.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.LINEAS.getColor(), 4), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        BtnTablero = new JButton[10][9];
        
        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 9; col++) {
                JButton boton = new JButton();
                boton.setFont(new Font("Palatino Linotype", Font.BOLD, 18));
                boton.setMargin(new Insets(0, 0, 0, 0));
                boton.setFocusPainted(false);
                boton.setContentAreaFilled(true);
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
        tablerowrap.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));
        tablerowrap.add(PanelTablero, BorderLayout.CENTER);
        
        PanelFondo.add(tablerowrap, BorderLayout.CENTER);
        
        JPanel barrainferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        barrainferior.setBackground(UIColors.HEADER_BG.getColor());
        barrainferior.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, UIColors.ORO_DECORATIVO.getColor()));
        
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
                } else {
                    boton.setBackground(UIColors.FONDO_TABLERO.getColor());
                }
                
                boton.setBorder(CrearBordeCelda(fila, col));
                
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
    
    private Border CrearBordeCelda(int fila, int col) {
        Border bordebase = BorderFactory.createLineBorder(UIColors.LINEAS.getColor());
        
        if (fila == 4) {
            Border lineario = BorderFactory.createMatteBorder(0, 0, 5, 0, UIColors.RIO_GRADIENTE_IZQ.getColor());
            return BorderFactory.createCompoundBorder(bordebase, lineario);
        }
        
        return bordebase;
    }
    
    private JPanel CrearPanelCapturas() {
        JPanel panellateral = new JPanel();
        panellateral.setLayout(new BoxLayout(panellateral, BoxLayout.Y_AXIS));
        panellateral.setBackground(UIColors.HEADER_BG.getColor());
        panellateral.setPreferredSize(new Dimension(185, 0));
        panellateral.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, UIColors.LINEAS.getColor()), BorderFactory.createEmptyBorder(14, 10, 14, 10)));
        
        LblNombreArriba = new JLabel("", SwingConstants.CENTER);
        LblNombreArriba.setForeground(new Color(210, 210, 255));
        LblNombreArriba.setFont(new Font("Palatino Linotype", Font.BOLD, 13));
        LblNombreArriba.setAlignmentX(Component.CENTER_ALIGNMENT);
        LblNombreArriba.setMaximumSize(new Dimension(170, 40));
        
        PanelCapturaArriba = new JPanel(new GridLayout(5, 3, 3, 3));
        PanelCapturaArriba.setBackground(UIColors.GRID_CAPTURA_FONDO.getColor());
        PanelCapturaArriba.setMaximumSize(new Dimension(165, 245));
        PanelCapturaArriba.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.GRID_CAPTURA_FONDO.getColor(), 2), BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        
        JLabel lblseparador = new JLabel("-- vs --", SwingConstants.CENTER);
        lblseparador.setForeground(UIColors.TEXTO_HEADER.getColor());
        lblseparador.setFont(new Font("Palatino Linotype", Font.BOLD, 14));
        lblseparador.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblseparador.setMaximumSize(new Dimension(170, 35));
        
        LblNombreAbajo = new JLabel("", SwingConstants.CENTER);
        LblNombreAbajo.setForeground(UIColors.TEXTO_PIEZA_ROJO.getColor());
        LblNombreAbajo.setFont(new Font("Palatino Linotype", Font.BOLD, 13));
        LblNombreAbajo.setAlignmentX(Component.CENTER_ALIGNMENT);
        LblNombreAbajo.setMaximumSize(new Dimension(170, 40));
        
        PanelCapturaAbajo = new JPanel(new GridLayout(5, 3, 3, 3));
        PanelCapturaAbajo.setBackground(UIColors.GRID_CAPTURA_FONDO.getColor());
        PanelCapturaAbajo.setMaximumSize(new Dimension(165, 245));
        PanelCapturaAbajo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.CELDA_CAPTURA_BORDE.getColor(), 2), BorderFactory.createEmptyBorder(6, 6, 6, 6)));
        
        panellateral.add(Box.createVerticalStrut(8));
        panellateral.add(LblNombreArriba);
        panellateral.add(Box.createVerticalStrut(8));
        panellateral.add(PanelCapturaArriba);
        panellateral.add(Box.createVerticalGlue());
        panellateral.add(lblseparador);
        panellateral.add(Box.createVerticalGlue());
        panellateral.add(PanelCapturaAbajo);
        panellateral.add(Box.createVerticalStrut(8));
        panellateral.add(LblNombreAbajo);
        panellateral.add(Box.createVerticalStrut(8));
        
        return panellateral;
    }
    
    private void ActualizarCapturas() {
        Partida partida = Juego.getPartida();
        ColorPieza coloractivo = partida.getColorJugador(UsuarioActivo);
        
        ArrayList<Pieza> piezasmuertasrival;
        ArrayList<Pieza> piezasmuertasmias;
        
        String rival = coloractivo == ColorPieza.ROJO ? partida.getJugador2() : partida.getJugador1();
        
        if (coloractivo == ColorPieza.ROJO) {
            piezasmuertasrival = Juego.getCapturasNegro();
            piezasmuertasmias = Juego.getCapturasRojo();
        } else {
            piezasmuertasrival = Juego.getCapturasRojo();
            piezasmuertasmias = Juego.getCapturasNegro();
        }
        
        LblNombreArriba.setText("<html><center>" + rival + "<br>capturo</center></html>");
        LblNombreAbajo.setText("<html><center>" + UsuarioActivo + "<br>capturo</center></html>");
        
        RefrescarPanelCaptura(PanelCapturaArriba, piezasmuertasrival);
        RefrescarPanelCaptura(PanelCapturaAbajo, piezasmuertasmias);
    }
    
    private void RefrescarPanelCaptura(JPanel panel, ArrayList<Pieza> capturas) {
        panel.removeAll();
        
        for (int i = 0; i < 15; i++) {
            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(UIColors.CELDA_CAPTURA_FONDO.getColor());
            lbl.setPreferredSize(new Dimension(42, 42));
            lbl.setFont(new Font("Palatino Linotype", Font.BOLD, 18));
            lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.CELDA_CAPTURA_BORDE.getColor(), 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

            if (i < capturas.size()) {
                Pieza pieza = capturas.get(i);
                lbl.setText(pieza.getSimbolo());
                
                if (pieza.getColor() == ColorPieza.ROJO) {
                    lbl.setForeground(new Color(235, 45, 35));
                    lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 55, 45), 2), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
                } else {
                    lbl.setForeground(new Color(245, 245, 195));
                    lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 210, 160), 2), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
                }
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
//    
//    private boolean esCeldaRio(int fila, int col) {
//        return fila == 4 || fila == 5;
//    }
    
    private void DibujarPalacio() {
        //Pinta las dos diagonales del palacio encima de los botones existentes
        PintarDiagonalPalacio(0, 7);
    }
    
    private void PintarDiagonalPalacio(int filainiciopalacionegro, int filainiciopalaciorojo) {
        int[][] palacios = {{filainiciopalacionegro, filainiciopalacionegro + 2}, {filainiciopalaciorojo, filainiciopalaciorojo + 2}};
        
        for (int[] rango : palacios) {
            int finicio = rango[0];
            
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
        
        if (color == ColorPieza.ROJO) {
            LblTurno.setText("TURNO DE " + turno.toUpperCase() + " • ROJO");
            LblTurno.setForeground(new Color(255, 95, 80));
            LblTurno.setBackground(new Color(70, 25, 15));
        } else {
            LblTurno.setText("TURNO DE " + turno.toUpperCase() + " • NEGRO");
            LblTurno.setForeground(new Color(210, 210, 255));
            LblTurno.setBackground(new Color(30, 28, 45));
        }
    }
    
    private void setInfo(String mensaje) {
        LblInfo.setText(mensaje);
    }
    
    private void LimpiarSeleccion() {
        FilaSeleccionada = -1;
        ColSeleccionada = -1;
        HaySeleccion = false;
        setInfo("Selecciona una pieza para mover");
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
            setInfo("Pieza seleccionada. Elige una casilla de destino");
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
            setInfo("Nueva pieza seleccionada");
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
        lbl.setForeground(UIColors.TEXTO_HEADER.getColor());
        lbl.setFont(new Font("Palatino Linotype", Font.BOLD, 16));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);
        combo.setMaximumSize(new Dimension(260, 38));
        
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
    
    private void EstilizarBoton(JComponent boton) {
        boton.setFont(new Font("Palatino Linotype", Font.BOLD, 16));
        boton.setBackground(UIColors.BOTON_FONDO.getColor());
        boton.setForeground(UIColors.TEXTO_HEADER.getColor());
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(160, 110, 40), 2), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(220, 44));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(UIColors.BOTON_HOVER_FONDO.getColor());
                boton.setForeground(UIColors.BOTON_HOVER_TEXTO.getColor());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(UIColors.BOTON_FONDO.getColor());
                boton.setForeground(UIColors.TEXTO_HEADER.getColor());
            }
        });
    }
    
    private void MostrarMensaje(String mensaje, String titulo, int tipo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(58, 34, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(160, 110, 40), 3), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel lblmensaje = new JLabel("<html><div style='text-align: center; width: 250px;'>" + mensaje.replace("\n", "<br>") + "</div></html>");
        lblmensaje.setForeground(UIColors.TEXTO_HEADER.getColor());
        lblmensaje.setFont(new Font("Palatino Linotype", Font.BOLD, 16));
        lblmensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblmensaje);
        
        UIManager.put("OptionPane.background", UIColors.FONDO_PANEL.getColor());
        UIManager.put("Panel.background", UIColors.FONDO_PANEL.getColor());
        UIManager.put("OptionPane.messageForeground", UIColors.TEXTO_HEADER.getClass());
        UIManager.put("Button.background", UIColors.BOTON_FONDO.getColor());
        UIManager.put("Button.foreground", UIColors.TEXTO_HEADER.getColor());
        UIManager.put("Button.font", new Font("Palatino Linotype", Font.BOLD, 14));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(160, 110, 40), 1), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        JOptionPane.showMessageDialog(this, panel, titulo, tipo);
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.font", null);
        UIManager.put("Button.border", null);
    }
    
    private int MostrarConfirmacion(String mensaje, String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(58, 34, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(160, 110, 40), 3), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        JLabel lblmensaje = new JLabel("<html><div style='text-align: center; width: 250px;'>" + mensaje.replace("\n", "<br>") + "</div></html>");
        lblmensaje.setForeground(new Color(230, 190, 100));
        lblmensaje.setFont(new Font("Palatino Linotype", Font.BOLD, 16));
        lblmensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblmensaje);
        
        UIManager.put("OptionPane.background", UIColors.FONDO_PANEL.getColor());
        UIManager.put("Panel.background", UIColors.FONDO_PANEL.getColor());
        UIManager.put("OptionPane.messageForeground", UIColors.TEXTO_HEADER.getClass());
        UIManager.put("Button.background", UIColors.BOTON_FONDO.getColor());
        UIManager.put("Button.foreground", UIColors.TEXTO_HEADER.getColor());
        UIManager.put("Button.font", new Font("Palatino Linotype", Font.BOLD, 14));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(160, 110, 40), 1), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        int resultado = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.font", null);
        UIManager.put("Button.border", null);
        
        return resultado;
    }
    
    public boolean isInicializacionExitosa() {
        return InicializacionExitosa;
    }
}