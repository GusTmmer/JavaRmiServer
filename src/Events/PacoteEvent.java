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
public class PacoteEvent implements IEvent {
    
    private final HospedagemEvent hospedagemEvent;
    private final PassagemEvent passagemEvent;

    public PacoteEvent(HospedagemEvent hospedagemEvent, PassagemEvent passagemEvent) {
        this.hospedagemEvent = hospedagemEvent;
        this.passagemEvent = passagemEvent;
    }

    public HospedagemEvent getHospedagemEvent() {
        return hospedagemEvent;
    }

    public PassagemEvent getPassagemEvent() {
        return passagemEvent;
    }
    
    public boolean equalsToEvent(PacoteEvent p) {
        
        if (!passagemEvent.equalsToEvent(p.getPassagemEvent()))
            return false;
        
        if (!hospedagemEvent.equalsToEvent(p.getHospedagemEvent()))
            return false;
        
        return true;
    }
    
}
