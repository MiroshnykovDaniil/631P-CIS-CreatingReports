package sample;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.Activity;
import db.entity.Department;
import db.entity.Report;
import db.entity.Reports;
import db.entity.Teacher;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private TableView<Report> tableViewReportsTable;

    @FXML
    private TableColumn<Report, Integer> numberColumnInTable;

    @FXML
    private TableColumn<Report, String> NameColumnEventInTable;

    @FXML
    private TableColumn<Report, Date> dataColumnInTable;

    @FXML
    private TableColumn<Report, String> authorColumnInTable;

    @FXML
    private TableColumn<Report, String> rateColumnInTable;

    @FXML
    private ComboBox<Activity> activityTb;

    @FXML
    private TextField dateTb;

    @FXML
    private ComboBox<Teacher> userTb;

    @FXML
    private TextField statusTb;

    @FXML
    private Button saverRecord;

    @FXML
    private Button changeRecord;

    @FXML
    private Button deleteRecord;

    @FXML
    private ComboBox<Department> departmentNameTb;

    private long id = -1;

    private long selectedReportId = -1;

    @FXML
    void initialize() {
        DBManager db = DBManager.getInstance();
        departmentNameTb.setItems(FXCollections.observableArrayList(db.getAllDepartments()));
        if (departmentNameTb.getItems().size() == 0) {
            alertError("Немає кафедр у БД!");
        } else {
            departmentNameTb.getSelectionModel().select(0);
        }

        activityTb.setItems(FXCollections.observableArrayList(db.getAllActivities()));
        if (activityTb.getItems().size() == 0) {
            alertError("Немає заходів у БД!");
        } else {
            activityTb.getSelectionModel().select(0);
        }

        userTb.setItems(FXCollections.observableArrayList(db.getAllTeachers()));
        if (userTb.getItems().size() == 0) {
            alertError("Немає викладачів у БД!");
        } else {
            userTb.getSelectionModel().select(0);
        }

        saveProject.setOnAction(event -> {
            if (isNullOrEmpty(reportNameTb.getText()))
                alertWarning("Введіть назву звіту!");
            else {
                Reports report = new Reports();
                DBManager manager = DBManager.getInstance();
                long depId = manager.getDepartmentByName(
                            departmentNameTb.getSelectionModel().getSelectedItem().getName())
                            .getId();
                if (id != -1) {
                    report = manager.findReportById(id);
                    report.getId();
                    report.setUser_id(Controller.userId);
                    report.setName(reportNameTb.getText());
                    report.setDepartment_id(depId);
                    manager.updateReports(report);
                    findReportsByReportsId(id);
                } else {
                    report.setName(reportNameTb.getText());
                    report.setDepartment_id(depId);
                    report.setUser_id(Controller.userId);
                    report.setDate(new Date(Calendar.getInstance().getTime().getTime()));
                    manager.insertReports(report);
                    id = manager.findReportId(report.getName());
                    if (id == -1) {
                        alertError("Неуспішне створення");
                    } else {
                        findReportsByReportsId(id);
                    }
                }
            }
        });

        saverRecord.setOnAction(e -> {
            if (id == -1) {
                alertWarning("Створіть запис!");
                return;
            }
            if (isNullOrEmpty(dateTb.getText()) || isNullOrEmpty(statusTb.getText())) {
                alertWarning("Заповніть всі поля!");
                return;
            }
            Report report = new Report();
            report.setReport_id(id);
            report.setUser_id(userTb.getSelectionModel().getSelectedItem().getId());
            report.setActivity_id(activityTb.getSelectionModel().getSelectedItem().getId());
            report.setStatus(statusTb.getText());
            try {
                report.setDate(Date.valueOf(dateTb.getText()));
                DBManager.getInstance().insertReport(report);
            } catch (IllegalArgumentException ex) {
                alertWarning("Укажіть дату у форматі рррр-мм-дд!");
            }
            findReportsByReportsId(id);
            selectedReportId = -1;
        });

        changeRecord.setOnAction(e -> {
            if (id == -1) {
                alertWarning("Створіть запис!");
                return;
            }
            if (isNullOrEmpty(dateTb.getText()) || isNullOrEmpty(statusTb.getText())) {
                alertWarning("Заповніть всі поля!");
                return;
            }
            if (selectedReportId != -1) {
                Report report = new Report();
                report.setReport_id(id);
                report.setUser_id(userTb.getSelectionModel().getSelectedItem().getId());
                report.setActivity_id(activityTb.getSelectionModel().getSelectedItem().getId());
                report.setStatus(statusTb.getText());
                report.setDate(Date.valueOf(dateTb.getText()));
                report.setId(selectedReportId);
                DBManager.getInstance().updateReport(report);
                findReportsByReportsId(id);
                selectedReportId = -1;
            } else {
                alertWarning("Оберіть запис подвійним кліком!");
            }
        });

        deleteRecord.setOnAction(e -> {
            if (id == -1) {
                alertWarning("Створіть запис!");
                return;
            }
            if (isNullOrEmpty(dateTb.getText()) || isNullOrEmpty(statusTb.getText())) {
                alertWarning("Заповніть всі поля!");
                return;
            }
            if (selectedReportId != -1) {
                Report report = new Report();
                report.setId(selectedReportId);
                DBManager.getInstance().deleteReport(report);
                findReportsByReportsId(id);
                selectedReportId = -1;
            } else {
                alertWarning("Оберіть запис подвійним кліком!");
            }
        });

        tableViewReportsTable.setRowFactory(tv -> {
            TableRow<Report> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 2) {
                    Report clickedRow = row.getItem();
                    selectedReportId = clickedRow.getId();
                    ObservableList<Teacher> teachers = userTb.getItems();
                    for (int i = 0; i < teachers.size(); ++i) {
                        if (teachers.get(i).getId() == clickedRow.getUser_id()) {
                            userTb.getSelectionModel().select(i);
                            break;
                        }
                    }
                    ObservableList<Activity> activities = activityTb.getItems();
                    for (int i = 0; i < activities.size(); ++i) {
                        if (activities.get(i).getId() == clickedRow.getActivity_id()) {
                            activityTb.getSelectionModel().select(i);
                            break;
                        }
                    }
                    statusTb.setText(clickedRow.getStatus());
                    dateTb.setText(clickedRow.getDate().toString());
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
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private void findReportsByReportsId(long id) {
        DBManager manager = DBManager.getInstance();
        ObservableList<Report> report = FXCollections
                    .observableArrayList(manager.getReportById((int) id));
        tableViewReportsTable.setItems(report);
    }

    private void alertWarning(String text) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void alertError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
