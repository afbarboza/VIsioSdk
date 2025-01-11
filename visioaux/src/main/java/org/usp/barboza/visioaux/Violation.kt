package org.usp.barboza.visioaux

import kotlinx.serialization.Serializable

@Serializable
data class Violation(
    val id: String,
    val violationType: String,
    val activityName: String,
    val conformanceLevel: String,
    val developerMessage: String,
    val deviceId: String? = ""
)
