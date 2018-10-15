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
public class HospedagemEvent implements IEvent {
    
    private final String location;
    private final String date;
    private final int desiredRooms;

    public HospedagemEvent(String location, String date, int desired_rooms) {
        this.location = location;
        this.date = date;
        this.desiredRooms = desired_rooms;
    }
    
    /** Compares two events.
     * 
     * @param h The instance of HospedagemEvent we're comparing to.
     * @return boolean true if equals; false, otherwise.
     */
    public boolean equalsToEvent(HospedagemEvent h) {
        
        if (!location.equalsIgnoreCase(h.getLocation()))
            return false;
        
        if (!date.equalsIgnoreCase(h.getDate()))
            return false;
        
        if (desiredRooms != h.getDesiredRooms())
            return false;
        
        return true;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getDesiredRooms() {
        return desiredRooms;
    }
    
    
    
}
