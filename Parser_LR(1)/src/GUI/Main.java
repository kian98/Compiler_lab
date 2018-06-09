/*
整体思路参照手工解题过程:首先将根据输入文法求出初始项目,进而求得项目集规范族,产生闭包与GO函数
依此再求出分析表,获得各个状态的ACTION和GOTO,最后进行分析.
*/
package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Parser.fxml"));
        primaryStage.setTitle("Parser LR(1)");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setMinWidth(primaryScreenBounds.getWidth());
        primaryStage.setMinHeight(primaryScreenBounds.getHeight());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
