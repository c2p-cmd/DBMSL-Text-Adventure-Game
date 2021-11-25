package org.orons.dit.dbmslgame

import org.orons.dit.dbmslgame.jdbc.*

// private fun String.getNextID() = this.substring(this.length-2, this.length).toInt()
// fun String.getQuery() = this.substring(0, this.length-2)

fun GameController.townGate(): Unit = PlayerDAO.getGameQueryAt(1).
    let { map ->
        disableButtons(booleanArrayOf(false, false, false, false))
        val desc = buildString {
            for (s in map[DESC]!!.split("""\n"""))
                append("$s\n")
        }
        typeText("\n->$desc")

        setButtonText(
            listOf(
                "Talk To Guard.",
                "Slap The Guard.",
                "Try Sneaking.",
                "Leave."
            )
        )

        option1Button.setOnAction {
            talkToGuard()
        }
        option2Button.setOnAction {
            typeText("Guard: Hey don't be stupid.\nThe guard hit you so hard and you gave up.(You receive 1 damage)")
            this.player.playerHP--
        }
        option3Button.setOnAction {
            gameOver()
        }
        option4Button.setOnAction {
            crossRoad()
        }
    }

private fun GameController.talkToGuard(): Unit = PlayerDAO.getGameQueryAt(2).
        let { map ->
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = map[DESC]!!.replace("\"your_name\"", this.player.name).split("""\n""")

            typeText("->$desc")

            setButtonText(
                listOf(
                    "Leave",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                crossRoad()
            }
        }


private fun GameController.crossRoad(): Unit = PlayerDAO.getGameQueryAt(4).
    let { map ->
        disableButtons(booleanArrayOf(false,false,false,false))
        val desc = buildString {
            for (s in map[DESC]!!.split("""\n"""))
                append("$s\n")
        }
        typeText("->$desc")

        setButtonText(
            listOf(
                "Go North",
                "Go East",
                "Go South",
                "Go West"
            )
        )
        option1Button.setOnAction {
            goToRiver()
        }
        option2Button.setOnAction {
            goToForest()
        }
        option3Button.setOnAction {
            townGate()
        }
        option4Button.setOnAction {
            paleMan()
        }
    }

private fun GameController.goToRiver(): Unit = PlayerDAO.getGameQueryAt(5).
        let { map ->
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
                append("${player.name}'s HP = ${++player.playerHP}")
            }
            println(desc)
            gameTextArea.appendText("\n->$desc")

            crossRoad()
        }

private fun GameController.goToForest(): Unit = PlayerDAO.getGameQueryAt(6).
        let { map ->
            val newWeapon = listOf("Viking Battle Axes", "Machete").random()
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }.replace("weapon", newWeapon)

            gameTextArea.appendText("\n->$desc")
            this.player.weapon = newWeapon
            crossRoad()
        }

private fun GameController.paleMan(): Unit = PlayerDAO.getGameQueryAt(7).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Walk towards him",
                    "Walk away",
                    "N/A",
                    "N/A"
                )
            )

            option1Button.setOnAction {
                walkTowardsPaleMan()
            }
            option2Button.setOnAction {
                crossRoad()
            }
        }

private fun GameController.walkTowardsPaleMan(): Unit = PlayerDAO.getGameQueryAt(8).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc\n\n$player")

            setButtonText(
                listOf(
                    "Fight Pale Man",
                    "Run",
                    "N/A",
                    "N/A"
                )
            )

            option1Button.setOnAction {
                attackPaleMan()
            }
            option2Button.setOnAction {
                crossRoad()
            }
        }

private fun GameController.attackPaleMan() {
        disableButtons(booleanArrayOf(false, false, true, true))
        val desc = player.getStats()
        typeText("->$desc")

        setButtonText(
            listOf(
                "Attack!",
                "Run",
                "N/A",
                "N/A"
            )
        )

        option1Button.setOnAction {
            val damageToPlayer = (1..4).random()
            this.player.playerHP -= damageToPlayer

            var damageToMan = 0
            when (player.weapon) {
                "Fist" -> {
                    damageToMan = (1..3).random()
                }
                "Machete" -> {
                    damageToMan = (1..5).random()
                }
                "Viking Battle Axe" -> {
                    damageToMan = (1..8).random()
                }
            }
            player.manHP -= damageToMan
            gameTextArea.appendText("Damage to\nPlayer: $damageToPlayer\nMan: $damageToMan")
            println("Weapon = ${player.weapon}, damage = $damageToMan, manHP = ${player.manHP}, playerHP = ${player.playerHP}")
            if (player.manHP <= 0 && player.playerHP > 0) {
                monsterKilled()
            }
            else {
                attackPaleMan()
            }

        }
        option2Button.setOnAction {
            crossRoad()
        }
    }

private fun GameController.monsterKilled(): Unit = PlayerDAO.getGameQueryAt(12).
        let {
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = "You killed the monster!\n" +
                        "The monster dropped a shiny object!\n" +
                        "You obtained a silver pendant!\n" +
                        "\n" +
                        "\"Guard hears your battle victory and approaches you\"\n" +
                        "Guard: Oh you killed that creature??! Great!\n" +
                        "Guard: You seem like a trustworthy guy. Welcome to our town!"
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Follow the guard",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                this.followGuard()
            }
        }

private fun GameController.followGuard(): Unit = PlayerDAO.getGameQueryAt(13).
        let { map ->
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            this.player.playerHP+=5

            setButtonText(
                listOf(
                    "Drink",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                this.wakeUpInRoom()
            }
        }

private fun GameController.wakeUpInRoom(): Unit = PlayerDAO.getGameQueryAt(14).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            option1Button.text = "Look further"
            option2Button.text = "Leave the room"

            setButtonText(
                listOf(
                    "Look Further",
                    "Leave the room",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                this.checkDoor()
            }
            option2Button.setOnAction {
                leaveRoom()
            }
        }

private fun GameController.checkDoor(): Unit = PlayerDAO.getGameQueryAt(15).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Hit the door",
                    "Leave the room",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                typeText("Door doesn't open.")
                this.player.playerHP--
                gameTextArea.appendText("You receive 1 damage ${player.playerHP}")
            }
            option2Button.setOnAction {
                leaveRoom()
            }
        }

private fun GameController.leaveRoom(): Unit = PlayerDAO.getGameQueryAt(16).
        let { map ->
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Talk to merchant",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                questionMerchant()
            }
        }

private fun GameController.questionMerchant(): Unit = PlayerDAO.getGameQueryAt(17).
        let { map ->
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "What is this place?",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction { continueConversation() }
        }

private fun GameController.continueConversation(): Unit = PlayerDAO.getGameQueryAt(20).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = "Merchant: This is an isolated town and we have been affected by the plague. \n \"As the merchant can say anything further.\"\n" +
                        buildString {
                            for (s in map[DESC]!!.split("""\n"""))
                                append("$s\n")
                        }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Fight",
                    "Run",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction { gameOver() }
            option2Button.setOnAction { this.runBackUp() }
        }

private fun GameController.runBackUp(): Unit = PlayerDAO.getGameQueryAt(21).
        let { map ->
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Open Door",
                    "Fight the man",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction { openDoor() }
            option2Button.setOnAction { gameOver() }
        }

private fun GameController.openDoor(): Unit = PlayerDAO.getGameQueryAt(22).
        let { map ->
            disableButtons(booleanArrayOf(false, true, true, true))
            val desc = buildString {
                for (s in map[DESC]!!.split("""\n"""))
                    append("$s\n")
            }
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Look for supplies",
                    "N/A",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                this.player.weapon = "CrossBow"
                endingScene()
            }
        }

private fun GameController.endingScene() {
            disableButtons(booleanArrayOf(false, false, true, true))
            val desc = "You find some food supplies and a crossbow."
            typeText("->$desc")

            setButtonText(
                listOf(
                    "Use ${player.weapon} to shoot",
                    "Replenish your health.",
                    "N/A",
                    "N/A"
                )
            )
            option1Button.setOnAction {
                youWin()
            }
            option2Button.setOnAction {
                player.playerHP += 5
                val damage = (0..player.playerHP).random()
                typeText("You recover 5HP and now, the man attacks you and gives $damage damage.")
                player.playerHP -= damage
                youWin()
            }
        }

fun GameController.gameOver() {
    gameTextArea.clear()
    gameTextArea.appendText(showZombie())
}

fun GameController.youWin() {
    gameTextArea.clear()
    typeText("Game is over you have won!\nCongratulations you saved the town from \nfurther infection of the pale man!")
    disableButtons(booleanArrayOf(false, false, true, true))
    setButtonText(listOf("Play Again?","Quit","N/A","N/A"))
    option1Button.setOnAction {
        townGate()
    }
    option2Button.setOnAction {
        quitGame()
    }
}

fun GameController.showZombie(): String {
    disableButtons(booleanArrayOf(false, false, true, true))
    setButtonText(listOf("Retry?","Quit","N/A","N/A"))
    option1Button.setOnAction {
        player.resetValues()
        gameTextArea.clear()
        townGate()
    }
    option2Button.setOnAction {
        quitGame()
    }
    return (
            "                           \n" +
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
                    "                           \n" +
                    "The End!!\n" + "You Are Infected!\n"
            )
}
