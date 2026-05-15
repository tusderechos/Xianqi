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

public class Caballo extends Pieza {
    
    public Caballo(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.CABALLO, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        
        int df = fdestino - Fila;
        int dc = cdestino - Col;
        
        //Movimiento asi de L como en ajedrez normal
        if (Math.abs(df) == 2 && Math.abs(dc) == 1) {
            //Se verifica si existe un bloqueo si el paso ortogonal es vertical
            return tablero[Fila + df / 2][Col] == null;
        }
        if (Math.abs(df) == 1 && Math.abs(dc) == 2) {
            //Lo mismo de arriba pero en vez de vertical es horizontal
            return tablero[Fila][Col + dc / 2] == null;
        }
        
        return false;
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "CBR" : "CBN";
    }
}
