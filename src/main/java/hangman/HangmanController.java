package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class HangmanController {

    @FXML
    private TextField nameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private TextField usernameText;

    @FXML
    void logIn(ActionEvent event) throws IOException, SQLException {
        User user = DatabaseManager.getUser(usernameText.getText());
        if (user != null) {
            GameViewController.currentUser = DatabaseManager.getUser(usernameText.getText());
            changeScene(event, "/hangman/menu-view.fxml");
        } else showAlert();
    }

    @FXML
    void signUp(ActionEvent event) throws SQLException, IOException {
        if (nameText.isDisabled()) {
            nameText.setDisable(false);
            return;
        }
        if (DatabaseManager.findUser(usernameText.getText())) {
            showAlert();
            return;
        }
        DatabaseManager.insertUser(usernameText.getText(), passwordText.getText(), nameText.getText());
        GameViewController.currentUser = new User(nameText.getText(),usernameText.getText(),passwordText.getText());
        changeScene(event, "/hangman/menu-view.fxml");
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Username.");
        alert.setContentText("Enter a valid username...");
        if (alert.showAndWait().get() == ButtonType.OK) {
            usernameText.clear();
            passwordText.clear();
            nameText.clear();
            nameText.setDisable(true);
        }
    }

    private void changeScene(ActionEvent event, String address) throws IOException {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(address)));
        Scene scene = new Scene(root);
        stage.setTitle("Hangman");
        stage.setScene(scene);
        stage.getIcons().add(new Image("hangman_logo.png"));
        stage.show();
        loginStage.close();
    }
}
