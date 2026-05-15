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
    FONDO_TABLERO (new Color(219, 178, 101)),
    LINEAS (new Color(93,  56,  17)),
    FONDO_PALACIO (new Color(205, 158,  70)),
    FONDO_PALACIO_DIAGONAL (new Color(188, 138,  50)),
    FONDO_RIO (new Color(162, 198, 170)),
    CELDA_SELECCION (new Color(120, 200,  80)),
    CELDA_DESTINO (new Color(80,  160, 220)),
    CELDA_HOVER (new Color(240, 220,  80)),
    PIEZA_ROJO (new Color(180,  30,  30)),
    PIEZA_NEGRO (new Color(20,   20,  20)),
    TEXTO_PIEZA_ROJO (new Color(200,  30,  30)),
    TEXTO_PIEZA_NEGRA (new Color(30,   30,  30)),
    BORDE_PIEZA (new Color(60,   30,  10)),
    FONDO_PANEL (new Color(58,   34,  12)),
    HEADER_BG (new Color(38,   20,   6)),
    TEXTO_HEADER (new Color(230, 190, 100));
    
    private final Color color;
    
    UIColors(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
}
