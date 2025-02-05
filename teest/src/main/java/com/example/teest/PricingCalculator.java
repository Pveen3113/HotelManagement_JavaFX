package com.example.teest;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricingCalculator {

    private Map<String, Double> roomPrices = new HashMap<>();

    public PricingCalculator() {
        loadRoomDetails();
    }

    private void loadRoomDetails() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("room_details.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    roomPrices.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showPricingPopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(javafx.stage.StageStyle.UNDECORATED);
        popup.setTitle("Price Calculator");

        // Labels and ComboBox for Room Type
        Label roomLabel = new Label("Select Room Type:");
        roomLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        ComboBox<String> roomTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(roomPrices.keySet()));
        roomTypeComboBox.setStyle("-fx-font-size: 14px; -fx-pref-width: 250px;");

        // Date Pickers for Check-in and Check-out Dates
        Label checkInLabel = new Label("Check-in Date:");
        checkInLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        DatePicker checkInDatePicker = new DatePicker();

        Label checkOutLabel = new Label("Check-out Date:");
        checkOutLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        DatePicker checkOutDatePicker = new DatePicker();

        // Total Price Label with Styling
        Label totalPriceLabel = new Label("Total Price: RM 0.00");
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-radius: 5;");

        // Calculate Button
        Button calculateButton = new Button("Calculate");
        calculateButton.setStyle("-fx-background-color: #497D74; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 120px;");
        calculateButton.setOnAction(e -> {
            LocalDate checkIn = checkInDatePicker.getValue();
            LocalDate checkOut = checkOutDatePicker.getValue();
            String selectedRoom = roomTypeComboBox.getValue();

            if (checkIn == null || checkOut == null || selectedRoom == null) {
                showAlert("Invalid Selection", "Please select all fields.");
                return;
            }

            if (checkOut.isBefore(checkIn)) {
                showAlert("Invalid Dates", "Check-out date must be after check-in date.");
                return;
            }

            long daysStayed = ChronoUnit.DAYS.between(checkIn, checkOut);
            double roomPrice = roomPrices.getOrDefault(selectedRoom, 0.0);
            double totalPrice = daysStayed * roomPrice;
            totalPriceLabel.setText("Total Price: RM " + String.format("%.2f", totalPrice));
        });

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 120px;");
        closeButton.setOnAction(e -> popup.close());

        // Layout
        VBox layout = new VBox(15, roomLabel, roomTypeComboBox, checkInLabel, checkInDatePicker, checkOutLabel, checkOutDatePicker, calculateButton, totalPriceLabel, closeButton);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #EFE9D5; -fx-border-radius: 10; -fx-background-radius: 10;");

        popup.setScene(new Scene(layout, 400, 500));
        popup.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
