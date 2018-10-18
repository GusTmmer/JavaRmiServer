package ClientEvents;

/**
 * A class containing all the necessary information to make a travel package event registry in the server.
 * Used by the client to communicate with the server.
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

        if (!passagemEvent.equalsToEvent(p.passagemEvent))
            return false;

        if (!hospedagemEvent.equalsToEvent(p.hospedagemEvent))
            return false;

        return true;
    }
}
