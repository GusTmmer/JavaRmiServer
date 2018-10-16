/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Supervisionados.Hospedagem;

/**
 *
 * @author a1729756
 */
public class HospedagemEvent implements IEvent {
    
    private final String location;
    private final int firstDate;
    private final int lastDate;
    private final int desiredRooms;

    public HospedagemEvent(String location, int first_date, int last_date, int desired_rooms) {
        this.location = location;
        this.firstDate = first_date;
        this.lastDate = last_date;
        this.desiredRooms = desired_rooms;
    }
    
    public HospedagemEvent(Hospedagem h) {
        this.location = h.getLocation();
        this.firstDate = h.getFirstDate?
        this.lastDate = h.getLastDate?
        this.desiredRooms = desired_rooms;
    }
    
    /** Compares two events.
     * 
     * @param h The instance of HospedagemEvent we're comparing to.
     * @return boolean true if this event can "fit" in event h.
     */
    public boolean isInEvent(HospedagemEvent h) {
        
        if (!location.equalsIgnoreCase(h.getLocation()))
            return false;
        
        if (desiredRooms > h.getDesiredRooms())
            return false;
        
        if (firstDate < h.getFirstDate() || lastDate < h.getLastDate())
            return false;
        
        return true;
    }

    public String getLocation() {
        return location;
    }

    public int getFirstDate() {
        return firstDate;
    }

    public int getLastDate() {
        return lastDate;
    }

    public int getDesiredRooms() {
        return desiredRooms;
    }
    
    
    
}
