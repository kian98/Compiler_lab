package Interface;

import Storager.LexResult;
import Utils.FileReader;
import Utils.Identifier;
import Utils.Tokenizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    public FileReader fileReader = new FileReader();  //在此处实例化FileReader类，避免在词法分析时需要再次读入

    public Button load;
    public Button lexing;
    public AnchorPane ap;
    public TextArea code;
    public TableView<LexResult> table = new TableView<>();
    public TableColumn<LexResult, String> wordCol;
    public TableColumn<LexResult, Integer> wordTypeCol;
    public TableColumn<LexResult, String> wordPropertyCol;
    public TableColumn<LexResult, Integer> typeCol;
    public TableColumn<LexResult, Integer> rowCol;
    public TableColumn<LexResult, Integer> lineCol;

    private ObservableList<LexResult> data;


    @FXML
    //“打开文件”按钮响应动作
    private void loadCode(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        FileSystemView fsv = FileSystemView.getFileSystemView();
        fileChooser.setInitialDirectory(
                //new File(String.valueOf(fsv.getHomeDirectory())));  //设置文件选择窗口默认路径为桌面
                new File(System.getProperty("user.dir")));           //设置默认路径为当前工程文件所在目录
        Stage stage = (Stage) ap.getScene().getWindow();  //获取窗体Stage
        File loadFile = fileChooser.showOpenDialog(stage);

        if (loadFile != null) {  //若没有打开文件，则不继续
            String codeRead = fileReader.readin(loadFile);
            code.setText(codeRead);  //读入代码并在TextArea中显示
        }
        table.getItems().clear();   //读入新文件时清空TableView内容
    }

    @FXML
    //“词法分析”按钮响应动作
    private void lexicalAnalyze(ActionEvent event) {
        Tokenizer tokenizer = new Tokenizer();  //Tokenizer类用于分词得到Tokens
        List<List<String>> codeDivided = tokenizer.tokenize(fileReader.rawCode());
        //System.out.println(codeDivided);  //控制台查看分词结果是否正确
        Identifier identifier = new Identifier();  //Identifier类用于识别字符串
        data = FXCollections.observableArrayList(identifier.identify(codeDivided));

        table.setItems(data);
        //设置居中显示文字
        wordCol.setStyle("-fx-alignment: CENTER;");
        wordTypeCol.setStyle("-fx-alignment: CENTER;");
        wordPropertyCol.setStyle("-fx-alignment: CENTER;");
        typeCol.setStyle("-fx-alignment: CENTER;");
        rowCol.setStyle("-fx-alignment: CENTER;");
        lineCol.setStyle("-fx-alignment: CENTER;");
        //Tableview显示内容
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        wordTypeCol.setCellValueFactory(new PropertyValueFactory<>("wordTypeRank"));
        wordPropertyCol.setCellValueFactory(new PropertyValueFactory<>("wordProperty"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        rowCol.setCellValueFactory(new PropertyValueFactory<>("row"));
        lineCol.setCellValueFactory(new PropertyValueFactory<>("line"));
    }
}
