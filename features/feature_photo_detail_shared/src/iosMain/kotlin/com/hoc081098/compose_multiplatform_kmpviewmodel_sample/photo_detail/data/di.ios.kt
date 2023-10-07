package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Singleton

@Singleton
internal actual fun createHttpClient(json: Json): HttpClient =
  com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.createHttpClient(
    engineFactory = Darwin,
    json = json,
  ) {}
