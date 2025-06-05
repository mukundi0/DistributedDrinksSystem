package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DrinksService extends Remote {
    String placeOrder(String customerName, String branch, String drink, int quantity) throws RemoteException;
    List<String> getBranchReport(String branch) throws RemoteException;
    List<String> getAllSalesReport() throws RemoteException;
    boolean isLowStock(String branch, String drink) throws RemoteException;
}
