package com.example.teest;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;

public class MakeBookingController {

    private final String ROOM_FILENAME = "room_details.txt";

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private ComboBox<String> guestNameComboBox;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    public void initialize() throws IOException {
        loadRoomTypes();
        loadGuestNames();
    }

    private void loadRoomTypes() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("room_details.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            // This is to only allow rooms that are available to be booked
            if(Integer.parseInt(details[2])!=0){
                roomTypeComboBox.getItems().add(details[0]); // Add room type
            }
        }
        reader.close();
    }

    private void loadGuestNames() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("guest_details.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            guestNameComboBox.getItems().add(details[0]); // Add guest name
        }
        reader.close();
    }

    @FXML
    protected void confirmBooking(ActionEvent event) throws IOException {
        String roomType = roomTypeComboBox.getValue();
        String guestName = guestNameComboBox.getValue();
        long days = ChronoUnit.DAYS.between(checkInDatePicker.getValue(), checkOutDatePicker.getValue());

        BufferedReader reader = new BufferedReader(new FileReader("room_details.txt"));
        String line;
        double roomPrice = 0;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            if (details[0].equals(roomType)) {
                roomPrice = Double.parseDouble(details[1]);
                break;
            }
        }
        reader.close();

        double totalPrice = roomPrice * days;
        // Generate a unique booking ID
        HashSet<String> existingIds = new HashSet<>();
        Path bookingFilePath = Paths.get("booking_details.txt");

        if (Files.exists(bookingFilePath)) {
            BufferedReader bookingReader = new BufferedReader(new FileReader("booking_details.txt"));
            String bookingLine;
            while ((bookingLine = bookingReader.readLine()) != null) {
                String[] bookingDetails = bookingLine.split(",");
                if (bookingDetails.length > 0) {
                    existingIds.add(bookingDetails[0]); // First column is the booking ID
                }
            }
            bookingReader.close();
        }
        String bookingId;
        int payment = 0;
        Random random = new Random();
        do {
            bookingId = "BKG" + (10000 + random.nextInt(90000)); // Generates BKGxxxxx (5-digit number)
        } while (existingIds.contains(bookingId));

        // Save booking details
        updateRoomAvailability(roomType);
        BufferedWriter writer = new BufferedWriter(new FileWriter("booking_details.txt", true));
        writer.write(bookingId + "," + guestName + "," + roomType + "," + checkInDatePicker.getValue() + "," + checkOutDatePicker.getValue() + "," + totalPrice + "," + payment);
        writer.newLine();
        writer.close();

        // Show success message
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Confirmed");
        alert.setHeaderText(null);
        alert.setContentText("A booking has been made for " + guestName + " for the room type " + roomType +
                " from " + checkInDatePicker.getValue() + " till " + checkOutDatePicker.getValue() +
                " which cost RM " + totalPrice + ". Please make the payment soon for the Booking ID : " + bookingId);
        alert.showAndWait();

        // Close the popup
        closePopup(event);
    }
    public  void updateRoomAvailability(String roomType) {
        File inputFile = new File(ROOM_FILENAME);
        File tempFile = new File("temp_room_details.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                int newUnitsAvailable = 0;
                if (details.length == 3 && details[0].equals(roomType)) {
                    newUnitsAvailable = Integer.parseInt(details[2]);
                    newUnitsAvailable -= 1;
                    writer.write(details[0] + "," + details[1] + "," + newUnitsAvailable);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating guest: " + e.getMessage());
        }

        // Replace the original file with the updated file
        if (!inputFile.delete()) {
            System.out.println("Failed to delete the original file.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Failed to rename the temp file.");
        }
    }


    @FXML
    protected void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
