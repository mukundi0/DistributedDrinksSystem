package server;

import shared.DrinksService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public  static void main(String[] args) {
        try{
            DrinksService service = new DrinksServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("DrinksService", service);
            System.out.println("Server is running..");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
