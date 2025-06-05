package server;

import client.model.Customer;
import client.model.Drink;
import client.model.Order;
import shared.DrinksService;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinksServiceImpl extends UnicastRemoteObject implements DrinksService {

    protected DrinksServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String placeOrder(String customerName, String branch, String drinkName, int quantity) throws RemoteException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Begin transaction

            // 1. Insert customer if not exists
            int customerId = getOrInsertCustomer(conn, customerName);

            // 2. Get drink ID and stock
            PreparedStatement ps = conn.prepareStatement("SELECT id, stock FROM drinks WHERE name = ?");
            ps.setString(1, drinkName);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return "Drink not found!";
            int drinkId = rs.getInt("id");
            int stock = rs.getInt("stock");

            if (stock < quantity) return "Not enough stock!";

            // 3. Insert order
            PreparedStatement orderStmt = conn.prepareStatement(
                    "INSERT INTO orders (customer_id, drink_id, quantity, branch) VALUES (?, ?, ?, ?)");
            orderStmt.setInt(1, customerId);
            orderStmt.setInt(2, drinkId);
            orderStmt.setInt(3, quantity);
            orderStmt.setString(4, branch);
            orderStmt.executeUpdate();

            // 4. Update stock
            PreparedStatement updateStock = conn.prepareStatement(
                    "UPDATE drinks SET stock = stock - ? WHERE id = ?");
            updateStock.setInt(1, quantity);
            updateStock.setInt(2, drinkId);
            updateStock.executeUpdate();

            conn.commit();
            return "Order placed successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Order failed!";
        }
    }

    private int getOrInsertCustomer(Connection conn, String name) throws SQLException {
        // Simple way to get or insert a customer
        PreparedStatement select = conn.prepareStatement("SELECT id FROM customers WHERE name = ?");
        select.setString(1, name);
        ResultSet rs = select.executeQuery();
        if (rs.next()) return rs.getInt("id");

        PreparedStatement insert = conn.prepareStatement("INSERT INTO customers (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        insert.setString(1, name);
        insert.executeUpdate();
        ResultSet keys = insert.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    @Override
    public List<String> getBranchReport(String branch) throws RemoteException {
        List<String> report = new ArrayList<>();
        String sql = """
            SELECT d.name, SUM(o.quantity) as total_qty, SUM(o.quantity * d.price) as revenue
            FROM orders o
            JOIN drinks d ON o.drink_id = d.id
            WHERE branch = ?
            GROUP BY d.name
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, branch);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                report.add("Drink: " + rs.getString("name") +
                        ", Qty: " + rs.getInt("total_qty") +
                        ", Revenue: KES " + rs.getDouble("revenue"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            report.add("Error generating report");
        }

        return report;
    }

    @Override
    public List<String> getAllSalesReport() throws RemoteException {
        List<String> report = new ArrayList<>();
        String sql = """
            SELECT branch, SUM(o.quantity * d.price) as total_sales
            FROM orders o
            JOIN drinks d ON o.drink_id = d.id
            GROUP BY branch
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            double grandTotal = 0;
            while (rs.next()) {
                double branchTotal = rs.getDouble("total_sales");
                report.add("Branch: " + rs.getString("branch") + " → KES " + branchTotal);
                grandTotal += branchTotal;
            }

            report.add("TOTAL BUSINESS REVENUE: KES " + grandTotal);
        } catch (Exception e) {
            e.printStackTrace();
            report.add("Error generating report");
        }

        return report;
    }

    @Override
    public boolean isLowStock(String branch, String drinkName) throws RemoteException {
        String sql = "SELECT stock FROM drinks WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drinkName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock") < 10; // Example threshold
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
