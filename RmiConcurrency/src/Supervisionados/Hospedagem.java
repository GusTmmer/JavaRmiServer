package Supervisionados;

import Consultas.ConsultaHospedagem;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * A class used to represent a lodging in the server.
 */
public class Hospedagem implements Serializable {

    private String location;
    private String price;
    
    // Day representation number and capacity at that day.
    public Map <Integer, Integer> availableDates;

    public String getLocation() {
        return location;
    }
    
    public String getPrice() {
        return price;
    }
    
    public Hospedagem(String location, Map<Integer, Integer> availableDates, String price) {
        this.location = location;
        this.availableDates = availableDates;
        this.price = price;
    }
    
    public boolean matchesLocation(ConsultaHospedagem ch) {
        return location.equalsIgnoreCase(ch.getLocation());
    }

    /** Checks if this lodging has a record on a certain date.
     *
     * @param date : A date representation integer.
     * @return boolean : True if record exists; False, otherwise.
     */
    public boolean hasAvailableDay(int date) {
        for (Integer kDate : availableDates.keySet()) {
            if (kDate == date)
                return true;
        }
        return false;
    }

    /** Checks if this lodging has rooms available in a particular date.
     *
     * @param date : A date representation integer.
     * @param nRooms : The number of rooms being checked.
     * @return boolean True if capacity is greater or equal than demand; False, otherwise.
     */
    public boolean hasRooms(int date, int nRooms) {
        for (Integer kDate : availableDates.keySet()) {
            if (kDate == date)
                if (nRooms <= availableDates.get(kDate)) {
                    return true;
                }     
        }
        return false;
    }
}
