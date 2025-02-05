package com.example.teest;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateGuestController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField idField;
    @FXML
    private TextField contactField;

    private Stage stage;
    private String oldGuestName;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGuestName(String guestName) {
        this.oldGuestName = guestName;
        String[] guestDetails = GuestUtils.getGuestDetails(guestName).split("\n");
        if (guestDetails.length == 3) {
            nameField.setText(guestDetails[0].split(": ")[1]);
            idField.setText(guestDetails[1].split(": ")[1]);
            contactField.setText(guestDetails[2].split(": ")[1]);
        }
    }

    @FXML
    protected void saveUpdatedGuest() {
        String newName = nameField.getText();
        String newId = idField.getText();
        String newContact = contactField.getText();

        if (!newName.isEmpty() && !newId.isEmpty() && !newContact.isEmpty()) {
            GuestUtils.updateGuest(oldGuestName, newName, newId, newContact);
            System.out.println("Guest updated successfully.");
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
