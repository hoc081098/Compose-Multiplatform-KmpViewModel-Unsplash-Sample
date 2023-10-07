package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import io.ktor.client.HttpClient
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.ksp.generated.module

@Module
@ComponentScan
internal class DataModule

@Suppress("NOTHING_TO_INLINE")
internal inline fun dataModule() = DataModule().module

@Singleton
internal expect fun createHttpClient(json: Json): HttpClient

@Singleton
internal fun createJson(): Json =
  Json {
    serializersModule =
      SerializersModule {
        contextual(Instant::class, InstantSerializer)
      }
    ignoreUnknownKeys = true
    coerceInputValues = true
    prettyPrint = true
    isLenient = true
    encodeDefaults = true
    allowSpecialFloatingPointValues = true
    allowStructuredMapKeys = true
    useArrayPolymorphism = false
  }

internal object InstantSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor(
      "InstantSerializer",
      PrimitiveKind.STRING,
    )

  override fun serialize(
    encoder: Encoder,
    value: Instant,
  ) = encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}
