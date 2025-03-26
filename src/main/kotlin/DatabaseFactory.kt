package umfg

import org.jetbrains.exposed.sql.*

object DatabaseFactory {
    fun connect() = Database.connect(
        url = "jdbc:h2:file:./build/db;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
}



