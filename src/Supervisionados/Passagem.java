package Supervisionados;

import Consultas.ConsultaPassagem;

/**
 *
 * A class used to represent a plane ticket in the server.
 */
public class Passagem {

    private String origin;
    private String destination;
    private int date;
    private int nSpotsLeft;
    private String price;

    public Passagem(String origin, String destination, int date, int n_spots_left, String price) {

        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.nSpotsLeft = n_spots_left;
        this.price = price;
    }

    /**
     *
     * @param cp : An object that holds info of a query from a client.
     * @param isOriginDest Type of plane ticket query.
     * @return boolean True if it matches; False, otherwise.
     */
    public boolean matchesConsulta(ConsultaPassagem cp, boolean isOriginDest) {
  
        if (!isOriginDest) {
            
            if (!origin.equalsIgnoreCase(cp.getDestination()))
                return false;
        
            if (!destination.equalsIgnoreCase(cp.getOrigin()))
                return false;
        } else {
        
            if (!origin.equalsIgnoreCase(cp.getOrigin()))
                return false;

            if (!destination.equalsIgnoreCase(cp.getDestination()))
                return false;
        }
        
        if (date != cp.getGoingDate())
            return false;
        
        if (nSpotsLeft < cp.getnPeople())
            return false;
        
        return true;
    }

    public void setNSpotsLeft(int n_spots_left) {
        this.nSpotsLeft = n_spots_left;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getGoingDate() {
        return date;
    }

    public int getAvailableSpots() {
        return nSpotsLeft;
    }
    
    public String getPrice() {
        return price;
    }
}
