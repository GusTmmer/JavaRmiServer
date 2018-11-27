package HelloWorld;


import Consultas.ConsultaHospedagem;
import Supervisionados.Hospedagem;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * A server implementation responsible for managing client requests for plane ticket and lodgings queries.
 */
public class HotelSupplier extends UnicastRemoteObject implements IHotelSupplier {
    
    private List<Hospedagem> hospedagensDisponiveis = new Vector<>();
    
    private FileReader logFile;
    
    HotelSupplier() {
        
    }


    /** Makes the query of a lodging.
     *
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return List<Hospedagem> A list containing all matching lodgings.
     */
    @Override
    public List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) {

        List<Hospedagem> hospedagens = new ArrayList<>();
        
        int entryDate = ch.getEntryDate();
        int leaveDate = ch.getLeaveDate();
        
        for (Hospedagem h : hospedagensDisponiveis) {
            if (h.matchesLocation(ch)) {

                boolean found = true;

                for (int date = entryDate; date <= leaveDate; date++) {
                    if (!h.hasAvailableDay(date)) {
                        found = false;
                        break;
                    } else {
                        if (!h.hasRooms(date, ch.getnRooms())) {
                            found = false;
                            break;
                        }
                    }
                }

                if(found)
                    hospedagens.add(h);
            }
        }

        if (hospedagens.isEmpty())
            return null;

        return hospedagens;  
    }

    /** Makes a purchase of a lodging stay registered in the database.
     *
     * @param ch : An object that holds the parameters of the lodge.
     * @return An object representing the bought lodging stay.
     */
    @Override
    public Hospedagem compraHospedagem(ConsultaHospedagem ch) {

        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        Hospedagem hospedagemComprada = null;

        for (Hospedagem h : hospedagens) {
            if (h.getPrice().contentEquals(ch.getPrice())) {
                hospedagemComprada = h;
                break;
            }
        }

        if (hospedagemComprada == null)
            return null;

        // Got objects.
        // Now synchronized verification and update.
        synchronized (this) {

            // Checking if lodging remains OK.
            int entryDate = ch.getEntryDate();
            int leaveDate = ch.getLeaveDate();

            for (int date = entryDate; date <= leaveDate; date++) {

                int nSpots = hospedagemComprada.availableDates.get(date);
                if (ch.getnRooms() > nSpots)
                    return null;
            }

            // Updating lodging details in the database.
            for (int date = entryDate; date <= leaveDate; date++) {

                int nSpots = hospedagemComprada.availableDates.get(date);
                hospedagemComprada.availableDates.put(date, nSpots - ch.getnRooms());

            }

            return hospedagemComprada;
        }
    }

    /** Adds a new lodging to the list of available lodgings.
     *
     * @param novaHospedagem : The ticket structure containing its details.
     */
    @Override
    public void adicionaHospedagem(Hospedagem novaHospedagem) {

        // Goes through existing lodgings by location and checks if there was
        // already a lodging defined with that name.
        for (Hospedagem h : hospedagensDisponiveis) {
            if (h.getLocation().contentEquals(novaHospedagem.getLocation())) {

                // If there was, update available dates.
                for (Map.Entry<Integer, Integer> entry : novaHospedagem.availableDates.entrySet())
                    h.availableDates.put(entry.getKey(), entry.getValue());

                break;
            }
        }

        // If did not exist, simply add to the list.
        hospedagensDisponiveis.add(novaHospedagem);
    }
}
