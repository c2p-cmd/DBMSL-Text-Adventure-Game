package org.orons.dit.dbmslgame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.orons.dit.dbmslgame.audio.MusicHandler;
import org.orons.dit.dbmslgame.jdbc.PlayerDAO;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application implements Runnable, Initializable {
    static String[] args;

    @FXML
    TextField playerNameTextField;

    @FXML
    Button readyButton;

    public static void main(String[] args) {
        Main.args = args;
        new Thread(new Main(), "Player Profile Thread").start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("player-profile.fxml"));

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
            Objects.requireNonNull(
                    Main.class.getResource("button.css")
            ).toExternalForm()
        );

        new Thread(MusicHandler::playBackgroundScore).start();

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setMaxWidth(scene.getWidth());
        stage.setMaxHeight(scene.getHeight());

        stage.show();
    }

    @Override
    public void run() {
        Application.launch(Main.args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readyButton.setOnAction(actionEvent -> {
            String name = playerNameTextField.getText();
            if (name == null || name.equalsIgnoreCase("")) {
                warnUser("Name cannot be empty.");
            } else {
                 try {
                     if (PlayerDAO.insertPlayer(name))
                         switchToGame(actionEvent);
                     else {
                         warnUser("Name already has been taken.");
                     }
                 } catch (Exception e) {
                     warnUser(e.getMessage());
                 }
            }
        });
    }

    private void switchToGame(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
            Stage gameStage = (Stage)((Node)event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            Main.class.getResource("button.css")
                    ).toExternalForm()
            );

            gameStage.setScene(scene);
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void warnUser(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input.");
        alert.setContentText(msg);
        alert.show();
    }
}
