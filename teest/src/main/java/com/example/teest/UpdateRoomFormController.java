package com.example.teest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class UpdateRoomFormController {

    private final String ROOM_FILE = "room_details.txt";

    @FXML
    private TextField roomTypeField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField unitsField;

    private Stage stage;

    private String oldRoomType;
    private Runnable updateAction;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRoomType(String roomType) {
        this.oldRoomType = roomType;
        String[] roomDetails = getRoomTypeDetails(roomType).split("\n");
        if (roomDetails.length == 3) {
            roomTypeField.setText(roomDetails[0].split(": ")[1]);
            priceField.setText(roomDetails[1].split(": ")[1]);
            unitsField.setText(roomDetails[2].split(": ")[1]);
        }
    }
    public String getRoomTypeDetails(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3 && details[0].equals(name)) {
                    return "Type: " + details[0] + "\nPrice: " + details[1] + "\nUnits Available: " + details[2];
                }
            }
        } catch (IOException e) {
            System.out.println("Error retrieving room details: " + e.getMessage());
        }
        return "Room not found.";
    }

    public  void updateRoom(String oldName, String newRoomType, String newPrice, String newUnitsAvailable) {
        File inputFile = new File(ROOM_FILE);
        File tempFile = new File("temp_room_details.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 3 && details[0].equals(oldName)) {
                    writer.write(newRoomType + "," + newPrice + "," + newUnitsAvailable);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating guest: " + e.getMessage());
        }


        if (!inputFile.delete()) {
            System.out.println("Failed to delete the original file.");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Failed to rename the temp file.");
        }
    }

    @FXML
    protected void saveUpdatedGuest() {
        String newRoomType = roomTypeField.getText();
        String newRoomPrice = priceField.getText();
        String newUnitsAvailable = unitsField.getText();

        if (!newRoomType.isEmpty() && !newRoomPrice.isEmpty() && !newUnitsAvailable.isEmpty()) {
            updateRoom(oldRoomType, newRoomType, newRoomPrice, newUnitsAvailable);
            System.out.println("Room Details updated successfully.");
            stage.close();
        } else {
            System.out.println("Please fill in all fields.");
        }
    }

    @FXML
    protected void cancelUpdate() {
        stage.close();
    }




}
