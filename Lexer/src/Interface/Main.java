package Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(
                getClass().getResource("Interface.fxml"));  //
        primaryStage.setTitle("词法分析器");
        primaryStage.setScene(new Scene(root, 1000, 645));
        primaryStage.show();
        primaryStage.setResizable(false);  //设置窗体大小不可改变
    }


    public static void main(String[] args) {
        launch(args);
    }
}
