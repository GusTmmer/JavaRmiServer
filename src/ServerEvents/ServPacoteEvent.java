/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerEvents;

import ClientEvents.PacoteEvent;

/**
 * A class responsible for generating a server event and comparing it with client events stored in the server.
 */
public class ServPacoteEvent {
    
    private final ServHospedagemEvent servHospedagemEvent;
    private final ServPassagemEvent servPassagemEvent;

    public ServPacoteEvent(ServHospedagemEvent hospedagemEvent, ServPassagemEvent passagemEvent) {
        this.servHospedagemEvent = hospedagemEvent;
        this.servPassagemEvent = passagemEvent;
    }
    
    public boolean matchesClientEvent(PacoteEvent pEvent) {
        
        boolean matchesPassagem = servPassagemEvent.matchesClientEvent(pEvent.getPassagemEvent());
        
        if (matchesPassagem)
            return servHospedagemEvent.matchesClientEvent(pEvent.getHospedagemEvent());
        
        return false;
    }

    public ServHospedagemEvent getServHospedagemEvent() {
        return servHospedagemEvent;
    }

    public ServPassagemEvent getServPassagemEvent() {
        return servPassagemEvent;
    }
}
