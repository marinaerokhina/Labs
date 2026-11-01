package com.rgz_e.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный класс приложения графического редактора.
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/rgz_e/view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        
        primaryStage.setTitle("Graphics Editor - Rectangle Editor");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

