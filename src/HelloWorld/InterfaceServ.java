package HelloWorld;

import ClientEvents.IEvent;
import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


/**
 *
 * Interface representing available methods in the server.
 */
public interface InterfaceServ extends Remote
{
    Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) throws RemoteException;
    
    List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) throws RemoteException;
    
    ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException;

    Map<String, Passagem> compraPassagem(ConsultaPassagem cp) throws RemoteException;

    Hospedagem compraHospedagem(ConsultaHospedagem ch) throws RemoteException;

    CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException;
    
    void registraInteresse(InterfaceCli cli, IEvent event) throws RemoteException;
    
    void removeInteresse(InterfaceCli cli, IEvent event) throws RemoteException;
}