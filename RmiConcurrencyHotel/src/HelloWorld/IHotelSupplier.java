package HelloWorld;


import Consultas.ConsultaHospedagem;
import Supervisionados.Hospedagem;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/**
 *
 * Interface representing available methods in the server.
 */
public interface IHotelSupplier extends Remote
{   
    void adicionaHospedagem(Hospedagem h) throws RemoteException;
    
    List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) throws RemoteException;

    Hospedagem compraHospedagem(ConsultaHospedagem ch) throws RemoteException;
}