package lk.ijse.gdse.finalproject;

import javafx.application.Application;
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
        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginForm.fxml")));
        stage.setScene(new Scene(load, 720, 605));
        stage.setTitle("LMS");
        //Image image = new Image(getClass().getResourceAsStream("/images/Labor-Management-System.png"));
       // stage.getIcons().add(image);

        stage.show();
    }
}
