package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.animations.Shake;

import javax.swing.*;

import db.DBManager;
import db.entity.User;

public class SingUpController {

	private boolean isRegistered;

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
			if (isNullOrEmpty(singUpName.getText()) || isNullOrEmpty(singUpLastName.getText())
					|| isNullOrEmpty(login_field.getText()) || isNullOrEmpty(password_field.getText())
					|| isNullOrEmpty(singUpDolznost.getText())) {
				JOptionPane.showMessageDialog(null, "Заполните все поля!!");
			} else {
				singUpNewUser();
				start(1);
			}
		});
	}

	/**
	 * НУЖНО ПОЛЯ ВВОДА СДЕЛАТЬ В СООТВЕТСТВИИ С ТАБЛИЦЕЙ БД
	 */
	private void singUpNewUser() {
		// String firstName = singUpName.getText();
		// String lastName = singUpLastName.getText();

		String login = login_field.getText();
		String password = password_field.getText();
		Long dolznost = Long.parseLong(singUpDolznost.getText());

		User user = new User();
		user.setEmail("email");
		user.setAuthority_id(dolznost);
		user.setName(login);
		user.setPassword(password);

		DBManager manager = DBManager.getInstance();
		if (manager.findUserByLogin(user.getName()) == null) {
			manager.insertUser(user);
			isRegistered = true;
		} else {
			new Shake(login_field).playAnim();
			new Shake(password_field).playAnim();
			new Shake(singUpName).playAnim();
			new Shake(singUpLastName).playAnim();
			new Shake(singUpDolznost).playAnim();
		}
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public void start(int primaryStage) {
		if (!isRegistered) {
			initialize();
		} else {
			singUpButton.setText("Вы успешно зарегистрировались");
			singUpButton.setOnAction((event -> {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				VBox dialogVbox = new VBox(20);
				dialogVbox.getChildren().add(new Text("Теперь перейдите в окно входа :)"));
				Scene dialogScene = new Scene(dialogVbox, 300, 200);
				dialog.setScene(dialogScene);
				dialog.show();

			}));
		}
	}
}
