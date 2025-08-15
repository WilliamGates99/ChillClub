package com.xeniac.chillclub.core.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createKtorTestClient(
    mockEngine: MockEngine,
    json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
        isLenient = true
    }
): HttpClient = HttpClient(engine = mockEngine) {
    install(ContentNegotiation) {
        register(
            contentType = ContentType.Text.Plain,
            converter = KotlinxSerializationConverter(format = json)
        )
        json(json = json)
    }
    install(DefaultRequest) {
        contentType(ContentType.Application.Json)
    }
}