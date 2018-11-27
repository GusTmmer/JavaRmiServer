package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * The main class. Creates a client instance and gets user input.
 */
public class Servidor {
    
    public static void main(String[] args) {
        
        TicketSupplier server;
        
        try {
            server = new TicketSupplier();
            
            Registry serviceNames = LocateRegistry.createRegistry(2000);
            serviceNames.rebind("Trivago", server);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        
        CommandParser parser = new CommandParser(server, scanner);
        
        while (true)
        {
            String commandInput = scanner.nextLine();
            
            parser.parseCommand(commandInput);
        }
    }
}
