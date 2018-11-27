/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloWorld;

import java.rmi.RemoteException;

/**
 *
 * @author a1236776
 */
public interface ICoordServerFromSupplier {
    
    boolean getStatus(int transactionId) throws RemoteException;
}
