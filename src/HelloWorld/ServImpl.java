package HelloWorld;

import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import Events.IEvent;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author a1729756
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ
{
    
    private List<Passagem> passagensDisponiveis;
    private List<Hospedagem> hospedagensDisponiveis;
    
    private Map<InterfaceCli, List<IEvent>> eventMapping = new ConcurrentHashMap<>();
    
    ServImpl() throws RemoteException {
        this.passagensDisponiveis = new ArrayList<>();
        this.hospedagensDisponiveis = new ArrayList<>();
        
    }

    @Override
    public Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) throws RemoteException {
        
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


        if (consultaRetorno.get("Ida").size() == 0)
            return null;

        if (!cp.isOneWay() && consultaRetorno.get("Volta").size() == 0)
            return null;

        return consultaRetorno;
    }
    
    @Override
    public List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) throws RemoteException {

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

        if (hospedagens.size() == 0)
            return null;

        return hospedagens;  
    }

    /** Realiza a consulta de um pacote (Uma passagem e uma hospedagem).
     *
     * @param cp ConsultaPassagem : um objeto contendo os parâmetros da passagem.
     * @param ch ConsultaHospedagem : um objeto contendo os parâmetros da hospedagem.
     * @return ConsultaPacoteResponse Uma estrutura especializada que contém o retorno da consulta de passagem e de hospedagem.
     * @throws RemoteException quando ocorre exceção durante a comunicação dos sistemas.
     */
    @Override
    public ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException {

        Map<String, List<Passagem>> passagens = consultaPassagem(cp);

        if (passagens == null)
            return null;

        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        if (hospedagens == null)
            return null;

        return new ConsultaPacoteResponse(hospedagens, passagens);
    }

    /** Realiza a compra de uma passagem registrada no servidor.
     *
     * @param cp ConsultaPassagem : um objeto contendo os parâmetros da passagem.
     * @return Map<String, Passagem> Um campo do mapa contém a passagem de Ida e caso tenha sido requisitado,
     * contém também um campo com a passagem de Volta.
     * @throws RemoteException quando ocorre exceção durante a comunicação dos sistemas.
     */
    @Override
    public Map<String, Passagem> compraPassagem(ConsultaPassagem cp) throws RemoteException {

        // Pegando resultados de passagens que se assemelham a consulta.
        Map<String, List<Passagem>> consultaRetorno = consultaPassagem(cp);

        // Garantindo que existao passagens que respeitam os criterios da consulta.
        if (consultaRetorno == null)
            return null;

        // Tentando achar uma passagem dentre as retornadas que tenha o mesmo preco da qual estamos comprando.
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

            // Alterando dados do servidor.
            int spotsLeft = passagemVoltaComprada.getNSpotsLeft();
            passagemVoltaComprada.setNSpotsLeft(spotsLeft - cp.getnPeople());
        }

        // Alterando dados do servidor.
        int spotsLeft = passagemIdaComprada.getNSpotsLeft();
        passagemIdaComprada.setNSpotsLeft(spotsLeft - cp.getnPeople());


        return passagensCompradas;
    }

    /** Realiza a compra de uma hospedagem registrada no servidor.
     *
     * @param ch ConsultaHospedagem : um objeto contendo os parâmetros da hospedagem.
     * @return Hospedagem Uma cópia da hospedagem comprada.
     * @throws RemoteException quando ocorre exceção durante a comunicação dos sistemas.
     */
    @Override
    public Hospedagem compraHospedagem(ConsultaHospedagem ch) throws RemoteException {

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

        // Realizando alteracao dos dados do servidor.
        int entryDate = ch.getEntryDate();
        int leaveDate = ch.getLeaveDate();

        for (int date = entryDate; date <= leaveDate; date++) {

            int nSpots = hospedagemComprada.availableDates.get(date);
            hospedagemComprada.availableDates.put(date, nSpots - ch.getnRooms());

        }

        return hospedagemComprada;
    }

    @Override
    public CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException {

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


        // Tentando achar uma passagem dentre as retornadas que tenha o mesmo preco da qual estamos comprando.
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

            int goingSpotsLeft = passagemIdaComprada.getNSpotsLeft();

            // Checking if going passage remains OK.
            if (cp.getnPeople() > goingSpotsLeft)
                return null;

            int returnSpotsLeft = -1;

            // Checking if return passage, if present, remains OK.
            if (passagemVoltaComprada != null) {
                returnSpotsLeft = passagemVoltaComprada.getNSpotsLeft();

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

    @Override
    public void registraInteresse(IEvent event, InterfaceCli cli) throws RemoteException {
        
        if (!eventMapping.containsKey(cli))
            eventMapping.put(cli, new ArrayList<>());
        
        // TODO check if event already exists in list. (contains does not work)
        eventMapping.get(cli).add(event);
    }

    @Override
    public void removeInteresse(IEvent event, InterfaceCli cli) throws RemoteException {
        eventMapping.get(cli).remove(event);
    }
    
    public void adicionaPassagem(Passagem passagem) {
        passagensDisponiveis.add(passagem);
    }
    
    public void adicionaHospedagem(Hospedagem novaHospedagem) {

        // Goes through existing lodgings by location and checks if there was
        // already a lodging defined with that name.
        for (Hospedagem h : hospedagensDisponiveis) {
            if (h.location.contentEquals(novaHospedagem.location)) {

                // If there was, update available dates.
                for (Map.Entry<Integer, Integer> entry : novaHospedagem.availableDates.entrySet())
                    h.availableDates.put(entry.getKey(), entry.getValue());

                return;
            }
        }

        // If did not exist, simply add to the list.
        hospedagensDisponiveis.add(novaHospedagem);
    }
    
    private void notifyClients() {

    }
}
