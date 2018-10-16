/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import Events.HospedagemEvent;
import Supervisionados.Hospedagem;
import java.io.Serializable;

/**
 *
 * @author a1729756
 */
public class ConsultaHospedagem implements Serializable {

    private String location;
    private int entryDate;
    private int leaveDate;
    private int nRooms;
    private int nPeople;
    private String price = "";

    public ConsultaHospedagem(String location, int entryDate, int leaveDate, int nRooms, int nPeople) {
        this.location = location;
        this.entryDate = entryDate;
        this.leaveDate = leaveDate;
        this.nRooms = nRooms;
        this.nPeople = nPeople;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public int getEntryDate() {
        return entryDate;
    }

    public int getLeaveDate() {
        return leaveDate;
    }

    public int getnRooms() {
        return nRooms;
    }

    public int getnPeople() {
        return nPeople;
    }
}
