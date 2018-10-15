/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import Consultas.CompraPacoteResponse;
import Consultas.ConsultaHospedagem;
import Consultas.ConsultaPacoteResponse;
import Consultas.ConsultaPassagem;
import Events.IEvent;
import Supervisionados.Hospedagem;
import Supervisionados.Passagem;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


/**
 *
 * @author a1729756
 */
public interface InterfaceServ extends Remote
{
    public Map<String, List<Passagem>> consultaPassagem(ConsultaPassagem cp) throws RemoteException;
    
    public List<Hospedagem> consultaHospedagem(ConsultaHospedagem ch) throws RemoteException;
    
    public ConsultaPacoteResponse consultaPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException;

    public Map<String, Passagem> compraPassagem(ConsultaPassagem cp) throws RemoteException;

    public Hospedagem compraHospedagem(ConsultaHospedagem ch) throws RemoteException;

    public CompraPacoteResponse compraPacote(ConsultaPassagem cp, ConsultaHospedagem ch) throws RemoteException;
    
    public void registraInteresse(IEvent event, InterfaceCli cli) throws RemoteException;
    
    public void removeInteresse(IEvent event, InterfaceCli cli) throws RemoteException;
    
    class HospedagemEvent implements IEvent {
        
    }

    // How to get the class of a object.
    /*
    public interface MyInterface { }
  static class AClass implements MyInterface { }

  public static void main(String[] args) {
      MyInterface object = new AClass();
      System.out.println(object.getClass());
    */
}