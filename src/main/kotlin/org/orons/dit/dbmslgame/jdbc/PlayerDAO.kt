package org.orons.dit.dbmslgame.jdbc

import org.orons.dit.dbmslgame.prettytable.PrettyTable
import java.sql.*
const val dbURL = "jdbc:sqlite:" +
        "/home/sharan/VolumeDisk/Code/InternshalaTrainings/DBMSL-Game/src/main/resources/org/orons/dit/dbmslgame/database/adventure_game.db"

object PlayerDAO {
    private val conn: Connection = DriverManager.getConnection(dbURL)
    private val stmt: Statement = conn.createStatement()

    @JvmStatic
    fun insertPlayer(name: String): Boolean {
        return try {
            val query = "INSERT INTO player(name) VALUES(\"$name\");"
            val res = stmt.executeUpdate(query)
            (res > 0)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            false
        } finally {
            stmt.close()
        }
    }

    @JvmStatic
    fun showPlayers(): Map<Int, String> {
        val query = "SELECT * FROM player;"

        val resultSet = stmt.executeQuery(query)
        val resultMap = mutableMapOf<Int, String>()
        while (resultSet.next()) {
            resultMap[resultSet.getInt("pid")] = resultSet.getString("name")
        }
        resultSet.close()
        return resultMap
    }

    @JvmStatic
    fun clearPlayerTable(): Boolean =
        stmt.executeUpdate(
            "DELETE FROM player; DELETE FROM sqlite_sequence WHERE name='player';"
        ) > 0

    @JvmStatic
    fun getLatestPlayer(): String? {
        val query = "SELECT name FROM player ORDER BY pid DESC LIMIT 1;"

        val resultSet = stmt.executeQuery(query)
        if (resultSet.next()) {
            return resultSet.getString("name")
        }
        stmt.close()
        return null
    }
}

fun PrettyTable.print() {
    println(this.toString())
}

private fun main() {
    PrettyTable("pid","name").let { prettyTable ->
        for ((pid, name) in PlayerDAO.showPlayers()) {
            prettyTable.addRow(pid.toString(), name)
        }
        prettyTable.print()
    }
}