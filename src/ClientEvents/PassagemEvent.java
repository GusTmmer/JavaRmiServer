package ClientEvents;

import Consultas.Date;

/**
 * A class containing all the necessary information to make a plane ticket event registry in the server.
 * Used by the client to communicate with the server.
 */
public class PassagemEvent implements IEvent {
    
    private String origin;
    private String destination;
    private String dateString;
    private int date;
    private int desiredSpots;
    private final float maxPrice;

    public PassagemEvent(String origin, String destination, String date, int desiredSpots, float maxPrice) {
        this.origin = origin;
        this.destination = destination;
        this.dateString = date;
        this.date = new Date(date).reprDay;
        this.desiredSpots = desiredSpots;
        this.maxPrice = maxPrice;
    }

    public boolean equalsToEvent(PassagemEvent p) {

        if (!origin.equalsIgnoreCase(p.origin))
            return false;

        if (!destination.equalsIgnoreCase(p.destination))
            return false;

        if (date != p.date)
            return false;

        if (desiredSpots != p.desiredSpots)
            return false;

        if (maxPrice != maxPrice)
            return false;

        return true;
    }

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

    public float getMaxPrice() {
        return maxPrice;
    }

    public String getDateString() {
        return dateString;
    }
}
