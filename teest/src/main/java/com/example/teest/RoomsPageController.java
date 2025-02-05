package com.example.teest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomsPageController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void showAddRoomForm() {
        Stage popup = new Stage();
        try {
            Parent form = FXMLLoader.load(getClass().getResource("AddRoomForm.fxml"));
            popup.setScene(new Scene(form));
            popup.initStyle(javafx.stage.StageStyle.UNDECORATED);
            popup.setTitle("Add Room");
            popup.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToViewRooms(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewRoomsPage.fxml"));
        Parent viewRoomsPage = loader.load();


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(viewRoomsPage));
        stage.show();
    }


    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);
        stage.setScene(new Scene(root));
        stage.show();
    }

}
