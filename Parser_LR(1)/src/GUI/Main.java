package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Parser.fxml"));
        primaryStage.setTitle("Parser LR(1)");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1200);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
