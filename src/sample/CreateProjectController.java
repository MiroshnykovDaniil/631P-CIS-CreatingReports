package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CreateProjectController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> tableViewReportsTable;

    @FXML
    private TableColumn<?, ?> numberColumnInTable;

    @FXML
    private TableColumn<?, ?> NameColumnEventInTable;

    @FXML
    private TableColumn<?, ?> dataColumnInTable;

    @FXML
    private TableColumn<?, ?> authorColumnInTable;

    @FXML
    private TableColumn<?, ?> rateColumnInTable;

    @FXML
    void initialize() {

    }
}
