/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Supervisionados;

import Consultas.ConsultaHospedagem;
import Consultas.Date;

import java.util.Map;

/**
 *
 * @author a1729756
 */
public class Hospedagem {

    public String location;
    public String price;
    
    // Day representation number and capacity at that day.
    public Map <Integer, Integer> availableDates;

    public String getLocation() {
        return location;
    }
    
    public String getPrice() {
        return price;
    }

    public Map<Integer, Integer> getAvailableDates() {
        return availableDates;
    }
    
    public Hospedagem(String location, Map<Integer, Integer> availableDates, String price) {
        this.location = location;
        this.availableDates = availableDates;
        this.price = price;
    }
    
    public boolean matchesLocation(ConsultaHospedagem ch) {
        return location.equalsIgnoreCase(ch.getLocation());
    }
    
    public boolean hasAvailableDay(int date) {
        for (Integer kDate : availableDates.keySet()) {
            if (kDate == date)
                return true;
        }
        return false;
    }
    
    public boolean hasRooms(int date, int n_rooms) {
        for (Integer kDate : availableDates.keySet()) {
            if (kDate == date)
                if (n_rooms <= availableDates.get(kDate)) {
                    return true;
                }     
        }
        return false;
    }
}
