package com.example.teest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void goToRooms(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RoomsPage.fxml"));
        Parent roomsPage = loader.load();

        RoomsPageController roomsPageController = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        roomsPageController.setStage(stage);
        stage.setScene(new Scene(roomsPage));
        stage.show();
    }

    @FXML
    protected void goToGuests() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("guest-management.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        GuestController guestController = fxmlLoader.getController();
        guestController.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    protected void goToBookings(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("booking-management.fxml"));
        Parent bookingPage = loader.load();


        BookingController bookingController = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        bookingController.setStage(stage);
        stage.setScene(new Scene(bookingPage));
        stage.show();
    }

    @FXML
    protected void goToPayments() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment-page.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        PaymentController paymentController= fxmlLoader.getController();
        paymentController.setStage(stage);

        stage.setScene(scene);
    }

}
