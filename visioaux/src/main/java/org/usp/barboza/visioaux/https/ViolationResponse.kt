package org.usp.barboza.visioaux.https

import kotlinx.serialization.Serializable

@Serializable
data class ViolationResponse(
    val id: Int,
    val message: String
)