/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Supervisionados;

import Consultas.ConsultaPassagem;

/**
 *
 * @author a1729756
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

    public int getNSpotsLeft() {
        return nSpotsLeft;
    }
    
    public String getPrice() {
        return price;
    }
}
