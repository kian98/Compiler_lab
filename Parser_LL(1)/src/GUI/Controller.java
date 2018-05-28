package GUI;

import Storage.Grammars;
import Storage.ParseResult;
import com.sun.deploy.util.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.EliminateLR;
import utils.GrammarImporter;
import utils.Separator;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Controller {


    public GrammarImporter importer;

    public GridPane gp;
    public ListView<String> grammar;
    public ListView<String> eliminatedGrammar;
    public TableView<ParseResult> parseResultTableView;

    @FXML
    private void loadGrammar(ActionEvent event) throws IOException {
        grammar.getItems().clear();
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
            List<String> rawGrammarList = importer.importGrammar(loadFile);
            for (String singleGrammar : rawGrammarList)
                grammar.getItems().add(singleGrammar);  //读入文法并在ListView中显示
        }
        parseResultTableView.getItems().clear();
        eliminatedGrammar.getItems().clear();
    }

    @FXML
    private void eliminate(ActionEvent event) {
        Grammars grammars = new Grammars(importer.getRawGrammar());
        EliminateLR eliminateLR = new EliminateLR();
        String leftR = eliminateLR.eliminate(grammars);
        eliminatedGrammar.getItems().add(leftR);
        for (int i = 0; i < grammars.vt.size(); i++) {
            String temp = StringUtils.join(Arrays.asList(grammars.vt_infer.get(i).toArray()), "|");
            String singleGrammar = grammars.vt.get(i) + "->" + temp;
            eliminatedGrammar.getItems().add(singleGrammar);
        }
        Separator separator = new Separator();
        separator.separate(grammars.vt,grammars.vt_infer);
    }
}
