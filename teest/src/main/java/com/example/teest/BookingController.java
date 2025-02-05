package com.example.teest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BookingController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void openMakeBookingPopup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("make-booking-popup.fxml"));
        Parent popupRoot = loader.load();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Make a Booking");
        popupStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        popupStage.setScene(new Scene(popupRoot));
        popupStage.showAndWait();
    }

    @FXML
    protected void viewBookings(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-bookings.fxml"));
        Parent viewBookingsRoot = loader.load();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(viewBookingsRoot));
    }


    @FXML
    protected void goBack(ActionEvent event)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);

        stage.setScene(new Scene(root));
        stage.show();
    }
}
