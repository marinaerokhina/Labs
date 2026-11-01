package com.lab4.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Главный класс приложения JavaFX.
 */
public class App extends Application {
    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Загрузка FXML файла
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/lab4/view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 900);

        // Получение контроллера
        mainController = fxmlLoader.getController();

        // Настройка окна
        primaryStage.setTitle("Симулятор кастрюли на плите");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        // Обработка закрытия окна
        primaryStage.setOnCloseRequest(e -> {
            if (mainController != null) {
                mainController.shutdown();
            }
        });
    }

    @Override
    public void stop() throws Exception {
        // Корректное завершение работы при закрытии приложения
        if (mainController != null) {
            mainController.shutdown();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
