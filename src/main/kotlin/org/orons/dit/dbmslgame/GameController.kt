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
import kotlin.system.exitProcess

class GameController : Initializable {
    inner class Player(
        val name: String,
    ) {
        var playerHP = 10
        var manHP = 15

        var weapon = "Fist"

        fun isDead(): Boolean = playerHP <= 0

        override fun toString(): String =
            buildString {
                append("Name   : $name\n")
                append("Weapon : $weapon\n")
            }

        fun getStats(): String = buildString {
            append("$name HP=$playerHP\n")
            append("Man HP=$manHP")
        }

        fun resetValues() {
            playerHP = 10
            manHP = 15
            weapon = "Fist"
        }
    }

    val player = Player("\'${PlayerDAO.getLatestPlayer()}\'")

    @FXML // TextArea
    lateinit var gameTextArea: TextArea

    @FXML // Buttons
    lateinit var option1Button: Button
    lateinit var option2Button: Button
    lateinit var option3Button: Button
    lateinit var option4Button: Button

    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        gameTextArea.appendText("->Welcome ${player.name} to Text Traveller; today you shall embark upon\n->a new journey!\n")
        disableButtons(booleanArrayOf(true, true, true, true))

        townGate()
    }

    fun disableButtons(b: BooleanArray) {
        val (b1, b2, b3, b4) = b
        option1Button.isDisable = b1
        option2Button.isDisable = b2
        option3Button.isDisable = b3
        option4Button.isDisable = b4
    }

    fun typeText(text: String) {
        gameTextArea.appendText("\n")
        // variables
        var itr = 0
        val timeline = Timeline()
        val keyFrame = KeyFrame(
            Duration.seconds(0.01), {
                if (itr+1 > text.length) {
                    timeline.stop()
                    gameTextArea.appendText("\n")
                }
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

fun GameController.setButtonText(options: List<String>) {
    option1Button.text = options[0]
    option2Button.text = options[1]
    option3Button.text = options[2]
    option4Button.text = options[3]
}

fun quitGame(): Nothing = exitProcess(0)