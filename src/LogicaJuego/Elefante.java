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

public class Elefante extends Pieza {
    
    public Elefante(ColorPieza Color, int Fila, int Col) {
        super(Color, TipoPieza.ELEFANTE, Fila, Col);
    }
    
    @Override
    public boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero) {
        if (esMismoColor(tablero[fdestino][cdestino]))
            return false;
        if (!DentrodelTablero(fdestino, cdestino))
            return false;
        if (CruzaelRio(fdestino))
            return false;
        
        int df = fdestino - Fila;
        int dc = cdestino - Col;
        
        if (Math.abs(df) != 2 || Math.abs(dc) != 2)
            return false;
        
        //Verificacion del ojo de efelante
        int ojof = Fila + df / 2;
        int ojoc = Col + dc / 2;
        
        return tablero[ojof][ojoc] == null;
    }
    
    private boolean CruzaelRio(int fdestino) {
        return Color == ColorPieza.ROJO ? fdestino < 5 : fdestino > 4;
    }
    
    @Override
    public String getSimbolo() {
        return Color == ColorPieza.ROJO ? "相" : "象";
    }
}
