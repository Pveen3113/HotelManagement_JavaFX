package com.example.teest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewRoomsPageController {

    @FXML
    private ListView<String> roomsListView;


    private final String filePath = "room_details.txt";
    private ObservableList<String> roomTypes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadRoomTypes();
    }

    private void loadRoomTypes() {
        roomTypes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    roomTypes.add(parts[0]);
                }
            }
            roomsListView.setItems(roomTypes);
        } catch (IOException e) {
            showAlert("Error", "Failed to load room details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void viewRoom() {
        String selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room to view.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(selectedRoom)) {
                    String details = "Type: " + parts[0] + "\nPrice: " + parts[1] + "\nUnits Available: " + parts[2];
                    showAlert("Room Details", details);
                    return;
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to retrieve room details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteRoom() {
        String selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Room");
        confirmationAlert.setContentText("Are you sure you want to delete the room: " + selectedRoom + "?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                File inputFile = new File(filePath);
                File tempFile = new File("temp_room_details.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (!parts[0].equals(selectedRoom)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                reader.close();
                writer.close();

                if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                    showAlert("Error", "Failed to delete room.");
                    return;
                }

                showAlert("Success", "Room deleted successfully.");
                loadRoomTypes();

            } catch (IOException e) {
                showAlert("Error", "Failed to delete room.");
                e.printStackTrace();
            }
        }
    }

@FXML
private void updateRoom() {
    String selectedRoom = roomsListView.getSelectionModel().getSelectedItem();
    if (selectedRoom == null) {
        showAlert("Error", "Please select a room to update.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateRoomForm.fxml"));
        Stage updateStage = new Stage();
        Scene updateScene = new Scene(loader.load());

        UpdateRoomFormController updateRoomFormController = loader.getController();
        updateRoomFormController.setStage(updateStage);
        updateRoomFormController.setRoomType(selectedRoom);

        updateStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        updateStage.setTitle("Update Room Type");
        updateStage.setScene(updateScene);
        updateStage.showAndWait();

        loadRoomTypes();


    } catch (IOException e) {
        showAlert("Error", "Failed to load the update room form.");
        e.printStackTrace();
    }
}


    @FXML
    private void goBack() throws IOException {
        Parent roomsPage = FXMLLoader.load(getClass().getResource("RoomsPage.fxml"));
        Stage stage = (Stage) roomsListView.getScene().getWindow();
        stage.setScene(new Scene(roomsPage));
        stage.show();
    }

    private void showAlert(String title, String content) {
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
        Scene scene = new Scene(layout, 350, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }
}
