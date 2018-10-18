package ServerEvents;

import ClientEvents.HospedagemEvent;
import Supervisionados.Hospedagem;

/**
 * A class responsible for generating a server event and comparing it with client events stored in the server.
 */
public class ServHospedagemEvent {
    
    private final Hospedagem hospedagem;
    private final float hospedagemPrice;
    private final String hospedagemPriceString;
    
    public ServHospedagemEvent(Hospedagem h) {
        this.hospedagem = h;
        this.hospedagemPrice = Float.parseFloat(h.getPrice());
        this.hospedagemPriceString = h.getPrice();
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

    public String getHospedagemPrice() {
        return hospedagemPriceString;
    }    
}
