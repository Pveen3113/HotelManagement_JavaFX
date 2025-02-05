package com.example.teest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentManagement {
    private Stage stage;

    @FXML
    private ListView<String> bookingListView;

    private ObservableList<String> bookings = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
        loadBookingData();
    }

    private void loadBookingData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("booking_details.txt"));
            bookings.clear();
            for (String line : lines) {
                String[] parts = line.split(",");
                String bookingID = parts[0];
                String status = parts[6].equals("0") ? "Pending      ⏳" : "Completed ✔";
                bookings.add(bookingID + " - " + status);
            }
            bookingListView.setItems(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMakePayment() {
        String selectedItem = bookingListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select a booking.");
            return;
        }

        String bookingID = selectedItem.split(" - ")[0];

        try {
            List<String> lines = Files.readAllLines(Paths.get("booking_details.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookingID) && parts[6].equals("0")) {
                    showPaymentPopup(parts);
                    return;
                }
            }
            showAlert("Payment Completed", "This payment has already been completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPaymentPopup(String[] bookingDetails) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Make Payment");
        popup.initStyle(javafx.stage.StageStyle.UNDECORATED);


        Label detailsLabel = new Label("Booking Details:");
        detailsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label bookingInfo = new Label(
                "Booking ID: " + bookingDetails[0] + "\n" +
                        "Name: " + bookingDetails[1] + "\n" +
                        "Check-in: " + bookingDetails[3] + "\n" +
                        "Check-out: " + bookingDetails[4] + "\n" +
                        "Price: RM " + bookingDetails[5]
        );
        bookingInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        VBox bookingDetailsContainer = new VBox(10, detailsLabel, bookingInfo);
        bookingDetailsContainer.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15;");
        bookingDetailsContainer.setPrefWidth(300);

        // Payment Mode ComboBox
        Label paymentMethodLabel = new Label("Payment Method:");
        paymentMethodLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        ComboBox<String> paymentMode = new ComboBox<>();
        paymentMode.getItems().addAll("Cash", "E-wallet", "Atome", "Credit/Debit Card");
        paymentMode.setPromptText("Select Payment Mode");
        paymentMode.setPrefWidth(500);

        VBox paymentMethodContainer = new VBox(5, paymentMethodLabel, paymentMode);
        paymentMethodContainer.setSpacing(10);

        // Amount TextField
        Label amountLabel = new Label("Amount:");
        amountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount in RM");
        amountField.setPrefWidth(300);

        VBox amountContainer = new VBox(5, amountLabel, amountField);
        amountContainer.setSpacing(10);


        // Pay Button
        Button payButton = new Button("PAY");
        payButton.setStyle("-fx-background-color: #A7B49E; -fx-text-fill: #34495e; -fx-font-weight: bold;");
        payButton.setPrefWidth(200);
        payButton.setOnAction(e -> processPayment(popup, bookingDetails, amountField.getText(), paymentMode.getValue()));

        Button cancelButton = new Button("CANCEL");
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setPrefWidth(200);
        cancelButton.setOnAction(e -> popup.close());

        HBox buttonContainer = new HBox(20, payButton, cancelButton);
        buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);

        // Layout and Styling
        VBox layout = new VBox(20, bookingDetailsContainer, paymentMethodContainer, amountContainer, buttonContainer);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 15; -fx-border-color: white;");

        Scene scene = new Scene(layout, 450, 400);
        popup.setScene(scene);
        popup.show();
    }

    private void processPayment(Stage popup, String[] bookingDetails, String amount, String paymentMode) {
        try {
            double enteredAmount = Double.parseDouble(amount);
            double requiredAmount = Double.parseDouble(bookingDetails[5]);

            if (enteredAmount < requiredAmount) {
                showAlert("Insufficient Payment", "Please ensure the amount is sufficient.");
                return;
            }


            List<String> lines = Files.readAllLines(Paths.get("booking_details.txt"));
            List<String> updatedLines = lines.stream()
                    .map(line -> line.startsWith(bookingDetails[0]) ? line.replace(",0", ",1") : line)
                    .collect(Collectors.toList());
            Files.write(Paths.get("booking_details.txt"), updatedLines);


            try (BufferedWriter writer = new BufferedWriter(new FileWriter("payment_details.txt", true))) {
                writer.write(bookingDetails[0] + "," + enteredAmount + "," + paymentMode + "," + LocalDate.now());
                writer.newLine();
            }

            showAlert("Payment Successful", "Payment has been successfully processed.");
            popup.close();
            loadBookingData();  // Refresh list
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid number.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewPaymentDetails() {
        String selectedItem = bookingListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select a booking.");
            return;
        }

        String bookingID = selectedItem.split(" - ")[0];

        try {
            List<String> lines = Files.readAllLines(Paths.get("booking_details.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookingID)) {
                    if (parts[6].equals("0")) {
                        showAlert("Payment Pending", "Payment has not been made yet.");
                    } else {
                        showPaymentDetailsPopup(bookingID);
                    }
                    return;
                }
            }
            showAlert("Booking Not Found", "No matching booking found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPaymentDetailsPopup(String bookingID) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Payment Details");
        popup.initStyle(javafx.stage.StageStyle.UNDECORATED);  // Removes default window frame for a modern look

        String paymentDetails = findPaymentDetails(bookingID);
        if (paymentDetails == null) {
            showAlert("No Payment Record", "Payment details not found.");
            return;
        }

        // Title Label
        Label titleLabel = new Label("Payment Details");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        // Payment Details Section
        Label detailsLabel = new Label(paymentDetails);
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        VBox detailsContainer = new VBox(detailsLabel);
        detailsContainer.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #497D74; -fx-text-fill: white;  -fx-font-weight: bold;");
        closeButton.setPrefWidth(100);
        closeButton.setOnAction(e -> popup.close());

        VBox layout = new VBox(20, titleLabel, detailsContainer, closeButton);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-background-color: #EFE9D5; -fx-padding: 20; -fx-background-radius: 15;");

        Scene scene = new Scene(layout, 400, 250);
        popup.setScene(scene);
        popup.show();
    }

    private String findPaymentDetails(String bookingID) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("payment_details.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookingID)) {
                    return "Booking ID: " + parts[0] +
                            "\nAmount Paid: RM " + parts[1] +
                            "\nPayment Mode: " + parts[2] +
                            "\nDate: " + parts[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @FXML
    private void handleBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PaymentController paymentController = fxmlLoader.getController();
        paymentController.setStage(stage);
        stage.setScene(scene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
