package com.example.teest;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewGuestDetailsController {
    @FXML
    private Label guestDetailsLabel;


    public void setGuestName(String guestName) {
        String guestDetails = GuestUtils.getGuestDetails(guestName);
        guestDetailsLabel.setText(guestDetails);
    }


    @FXML
    protected void closeDetails() {
        Stage currentStage = (Stage) guestDetailsLabel.getScene().getWindow();
        currentStage.close();
    }
}
