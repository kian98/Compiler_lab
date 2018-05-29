package GUI;

import Storage.Grammars;
import Storage.ParseResult;
import Storage.Predictor;
import com.sun.deploy.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    public GrammarImporter importer;
    public Grammars grammars;
    public Separator separator;
    public FirstSet firstSet;
    public FollowSet followSet;

    public GridPane gp;
    public ListView<String> grammar;
    public ListView<String> eliminatedGrammar;
    public ListView<String> FirstSet;
    public ListView<String> FollowSet;
    public TableView<ParseResult> parseResultTableView;
    public TableView<Predictor> predictTable;

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
            grammar.getItems().clear();
            List<String> rawGrammarList = importer.importGrammar(loadFile);
            for (String singleGrammar : rawGrammarList)
                grammar.getItems().add(singleGrammar);  //读入文法并在ListView中显示
            parseResultTableView.getItems().clear();
            eliminatedGrammar.getItems().clear();
            FirstSet.getItems().clear();
            FollowSet.getItems().clear();
        }
    }

    @FXML
    private void eliminate(ActionEvent event) {
        try {
            eliminatedGrammar.getItems().clear();
            grammars = new Grammars(importer.getRawGrammar());
            EliminateLR eliminateLR = new EliminateLR();
            String leftR = eliminateLR.eliminate(grammars);
            eliminatedGrammar.getItems().add(leftR);
            for (int i = 0; i < grammars.vn.size(); i++) {
                String temp = StringUtils.join(Arrays.asList(grammars.vn_infer.get(i).toArray()), "|");
                String singleGrammar = grammars.vn.get(i) + " ->" + temp;
                eliminatedGrammar.getItems().add(singleGrammar);
            }
            separator = new Separator();
            separator.separate(grammars.vn, grammars.vn_infer);
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
            List<String> allVtLocal = new ArrayList<>(separator.allVt);
            allVtLocal.remove("ε");
            double width = predictTable.getWidth() / (allVtLocal.size() + 1);
            TableColumn vnCol = new TableColumn("");
            vnCol.setPrefWidth(width);
            predictTable.getColumns().add(vnCol);
            for (String vt : allVtLocal) {
                TableColumn vtCol = new TableColumn(vt);
                vtCol.setPrefWidth(width);
                predictTable.getColumns().add(vtCol);
            }

            Predictor predictor = new Predictor();
            List<String> predictResult = predictor.predictTable(allVtLocal,separator.allVn,
                    separator.vnInfer_separated,firstSet.firstSet,followSet.followSet);
            

        } catch (NullPointerException nullptr) {
            Alert warning = new Alert(Alert.AlertType.WARNING, "未进行必要操作");
            warning.showAndWait();
        }
    }

}
