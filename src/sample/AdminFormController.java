package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AdminFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button goOtchetButton;

    @FXML
    private TextField facultetNumber;

    @FXML
    private TextField groupNumber;

    @FXML
    private Button showAllReports;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button importInExcelButton;

    @FXML
    private ScrollBar scrollBaR;

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
