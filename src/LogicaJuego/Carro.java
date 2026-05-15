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

public class Carro extends Pieza {
    
    public Carro(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.CARRO, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        if (Fila == fdestino && Col == cdestino)
            return false;
        
        if (Fila == fdestino) {
            return ContarPiezasEntreHorizontales(Fila, Col, cdestino, tablero) == 0;
        }
        if (Col == cdestino) {
            return ContarPiezasEntreVertical(Fila, fdestino, Col, tablero) == 0;
        }
        
        return false;
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "CR" : "CN";
    }
}
