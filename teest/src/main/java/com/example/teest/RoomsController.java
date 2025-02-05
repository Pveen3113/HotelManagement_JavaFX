package com.example.teest;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RoomsController {
    @FXML
    private TextField roomTypeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField availabilityField;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void saveRoom() {
        // Capture input
        String roomType = roomTypeField.getText().trim();
        String price = priceField.getText().trim();
        String availability = availabilityField.getText().trim();

        // Validate input
        if (roomType.isEmpty() || price.isEmpty() || availability.isEmpty()) {
            showAlert("Error", "All fields must be filled in!");
            return;
        }

        try {
            double priceValue = Double.parseDouble(price);
            int availabilityValue = Integer.parseInt(availability);

            // Save data to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("ROOMS", true))) {
                writer.write("Room Type: " + roomType);
                writer.newLine();
                writer.write("Price: " + priceValue);
                writer.newLine();
                writer.write("Availability: " + availabilityValue);
                writer.newLine();
                writer.write("----------------------------");
                writer.newLine();
            }

            // Clear fields after saving
            roomTypeField.clear();
            priceField.clear();
            availabilityField.clear();

            showAlert("Success", "Room details saved successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Price and availability must be numbers!");
        } catch (IOException e) {
            showAlert("Error", "Failed to save room details!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
