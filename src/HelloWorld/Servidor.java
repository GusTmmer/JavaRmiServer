/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author a1729756
 */
public class Servidor {
    
    public static void main(String[] args) {
        
        ServImpl serv_impl;
        
        try {
            serv_impl = new ServImpl();
            
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(2000);
            referenciaServicoNomes.rebind("Trivago", serv_impl);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        CommandParser parser = new CommandParser(serv_impl, scanner);
        
        while(true)
        {
            String command_input = scanner.nextLine();
            
            parser.parseCommand(command_input);
        }
    }
}
