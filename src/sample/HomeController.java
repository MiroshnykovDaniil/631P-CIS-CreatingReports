package sample;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.Report;
import db.entity.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import javax.swing.*;

public class HomeController {

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
    private ScrollBar scrollBaR;

    @FXML
    private TableView<Reports> tableViewReportsTable2;

    @FXML
    private TableColumn<Reports, Long> DepartmentNameInTable;

    @FXML
    private TableColumn<Reports, String> ProjectNameInTable;

    @FXML
    private TableView<Report> tableViewReportsTable;

    @FXML
    private TableColumn<?, ?> numberColumnInTable;

    @FXML
    private TableColumn<Report, Integer> NameColumnEventInTable;

    @FXML
    private TableColumn<Report, Date> dataColumnInTable;

    @FXML
    private TableColumn<Report, String> rateColumnInTable;

    @FXML
    private TableColumn<Report, Integer> authorColumnInTable;

    @FXML
    private TableColumn<?, ?> NameColumnEventInTable1;

    @FXML
    void initialize(){

        showAllReports.setOnAction(event -> {
            if (isNullOrEmpty(groupNumber.getText())){
                findAllReports();
            }
            else {
                findReportsByDep();
            }
        });
        tableViewReportsTable2.setRowFactory(tv -> {
            TableRow<Reports> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Reports clickedRow = row.getItem();
                    findReport((int)clickedRow.getId());
                }
            });
            return row;});
    }
   // private ObservableList<Reports> reports = FXCollections.observableArrayList();

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private void findReportsByDep(){
        int department = Integer.parseInt(groupNumber.getText());
        DBManager manager = DBManager.getInstance();
        ObservableList<Reports> reports =  FXCollections.observableArrayList(manager.getReportsByDepartment(department));

        DepartmentNameInTable.setCellValueFactory(new PropertyValueFactory<>("department_name"));
        ProjectNameInTable.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableViewReportsTable2.setItems(reports);

    }

    private void findAllReports(){
        DBManager manager = DBManager.getInstance();
        ObservableList<Reports> reports =  FXCollections.observableArrayList(manager.getAllReports());
        DepartmentNameInTable.setCellValueFactory(new PropertyValueFactory<>("department_name"));
        ProjectNameInTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewReportsTable2.setItems(reports);

    }

    private void findReport(int id){
        DBManager manager = DBManager.getInstance();
        ObservableList<Report> report = FXCollections.observableArrayList(manager.getReportById(id));
        tableViewReportsTable.setItems(report);
        NameColumnEventInTable.setCellValueFactory(new PropertyValueFactory<>("activity_id"));
        dataColumnInTable.setCellValueFactory(new PropertyValueFactory<>("date"));
        rateColumnInTable.setCellValueFactory(new PropertyValueFactory<>("status"));
        authorColumnInTable.setCellValueFactory(new PropertyValueFactory<>("user_id"));

    }

}
