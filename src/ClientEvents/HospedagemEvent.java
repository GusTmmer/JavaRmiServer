package ClientEvents;

import Consultas.Date;

/**
 * A class containing all the necessary information to make a lodging event registry in the server.
 * Used by the client to communicate with the server.
 */
public class HospedagemEvent implements IEvent {
    
    private final String location;
    private final String entryDate;
    private final String leaveDate;

    private final int firstDate;
    private final int lastDate;
    private final int desiredRooms;
    private final float maxPrice;

    public HospedagemEvent(String location, String entryDate, String leaveDate, int desired_rooms, float maxPrice) {
        this.location = location;
        this.desiredRooms = desired_rooms;
        this.maxPrice = maxPrice;
        this.entryDate = entryDate;
        this.leaveDate = leaveDate;
        this.firstDate = new Date(entryDate).reprDay;
        this.lastDate = new Date(leaveDate).reprDay;
    }

    /** Compares two HospedagemEvent objects.
     *
     * @param h The instance of HospedagemEvent we're comparing to.
     * @return boolean true if equals; false, otherwise.
     */
    public boolean equalsToEvent(HospedagemEvent h) {

        if (!location.equalsIgnoreCase(h.location))
            return false;

        if (firstDate != h.firstDate)
            return false;

        if (lastDate != h.lastDate)
            return false;

        if (desiredRooms != h.desiredRooms)
            return false;

        if (maxPrice != h.maxPrice)
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

    public float getMaxPrice() {
        return maxPrice;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }
}
