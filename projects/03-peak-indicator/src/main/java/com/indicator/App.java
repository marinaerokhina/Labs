package com.indicator;

import com.indicator.Presentation.MainController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private MainController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        controller = new MainController();

        Scene scene = new Scene(controller.getRoot(), 550, 420);
        stage.setTitle("Peak Indicator");
        stage.setScene(scene);
        stage.show();        
    }
    
    @Override
    public void stop() throws Exception {
        if (controller != null) {
            controller.stop();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
