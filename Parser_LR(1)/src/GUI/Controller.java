package GUI;

import Models.Grammars;
import Utils.Closure;
import Utils.GrammarImporter;
import Utils.Separator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {
    public GrammarImporter importer;
    public Grammars grammars;

    public GridPane gp;
    public ListView<String> rawGrammar;
    public ListView<String> extendedGrammar;

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
            List<String> rawGrammarList = importer.importGrammar(loadFile);
            for (String singleGrammar : rawGrammarList)
                rawGrammar.getItems().add(singleGrammar);  //读入文法并在ListView中显示

        }
    }

    @FXML
    private void getLrAnalysisTable(ActionEvent event){
        grammars = new Grammars(importer.getRawGrammar());
        dispExtendedGrammar(grammars);
        Closure closure = new Closure();
        closure.setClosure(grammars);
    }

    private void dispExtendedGrammar(Grammars grammars){
        for(int i=0;i<grammars.vn.size();i++){
            for(String everyInfer : grammars.vn_infer.get(i)){
                extendedGrammar.getItems().add("("+i+"): "+grammars.vn.get(i)+" -> "+everyInfer+" ;");
            }
        }
    }
}
