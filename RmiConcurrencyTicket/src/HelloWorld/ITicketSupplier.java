package HelloWorld;

import Consultas.ConsultaPassagem;
import Supervisionados.Passagem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


/**
 *
 * Interface representing available methods in the server.
 */
public interface ITicketSupplier extends Remote
{
    Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) throws RemoteException;

    Map<String, Passagem> compraPassagem(ConsultaPassagem cp) throws RemoteException;
    
    void adicionaPassagem(Passagem p) throws RemoteException;
}