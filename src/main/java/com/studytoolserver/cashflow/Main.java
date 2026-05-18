package com.studytoolserver.cashflow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    //Loads the FXML file for gui stuff

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("hello-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CashFlow");
        stage.setScene(scene);
        stage.show();

        //Load stuff

        FinancialHistory controller = fxmlLoader.getController();

        try {
            FinancialData.load();
            controller.updateMoney();
            controller.updateTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}