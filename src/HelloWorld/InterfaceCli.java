package HelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author a1729756
 */
public interface InterfaceCli extends Remote 
{
    public void printNotification(String str) throws RemoteException;
}
