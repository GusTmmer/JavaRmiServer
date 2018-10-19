package HelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * Interface representing available methods in the client.
 */
public interface InterfaceCli extends Remote 
{
    public void printNotification(String str) throws RemoteException;
}
