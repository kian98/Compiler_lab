package GUI;

import Storage.PTableColumn;
import Storage.ParseResult;
import Utils.*;
import Utils.Separator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public GrammarImporter importer;
    public Grammars grammars;
    public Separator separator;
    public List<List<String>> extendedGrammar;
    public ParseTableCreator parseTable;
    public Closure closureList;

    public GridPane gp;
    public ListView<String> rawGrammar;
    public ListView<String> extendedGrammarList;
    public HBox TableTitle;
    public HBox TableContent;
    public VBox WholeTable;
    public TextField text;
    public TableView ParseProcess;
    public PTableColumn<ParseResult, Integer> step;
    public PTableColumn<Parser, String> status;
    public PTableColumn<Parser, String> symbols;
    public PTableColumn<Parser, String> remain;
    public PTableColumn<Parser, String> action;


    @FXML
    private void loadGrammar(ActionEvent event) throws IOException {
        try {
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
                rawGrammar.getItems().clear();
                List<String> rawGrammarList = importer.importGrammar(loadFile);
                for (String singleGrammar : rawGrammarList)
                    rawGrammar.getItems().add(singleGrammar);  //读入文法并在ListView中显示
                extendedGrammarList.getItems().clear();
                TableTitle.getChildren().clear();
                TableContent.getChildren().clear();
                grammars = new Grammars(importer.getRawGrammar());
                showExtendedGrammar(grammars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getParseTable(ActionEvent event) {
        try {
            TableTitle.getChildren().clear();
            TableContent.getChildren().clear();

            separator = new Separator();
            separator.separate(grammars.vn, grammars.vn_infer);

            closureList = new Closure();
            closureList.setClosure(grammars, separator);

            parseTable = new ParseTableCreator();
            parseTable.createParseTable(closureList.closureList, closureList.go, separator.allVn.subList(1, separator.allVn.size()), separator.allVt, extendedGrammar);

            double aver = WholeTable.getWidth() / (1 + separator.allVn.size() + separator.allVt.size());

            Label statusTitle = new Label("状态");
            statusTitle.setPrefWidth(aver);
            statusTitle.setPrefHeight(TableTitle.getHeight());
            statusTitle.setStyle("-fx-border-width: 0.5");
            statusTitle.setStyle("-fx-border-color: #BABABA;");
            statusTitle.setAlignment(Pos.CENTER);

            Label actionTitle = new Label("ACTION");
            actionTitle.setPrefWidth(aver * (separator.allVt.size() + 1));
            actionTitle.setPrefHeight(TableTitle.getHeight());
            actionTitle.setAlignment(Pos.CENTER);
            actionTitle.setStyle("-fx-border-width: 0.5");
            actionTitle.setStyle("-fx-border-color: #BABABA;");

            Label gotoTitle = new Label("GOTO");
            gotoTitle.setPrefWidth(aver * (separator.allVn.size() - 1));
            gotoTitle.setPrefHeight(TableTitle.getHeight());
            gotoTitle.setStyle("-fx-border-width: 0.5");
            gotoTitle.setStyle("-fx-border-color: #BABABA;");
            gotoTitle.setAlignment(Pos.CENTER);
            statusTitle.setPrefHeight(WholeTable.getHeight());

            TableTitle.getChildren().add(statusTitle);
            TableTitle.getChildren().add(actionTitle);
            TableTitle.getChildren().add(gotoTitle);

            ListView<String> stepCount = new ListView<>();
            stepCount.getItems().add(" ");
            for (int step = 0; step < parseTable.ACTION.size() - 1; step++)
                stepCount.getItems().add(step + "");
            stepCount.setPrefWidth(aver);
            stepCount.setPrefHeight(TableContent.getHeight());
            TableContent.getChildren().add(stepCount);
            HBox.setHgrow(stepCount, Priority.ALWAYS);


            for (int i = 0; i < parseTable.ACTION.get(0).length; i++) {
                ListView<String> colList = new ListView<>();
                for (int j = 0; j < parseTable.ACTION.size(); j++) {
                    colList.getItems().add(parseTable.ACTION.get(j)[i]);
                }
                colList.setPrefWidth(aver);
                colList.setPrefHeight(TableContent.getHeight());
                TableContent.getChildren().add(colList);
                HBox.setHgrow(colList, Priority.ALWAYS);
            }
            for (int i = 0; i < parseTable.GOTO.get(0).length; i++) {
                ListView<String> colList = new ListView<>();
                for (int j = 0; j < parseTable.GOTO.size(); j++) {
                    colList.getItems().add(parseTable.GOTO.get(j)[i]);
                }
                colList.setPrefWidth(aver);
                colList.setPrefHeight(TableContent.getHeight());
                TableContent.getChildren().add(colList);
                HBox.setHgrow(colList, Priority.ALWAYS);

            }
        } catch (Exception e) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }

    }

    private void showExtendedGrammar(Grammars grammars) {
        extendedGrammar = new ArrayList<>();
        extendedGrammar.add(new ArrayList<>());
        extendedGrammar.add(new ArrayList<>());
        int count = 0;
        for (int i = 0; i < grammars.vn.size(); i++) {
            for (String everyInfer : grammars.vn_infer.get(i)) {
                extendedGrammarList.getItems().add("(" + count + "): " + grammars.vn.get(i) + " -> " + everyInfer + " ;");
                count++;
                extendedGrammar.get(0).add(grammars.vn.get(i));
                extendedGrammar.get(1).add(everyInfer);
            }
        }
    }

    @FXML
    private void startParse(ActionEvent event) {
        try {
            ParseProcess.getItems().clear();
            if (text.getText().equals("")) {
                Alert error = new Alert(Alert.AlertType.WARNING, "输入为空");
                error.showAndWait();
                return;
            }
            String analyzeText = text.getText() + "#";

            ObservableList<ParseResult> data = FXCollections.observableArrayList(Parser.parse(analyzeText, parseTable, extendedGrammar));
            ParseProcess.setItems(data);

            step.setCellValueFactory(new PropertyValueFactory<>("step"));
            status.setCellValueFactory(new PropertyValueFactory<>("status"));
            symbols.setCellValueFactory(new PropertyValueFactory<>("symbols"));
            remain.setCellValueFactory(new PropertyValueFactory<>("remain"));
            action.setCellValueFactory(new PropertyValueFactory<>("movement"));

            status.setResizable(true);
            symbols.setResizable(true);
            remain.setResizable(true);
            action.setResizable(true);

            if (!Parser.isSuccess()) {
                Alert error = new Alert(Alert.AlertType.ERROR, "语句错误");
                error.showAndWait();
            }
        } catch (Exception e) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

}
