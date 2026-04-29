/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

/**
 *
 * @author Hp
 */

public interface Datos {
    int getIndiceUsuario(String usuario);
    
    void AgregarLog(String actor, String fecha, String rival, String resultado);
    
    String[] ObtenerLogsUsuario(String usuario);
}
