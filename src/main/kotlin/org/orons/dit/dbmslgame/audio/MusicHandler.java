package org.orons.dit.dbmslgame.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.orons.dit.dbmslgame.Main;

import java.util.Objects;

public class MusicHandler {
    private static final MediaPlayer backgroundScore;
    private static final MediaPlayer gameOverSound;

    static {
        backgroundScore = attachBackgroundScore();
        gameOverSound = attachGameOverSound();
    }

    private static MediaPlayer attachGameOverSound() {
        return new MediaPlayer(
            new Media(
                Objects.requireNonNull(
                        Main.class.getResource("sounds/sfx-defeat5.mp3")
                ).toExternalForm()
            )
        );
    }

    private static MediaPlayer attachBackgroundScore() {
        MediaPlayer player = new MediaPlayer(new Media(
                Objects.requireNonNull(
                        Main.class.getResource("sounds/bg-score.mp3")
                ).toExternalForm()
        ));
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setOnEndOfMedia(() -> {
            player.seek(Duration.ZERO);
            player.play();
        });
        player.stop();
        return player;
    }

    public static void playBackgroundScore() {
        if (gameOverSound.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            gameOverSound.stop();
        }
        MediaPlayer.Status status = backgroundScore.getStatus();
        if (status.equals(MediaPlayer.Status.STOPPED) || status.equals(MediaPlayer.Status.UNKNOWN)) {
            backgroundScore.play();
        }
    }

    public static void playGameOverSound() {
        if (backgroundScore.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            backgroundScore.stop();
        }
        MediaPlayer.Status status = gameOverSound.getStatus();
        if (status.equals(MediaPlayer.Status.STOPPED) || status.equals(MediaPlayer.Status.READY)) {
            gameOverSound.play();
        }
    }
}
