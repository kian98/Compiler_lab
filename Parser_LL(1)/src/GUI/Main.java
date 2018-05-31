package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("LL(1) Parser");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(1300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
