package umfg.application.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserCreatedResponse(
    val id: Int
)
