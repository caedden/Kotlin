package umfg.application.payloads

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserPayload(
    val name: String,
    val age: Int
)
