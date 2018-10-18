package Consultas;

import java.io.Serializable;

/**
 * A class containing all the necessary information to make a lodging query in the server.
 * Used by the client to communicate with the server.
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
