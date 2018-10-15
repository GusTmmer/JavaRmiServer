/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

/**
 *
 * @author a1729756
 */
public class PassagemEvent implements IEvent {
    
    private String origin;
    private String destination;
    private String date;
    private int desiredSpots;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public int getDesiredSpots() {
        return desiredSpots;
    }
    
    public boolean equalsToEvent(PassagemEvent p) {
        
        if (!origin.equalsIgnoreCase(p.getOrigin()))
            return false;
        
        if (!date.equalsIgnoreCase(p.getDate()))
            return false;
        
        if (desiredSpots != p.getDesiredSpots())
            return false;
        
        return true;
    }    
}
