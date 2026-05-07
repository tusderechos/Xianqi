/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author USUARIO
 */

import java.awt.Color;

public enum UIColors {
    FONDO_TABLERO(new Color(220, 179, 92)),
    LINEAS(new Color(80, 50, 20)),
    CELDA_HOVER(new Color(255, 255, 100, 160)),
    CELDA_SELECCION(new Color(100, 220, 100, 180)),
    CELDA_DESTINO(new Color(100, 180, 255, 160)),
    PIEZA_ROJO(new Color(180, 30, 30)),
    PIEZA_NEGRO(new Color(20, 20, 20)),
    TEXTO_PIEZA_ROJO(new Color(255, 220, 180)),
    TEXTO_PIEZA_NEGRA(new Color(180, 180, 180)),
    BORDE_PIEZA(new Color(60, 30, 10)),
    FONDO_PANEL(new Color(40, 25, 10)),
    HEADER_BG(new Color(25, 15, 5)),
    TEXTO_HEADER(new Color(230, 190, 100));
    
    private final Color color;
    
    UIColors(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
}
