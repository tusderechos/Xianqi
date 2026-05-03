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

public class Canon extends Pieza {
    
    public Canon(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.CANON, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (Fila == fdestino && Col == cdestino)
            return false;
        
        boolean eslinea = Fila == fdestino || Col == cdestino;
        if (eslinea)
            return false;
        
        Pieza destino = tablero[fdestino][cdestino];
        int pantallas = Fila == fdestino ? ContarPiezasEntreHorizontales(Fila, Col, cdestino, tablero) : ContarPiezasEntreVertical(Fila, fdestino, Col, tablero);
        
        if (destino == null) {
            //Movimiento sin captura: ninguna pieza intermedia
            return pantallas == 0;
        } else {
            //Si hay una captura pues, se captura la otra pieza, que mas decir
            return pantallas == 1 && !esMismoColor(destino);
        }
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "兵" : "卒";
    }
}
