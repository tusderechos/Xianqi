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

public class Asesor extends PiezaConfinada {
    
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
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "仕" : "士";
    }
}
