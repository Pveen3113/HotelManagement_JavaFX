package com.example.teest;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewBookingsController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, String> bookingIDCol;
    @FXML
    private TableColumn<Booking, String> guestNameCol;
    @FXML
    private TableColumn<Booking, String> roomTypeCol;
    @FXML
    private TableColumn<Booking, LocalDate> checkInCol;
    @FXML
    private TableColumn<Booking, LocalDate> checkOutCol;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    public void initialize() {
        bookingsTable.getColumns().clear();
        bookingsTable.getColumns().addAll(bookingIDCol, guestNameCol, roomTypeCol, checkInCol, checkOutCol);

        // Set up column bindings
        bookingIDCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBookingID()));
        guestNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuestName()));
        guestNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGuestName()));
        roomTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomType()));
        checkInCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCheckInDate()));
        checkOutCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCheckOutDate()));

        // Load bookings from file
        loadBookings();
    }

    protected void loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("booking_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 7) {
                    System.err.println("Skipping invalid line: " + line);
                    continue; // Skip improperly formatted lines
                }

                try {
                    String bookingID = details[0];
                    String guestName = details[1];
                    String roomType = details[2];
                    LocalDate checkInDate = LocalDate.parse(details[3]);
                    LocalDate checkOutDate = LocalDate.parse(details[4]);
                    double price = Double.parseDouble(details[5]);
                    int paymentStatus = Integer.parseInt(details[6]);

                    bookings.add(new Booking(bookingID, guestName, roomType, checkInDate, checkOutDate, price , paymentStatus));
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Load bookings into the table
        bookingsTable.getItems().setAll(bookings);
        System.out.println("Loaded Bookings: " + bookings.size());

    }



    @FXML
    protected void viewBookingDetails() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            showBookingDetailsPopup(selectedBooking);
        }else {
            // Show alert if no booking is selected
            new Alert(Alert.AlertType.WARNING, "Please select a booking first.").showAndWait();
        }
    }

    private void showBookingDetailsPopup(Booking booking) {
        String title = "Booking Details";
        String payment_status;
        if(booking.getPaymentStatus()==1){
            payment_status = "Paid ✔";
        }
        else {
            payment_status = "Pending ⏳";}
        String content = "Guest Name: " + booking.getGuestName() +
                "\nRoom Type: " + booking.getRoomType() +
                "\nCheck-in Date: " + booking.getCheckInDate() +
                "\nCheck-out Date: " + booking.getCheckOutDate() +
                "\nTotal Price: " + booking.getPrice() +"\nPayment Status: "+ payment_status;
        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);  // Removes the title bar
        popup.initModality(Modality.APPLICATION_MODAL);  // Blocks interaction with other windows until closed

        // Create content
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: #EFE9D5; -fx-padding: 20; -fx-background-radius: 15;");  // Curved borders with background color

        // Title label
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Content label
        Label message = new Label(content);
        message.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14px;");

        // Close button
        Button closeButton = new Button("OK");
        closeButton.setStyle("-fx-background-color: #497D74; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        closeButton.setOnAction(e -> popup.close());

        layout.getChildren().addAll(titleLabel, message, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Set up the scene
        Scene scene = new Scene(layout, 450, 300);
        popup.setScene(scene);
        popup.showAndWait();
    }

    @FXML
    protected void updateBooking(ActionEvent event) throws IOException {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-booking-popup.fxml"));
            Parent popupRoot = loader.load();
            Stage popupStage = new Stage();
            UpdateBookingController updateBookingController = loader.getController();
            updateBookingController.setBookingDetails(selectedBooking.getBookingID(),selectedBooking.getGuestName(),selectedBooking.getRoomType(),selectedBooking.getCheckInDate(),selectedBooking.getCheckOutDate(),selectedBooking.getPrice(),selectedBooking.getPaymentStatus());
            updateBookingController.setViewBookingsController(this);


            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Update a Booking");
            popupStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();
            bookingsTable.refresh(); // Refresh table to reflect changes
        } else {
//            showAlert("No Selection", "Please select a booking to update.");
        }
    }

    @FXML
    protected void deleteBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Booking");
            alert.setHeaderText("Are you sure you want to delete this booking?");
            alert.setContentText("Guest: " + selectedBooking.getGuestName() +
                    "\nRoom: " + selectedBooking.getRoomType() +
                    "\nFrom: " + selectedBooking.getCheckInDate() +
                    " To: " + selectedBooking.getCheckOutDate());

            if (alert.showAndWait().get() == ButtonType.OK) {
                deleteBookingFromFile(selectedBooking);
            }
        }
    }

    private void deleteBookingFromFile(Booking booking) {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("booking_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (!(details[0].equals(booking.getBookingID()))){
                    bookings.add(new Booking(details[0] , details[1], details[2], LocalDate.parse(details[3]), LocalDate.parse(details[4]) ,Double.parseDouble(details[5]), Integer.parseInt(details[6])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("booking_details.txt"))) {
            for (Booking b : bookings) {
                writer.write(b.getBookingID() + "," + b.getGuestName() + "," + b.getRoomType() + "," + b.getCheckInDate() + "," + b.getCheckOutDate() + "," + b.getPrice() + "," + b.getPaymentStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookingsTable.getItems().remove(booking);
    }

    @FXML
    protected void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("booking-management.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            BookingController bookingController = loader.getController();
            bookingController.setStage(stage);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}