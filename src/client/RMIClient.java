package client;

import shared.DrinksService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            DrinksService service = (DrinksService) registry.lookup("DrinksService");

            System.out.println(service.placeOrder("John", "NAKURU","Coca-Cola", 3));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
