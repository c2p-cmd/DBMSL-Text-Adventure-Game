package org.orons.dit.dbmslgame

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline

import javafx.fxml.FXML
import javafx.fxml.Initializable

import javafx.scene.control.Button
import javafx.scene.control.TextArea

import javafx.util.Duration
import org.orons.dit.dbmslgame.jdbc.PlayerDAO

import java.net.URL
import java.util.*

private val playerName = "\'${PlayerDAO.getLatestPlayer()}\'"

class GameController : Initializable {
    @FXML // TextArea
    lateinit var gameTextArea: TextArea

    @FXML // Buttons
    lateinit var option1Button: Button
    lateinit var option2Button: Button
    lateinit var option3Button: Button
    lateinit var option4Button: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        typeText("->Welcome $playerName to Text Traveller; today you shall embark upon\n->a new journey!\n->")
        disableButtons(booleanArrayOf(true, true, true, true))

    }

    private fun disableButtons(b: BooleanArray) {
        val (b1, b2, b3, b4) = b
        option1Button.isDisable = b1
        option2Button.isDisable = b2
        option3Button.isDisable = b3
        option4Button.isDisable = b4
    }

    private fun typeText(text: String) {
        // variables
        var itr = 0
        val timeline = Timeline()
        val keyFrame = KeyFrame(
            Duration.seconds(0.05), {
                if (itr+1 > text.length)
                    timeline.stop()
                else {
                    gameTextArea.appendText(text.substring(itr, itr+1))
                    itr++
                }
            }
        )
        timeline.keyFrames.add(keyFrame)
        timeline.cycleCount = Animation.INDEFINITE
        timeline.play()
    }
}

private fun showZombie(): String =
    ("                           \n" +
    "          `-:smdhs:`       \n" +
    "       `+dNNMMMMMMMmo      \n" +
    "     `:mMMMMMMMMMMMMMh     \n" +
    "    :mNNmmMMMMMMNmmMMM/    \n" +
    "    oMs:o/:mMMMs..-:mMo    \n" +
    "    oM`-h/ oMMM` -dooMo    \n" +
    "    /Mh:--+NmsNh:-+sNM/    \n" +
    "     +mMNMMM+:+MMNMMm+     \n" +
    "      :MMMMNNmNNMMMM:      \n" +
    "      -MMMd/N-N/dMMM-      \n" +
    "       +dNmyMoMymNd+       \n" +
    "         -sdmmmds-         \n" +
    "                           \n")
