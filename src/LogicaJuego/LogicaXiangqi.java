/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

/**
 *
 * @author USUARIO
 */

import enums.*;
import java.util.ArrayList;

public class LogicaXiangqi {
    
    private Pieza[][] Tablero;
    private Partida partida;
    
    private ArrayList<Pieza> CapturasRojo;
    private ArrayList<Pieza> CapturasNegro;
    
    public LogicaXiangqi(String Jugador1, String Jugador2) {
        this.Tablero = new Pieza[10][9];
        this.partida = new Partida(Jugador1, Jugador2);
        
        CapturasRojo = new ArrayList<>();
        CapturasNegro = new ArrayList<>();
        
        //Creacion y posicionamiento de piezas
        
        //Fichas Negras
        Tablero[0][0] = new Carro(ColorPieza.NEGRO, 0, 0);
        Tablero[0][1] = new Caballo(ColorPieza.NEGRO, 0, 1);
        Tablero[0][2] = new Elefante(ColorPieza.NEGRO, 0, 2);
        Tablero[0][3] = new Asesor(ColorPieza.NEGRO, 0, 3);
        Tablero[0][4] = new General(ColorPieza.NEGRO, 0, 4);
        Tablero[0][5] = new Asesor(ColorPieza.NEGRO, 0, 5);
        Tablero[0][6] = new Elefante(ColorPieza.NEGRO, 0, 6);
        Tablero[0][7] = new Caballo(ColorPieza.NEGRO, 0, 7);
        Tablero[0][8] = new Carro(ColorPieza.NEGRO, 0, 8);
        
        Tablero[2][1] = new Canon(ColorPieza.NEGRO, 2, 1);
        Tablero[2][7] = new Canon(ColorPieza.NEGRO, 2, 7);
        
        Tablero[3][0] = new Soldado(ColorPieza.NEGRO, 3, 0);
        Tablero[3][2] = new Soldado(ColorPieza.NEGRO, 3, 2);
        Tablero[3][4] = new Soldado(ColorPieza.NEGRO, 3, 4);
        Tablero[3][6] = new Soldado(ColorPieza.NEGRO, 3, 6);
        Tablero[3][8] = new Soldado(ColorPieza.NEGRO, 3, 8);
        
        //Fichas Rojas
        Tablero[9][0] = new Carro(ColorPieza.ROJO, 9, 0);
        Tablero[9][1] = new Caballo(ColorPieza.ROJO, 9, 1);
        Tablero[9][2] = new Elefante(ColorPieza.ROJO, 9, 2);
        Tablero[9][3] = new Asesor(ColorPieza.ROJO, 9, 3);
        Tablero[9][4] = new General(ColorPieza.ROJO, 9, 4);
        Tablero[9][5] = new Asesor(ColorPieza.ROJO, 9, 5);
        Tablero[9][6] = new Elefante(ColorPieza.ROJO, 9, 6);
        Tablero[9][7] = new Caballo(ColorPieza.ROJO, 9, 7);
        Tablero[9][8] = new Carro(ColorPieza.ROJO, 9, 8);
        
        Tablero[7][1] = new Canon(ColorPieza.ROJO, 7, 1);
        Tablero[7][7] = new Canon(ColorPieza.ROJO, 7, 7);
        
        Tablero[6][0] = new Soldado(ColorPieza.ROJO, 6, 0);
        Tablero[6][2] = new Soldado(ColorPieza.ROJO, 6, 2);
        Tablero[6][4] = new Soldado(ColorPieza.ROJO, 6, 4);
        Tablero[6][6] = new Soldado(ColorPieza.ROJO, 6, 6);
        Tablero[6][8] = new Soldado(ColorPieza.ROJO, 6, 8);
    }
    
    public ResultadoMovimiento Mover(String usuario, int forigen, int corigen, int fdestino, int cdestino) {
        if (!partida.isActiva())
            return ResultadoMovimiento.JUEGO_TERMINADO;
        if (!partida.getJugadorTurno().equals(usuario))
            return ResultadoMovimiento.NO_ES_TU_PIEZA;
        
        Pieza pieza = Tablero[forigen][corigen];
        
        if (pieza == null)
            return ResultadoMovimiento.CELDA_VACIA;
        
        ColorPieza colorjugador = partida.getColorJugador(usuario);
        
        if (pieza.getColor() != colorjugador)
            return ResultadoMovimiento.NO_ES_TU_PIEZA;
        
        if (!pieza.esMovimientoValido(fdestino, cdestino, Tablero))
            return ResultadoMovimiento.MOVIMIENTO_INVALIDO;
        
        Pieza capturada = Tablero[fdestino][cdestino];
        
        Tablero[fdestino][cdestino] = pieza;
        Tablero[forigen][corigen] = null;
        pieza.setFila(fdestino);
        pieza.setCol(cdestino);
        
        if (capturada != null) {
            if (capturada.getColor() == ColorPieza.NEGRO) {
                CapturasRojo.add(capturada);
            } else {
                CapturasNegro.add(capturada);
            }
            
            if (capturada.getTipo() == TipoPieza.GENERAL) {
                partida.TerminarPorCaptura();
                return ResultadoMovimiento.GANO;
            }
        }
        
        partida.CambiarTurno();
        return ResultadoMovimiento.OK;
    }
    
    public ResultadoMovimiento Retirar(String usuario) {
        if (!partida.isActiva())
            return ResultadoMovimiento.JUEGO_TERMINADO;
        if (!partida.getJugadorTurno().equals(usuario))
            return ResultadoMovimiento.NO_ES_TU_PIEZA;
        
        partida.TerminarPorRetiro();
        return ResultadoMovimiento.RETIRO;
    }
    
    public Pieza BuscarPieza(TipoPieza tipo, ColorPieza color, int indice) {
        if (indice >= 90) 
            return null;
        
        int fila = indice / 9;
        int col = indice % 9;
        Pieza pieza = Tablero[fila][col];
        
        if (pieza != null && pieza.getTipo() == tipo && pieza.getColor() == color)
            return pieza;
        
        return BuscarPieza(tipo, color, indice + 1);
    }
    
    public int ContarPiezas(ColorPieza color, int indice) {
        if (indice >= 90)
            return 0;
        
        int fila = indice / 9;
        int col = indice % 9;
        int suma = (Tablero[fila][col] != null && Tablero[fila][col].getColor() == color) ? 1 : 0;
        
        return suma + ContarPiezas(color, indice + 1);
    }

    public Pieza[][] getTablero() {
        return Tablero;
    }

    public Partida getPartida() {
        return partida;
    }
    
    public Pieza getPieza(int fila, int col) {
        if (fila < 0 || fila >= 10 || col < 0 || col >= 9)
            return null;
        
        return Tablero[fila][col];
    }
    
    public String getJugadorTurno() {
        return partida.getJugadorTurno();
    }
    public boolean isActiva() {
        return partida.isActiva();
    }
    public String getResultadoFinal() {
        return partida.getResultadoFinal();
    }
    public String getGanador() {
        return partida.getGanador();
    }
    public String getPerdedor() {
        return partida.getPerdedor();
    }

    public ArrayList<Pieza> getCapturasRojo() {
        return CapturasRojo;
    }

    public ArrayList<Pieza> getCapturasNegro() {
        return CapturasNegro;
    }
    
}
