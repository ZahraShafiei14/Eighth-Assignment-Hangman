package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuViewController {

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

    @FXML
    void showLeaderboard(ActionEvent event) {
        try {
            changeScene(event, "/hangman/leaderboard-view.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void startGame(ActionEvent event) {
        try {
            changeScene(event, "/hangman/game-view.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void backButton(ActionEvent event) {
        try {
            changeScene(event, "/hangman/hangman-view.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
