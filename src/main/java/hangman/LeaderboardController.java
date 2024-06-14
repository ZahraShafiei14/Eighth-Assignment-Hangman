package hangman;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LeaderboardController {

    @FXML
    private TableView<Game> table;
    @FXML
    private TableColumn<Game, String> usernameColumn;
    @FXML
    private TableColumn<Game, String> wordColumn;
    @FXML
    private TableColumn<Game, Integer> wrongGuessesColumn;
    @FXML
    private TableColumn<Game, Integer> timeColumn;
    @FXML
    private TableColumn<Game, Boolean> winColumn;

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        wrongGuessesColumn.setCellValueFactory(new PropertyValueFactory<>("wrongGuesses"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        winColumn.setCellValueFactory(new PropertyValueFactory<>("win"));

        try {
            loadGames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void changeScene(ActionEvent event, String address) throws IOException {
        Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(address)));
        Scene scene = new Scene(root);
        stage.setTitle("Hangman");
        stage.setScene(scene);
        stage.getIcons().add(new Image("hangman_logo.png"));
        stage.show();
        menuStage.close();
    }
    private void loadGames() throws SQLException {
        List<Game> games = DatabaseManager.getGame(); // Fetch games from database
        ObservableList<Game> gameData = FXCollections.observableArrayList(games);
        table.setItems(gameData);
    }
    @FXML
    void backButton(ActionEvent event) {
        try {
            changeScene(event, "/hangman/menu-view.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
