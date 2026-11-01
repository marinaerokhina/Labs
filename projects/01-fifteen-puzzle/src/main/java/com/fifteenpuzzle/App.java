package com.fifteenpuzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем главное окно
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 520, 560);

        primaryStage.setTitle("Пятнашки");
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
