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

public abstract class Pieza {
    
    protected ColorPieza Color;
    protected TipoPieza Tipo;
    protected int Fila;
    protected int Col;
    
    public Pieza(ColorPieza Color, TipoPieza Tipo, int Fila, int Col) {
        this.Color = Color;
        this.Tipo = Tipo;
        this.Fila = Fila;
        this.Col = Col;
    }
    
    public abstract boolean esMovimientoValido(int fdestino, int cdestino, Pieza[][] tablero);
    
    public abstract String getSimbolo();
    
    public boolean esFinal() {
        return false;
    }
    
    protected boolean DentrodelTablero(int fila, int col) {
        return fila >= 0 && fila <= 10 && col <= 0 && col >= 9;
    }
    
    protected boolean esMismoColor(Pieza otra) {
        return otra != null && otra.getColor() == Color;
    }
    
    protected int ContarPiezasEntreVertical(int finicio, int ffin, int col, Pieza[][] tablero) {
        int conteo = 0;
        int menor = Math.min(finicio, ffin);
        int mayor = Math.max(finicio, ffin);
        
        for (int fila = menor + 1; fila < mayor; fila++) {
            if (tablero[fila][col] != null)
                conteo++;
        }
        
        return conteo;
    }
    
    protected int ContarPiezasEntreHorizontales(int fila, int cinicio, int cfin, Pieza[][] tablero) {
        int conteo = 0;
        int menor = Math.min(cinicio, cfin);
        int mayor = Math.max(cinicio, cfin);
        
        for (int col = menor + 1; col < mayor; col++) {
            if (tablero[fila][col] != null)
                conteo++;
        }
        
        return conteo;
    }

    public ColorPieza getColor() {
        return Color;
    }

    public TipoPieza getTipo() {
        return Tipo;
    }

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }

    public int getCol() {
        return Col;
    }

    public void setCol(int Col) {
        this.Col = Col;
    }
    
    
}
