package com.forever.bee.jvm

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Database

/**
 * The [DB] CatsHostel Database
 * */
object DB {
    private val host = System.getenv("DB_HOST") ?: "localhost"
    private val port = System.getenv("DB_PORT")?.toIntOrNull() ?: 5432
    private val dbName = System.getenv("DB_NAME") ?: "cats_db"
    private val dbUser = System.getenv("DB_USER") ?: "cats_admin"
    private val dbPassword = System.getenv("DB_PASSWORD") ?: "A28051993"

    /*
    * Reads the variables from our environment
    * */
    fun connect() = Database.connect(
        "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}

/**
 * The [CatsTable] table with a primary key of Int type
 *
 * @property name The name of the cat, is 20 characters, is also unique.
 * @property age The age of the cat, with 0 is the default value.
 * */
object CatsTable: IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val age = integer("age").default(0)
}

/**
 * The [Cat] entity to represent a single cat
 */
@kotlinx.serialization.Serializable
data class Cat(
    val id: Int,
    val name: String,
    val age: Int
)



