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

public class Soldado extends Pieza {
    
    public Soldado(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.SOLDADO, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        
        int df = fdestino - Fila;
        int dc = Math.abs(cdestino - Col);
        
        boolean cruzoelrio = HaCruzadoElRio();
        
        if (Color == ColorPieza.ROJO) {
            //Avanza hacia arriba (la fila decrece)
            if (!cruzoelrio)
                return df == -1 && dc == 0;
            return (df == -1 && dc == 0) || (df == 0 && dc == 1);
        } else {
            //Avanza hacia abajo (fila crece)
            if (!cruzoelrio)
                return df == 1 && dc == 0;
            return (df == 1 && dc == 0) || (df == 0 && dc == 1);
        }
    }
    
    private boolean HaCruzadoElRio() {
        return Color == ColorPieza.ROJO ? Fila < 5 : Fila > 4;
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "SR" : "SN";
    }
}
