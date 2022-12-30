package main.java.hr.java.covidportal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        mainStage.setTitle("Aplikacija za evidenciju boleština");
        mainStage.setScene(new Scene(root, 600, 475));
        mainStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage nekiStage) {
        mainStage = nekiStage;
    }

    public static void main(String[] args) throws SQLException, IOException {
        PocetniEkranController.logger.info("Aplikacija je pokrenuta!");

        launch(args);

        PocetniEkranController.logger.info("Završetak programa aplikacije!");
        PocetniEkranController.logger.info("------------------------------------------------------------------------------------------------------");
    }
}
