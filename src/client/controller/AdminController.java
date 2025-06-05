package client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import shared.DrinksService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class AdminController {

    @FXML private ComboBox<String> branchBox;
    @FXML private ListView<String> branchReportList;
    @FXML private ListView<String> totalReportList;

    private DrinksService service;

    @FXML
    public void initialize() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (DrinksService) registry.lookup("DrinksService");

            branchBox.getItems().addAll("NAKURU", "MOMBASA", "KISUMU", "NAIROBI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadBranchReport() {
        try {
            String branch = branchBox.getValue();
            List<String> report = service.getBranchReport(branch);
            branchReportList.getItems().setAll(report);
        } catch (Exception e) {
            branchReportList.getItems().setAll("Error loading report.");
            e.printStackTrace();
        }
    }

    @FXML
    public void loadTotalReport() {
        try {
            List<String> report = service.getAllSalesReport();
            totalReportList.getItems().setAll(report);
        } catch (Exception e) {
            totalReportList.getItems().setAll("Error loading total report.");
            e.printStackTrace();
        }
    }
}
