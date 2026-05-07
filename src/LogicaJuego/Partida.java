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

public class Partida {
    
    private String Jugador1;
    private String Jugador2;
    private ColorPieza Turno;
    private boolean Activa;
    private String ResultadoFinal;
    
    public Partida(String Jugador1, String Jugador2) {
        this.Jugador1 = Jugador1;
        this.Jugador2 = Jugador2;
        Turno = ColorPieza.ROJO;
        Activa = true;
        ResultadoFinal = null;
    }
    
    public void CambiarTurno() {
        Turno = Turno.Opuesto();
    }
    
    public String getJugadorTurno() {
        return Turno == ColorPieza.ROJO ? Jugador1 : Jugador2;
    }
    
    public String getJugadorEsperando() {
        return Turno == ColorPieza.ROJO ? Jugador2 : Jugador1;
    }
    
    public ColorPieza getColorJugador(String usuario) {
        if (usuario == null)
            return null;
        if (usuario.equals(Jugador1))
            return ColorPieza.ROJO;
        if (usuario.equals(Jugador2))
            return ColorPieza.NEGRO;
        
        return null;
    }
    
    public void TerminarPorCaptura() {
        ResultadoFinal = getJugadorTurno() + " VENCIO A " + getJugadorEsperando();
        Activa = false;
    }
    
    public void TerminarPorRetiro() {
        ResultadoFinal = getJugadorTurno() + " SE HA RETIRADO, " + getJugadorEsperando() + " HA GANADO";
        Activa = false;
    }

    public String getJugador1() {
        return Jugador1;
    }

    public String getJugador2() {
        return Jugador2;
    }

    public ColorPieza getTurno() {
        return Turno;
    }

    public boolean isActiva() {
        return Activa;
    }

    public String getResultadoFinal() {
        return ResultadoFinal;
    }
    
    public String getGanador() {
        if (Activa || ResultadoFinal == null)
            return null;
        return getJugadorEsperando().equals(Jugador1) ? Jugador1 : Jugador2;
    }
    
    public String getPerdedor() {
        if (Activa || ResultadoFinal == null)
            return null;
        
        String ganador = getGanador();
        return ganador.equals(Jugador1) ? Jugador2 : Jugador1;
    }
}
