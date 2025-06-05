package client.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private Customer customer;
    private Drink drink;
    private int quantity;
    private String branch;
    private LocalDateTime dateTime;

    public Order(int id, Customer customer, Drink drink, int quantity, String branch, LocalDateTime dateTime) {
        this.id = id;
        this.customer = customer;
        this.drink = drink;
        this.quantity = quantity;
        this.branch = branch;
        this.dateTime = dateTime;
    }

    public Order(Customer customer, Drink drink, int quantity, String branch, LocalDateTime dateTime) {
        this.customer = customer;
        this.drink = drink;
        this.quantity = quantity;
        this.branch = branch;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    public Drink getDrink(){
        return drink;
    }
    public void setDrink(Drink drink){
        this.drink = drink;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public String getBranch(){
        return branch;
    }
    public void setBranch(String branch){
        this.branch = branch;
    }
    public LocalDateTime getDateTime(){
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

}
