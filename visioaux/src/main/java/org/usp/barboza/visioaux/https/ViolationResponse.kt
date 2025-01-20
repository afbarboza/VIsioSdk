package org.usp.barboza.visioaux.https

import kotlinx.serialization.Serializable

@Serializable
data class ViolationResponse(
    val id: Int,
    val violationType: String,
    val activityName: String,
    val conformanceLevel: String,
    val developerMessage: String,
    val deviceId: String? = ""
)