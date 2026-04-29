/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

/**
 *
 * @author USUARIO
 */
public enum ColorPieza {
    ROJO,
    NEGRO;
    
    public ColorPieza Opuesto() {
        return this == ROJO ? NEGRO : ROJO;
    }
}
