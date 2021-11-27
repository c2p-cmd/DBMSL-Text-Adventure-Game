package org.orons.dit.dbmslgame.jdbc

import org.orons.dit.dbmslgame.prettytable.PrettyTable
import java.sql.*
const val dbURL = "jdbc:sqlite:" +
        "/home/sharan/VolumeDisk/Code/InternshalaTrainings/DBMSL-Game/src/main/resources/org/orons/dit/dbmslgame/database/adventure_game.db"

const val ID = "id"
const val DESC = "description"
const val OP1 = "option1"
const val OP2 = "option2"
const val OP3 = "option3"
const val OP4 = "option4"

object PlayerDAO {
    private val conn: Connection = DriverManager.getConnection(dbURL)
    private val stmt: Statement = conn.createStatement()

    @JvmStatic
    fun insertPlayer(name: String): Boolean =
        try {
            stmt.executeUpdate(
                "INSERT INTO player(name) VALUES(\"$name\");"
            ) > 0
        } catch (e: Exception) {
            false
        } finally {
            stmt.close()
        }


    @JvmStatic
    fun showPlayers(): Map<Int, String> =
        mutableMapOf<Int, String>().let { resultMap ->
            stmt.executeQuery(
                "SELECT * FROM player;"
            ).let { resultSet ->
                while (resultSet.next()) {
                    resultMap[resultSet.getInt("pid")] = resultSet.getString("name")
                }
                resultSet.close()
            }
            stmt.close()
            return resultMap
        }


    @JvmStatic
    fun clearPlayerTable(): Boolean =
        stmt.executeUpdate(
            "DELETE FROM player; DELETE FROM sqlite_sequence WHERE name='player';"
        ) > 0

    @JvmStatic
    fun getLatestPlayer(): String? =
        stmt.executeQuery(
            "SELECT name FROM player ORDER BY pid DESC LIMIT 1;"
        ).let { resultSet ->
            if (resultSet.next()) {
                return resultSet.getString("name")
            }
            stmt.close()
            return null
        }

    @JvmStatic
    fun getGameQueryAt(i: Int): Map<String, String> =
        stmt.executeQuery(
            "SELECT q.$DESC, ops.$OP1, ops.$OP2, ops.$OP3, ops.$OP4 FROM game_queries q JOIN game_options ops ON q.id=ops.id WHERE q.id=$i;"
        ).let { resultSet ->
            if (resultSet.next()) {
                return mapOf(
                    Pair(DESC, resultSet.getString(DESC)),
                    Pair(OP1, resultSet.getString(OP1)?: "N/A"),
                    Pair(OP2, resultSet.getString(OP2)?: "N/A"),
                    Pair(OP3, resultSet.getString(OP3)?: "N/A"),
                    Pair(OP4, resultSet.getString(OP4)?: "N/A")
                )
            }
            return mapOf()
        }

}

private fun main() {
    PrettyTable("pid","name").let { prettyTable ->
        for ((pid, name) in PlayerDAO.showPlayers()) {
            prettyTable.addRow(pid.toString(), name)
        }
        prettyTable.print()
    }

    println(PlayerDAO.getLatestPlayer())


}