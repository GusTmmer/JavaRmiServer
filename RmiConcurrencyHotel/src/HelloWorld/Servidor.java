package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * The main class. Creates a client instance and gets user input.
 */
public class Servidor {
    
    public static void main(String[] args) {
        
        HotelSupplier server;
        
        try {
            server = new HotelSupplier();
            
            Registry serviceNames = LocateRegistry.createRegistry(2222);
            serviceNames.rebind("TrivagoH", server);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        server.restoreFromBackup();
    }
}
