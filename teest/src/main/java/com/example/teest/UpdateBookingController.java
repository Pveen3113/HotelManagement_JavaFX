package com.example.teest;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UpdateBookingController {


    private String bookingID;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private ComboBox<String> guestNameComboBox;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    private String originalBookingDetails;

    private ViewBookingsController viewBookingsController;

    private double price;

    private int paymentStatus;

    @FXML
    public void initialize() throws IOException {
        loadRoomTypes();
        loadGuestNames();
    }
    public void setViewBookingsController(ViewBookingsController controller) {
        this.viewBookingsController = controller;
    }

    private void loadRoomTypes() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("room_details.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            roomTypeComboBox.getItems().add(details[0]); // Add room type
        }
        reader.close();
    }

    private void loadGuestNames() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("guest_details.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            guestNameComboBox.getItems().add(details[0]);
        }
        reader.close();
    }

    public void setBookingDetails(String bookingID, String guestname, String roomtype , LocalDate checkindate , LocalDate checkoutdate , double price , int paymentstatus) {
        this.bookingID = bookingID;

        guestNameComboBox.setValue(guestname);
        roomTypeComboBox.setValue(roomtype);
        checkInDatePicker.setValue(checkindate);
        checkOutDatePicker.setValue(checkoutdate);
        this.price = price;
        this.paymentStatus = paymentstatus;

    }

    @FXML
    protected void confirmUpdate(ActionEvent event) throws IOException {
        String updatedGuestName = guestNameComboBox.getValue();
        String updatedRoomType = roomTypeComboBox.getValue();
        String updatedCheckIn = checkInDatePicker.getValue().toString();
        String updatedCheckOut = checkOutDatePicker.getValue().toString();

        String bookingID = this.bookingID;
        String payment_status = String.valueOf(this.paymentStatus);
        long days = ChronoUnit.DAYS.between(checkInDatePicker.getValue(), checkOutDatePicker.getValue());

        BufferedReader reader = new BufferedReader(new FileReader("room_details.txt"));
        String line;
        double roomPrice = 0;
        while ((line = reader.readLine()) != null) {
            String[] details = line.split(",");
            if (details[0].equals(updatedRoomType)) {
                roomPrice = Double.parseDouble(details[1]);
                break;
            }
        }
        reader.close();

        double totalPrice = roomPrice * days;
        String price = String.valueOf(totalPrice);

        String updatedDetails = bookingID + "," + updatedGuestName + "," + updatedRoomType + "," + updatedCheckIn + "," + updatedCheckOut + "," + price + "," + payment_status;

        updateBookingInFile(bookingID, updatedDetails);
        if (viewBookingsController != null) {
            viewBookingsController.loadBookings(); // Reload data
        }


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void updateBookingInFile(String bookingID, String newDetails) throws IOException {
        File inputFile = new File("booking_details.txt");
        File tempFile = new File("guest_details_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] file_details = line.split(",");
                if ((file_details[0].equals(bookingID))) {
                    writer.write(newDetails);
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
