package sample;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.Reports;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;

public class CreateProjectController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea reportNameTb;

    @FXML
    private Button saveProject;

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
    private TextField activityTb;

    @FXML
    private TextField dateTb;

    @FXML
    private TextField userTb;

    @FXML
    private TextField statusTb;

    @FXML
    private Button saverRecord;

    @FXML
    private TextField departmentNameTb;

    private long id=-1;
    @FXML
    void initialize(){
        saveProject.setOnAction(event -> {
            if(isNullOrEmpty(reportNameTb.getText())||isNullOrEmpty(departmentNameTb.getText()))
            JOptionPane.showMessageDialog(null, "Введите название отчета и кафедры!!");
            else {
                Reports report = new Reports();
                DBManager manager = DBManager.getInstance();
                long depId = manager.getDepartmentByName(departmentNameTb.getText()).getId();
                if (id != -1){
                    report = manager.findReportById(id);
                    report.getId();
                    report.setUser_id(Controller.userId);
                    report.setName(reportNameTb.getText());
                    report.setDepartment_id(depId);
                    manager.updateReports(report);
                }
                else {
                    report.setName(reportNameTb.getText());
                    report.setDepartment_id(depId);
                    report.setUser_id(Controller.userId);
                    report.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    manager.insertReports(report);
                    id = manager.findReportId(report.getName());
                }
            }
        });
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
