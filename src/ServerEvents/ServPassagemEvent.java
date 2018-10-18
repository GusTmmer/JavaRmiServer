package ServerEvents;

import ClientEvents.PassagemEvent;
import Supervisionados.Passagem;

/**
 * A class responsible for generating a server event and comparing it with client events stored in the server.
 */
public class ServPassagemEvent implements IEvent {
    
    private final Passagem passagem;
    private final float passagemPrice;
    private final String passagemPriceString;
    
    public ServPassagemEvent(Passagem p) {
        this.passagem = p;
        this.passagemPrice = Float.parseFloat(p.getPrice());
        this.passagemPriceString = p.getPrice();
    }
    
    public boolean matchesClientEvent(PassagemEvent pEvent) {
        
        if (!passagem.getOrigin().equalsIgnoreCase(pEvent.getOrigin()))
            return false;
        
        if (!passagem.getDestination().equalsIgnoreCase(pEvent.getDestination()))
            return false;
        
        if (passagem.getGoingDate() != pEvent.getDate())
            return false;
        
        if (passagem.getAvailableSpots() < pEvent.getDesiredSpots())
            return false;
        
        if (passagemPrice > pEvent.getMaxPrice())
            return false;
        
        return true;
    }

    public String getPassagemPrice() {
        return passagemPriceString;
    }
}
