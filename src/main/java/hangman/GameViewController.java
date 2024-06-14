package hangman;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;

public class GameViewController implements Initializable {
    String[] words = {
            "computer",
            "butterfly",
            "programming",
            "happiness",
            "pineapple",
            "vacation",
            "rainbow",
            "elephant"};
    String SecretWord;
    List<Button> buttons = new ArrayList<>();
    LocalTime startTime;
    LocalTime endTime;
    boolean finalWin;
    int duration;
    int wrongGuesses = 0;
    int trueGuesses = 0;
    public static User currentUser;

    @FXML
    private AnchorPane keyboard;
    @FXML
    private ImageView manView;
    @FXML
    private ImageView livesView;
    @FXML
    private HBox wordBox;
    @FXML
    private Label winLabel;
    @FXML
    private Label loseLabel;
    @FXML
    private Label secretWord;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SecretWord = words[new Random().nextInt(words.length)];
        for (int i = 0; i < SecretWord.length(); i++) {
            Button wordButton = new Button("-");
            wordButton.setStyle("-fx-background-color:  #fce8a0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-font-weight: bold; -fx-font-family: 'System';");
            wordButton.setAlignment(Pos.CENTER);
            wordButton.setPrefSize(34,33);
            wordButton.setTextAlignment(TextAlignment.CENTER);
            wordBox.getChildren().add(wordButton);
            buttons.add(wordButton);
        }
        for (Node button : keyboard.getChildren()) {
            Button btn = (Button) button;
            btn.setOnAction(event -> {
                try {
                    pressButton(btn);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        startTime = LocalTime.now();
    }

    void pressButton(Button button) throws IOException {
        char letter = button.getText().toLowerCase().charAt(0);
        if (SecretWord.contains(String.valueOf(letter))) {
            for (int i = 0; i < SecretWord.length(); i++) {
                if (SecretWord.charAt(i) == letter) {
                    button.setStyle("-fx-background-color:  #afcf28;");
                    buttons.get(i).setText(""+Character.toUpperCase(letter)+"");
                    trueGuesses++;
                }
            }
        } else {
            wrongGuesses++;
            button.setStyle("-fx-background-color:  #f8504f;");
            updateHangmanImage();
            updateLivesImage();
            check(wrongGuesses);
        }
        button.setDisable(true);
        if (trueGuesses == SecretWord.length()) endGame(true);
    }

    void check(int wrongGuesses) throws IOException {
        if (wrongGuesses == 7) endGame(false);
    }
    private void updateHangmanImage() {
        String imagePath = getClass().getResource("/images/hang" + wrongGuesses + ".png").toString();
        Image image = new Image(imagePath);
        manView.setImage(image);
    }
    private void updateLivesImage() {
        String imagePath = getClass().getResource("/images/item" + (wrongGuesses + 1)+ ".png").toString();
        Image image = new Image(imagePath);
        livesView.setImage(image);
    }
    public void insertToDatabase(){
        DatabaseManager.insertGame(currentUser.getUsername(), SecretWord, wrongGuesses, duration, finalWin);
    }
    public void disableButtons(){
        for (Node button : keyboard.getChildren()) {
            Button btn = (Button) button;
            btn.setDisable(true);
        }
    }
    void endGame(boolean win) throws IOException {
        disableButtons();
        endTime = LocalTime.now();
        duration = (int) SECONDS.between(startTime, endTime);
        if (win) {
            winLabel.setVisible(true);
            finalWin = true;
        } else {
            loseLabel.setVisible(true);
            secretWord.setVisible(true);
            secretWord.setText("The Word: "+ SecretWord);
            finalWin = false;
        }
        insertToDatabase();
        showLeaderboard();
    }

    public void showLeaderboard() throws IOException {
        Stage gameStage = (Stage) keyboard.getScene().getWindow();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/hangman/leaderboard-view.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        gameStage.close();
    }
}
