package HelloWorld;

import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import Log.LogHandler;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;
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
public class CoordServer extends UnicastRemoteObject implements ICoordServerFromClient
{   
    private LogHandler log = new LogHandler("coord.log");
    
  
    
    CoordServer() throws RemoteException {

    }

    /** Makes the query of a plane ticket.
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @return Map<String, List<Passagem>> A list containing all matching lodgings. Be it going or returning.
     */
    @Override
    public Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) {
        return null;
    }

    /** Makes the query of a lodging.
     *
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return List<Hospedagem> A list containing all matching lodgings.
     */
    @Override
    public List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) {
        return null;
    }

    /** Makes the query of a travel package (Plane ticket and Lodging).
     *
     * @param cp ConsultaPassagem : An object that holds the parameters of the plane ticket.
     * @param ch ConsultaHospedagem : An object that holds the parameters of the lodge.
     * @return ConsultaPacoteResponse A specialized structure that holds the return of this query.
     */
    @Override
    public ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {
        return null;
    }

    /** Makes a purchase of a plane ticket registered in the database.
     *
     * @param cp : An object that holds the parameters of the plane ticket.
     * @return Map<String, Passagem> A map where the keys are "Ida" and "Volta".
     * Each holds an object with the bought ticket.
     */
    @Override
    public Map<String, Passagem> compraPassagem(ConsultaPassagem cp) {
        return null;
    }

    /** Makes a purchase of a lodging stay registered in the database.
     *
     * @param ch : An object that holds the parameters of the lodge.
     * @return An object representing the bought lodging stay.
     */
    @Override
    public Hospedagem compraHospedagem(ConsultaHospedagem ch) {
        return null;
    }

    /** Makes a purchase of a lodging stay and a plane ticket registered in the database.
     *
     * @param cp : An object that holds the parameters of the plane ticket.
     * @param ch : An object that holds the parameters of the lodge.
     * @return An object representing the bought lodging stay.
     */
    @Override
    public CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) {
        
        synchronized(this) {
            try {
                log.write(Integer.toString(++transacoes));
            } catch (IOException ex) {

            }
        }
        
        

        
    }
    
    
}
}
