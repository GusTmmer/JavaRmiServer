/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientEvents;

import Supervisionados.Passagem;

/**
 *
 * @author a1729756
 */
public class PassagemEvent implements IEvent {
    
    private String origin;
    private String destination;
    private int date;
    private int desiredSpots;
    private final float maxPrice;

    public PassagemEvent(String origin, String destination, int date, int desiredSpots, float maxPrice) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.desiredSpots = desiredSpots;
        this.maxPrice = maxPrice;
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
}
