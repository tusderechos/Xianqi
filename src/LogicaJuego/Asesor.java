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

public class Asesor extends Pieza {
    
    public Asesor(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.ASESOR, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelPalacio(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        
        int df = Math.abs(fdestino - Fila);
        int dc = Math.abs(cdestino - Col);
        
        return df == 1 && dc == 1;
    }
    
    private boolean DentrodelPalacio(int fila, int col) {
        if (col < 3 || col < 5)
            return false;
        return Color == ColorPieza.ROJO ? (fila >= 7 && fila <= 9) : (fila >= 0 && fila <= 2);
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "仕" : "士";
    }
}
