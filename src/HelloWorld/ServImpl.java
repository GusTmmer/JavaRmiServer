package HelloWorld;

import ClientEvents.IEvent;
import ClientEvents.HospedagemEvent;
import ClientEvents.PacoteEvent;
import ClientEvents.PassagemEvent;
import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import ServerEvents.ServHospedagemEvent;
import ServerEvents.ServPacoteEvent;
import ServerEvents.ServPassagemEvent;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * A server implementation responsible for managing client requests for plane ticket and lodgings queries.
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ
{
    
    private List<Passagem> passagensDisponiveis = new Vector<>();
    private List<Hospedagem> hospedagensDisponiveis = new Vector<>();
    
    private Map<InterfaceCli, List<HospedagemEvent>> eventHospedagem = new ConcurrentHashMap<>();
    private Map<InterfaceCli, List<PassagemEvent>> eventPassagem = new ConcurrentHashMap<>();
    private Map<InterfaceCli, List<PacoteEvent>> eventPacote = new ConcurrentHashMap<>();
    
    ServImpl() throws RemoteException {

    }

    /** Makes the query of a plane ticket.
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @return Map<String, List<Passagem>> A list containing all matching lodgings. Be it going or returning.
     */
    @Override
    public Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) {
        
        List<Passagem> passagensIda = new ArrayList<>();
        List<Passagem> passagensVolta = new ArrayList<>();
        
        Map <String, List<Passagem>> consultaRetorno = new HashMap<>();
        
        for (Passagem p : passagensDisponiveis) {
            if (p.matchesConsulta(cp, true))
                passagensIda.add(p);
        }
        
        consultaRetorno.put("Ida", passagensIda);
        
        if (!cp.isOneWay()) {
            for (Passagem p : passagensDisponiveis) {
                if (p.matchesConsulta(cp, false))
                    passagensVolta.add(p);
            }
            
            consultaRetorno.put("Volta", passagensVolta);
        }

        if (consultaRetorno.get("Ida").isEmpty())
            return null;

        if (!cp.isOneWay() && consultaRetorno.get("Volta").isEmpty())
            return null;

        return consultaRetorno;
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

    /** Makes the query of a travel package (Plane ticket and Lodging).
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return ConsultaPacoteResponse A specialized structure that holds the return of this query.
     */
    @Override
    public ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {

        Map<String, List<Passagem>> passagens = consultaPassagem(cp);

        if (passagens == null)
            return null;

        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        if (hospedagens == null)
            return null;

        return new ConsultaPacoteResponse(hospedagens, passagens);
    }

    /** Makes a purchase of a plane ticket registered in the database.
     *
     * @param cp : An object that holds the parameters of the plane ticket.
     * @return Map<String, Passagem> A map where the keys are "Ida" and "Volta".
     * Each holds an object with the bought ticket.
     */
    @Override
    public Map<String, Passagem> compraPassagem(ConsultaPassagem cp) {

        // Getting tickets that matches the query.
        Map<String, List<Passagem>> consultaRetorno = consultaPassagem(cp);

        // Checking if really found a valid query.
        if (consultaRetorno == null)
            return null;

        // Trying to find a ticket that has a matching price with the query.
        Passagem passagemIdaComprada = null, passagemVoltaComprada = null;

        for (Passagem p : consultaRetorno.get("Ida")) {
            if (p.getPrice().contentEquals(cp.getPrice())) {
                passagemIdaComprada = p;
                break;
            }
        }

        // Checking if could find a valid ticket.
        if (passagemIdaComprada == null)
            return null;

        // Saving the ticket to return it to the client.
        Map<String, Passagem> passagensCompradas = new HashMap<>();
        passagensCompradas.put("Ida", passagemIdaComprada);

        // If not one way, check the returning tickets.
        if (!cp.isOneWay()) {
            for (Passagem p : consultaRetorno.get("Volta")) {
                if (p.getPrice().contentEquals(cp.getPrice())) {
                    passagemVoltaComprada = p;
                    break;
                }
            }

            if (passagemVoltaComprada == null)
                return null;

            passagensCompradas.put("Volta", passagemVoltaComprada);
        }

        // Got objects.
        // Now synchronized verification and update.
        synchronized (this) {

            int goingSpotsLeft = passagemIdaComprada.getAvailableSpots();

            // Checking if going passage remains OK.
            if (cp.getnPeople() > goingSpotsLeft)
                return null;

            int returnSpotsLeft = -1;

            // Checking if return passage, if present, remains OK.
            if (passagemVoltaComprada != null) {
                returnSpotsLeft = passagemVoltaComprada.getAvailableSpots();

                if (cp.getnPeople() > returnSpotsLeft)
                    return null;
            }

            // Updating the database with passage details.
            passagemIdaComprada.setNSpotsLeft(goingSpotsLeft - cp.getnPeople());

            if (passagemVoltaComprada != null) {
                passagemVoltaComprada.setNSpotsLeft(returnSpotsLeft - cp.getnPeople());
            }

            return passagensCompradas;
        }
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

    /** Makes a purchase of a lodging stay and a plane ticket registered in the database.
     *
     * @param cp : An object that holds the parameters of the plane ticket.
     * @param ch : An object that holds the parameters of the lodge.
     * @return An object representing the bought lodging stay.
     */
    @Override
    public CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {

        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        if (hospedagens == null)
            return null;

        Hospedagem hospedagemComprada = null;

        for (Hospedagem h : hospedagens) {
            if (h.getPrice().contentEquals(ch.getPrice())) {
                hospedagemComprada = h;
                break;
            }
        }

        if (hospedagemComprada == null)
            return null;

        Map<String, List<Passagem>> consultaRetorno = consultaPassagem(cp);

        if (consultaRetorno == null)
            return null;

        // Trying to find a ticket in the returned ones that matches the price we're paying.
        Passagem passagemIdaComprada = null, passagemVoltaComprada = null;

        for (Passagem p : consultaRetorno.get("Ida")) {
            if (p.getPrice().contentEquals(cp.getPrice())) {
                passagemIdaComprada = p;
                break;
            }
        }

        if (passagemIdaComprada == null)
            return null;

        Map<String, Passagem> passagensCompradas = new HashMap<>();
        passagensCompradas.put("Ida", passagemIdaComprada);

        if (!cp.isOneWay()) {
            for (Passagem p : consultaRetorno.get("Volta")) {
                if (p.getPrice().contentEquals(cp.getPrice())) {
                    passagemVoltaComprada = p;
                    break;
                }
            }

            if (passagemVoltaComprada == null)
                return null;

            passagensCompradas.put("Volta", passagemVoltaComprada);
        }

        // Got objects.
        // Now synchronized verification and update.
        synchronized (this) {

            int goingSpotsLeft = passagemIdaComprada.getAvailableSpots();

            // Checking if going passage remains OK.
            if (cp.getnPeople() > goingSpotsLeft)
                return null;

            int returnSpotsLeft = -1;

            // Checking if return passage, if present, remains OK.
            if (passagemVoltaComprada != null) {
                returnSpotsLeft = passagemVoltaComprada.getAvailableSpots();

                if (cp.getnPeople() > returnSpotsLeft)
                    return null;
            }

            // Checking if lodging remains OK.
            int entryDate = ch.getEntryDate();
            int leaveDate = ch.getLeaveDate();

            for (int date = entryDate; date <= leaveDate; date++) {

                int nSpots = hospedagemComprada.availableDates.get(date);
                if (ch.getnRooms() > nSpots)
                    return null;
            }

            // Updating the database with passage details.
            passagemIdaComprada.setNSpotsLeft(goingSpotsLeft - cp.getnPeople());

            if (passagemVoltaComprada != null) {
                passagemVoltaComprada.setNSpotsLeft(returnSpotsLeft - cp.getnPeople());
            }

            // Updating lodging details.
            for (int date = entryDate; date <= leaveDate; date++) {

                int nSpots = hospedagemComprada.availableDates.get(date);
                hospedagemComprada.availableDates.put(date, nSpots - ch.getnRooms());

            }

            return new CompraPacoteResponse(hospedagemComprada, passagensCompradas);
        }
    }

    /** Registers a client event in the system.
     *
     * @param cli A reference to the client registering the event.
     * @param event The event structure. Can be a HospedagemEvent, PassagemEvent or PacoteEvent.
     */
    @Override
    public void registraInteresse(InterfaceCli cli, IEvent event) {

        // Handling HospedagemEvent register.
        if (event instanceof HospedagemEvent) {
            if (!eventHospedagem.containsKey(cli))
                eventHospedagem.put(cli, new ArrayList<>());

            boolean eventAlreadyExists = false;
            for (HospedagemEvent h : eventHospedagem.get(cli)) {
                if (h.equalsToEvent((HospedagemEvent) event)) {
                    eventAlreadyExists = true;
                    break;
                }
            }

            if (!eventAlreadyExists)
                eventHospedagem.get(cli).add((HospedagemEvent) event);
        }

        // Handling PassagemEvent register.
        if (event instanceof PassagemEvent) {
            if (!eventPassagem.containsKey(cli))
                eventPassagem.put(cli, new ArrayList<>());

            boolean eventAlreadyExists = false;
            for (PassagemEvent p : eventPassagem.get(cli)) {
                if (p.equalsToEvent((PassagemEvent) event)) {
                    eventAlreadyExists = true;
                    break;
                }
            }

            if (!eventAlreadyExists)
                eventPassagem.get(cli).add((PassagemEvent) event);
        }

        // Handling PackageEvent register.
        if (event instanceof PacoteEvent) {
            if (!eventPacote.containsKey(cli))
                eventPacote.put(cli, new ArrayList<>());

            boolean eventAlreadyExists = false;
            for (PacoteEvent p : eventPacote.get(cli)) {
                if (p.equalsToEvent((PacoteEvent) event)) {
                    eventAlreadyExists = true;
                    break;
                }
            }

            if (!eventAlreadyExists)
                eventPacote.get(cli).add((PacoteEvent) event);
        }
    }

    /** Removes a client event from the system.
     *
     * @param cli A reference to the client registering the event.
     * @param event The event structure. Can be a HospedagemEvent, PassagemEvent or PacoteEvent.
     */
    @Override
    public void removeInteresse(InterfaceCli cli, IEvent event) {

        // Handling HospedagemEvent removal.
        if (event instanceof HospedagemEvent) {
            if (!eventHospedagem.containsKey(cli))
                return;

            for (HospedagemEvent h : eventHospedagem.get(cli)) {
                if (h.equalsToEvent((HospedagemEvent) event)) {
                    eventHospedagem.get(cli).remove(h);
                    break;
                }
            }
        }

        // Handling PassagemEvent removal.
        if (event instanceof PassagemEvent) {
            if (!eventPassagem.containsKey(cli))
                return;

            for (PassagemEvent p : eventPassagem.get(cli)) {
                if (p.equalsToEvent((PassagemEvent) event)) {
                    eventPassagem.get(cli).remove(p);
                    break;
                }
            }
        }

        // Handling PackageEvent removal.
        if (event instanceof PacoteEvent) {
            if (!eventPacote.containsKey(cli))
                return;

            for (PacoteEvent p : eventPacote.get(cli)) {
                if (p.equalsToEvent((PacoteEvent) event)) {
                    eventPacote.get(cli).remove(p);
                    break;
                }
            }
        }
    }

    /** Adds a new ticket to the list of available tickets.
     *
     * @param novaPassagem : The ticket structure containing its details.
     */
    public void adicionaPassagem(Passagem novaPassagem) {
        passagensDisponiveis.add(novaPassagem);

        // Creates a server event and checks if it matches with any registered client event.
        ServPassagemEvent passagemEvent = new ServPassagemEvent(novaPassagem);
        comparaEventos(passagemEvent);

        // Generates an event for a package.
        for (Hospedagem h : hospedagensDisponiveis) {
            ServHospedagemEvent hospedagemEvent = new ServHospedagemEvent(h);
            ServPacoteEvent pacoteEvent = new ServPacoteEvent(hospedagemEvent, passagemEvent);
            comparaEventos(pacoteEvent);
        }
    }

    /** Adds a new lodging to the list of available lodgings.
     *
     * @param novaHospedagem : The ticket structure containing its details.
     */
    public void adicionaHospedagem(Hospedagem novaHospedagem) {

        // Goes through existing lodgings by location and checks if there was
        // already a lodging defined with that name.
        for (Hospedagem h : hospedagensDisponiveis) {
            if (h.getLocation().contentEquals(novaHospedagem.getLocation())) {

                // If there was, update available dates.
                for (Map.Entry<Integer, Integer> entry : novaHospedagem.availableDates.entrySet())
                    h.availableDates.put(entry.getKey(), entry.getValue());

                return;
            }
        }

        // If did not exist, simply add to the list.
        hospedagensDisponiveis.add(novaHospedagem);

        // Creates a server event and checks if it matches with any registered client event.
        ServHospedagemEvent hospedagemEvent = new ServHospedagemEvent(novaHospedagem);
        comparaEventos(hospedagemEvent);

        // Generates an event for a package.
        for (Passagem p : passagensDisponiveis) {
            ServPassagemEvent passagemEvent = new ServPassagemEvent(p);
            ServPacoteEvent pacoteEvent = new ServPacoteEvent(hospedagemEvent, passagemEvent);
            comparaEventos(pacoteEvent);
        }
    }

    /** Compares a server generated event with client events. If any match, issues a notify message to that client.
     *
     * @param servEvent : The server generated event.
     */
    private void comparaEventos(ServHospedagemEvent servEvent) {
        
        for (InterfaceCli cliente : eventHospedagem.keySet()) {
            for (HospedagemEvent clientEvent : eventHospedagem.get(cliente)) {
                if (servEvent.matchesClientEvent(clientEvent)) {
                    
                    try {
                        notifyClient(cliente, clientEvent, servEvent.getHospedagemPrice());
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /** Compares a server generated event with client events. If any match, issues a notify message to that client.
     *
     * @param servEvent : The server generated event.
     */
    private void comparaEventos(ServPassagemEvent servEvent) {
        
        for (InterfaceCli client : eventPassagem.keySet()) {
            for (PassagemEvent clientEvent : eventPassagem.get(client)) {
                if (servEvent.matchesClientEvent(clientEvent)) {
                    try {
                        notifyClient(client, clientEvent, servEvent.getPassagemPrice());
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /** Compares a server generated event with client events. If any match, issues a notify message to that client.
     *
     * @param servEvent : The server generated event.
     */
    private void comparaEventos(ServPacoteEvent servEvent) {
        
        for (InterfaceCli cliente : eventPacote.keySet()) {
            for (PacoteEvent clientEvent : eventPacote.get(cliente)) {
                if (servEvent.matchesClientEvent(clientEvent)) {
                    try {
                        notifyClient(cliente, clientEvent, servEvent);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /** Sends a message to the client about a new lodging available.
     *
     * @param client : A client reference
     * @param hEvent : The client event being processed.
     * @param price : The price of the lodging.
     * @throws RemoteException In the case of network error.
     */
    private void notifyClient(InterfaceCli client, HospedagemEvent hEvent, String price) throws RemoteException {

        String message = "Uma nova hospedagem em " + hEvent.getLocation() + " esta disponivel.\n" +
                "Detalhes:\nDia de entrada: " + hEvent.getEntryDate() + "\n" +
                "Dia de saida: " + hEvent.getLeaveDate() + "\n" +
                "Numero de quartos: " + hEvent.getDesiredRooms() + "\n" +
                "Preco: " + price + "\n";

        client.printNotification(message);
    }

    /** Sends a message to the client about a new plane ticket available.
     *
     * @param client : A client reference.
     * @param pEvent : The client event being processed.
     * @param price : The price of the ticket.
     * @throws RemoteException In the case of network error.
     */
    private void notifyClient(InterfaceCli client, PassagemEvent pEvent, String price) throws RemoteException {

        String message = "Uma nova passagem de " + pEvent.getOrigin() + " para " + pEvent.getDestination() +
                " esta disponivel.\n" + "Detalhes:\nDia: " + pEvent.getDate() + "\n" +
                "Numero de passagens: " + pEvent.getDesiredSpots() + "\n" +
                "Preco: " + price + "\n";

        client.printNotification(message);
    }

    /** Sends a message to the client about a new plane ticket available.
     *
     * @param client : A client reference.
     * @param pacoteEvent : The client event being processed.
     * @param servEvent : The event that generated this notification.
     * @throws RemoteException In the case of network error.
     */
    private void notifyClient(InterfaceCli client, PacoteEvent pacoteEvent, ServPacoteEvent servEvent) throws RemoteException {
        
        PassagemEvent pEvent = pacoteEvent.getPassagemEvent();
        HospedagemEvent hEvent = pacoteEvent.getHospedagemEvent();

        String message = "Um pacote de seu interesse está agora disponível.\n" +
                "Hospedagem em " + hEvent.getLocation() + ".\n" +
                "Detalhes:\nDia de entrada: " + hEvent.getEntryDate() + "\n" +
                "Dia de saida: " + hEvent.getLeaveDate() + "\n" +
                "Numero de quartos: " + hEvent.getDesiredRooms() + "\n" +
                "Preco: " + servEvent.getServHospedagemEvent().getHospedagemPrice() + "\n\n" +
                "Passagem de " + pEvent.getOrigin() + " para " + pEvent.getDestination() + ".\n" +
                "Detalhes:\nDia: " + pEvent.getDate() + "\n" +
                "Numero de passagens: " + pEvent.getDesiredSpots() + "\n" +
                "Preco: " + servEvent.getServPassagemEvent().getPassagemPrice() + "\n";

        client.printNotification(message);
    }
}
