package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Templates/loginWindow.fxml"));
        primaryStage.setTitle("Автосервис");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

/**
 * Цвета:
 * Темно-серый  | 495867
 * Темно-синий  | 577399
 * Светло-синий | BDD5EA
 * Светлый      | F7F7FF
 * Ярко-Красный | FE5F55
 **/