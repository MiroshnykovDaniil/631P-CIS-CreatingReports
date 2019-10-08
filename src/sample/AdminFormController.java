package sample;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

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
    private TableView<Reports> tableViewReportsTable2;

    @FXML
    private TableColumn<Reports, String> DepartmentNameInTable;

    @FXML
    private TableColumn<Reports, String> ProjectNameInTable;

    @FXML
    private TableView<Department> tableDepartment;

    @FXML
    private TableColumn<Department, String> depColumn;

    @FXML
    private Button buttonChangeDep;

    @FXML
    private Button buttonDeleteDep;

    @FXML
    private Button buttonAddDep;

    @FXML
    private TextField textDep;

    @FXML
    private TableView<Position> tablePos;

    @FXML
    private TableColumn<Position, String> posColumn;

    @FXML
    private Button buttonPosDel;

    @FXML
    private Button buttonPosChange;

    @FXML
    private Button buttonPosAdd;

    @FXML
    private TextField textPos;

    @FXML
    void initialize() {
        showAllDep();
        showAllPos();
        showAllReports.setOnAction(event -> {
            if (isNullOrEmpty(groupNumber.getText())) {
                findAllReports();
            } else {
                findReportsByDep();
            }
        });
        buttonDeleteDep.setOnAction(event -> {
            deleteDep();
            showAllDep();
        });
        buttonChangeDep.setOnAction(event -> {
            if (textDep.getText() != "")
                updateDep();
            showAllDep();
        });
        buttonAddDep.setOnAction(event -> {
            if (textDep.getText() != "")
                addDep();
            showAllDep();
        });
        buttonPosAdd.setOnAction(event -> {
            if(textPos.getText()!="")
                addPos();
            showAllPos();
        });
        buttonPosChange.setOnAction(event -> {
            if(textPos.getText()!="")
                updatePos();
            showAllPos();
        });
        buttonPosDel.setOnAction(event -> {
            deletePos();
            showAllPos();
        });

        depColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberColumnInTable.setCellValueFactory(new PropertyValueFactory<>("id"));

        DepartmentNameInTable.setCellValueFactory(new PropertyValueFactory<>("name"));

        DepartmentNameInTable.setCellValueFactory(
                new PropertyValueFactory<Reports, String>("department_id") {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Reports, String> param) {
                        Object value = super.call(param).getValue();
                        String data = DBManager.getInstance().getDepartmentById((Long) value)
                                .getName();
                        ObservableValue<String> newValue = new ReadOnlyObjectWrapper<String>(
                                data);
                        return newValue;
                    }
                });
        posColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        ProjectNameInTable.setCellValueFactory(new PropertyValueFactory<>("name"));

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
                            TableColumn.CellDataFeatures<Report, String> param) {
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
                            TableColumn.CellDataFeatures<Report, String> param) {
                        Object value = super.call(param).getValue();
                        for (Teacher teacher : DBManager.getInstance().getAllTeachers()) {
                            if (teacher.getId() == (Long) value) {
                                return new ReadOnlyObjectWrapper<String>(teacher.getName());
                            }
                        }
                        return new ReadOnlyObjectWrapper<String>("");
                    }
                });

        deleteButton.setOnAction(event ->{
            DBManager manager = DBManager.getInstance();
            System.out.println(tableViewReportsTable2.getSelectionModel().getSelectedItem());
            if (tableViewReportsTable2.getSelectionModel().getSelectedItem() != null)
                manager.deleteReports(tableViewReportsTable2.getSelectionModel().getSelectedItem());
            showAllReports.fire();
            System.out.println("deleted");
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

    private void showAllDep(){
        DBManager manager = DBManager.getInstance();
        ObservableList<Department> departments = FXCollections
                .observableArrayList(manager.getDepartment());
        tableDepartment.setItems(departments);
    }

    private void deleteDep(){
        DBManager manager = DBManager.getInstance();
        if (tableDepartment.getSelectionModel().getSelectedItem() != null)
            manager.deleteDepartment(tableDepartment.getSelectionModel().getSelectedItem());
    }
    private void updateDep(){
        DBManager manager = DBManager.getInstance();
        if (tableDepartment.getSelectionModel().getSelectedItem() != null)
            manager.updateDepartment(textDep.getText(),tableDepartment.getSelectionModel().getSelectedItem());
    }
    private void addDep(){
        DBManager manager = DBManager.getInstance();
        manager.insertDepartment(textDep.getText());
    }

    private void showAllPos(){
        DBManager manager = DBManager.getInstance();
        ObservableList<Position> positions = FXCollections
                .observableArrayList(manager.getPosition());
        tablePos.setItems(positions);
    }
    private void addPos(){
        DBManager manager = DBManager.getInstance();
        manager.insertPosition(textPos.getText());
    }
    private void updatePos(){
        DBManager manager = DBManager.getInstance();
        if (tablePos.getSelectionModel().getSelectedItem() != null)
            manager.updatePosition(textPos.getText(),tablePos.getSelectionModel().getSelectedItem());
    }
    private void deletePos(){
        DBManager manager = DBManager.getInstance();
        if (tablePos.getSelectionModel().getSelectedItem() != null)
            manager.deletePosition(tablePos.getSelectionModel().getSelectedItem());
    }
}
