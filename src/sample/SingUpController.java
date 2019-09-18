package sample;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

public class SingUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private Button singUpButton;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField singUpName;

    @FXML
    private TextField singUpLastName;

    @FXML
    private TextField singUpDolznost;

    @FXML
    void initialize() {
    singUpButton.setOnAction(event -> {
        if (isNullOrEmpty(singUpName.getText()) || isNullOrEmpty(singUpLastName.getText()) ||
                isNullOrEmpty(login_field.getText()) || isNullOrEmpty(password_field.getText()) || isNullOrEmpty( singUpDolznost.getText())) {
            JOptionPane.showMessageDialog(null, "Заполните все поля!!");
        } else {
            singUpNewUser();
            start(1);
        }
       });
    }

    private void singUpNewUser() {
        DataBaseHandler dbHandler = new DataBaseHandler();

        String firstName = singUpName.getText();
        String lastName = singUpLastName.getText();
        String userName = login_field.getText();
        String password = password_field.getText();
        String dolznost =  singUpDolznost.getText();

    User user = new User(firstName, lastName, userName, password, dolznost);
    dbHandler.singUpUser(user);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }


    public void start(int primaryStage) {
        singUpButton.setText("Вы успешно зарегистрировались");
        singUpButton.setOnAction(
                (event -> {
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Теперь перейдите в окно входа :)"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();

                }
        ));
    }
}
