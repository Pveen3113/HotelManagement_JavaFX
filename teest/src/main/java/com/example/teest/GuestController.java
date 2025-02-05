package com.example.teest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuestController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField nameField;
    @FXML
    private TextField idField;
    @FXML
    private TextField contactField;

    @FXML
    protected void addGuest() {
        String name = nameField.getText();
        String id = idField.getText();
        String contact = contactField.getText();

        if (name.isEmpty() || id.isEmpty() || contact.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        // Save guest data to a file
        GuestUtils.saveGuest(name, id, contact);
        System.out.println("Guest added successfully!");

        // Clear input fields
        nameField.clear();
        idField.clear();
        contactField.clear();
    }


    @FXML
    protected void goToViewGuests() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view-guests.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        ViewGuestsController viewGuestsController = fxmlLoader.getController();
        viewGuestsController.setStage(stage);

        stage.setScene(scene);
    }

    @FXML
    protected void goToMainMenu(ActionEvent event) throws IOException {
        // Navigate back to the Main Menu
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainController mainController = fxmlLoader.getController();
        mainController.setStage((Stage) ((Node) event.getSource()).getScene().getWindow());
        mainController.setStage((Stage) ((Node) event.getSource()).getScene().getWindow());

        stage.setScene(scene);
    }
}
