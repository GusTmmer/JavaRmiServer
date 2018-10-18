/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientEvents;

import Consultas.Date;
import Supervisionados.Hospedagem;

/**
 *
 * @author a1729756
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
