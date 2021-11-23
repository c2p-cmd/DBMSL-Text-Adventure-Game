package org.orons.dit.dbmslgame

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import java.net.URL
import java.util.*

class GameController : Initializable {
    @FXML
    lateinit var gameTextArea: TextArea

    @FXML
    lateinit var option1Button: Button

    @FXML
    lateinit var option2Button: Button

    @FXML
    lateinit var option3Button: Button

    @FXML
    lateinit var option4Button: Button

    override fun initialize(p0: URL?, p1: ResourceBundle?) {

    }
}