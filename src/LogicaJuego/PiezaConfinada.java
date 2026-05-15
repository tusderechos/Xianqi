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

public class PiezaConfinada extends Pieza {
    
    public PiezaConfinada(ColorPieza color, TipoPieza tipo, int fila, int col) {
        super(color, tipo, fila, col);
    }
    
    protected boolean DentrodelPalacio(int fila, int col) {
        if (col < 3 || col > 5) 
            return false;
        return Color == ColorPieza.ROJO ? (fila >= 7 && fila <= 9) : (fila >= 0 && fila <= 2);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        return false;
    }
    
    @Override
    public String getSimbolo() {
        return "";
    }
}
