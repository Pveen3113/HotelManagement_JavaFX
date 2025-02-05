package com.example.teest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentController {
    private Stage stage;
    private PricingCalculator pricingCalculator = new PricingCalculator();


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void goToMakePayments() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("payment-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        PaymentManagement paymentManagement = fxmlLoader.getController();
        paymentManagement.setStage(stage);

        stage.setScene(scene);
    }

    @FXML
    protected void goToPriceCalculator() throws IOException {
        pricingCalculator.showPricingPopup();
    }


    @FXML
    protected void goToMainMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);

        stage.setScene(scene);
    }
}
