package umfg.infra.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import umfg.DatabaseFactory
import umfg.application.payloads.CreateUserPayload
import umfg.application.payloads.UpdateUserPayload

class UserRepository {

    private val database = DatabaseFactory.connect()

    object UserTable : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun create(payload: CreateUserPayload): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            UserTable.insert {
                it[name] = payload.name
                it[age] = payload.age
            }[UserTable.id]
        }
    }

    suspend fun findAll(): List<User> {
        return newSuspendedTransaction(Dispatchers.IO) {
            UserTable.selectAll()
                .map {
                    User(
                        id = it[UserTable.id],
                        name = it[UserTable.name],
                        age = it[UserTable.age]
                    )
                }
        }
    }

    suspend fun findById(id: Int): User? {
        return newSuspendedTransaction(Dispatchers.IO) {
            UserTable.selectAll()
                .where { UserTable.id eq id }
                .map {
                    User(
                        id = it[UserTable.id],
                        name = it[UserTable.name],
                        age = it[UserTable.age]
                    )
                }.singleOrNull()
        }
    }
    suspend fun update(id: Int, payload: UpdateUserPayload): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            UserTable.update({ UserTable.id eq id }) {
                it[name] = payload.name
                it[age] = payload.age
            }
        }
    }
    suspend fun delete(id: Int): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val rowsAffected = UserTable.deleteWhere { UserTable.id eq id }
            require(rowsAffected > 0) { "Erro: Usuário com ID $id não encontrado!" }
            rowsAffected
        }
    }



}