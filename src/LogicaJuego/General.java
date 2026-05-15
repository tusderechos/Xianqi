/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJuego;

/**
 *
 * @author USUARIO
 */

import enums.ColorPieza;
import enums.TipoPieza;

public class General extends PiezaConfinada {
    
    public General(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.GENERAL, Fila, Col);
    }
    
    @Override
    public final boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelPalacio(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        
        int df = Math.abs(fdestino - Fila);
        int dc = Math.abs(cdestino - Col);
        
        //Un paso ortogonal
        return (df == 1 && dc == 0) || (df == 0 && dc == 1);
    }
    
    @Override
    public final String getSimbolo() {
        return Color == ColorPieza.ROJO ? "GR" : "GN";
    }
    
    @Override
    public boolean esFinal() {
        return true;
    }
}
