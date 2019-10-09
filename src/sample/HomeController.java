package sample;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.Report;
import db.entity.Reports;
import db.entity.Teacher;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

public class HomeController extends Controller{

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
    private Button importInExcelButton;

    @FXML
    private TableView<Reports> tableViewReportsTable2;

    @FXML
    private TableColumn<Reports, String> DepartmentNameInTable;

    @FXML
    private TableColumn<Reports, String> ProjectNameInTable;

    @FXML
    private TableView<Report> tableViewReportsTable;

    @FXML
    private TableColumn<Report, Integer> numberColumnInTable;

    @FXML
    private TableColumn<Report, String> NameColumnEventInTable;

    @FXML
    private TableColumn<Report, Date> dataColumnInTable;

    @FXML
    private TableColumn<Report, String> rateColumnInTable;

    @FXML
    private TableColumn<Report, String> authorColumnInTable;

    @FXML
    void initialize() {
        showAllReports.setOnAction(event -> {
            if (isNullOrEmpty(groupNumber.getText())) {
                findAllReports();
            } else {
                findReportsByDep();
            }
        });
        importInExcelButton.setOnAction((event -> {
            exportToExcel();
        }));
        tableViewReportsTable2.setRowFactory(tv -> {
            TableRow<Reports> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2) {
                    Reports clickedRow = row.getItem();
                    findReport((int) clickedRow.getId());
                }
            });
            return row;
        });

        NameColumnEventInTable
                    .setCellValueFactory(new PropertyValueFactory<Report, String>("activity_id") {
                        @Override
                        public ObservableValue<String> call(
                                    CellDataFeatures<Report, String> param) {
                            Object value = super.call(param).getValue();
                            String data = DBManager.getInstance().getActivityById((Long) value)
                                        .getName();
                            ObservableValue<String> newValue = new ReadOnlyObjectWrapper<String>(
                                        data);
                            return newValue;
                        }
                    });
        dataColumnInTable.setCellValueFactory(new PropertyValueFactory<>("date"));
        rateColumnInTable.setCellValueFactory(new PropertyValueFactory<>("status"));
        authorColumnInTable
                    .setCellValueFactory(new PropertyValueFactory<Report, String>("user_id") {
                        @Override
                        public ObservableValue<String> call(
                                    CellDataFeatures<Report, String> param) {
                            Object value = super.call(param).getValue();
                            for (Teacher teacher : DBManager.getInstance().getAllTeachers()) {
                                if (teacher.getId() == (Long) value) {
                                    return new ReadOnlyObjectWrapper<String>(teacher.getName());
                                }
                            }
                            return new ReadOnlyObjectWrapper<String>("");
                        }
                    });
        numberColumnInTable.setCellValueFactory(new PropertyValueFactory<>("id"));

        DepartmentNameInTable.setCellValueFactory(
                    new PropertyValueFactory<Reports, String>("department_id") {
                        @Override
                        public ObservableValue<String> call(
                                    CellDataFeatures<Reports, String> param) {
                            Object value = super.call(param).getValue();
                            String data = DBManager.getInstance().getDepartmentById((Long) value)
                                        .getName();
                            ObservableValue<String> newValue = new ReadOnlyObjectWrapper<String>(
                                        data);
                            return newValue;
                        }
                    });
        ProjectNameInTable.setCellValueFactory(new PropertyValueFactory<>("name"));

        goOtchetButton.setOnAction(event -> {
            openNewScene("/sample/view/createReport.fxml");

        });
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private void findReportsByDep() {
        int department = Integer.parseInt(groupNumber.getText());
        DBManager manager = DBManager.getInstance();
        ObservableList<Reports> reports = FXCollections
                    .observableArrayList(manager.getReportsByDepartment(department));
        tableViewReportsTable2.setItems(reports);

    }

    private void findAllReports() {
        DBManager manager = DBManager.getInstance();
        ObservableList<Reports> reports = FXCollections
                    .observableArrayList(manager.getAllReports());
        tableViewReportsTable2.setItems(reports);

    }

    private void findReport(int id) {
        DBManager manager = DBManager.getInstance();
        ObservableList<Report> report = FXCollections
                    .observableArrayList(manager.getReportById(id));
        tableViewReportsTable.setItems(report);
    }
    private void exportToExcel() {
        DBManager manager = DBManager.getInstance();
        manager.exportToExcel();
    }

}
