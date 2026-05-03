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

public class Oficial extends Pieza {
    
    public Oficial(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.OFICIAL, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        
        int df = Math.abs(fdestino - Fila);
        int dc = Math.abs(cdestino - Col);
        
        if (df != 1 || dc != 1)
            return false;
        
        //Aqui simplemente "desbloqueo" el paso ortogonal intermedio
        return tablero[Fila][cdestino] == null || tablero[fdestino][Col] == null;
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "傌" : "馬";
    }
}
