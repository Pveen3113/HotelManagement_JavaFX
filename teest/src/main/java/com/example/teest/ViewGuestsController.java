package com.example.teest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class ViewGuestsController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        populateGuestListView();
    }

    @FXML
    private ListView<String> guestListView;

    @FXML
    protected void deleteSelectedGuest() {
        String selectedGuest = guestListView.getSelectionModel().getSelectedItem();
        if (selectedGuest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("Guest: " + selectedGuest);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                GuestUtils.deleteGuest(selectedGuest);
                guestListView.getItems().remove(selectedGuest);
                System.out.println("Guest deleted successfully.");
            } else {
                System.out.println("Deletion canceled.");
            }
        } else {
            System.out.println("Please select a guest to delete.");
        }
    }

    @FXML
    protected void updateSelectedGuest() throws IOException {
        String selectedGuest = guestListView.getSelectionModel().getSelectedItem();
        if (selectedGuest != null) {
            // Load the update guest form
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("update-guest.fxml"));
            Stage updateStage = new Stage();
            Scene updateScene = new Scene(fxmlLoader.load());

            // Pass data to the UpdateGuestController
            UpdateGuestController updateGuestController = fxmlLoader.getController();
            updateGuestController.setStage(updateStage);
            updateGuestController.setGuestName(selectedGuest);

            updateStage.setTitle("Update Guest");
            updateStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            updateStage.setScene(updateScene);
            updateStage.showAndWait();

            // Refresh the guest list after the update
            populateGuestListView();
        } else {
            System.out.println("Please select a guest to update.");
        }
    }
    @FXML
    protected void viewGuestDetails() throws IOException {
        String selectedGuest = guestListView.getSelectionModel().getSelectedItem();
        if (selectedGuest != null) {
            // Load the Guest Details pop-up FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view-guest-details.fxml"));
            Stage detailsStage = new Stage();
            Scene detailsScene = new Scene(fxmlLoader.load());

            // Set a better window size
            detailsStage.setWidth(400);
            detailsStage.setHeight(300);

            // Pass selected guest details to the controller of the pop-up
            ViewGuestDetailsController detailsController = fxmlLoader.getController();
            detailsController.setGuestName(selectedGuest);

            detailsStage.setTitle("Guest Details");
            detailsStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            detailsStage.setScene(detailsScene);
            detailsStage.setResizable(false);  // Disable resizing
            detailsStage.showAndWait();  // Modal window (blocks interaction with the main window until closed)
        } else {
            System.out.println("Please select a guest to view details.");
        }
    }


    @FXML
    protected void goBackToGuestPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guest-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        GuestController guestController = fxmlLoader.getController();
        guestController.setStage(stage);
        stage.setScene(scene);
    }

    public void populateGuestListView() {
        guestListView.getItems().clear();
        guestListView.getItems().addAll(GuestUtils.loadGuests());
    }
}