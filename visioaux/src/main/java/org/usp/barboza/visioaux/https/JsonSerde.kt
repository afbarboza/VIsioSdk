package org.usp.barboza.visioaux.https

import kotlinx.serialization.json.Json

val jsonSerde = Json {
    ignoreUnknownKeys = true
}