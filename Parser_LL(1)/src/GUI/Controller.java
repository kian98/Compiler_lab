package GUI;

import Storage.Grammars;
import Storage.Parser;
import com.sun.deploy.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.*;
import utils.Separator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    public GrammarImporter importer;
    public Grammars grammars;
    public Separator separator;
    public FirstSet firstSet;
    public FollowSet followSet;
    public Predictor predictor;

    public GridPane gp;
    public ListView<String> grammar;
    public ListView<String> eliminatedGrammar;
    public ListView<String> FirstSet;
    public ListView<String> FollowSet;
    public HBox AnalysisList;
    public TableView<Parser> parseResultTableView;
    public TableColumn<Parser, Integer> step;
    public TableColumn<Parser, String> stack;
    public TableColumn<Parser, String> remain;
    public TableColumn<Parser, String> formula;
    public TableColumn<Parser, String> movement;
    public TextField text;

    private ObservableList<Parser> data;


    @FXML
    private void loadGrammar(ActionEvent event) throws IOException {
        importer = new GrammarImporter();      //每次打开文件都保证重置
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialDirectory(
                //new File(String.valueOf(fsv.getHomeDirectory())));  //设置文件选择窗口默认路径为桌面
                new File(System.getProperty("user.dir")));           //设置默认路径为当前工程文件所在目录
        Stage stage = (Stage) gp.getScene().getWindow();  //获取窗体Stage
        File loadFile = fileChooser.showOpenDialog(stage);

        if (loadFile != null) {  //若没有打开文件，则不继续

            grammars = null;
            separator = null;
            firstSet = null;
            followSet = null;
            predictor = null;

            grammar.getItems().clear();
            List<String> rawGrammarList = importer.importGrammar(loadFile);
            for (String singleGrammar : rawGrammarList)
                grammar.getItems().add(singleGrammar);  //读入文法并在ListView中显示

            //每次打开新文件后清除已存在的所有内容(除了输入表达式)
            parseResultTableView.getItems().clear();
            eliminatedGrammar.getItems().clear();
            FirstSet.getItems().clear();
            FollowSet.getItems().clear();
            AnalysisList.getChildren().clear();
            parseResultTableView.getItems().clear();
        }
    }

    @FXML
    private void eliminate(ActionEvent event) {
        try {
            eliminatedGrammar.getItems().clear();
            grammars = new Grammars(importer.getRawGrammar());      //对获取的文法进行分割,grammars对象包含非终结符及其对应导出式
            EliminateLR eliminateLR = new EliminateLR();
            String leftR = eliminateLR.eliminate(grammars);         //leftR保存消除左递归结果

            eliminatedGrammar.getItems().add(leftR);
            for (int i = 0; i < grammars.vn.size(); i++) {
                String temp = StringUtils.join(Arrays.asList(grammars.vn_infer.get(i).toArray()), "|");
                String singleGrammar = grammars.vn.get(i) + " ->" + temp;
                eliminatedGrammar.getItems().add(singleGrammar);
            }

            separator = new Separator();
            separator.separate(grammars.vn, grammars.vn_infer);     //按照消除左递归的结果,对所有单个标识符进行分割
        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

    @FXML
    private void getFirst(ActionEvent event) {
        try {
            FirstSet.getItems().clear();
            firstSet = new FirstSet();
            List<List<String>> grammarFirstSet = firstSet.getFirstSet(separator.allVn, separator.vnInfer_separated);
            for (List<String> list : grammarFirstSet) {
                String string = list.get(0).length() == 1 ? (list.get(0) + " : { ") : (list.get(0) + ": { ");
                string += list.get(1);
                for (int i = 2; i < list.size(); i++) string += ", " + list.get(i);
                string += " }";
                FirstSet.getItems().add(string);
            }
        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

    @FXML
    private void getFollow(ActionEvent event) {
        try {
            FollowSet.getItems().clear();
            followSet = new FollowSet();
            List<List<String>> grammarFollowSet = followSet.getFollowSet(separator.allVn, separator.vnInfer_separated, firstSet.firstSet);
            for (List<String> list : grammarFollowSet) {
                String string = list.get(0).length() == 1 ? (list.get(0) + " : { ") : (list.get(0) + ": { ");
                string += list.get(1);
                for (int i = 2; i < list.size(); i++) string += ", " + list.get(i);
                string += " }";
                FollowSet.getItems().add(string);
            }
        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

    @FXML
    private void createLL1(ActionEvent event) {
        try {
            AnalysisList.getChildren().clear();

            List<String> allVtLocal = new ArrayList<>(separator.allVt);
            allVtLocal.remove("ε");
            allVtLocal.add("#");

            predictor = new Predictor();
            List<List<String>> analysisResult = predictor.predictTable(allVtLocal, separator.allVn,
                    separator.vnInfer_separated, firstSet.firstSet, followSet.followSet);

//          设置hbox布局,分别添加ListView以实现对齐
            for (int colCount = 0; colCount < analysisResult.get(0).size(); colCount++) {
                ListView<String> list = new ListView<>();
                list.setStyle("-fx-border-width: 0px");
                AnalysisList.getChildren().add(list);
                AnalysisList.setHgrow(list, Priority.ALWAYS);
                for (List<String> rowList : analysisResult) list.getItems().add(rowList.get(colCount));
            }

        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

    @FXML
    private void parse(ActionEvent event) {
        try {
            parseResultTableView.getItems().clear();
            Parse parser = new Parse();
            if (text.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.WARNING, "输入为空");
                error.showAndWait();
                return;
            }
            String analyzeText = text.getText() + "#";

            data = FXCollections.observableArrayList(parser.parse(analyzeText, predictor.analysisResult,
                    separator.allVn, separator.allVt, separator.vnInfer_separated));
            parseResultTableView.setItems(data);

            step.setCellValueFactory(new PropertyValueFactory<>("step"));
            stack.setCellValueFactory(new PropertyValueFactory<>("stack"));
            remain.setCellValueFactory(new PropertyValueFactory<>("remain"));
            formula.setCellValueFactory(new PropertyValueFactory<>("formula"));
            movement.setCellValueFactory(new PropertyValueFactory<>("movement"));
            if (!parser.isSuccess()) {
                Alert error = new Alert(Alert.AlertType.ERROR, "语句错误");
                error.showAndWait();
            }
        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

}
