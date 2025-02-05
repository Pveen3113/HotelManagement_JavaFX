package com.example.teest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class AddRoomFormController {

    @FXML
    private TextField roomTypeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField unitsField;

    @FXML
    private void addRoom() {
        String roomType = roomTypeField.getText();
        String price = priceField.getText();
        String units = unitsField.getText();

        if (roomType.isEmpty() || price.isEmpty() || units.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

        try (FileWriter writer = new FileWriter("room_details.txt", true)) {
            writer.write(roomType + "," + price + "," + units + "\n");
            showAlert("Success", "Room Details Added Successfully");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save room details.");
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) roomTypeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);  // Removes the title bar
        popup.initModality(Modality.APPLICATION_MODAL);  // Blocks interaction with other windows until closed


        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: #EFE9D5; -fx-padding: 20; -fx-background-radius: 15;");  // Curved borders with background color


        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 18px; -fx-font-weight: bold;");


        Label message = new Label(content);
        message.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 14px;");


        Button closeButton = new Button("OK");
        closeButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        closeButton.setOnAction(e -> popup.close());

        layout.getChildren().addAll(titleLabel, message, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Set up the scene
        Scene scene = new Scene(layout, 350, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }

}
