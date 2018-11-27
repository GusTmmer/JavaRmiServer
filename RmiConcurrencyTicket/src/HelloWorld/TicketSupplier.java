package HelloWorld;


import Consultas.ConsultaPassagem;
import Supervisionados.Passagem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *
 * A server implementation responsible for managing client requests for plane ticket and lodgings queries.
 */
public class TicketSupplier extends UnicastRemoteObject implements ITicketSupplier
{
    private List<Passagem> passagensDisponiveis = new Vector<>();
    
    TicketSupplier() throws RemoteException {

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

    /** Adds a new ticket to the list of available tickets.
     *
     * @param novaPassagem : The ticket structure containing its details.
     */
    @Override
    public void adicionaPassagem(Passagem novaPassagem) {
        passagensDisponiveis.add(novaPassagem);
    }
}
