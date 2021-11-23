package org.orons.dit.dbmslgame.jdbc

import java.sql.*

object PlayerDAO {
    private val conn: Connection
    private lateinit var stmt: Statement

    @JvmStatic
    fun insertPlayer(name: String) {
        val query = "INSERT INTO player(name) VALUES(\"$name\");"
        stmt = conn.createStatement()
        stmt.executeUpdate(query)
        stmt.close()
    }

    @JvmStatic
    fun showPlayers(): Map<Int, String> {
        val query = "SELECT * FROM player;"
        stmt = conn.createStatement()

        val resultSet = stmt.executeQuery(query)
        val resultMap = mutableMapOf<Int, String>()
        while (resultSet.next()) {
            resultMap[resultSet.getInt("pid")] = resultSet.getString("name")
        }
        resultSet.close()
        return resultMap
    }

    init {
        val dbURL = "jdbc:sqlite:/home/sharan/VolumeDisk/Code/InternshalaTrainings/DBMSL-Game/src/main/resources/org/orons/dit/dbmslgame/database/adventure_game.db"
        conn = DriverManager.getConnection(dbURL)
    }
}

fun main() {
    println("------------------")
    println("|PID\t|\tNAME\t\t|")
    for ((pid, name) in PlayerDAO.showPlayers()) {
        println("|$pid\t|\t$name\t\t|")
    }
    println("------------------")
}