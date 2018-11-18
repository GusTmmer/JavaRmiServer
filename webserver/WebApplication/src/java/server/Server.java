/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacote;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


/**
 * REST Web Service
 *
 * @author Vitor
 */
/**
 *
 * A server implementation responsible for managing client requests for plane ticket and lodgings queries.
 */
    @Path("Server")
public class Server
{
    @Context
    private UriInfo context;
    
    static private List<Passagem> passagensDisponiveis = new Vector<>();
    static private List<Hospedagem> hospedagensDisponiveis = new Vector<>();
    
    public Server() {
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("teste")
    public String teste()
    {
        return ("n de passagens: " + Integer.toString(passagensDisponiveis.size()) + "\n"
                + "n de hospedagens: " + Integer.toString(hospedagensDisponiveis.size()));
    }

    
    /** Web wrapper for consultaPassagem.
     *
     * @param data String : A JSON String containing a object that holds the parameters of the plane ticket.
     * @return String : A specialized structure that holds the return of this query.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("consulta/passagens")
    public String webConsultaPassagem(String data) {
        Gson g = new Gson();
        ConsultaPassagem cp = g.fromJson(data, ConsultaPassagem.class);
        
        Map<String, List<Passagem>> out = consultaPassagem(cp);
        
        return g.toJson(out);
    }
    
    /** Makes the query of a plane ticket.
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @return A list containing all matching lodgings. Be it going or returning.
     */ 
    public Map <String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) {       
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

    /** Web wrapper for consultaHospedagem.
     *
     * @param data String : A JSON String containing a object that holds the parameters of the lodging.
     * @return String : A JSON String containing the structure that holds the return of this query.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("consulta/hospedagens")
    public String webConsultaHospedagem(String data) {
        Gson g = new Gson();
        ConsultaHospedagem ch = g.fromJson(data, ConsultaHospedagem.class);
        
        System.out.println("Recebi consulta de hospedagem em " + ch.getLocation() + " do dia " + Integer.toString(ch.getEntryDate())
                + " ate " + Integer.toString(ch.getLeaveDate()));
        
        List<Hospedagem> out = consultaHospedagem(ch);
        
        return g.toJson(out);
    }
    
    /** Makes the query of a lodging.
     *
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return String :  A list containing all matching lodgings.
     */
    
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

    /** Web wrapper for consultaPacote.
     *
     * @param data String : A JSON String containing a object that holds the parameters of the package.
     * @return String : A JSON strinc containing the structure that holds the return of this query.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("consulta/pacotes")
    public String webConsultaPacote(String data) {
        Gson g = new Gson();
        
        ConsultaPacote cpkg = g.fromJson(data, ConsultaPacote.class);
        ConsultaPassagem cp = cpkg.consultaPassagem;
        ConsultaHospedagem ch = cpkg.consultaHospedagem;
        
        ConsultaPacoteResponse out = consultaPacote(cp, ch);
        
        return g.toJson(out);
    }
    
    /** Makes the query of a travel package (Plane ticket and Lodging).
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return ConsultaPacoteResponse A specialized structure that holds the return of this query.
     */
    public ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {

        Map<String, List<Passagem>> passagens = consultaPassagem(cp);

        if (passagens == null)
            return null;

        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        if (hospedagens == null)
            return null;

        return new ConsultaPacoteResponse(hospedagens, passagens);
    }

    /** Web wrapper for compraPassagem.
     *
     * @param data : A JSON string with the ticket requested.
     * @return String : A JSON string with the object representing the bought ticket.
     */
    @POST
    @Path("compra/passagens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String webCompraPassagem(String data) {
        Gson g = new Gson();
        ConsultaPassagem cp = g.fromJson(data, ConsultaPassagem.class);
        
        Map<String, Passagem> out = compraPassagem(cp);
        
        return g.toJson(out);
    }
    
    /** Makes a purchase of a plane ticket registered in the database.
     *
     * @param cp : An object that holds the parameters of the plane ticket.
     * @return Map<String, Passagem> : A map where the keys are "Ida" and "Volta".
     * Each holds an object with the bought ticket.
     */
    public Map<String, Passagem> compraPassagem(ConsultaPassagem cp) {

        // Getting tickets that matches the query.
        Map<String, List<Passagem>> consultaRetorno = consultaPassagem(cp);

        // Checking if really found a valid query.
        if (consultaRetorno == null)
            return null;

        // Trying to find a ticket that has a matching price with the query.
        Passagem passagemIdaComprada = null, passagemVoltaComprada = null;

        for (Passagem p : consultaRetorno.get("Ida")) {            
            if (Objects.equals(Float.valueOf(p.getPrice()), Float.valueOf(cp.getPrice()))) {
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
                if (Objects.equals(Float.valueOf(p.getPrice()), Float.valueOf(cp.getPrice()))) {
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
    
    /** Web wrapper for compraHospedagem.
     *
     * @param data : A JSON string with the lodging requested.
     * @return String : A JSON string with the object representing the bought lodging stay.
     */
    @POST
    @Path("compra/hospedagens")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String webCompraHospedagem(String data) {
        Gson g = new Gson();
        ConsultaHospedagem ch = g.fromJson(data, ConsultaHospedagem.class);
        
        Hospedagem out = compraHospedagem(ch);
        
        return g.toJson(out);
    }
    
    /** Makes a purchase of a lodging stay registered in the database.
     *
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return Hospedagem : An object representing the bought lodging stay.
     */
    public Hospedagem compraHospedagem(ConsultaHospedagem ch) {
        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        Hospedagem hospedagemComprada = null;

        for (Hospedagem h : hospedagens) {
            if (Objects.equals(Float.valueOf(h.getPrice()), Float.valueOf(ch.getPrice()))) {
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

    /** Web wrapper for compraPacote.
     *
     * @param data String: A JSON string with the lodging and ticket requested.
     * @return String: A JSON string with the object representing the bought lodging and ticket.
     */
    @POST
    @Path("compra/pacotes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String webCompraPacote(String data) {
        Gson g = new Gson();
        ConsultaPacote cpkg = g.fromJson(data, ConsultaPacote.class);
        ConsultaPassagem cp = cpkg.consultaPassagem;
        ConsultaHospedagem ch = cpkg.consultaHospedagem;
        
        CompraPacoteResponse out = compraPacote(cp, ch);
        
        return g.toJson(out);
    }
    
    /** Makes a purchase of a lodging stay and a plane ticket registered in the database.
     *
     * @param cp ConsultaPassagem: An object that holds the parameters of the plane ticket.
     * @param ch ConsultaHospedagem: An object that holds the parameters of the lodge.
     * @return CompraPacoteResponse: An object representing the bought package.
     */
    public CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {        
        List<Hospedagem> hospedagens = consultaHospedagem(ch);

        if (hospedagens == null)
            return null;

        Hospedagem hospedagemComprada = null;

        for (Hospedagem h : hospedagens) {
            if (Objects.equals(Float.valueOf(h.getPrice()), Float.valueOf(ch.getPrice()))) {
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
            if (Objects.equals(Float.valueOf(p.getPrice()), Float.valueOf(cp.getPrice()))) {
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
                if (Objects.equals(Float.valueOf(p.getPrice()), Float.valueOf(cp.getPrice()))) {
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
            
            System.out.println("Comprou o pacote");
            return new CompraPacoteResponse(hospedagemComprada, passagensCompradas);
        }
    }

    /** Adds a new ticket to the list of available tickets.
     *
     * @param data : The JSON String containing a ticket structure.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add/passagens")
    public String adicionaPassagem(String data) {
        Gson g = new Gson();
        Passagem p = g.fromJson(data, Passagem.class);
        System.out.println("Adicionei passagem de " + p.getOrigin() + " para " + p.getDestination() + " dia " + 
                Integer.toString(p.getGoingDate()) + ". Vagas = " + Integer.toString(p.getAvailableSpots()));
        passagensDisponiveis.add(p);
        
        return "";
    }

    /** Adds a new lodging to the list of available lodgings.
     *
     * @param data String: The JSON String containing a lodging structure.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add/hospedagens")
    public String adicionaHospedagem(String data) {
        // Goes through existing lodgings by location and checks if there was
        // already a lodging defined with that name.
        Gson g = new Gson();
        Hospedagem novaHospedagem = g.fromJson(data, Hospedagem.class);
        
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
        
        System.out.println("Adicionei hospedagem em " + novaHospedagem.getLocation());
        
        return "";
    }
}