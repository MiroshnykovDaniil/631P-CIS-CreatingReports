package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import db.DBManager;
import db.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.animations.Shake;

// В классе Controller описывается реализация входа в систему
public class Controller {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField login_field;

	@FXML
	private Button authSigInButton;

	@FXML
	private PasswordField password_field;

	@FXML
	private Button loginSingUpButton;

	@FXML
	void initialize() {
		authSigInButton.setOnAction(event -> {
			String loginText = login_field.getText().trim();
			String loginPassword = password_field.getText().trim();

			if (!loginText.equals("") && !loginPassword.equals(""))
				signIn(loginText, loginPassword);
			else
				System.out.println("Логин и пароль пустые");
		});

		loginSingUpButton.setOnAction(event -> {
			openNewScene("/sample/view/singUp.fxml");

		});
	}

	private void signIn(String login, String password) {
		User user = DBManager.getInstance().findUserByLogin(login);
		if (user != null && user.getPassword().equals(password)) {
			if (user.getAuthority_id() == 0) {
				openNewScene("/sample/view/admin.fxml");
			} else {
				openNewScene("/sample/view/app.fxml");
			}
		} else {
			new Shake(login_field).playAnim();
			new Shake(password_field).playAnim();
		}
	}

// метод позволяющий открывать нам любое fxml окно исходя из какого-то действия.
// Достаточно прописать в теле вызова метода расположение файла пример - openNewScene("/sample/view/app.fxml");
	public void openNewScene(String window) {
		loginSingUpButton.getScene().getWidth();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(window));
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parent root = loader.getRoot();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.showAndWait();
	}
}
