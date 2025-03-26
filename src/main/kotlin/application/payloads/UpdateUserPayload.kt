package umfg.application.payloads

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPayload(
    val name: String,
    val age: Int
)
