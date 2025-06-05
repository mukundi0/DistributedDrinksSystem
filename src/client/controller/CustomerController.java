package client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import shared.DrinksService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CustomerController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> branchBox;
    @FXML private ComboBox<String> drinkBox;
    @FXML private TextField quantityField;
    @FXML private Label statusLabel;

    private DrinksService service;

    @FXML
    public void initialize() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (DrinksService) registry.lookup("DrinksService");

            // Populate static branches and sample drinks
            branchBox.getItems().addAll("NAKURU", "MOMBASA", "KISUMU", "NAIROBI");
            drinkBox.getItems().addAll("Coca-Cola", "Pepsi", "Fanta", "Sprite");

        } catch (Exception e) {
            statusLabel.setText("Error connecting to server");
            e.printStackTrace();
        }
    }

    @FXML
    public void placeOrder() {
        try {
            String name = nameField.getText();
            String branch = branchBox.getValue();
            String drink = drinkBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());

            String result = service.placeOrder(name, branch, drink, quantity);
            statusLabel.setText(result);
        } catch (Exception e) {
            statusLabel.setText("Failed to place order.");
            e.printStackTrace();
        }
    }
}
