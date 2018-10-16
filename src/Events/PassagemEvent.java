/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Supervisionados.Passagem;

/**
 *
 * @author a1729756
 */
public class PassagemEvent implements IEvent {
    
    private String origin;
    private String destination;
    private int date;
    private int desiredSpots;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDate() {
        return date;
    }

    public int getDesiredSpots() {
        return desiredSpots;
    }
    
    public boolean isInEvent(PassagemEvent p) {
        
        if (!origin.equalsIgnoreCase(p.getOrigin()))
            return false;
        
        if (!destination.equalsIgnoreCase(p.getDestination()))
            return false;
        
        if (date != p.getDate())
            return false;
        
        if (desiredSpots > p.getDesiredSpots())
            return false;
        
        return true;
    }    

    public PassagemEvent(String origin, String destination, int date, int desiredSpots) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.desiredSpots = desiredSpots;
    }
    
    public PassagemEvent(Passagem p) {
        this.origin = p.getOrigin();
        this.destination = p.getDestination();
        this.date = p.getGoingDate();
        this.desiredSpots = p.getNSpotsLeft();
    }
}
