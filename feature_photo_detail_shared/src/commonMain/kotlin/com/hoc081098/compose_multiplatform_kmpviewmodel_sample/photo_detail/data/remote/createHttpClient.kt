package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal fun <T : HttpClientEngineConfig> createHttpClient(
  engineFactory: HttpClientEngineFactory<T>,
  json: Json,
  block: T.() -> Unit,
): HttpClient =
  HttpClient(engineFactory) {
    engine(block)

    install(HttpTimeout) {
      requestTimeoutMillis = 15_000
      connectTimeoutMillis = 10_000
      socketTimeoutMillis = 10_000
    }

    install(ContentNegotiation) {
      json(json)
      register(
        ContentType.Text.Plain,
        KotlinxSerializationConverter(json),
      )
    }

    install(Logging) {
      level = LogLevel.ALL
      logger =
        object : Logger {
          override fun log(message: String) {
            Napier.d(message = message, tag = "[HttpClient]")
          }
        }
    }
  }
