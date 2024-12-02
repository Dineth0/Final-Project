package lk.ijse.gdse.finalproject;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoadingScreen.fxml"))));
        stage.show();


        Task<Scene> loadingTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {

                FXMLLoader fxmlLoader = new FXMLLoader(AppInitializer.class.getResource("/view/LoginForm.fxml"));
                return new Scene(fxmlLoader.load());
            }
        };


        loadingTask.setOnSucceeded(event -> {
            Scene value = loadingTask.getValue();

            stage.setTitle("Labor Management System");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/worker.png")));
            stage.setMaximized(true);


            stage.setScene(value);

            stage.show();
        });


        loadingTask.setOnFailed(event -> {
            System.err.println("Failed to load the main layout.");
        });

        new Thread(loadingTask).start();

    }
}