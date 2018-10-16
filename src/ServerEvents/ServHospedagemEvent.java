/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerEvents;

import ClientEvents.HospedagemEvent;
import Supervisionados.Hospedagem;

/**
 *
 * @author a1729756
 */
public class ServHospedagemEvent implements IEvent {
    
    private final Hospedagem hospedagem;
    private final float hospedagemPrice;
    
    public ServHospedagemEvent(Hospedagem h) {
        this.hospedagem = h;
        this.hospedagemPrice = Float.parseFloat(h.getPrice());
    }
    
    /** Compares two events.
     * 
     * @param hEvent The instance of HospedagemEvent we're comparing to.
     * @return boolean true if this event can "fit" in event h.
     */
    public boolean matchesClientEvent(HospedagemEvent hEvent) {
        
        String location = hospedagem.getLocation();
        
        if (!location.equalsIgnoreCase(hEvent.getLocation()))
            return false;
        
        if (hospedagemPrice > hEvent.getMaxPrice())
            return false;
        
        int entryDate = hEvent.getFirstDate();
        int leaveDate = hEvent.getLastDate();

        for (int date = entryDate; date <= leaveDate; date++) {
            if (!hospedagem.hasAvailableDay(date)) {
                return false;
            } else {
                if (!hospedagem.hasRooms(date, hEvent.getDesiredRooms())) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public float getHospedagemPrice() {
        return hospedagemPrice;
    }    
}
