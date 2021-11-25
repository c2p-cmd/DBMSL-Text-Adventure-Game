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
    fun getGameQueries(): List<List<String?>> =
        mutableListOf<List<String?>>().let { list ->
            stmt.executeQuery(
                "SELECT * FROM game_queries;"
            ).let { resultSet ->
                while (resultSet.next()) {
                    val id = resultSet.getInt(ID).toString()
                    val description = resultSet.getString(DESC)
                    val opt1 = resultSet.getString(OP1)
                    val opt2 = resultSet.getString(OP2)
                    val opt3 = resultSet.getString(OP3)
                    val opt4 = resultSet.getString(OP4)
                    list.add(
                        listOf(id, description, opt1, opt2, opt3, opt4)
                    )
                }
                resultSet.close()
            }
            stmt.close()
            return list
        }

    @JvmStatic
    fun getGameQueryAt(i: Int): Map<String, String?> =
        stmt.executeQuery(
            "SELECT * FROM game_queries WHERE id=$i;"
        ).let { resultSet ->
            while (resultSet.next()) {
                return mapOf<String, String?> (
                    Pair(DESC, resultSet.getString(DESC)),
                    Pair(OP1, resultSet.getString(OP1)),
                    Pair(OP2, resultSet.getString(OP2)),
                    Pair(OP3, resultSet.getString(OP3)),
                    Pair(OP4, resultSet.getString(OP4))
                )
            }
            resultSet.close()
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

    PlayerDAO.getGameQueries().let { allQueries ->
        val q1 = allQueries[0]
        val id = q1[0]
        val desc = q1[1]
        desc?.split("""\n""")?.let { d ->
            for (c in d) {
                println(c)
            }
        }

        val op1 = q1[2]
        if (op1 != null) {
            val nextId = op1.substring(
                (op1.length - 2), (op1.length)
            ).toInt()
            val option1Query = op1.substring(
                0, op1.length-2
            )
            println("nextID = $nextId")
            println(PlayerDAO.getGameQueryAt(nextId)[DESC]?.replace("\"your_name\"", "Sharan"))
        }

        val op2 = q1[3]
        val op3 = q1[4]
        val op4 = q1[5]
    }
}