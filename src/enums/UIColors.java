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
    LINEAS (new Color(93, 56, 17)),
    FONDO_PALACIO (new Color(205, 158, 70)),
    FONDO_PALACIO_DIAGONAL (new Color(188, 138, 50)),
    FONDO_RIO (new Color(219, 178, 101)),
    CELDA_SELECCION (new Color(120, 200, 80)),
    CELDA_DESTINO (new Color(80, 160, 220)),
    CELDA_HOVER (new Color(240, 220, 80)),
    PIEZA_ROJO (new Color(180, 30, 30)),
    PIEZA_NEGRO (new Color(20, 20, 20)),
    TEXTO_PIEZA_ROJO (new Color(200, 30, 30)),
    TEXTO_PIEZA_NEGRA (new Color(30, 30, 30)),
    BORDE_PIEZA (new Color(60, 30, 10)),
    FONDO_PANEL (new Color(58, 34, 12)),
    HEADER_BG (new Color(38, 20, 6)),
    HEADER_GRADIENTE_TOP (new Color(50, 30, 8)),
    LATERAL_GRADIENTE_TOP (new Color(40, 22, 6)),
    TEXTO_HEADER (new Color(230, 190, 100)),
    TEXTO_INFO (new Color(180, 150, 80)),
    ORO_DECORATIVO (new Color(200, 160,  60)),
    BOTON_FONDO (new Color(80, 45, 10)),
    BOTON_HOVER_FONDO (new Color(120, 75, 20)),
    BOTON_HOVER_TEXTO (new Color(255, 220, 130)),
    CELDA_CAPTURA_FONDO (new Color(48, 30, 10)),
    CELDA_CAPTURA_BORDE (new Color(90, 60, 20)),
    GRID_CAPTURA_FONDO (new Color(25, 14, 4)),
    TEXTO_RIO (new Color(95, 170, 190)),
    RIO_GRADIENTE_IZQ (new Color(75, 155, 185)),
    RIO_GRADIENTE_DER (new Color(45, 115, 150));

    private final Color color;

    UIColors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}