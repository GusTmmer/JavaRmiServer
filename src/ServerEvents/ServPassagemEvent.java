/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerEvents;

import ClientEvents.PassagemEvent;
import Supervisionados.Passagem;

/**
 *
 * @author a1729756
 */
public class ServPassagemEvent implements IEvent {
    
    private final Passagem passagem;
    private final float passagemPrice;
    
    public ServPassagemEvent(Passagem p) {
        this.passagem = p;
        this.passagemPrice = Float.parseFloat(p.getPrice());
    }
    
    public boolean matchesClientEvent(PassagemEvent pEvent) {
        
        String origin = passagem.getOrigin();
        String destination = passagem.getDestination();
        int passagemDate = passagem.getGoingDate();
        int availableSpots = passagem.getNSpotsLeft();
        
        if (!origin.equalsIgnoreCase(pEvent.getOrigin()))
            return false;
        
        if (!destination.equalsIgnoreCase(pEvent.getDestination()))
            return false;
        
        if (passagemDate != pEvent.getDate())
            return false;
        
        if (availableSpots < pEvent.getDesiredSpots())
            return false;
        
        if (passagemPrice > pEvent.getMaxPrice())
            return false;
        
        return true;
    }

    public float getPassagemPrice() {
        return passagemPrice;
    }
}
