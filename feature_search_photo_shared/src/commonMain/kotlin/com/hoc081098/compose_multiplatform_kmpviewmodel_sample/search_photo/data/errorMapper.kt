package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import arrow.core.nonFatalOrThrow
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoError
import io.github.aakira.napier.Napier
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.util.cio.ChannelReadException
import org.koin.core.annotation.Singleton

internal interface SearchPhotoErrorMapper : (Throwable) -> SearchPhotoError

@Singleton(
  binds = [],
)
internal expect class PlatformSearchPhotoErrorMapper constructor() : (Throwable) -> SearchPhotoError?

@Singleton(
  binds = [
    SearchPhotoErrorMapper::class,
  ],
)
internal class RealSearchPhotoErrorMapper(
  private val platformMapper: PlatformSearchPhotoErrorMapper,
) : SearchPhotoErrorMapper {
  override fun invoke(throwable: Throwable): SearchPhotoError {
    Napier.d("SearchPhotoErrorMapperImpl.map $throwable")

    val t = throwable.nonFatalOrThrow()

    // Platform mapper has higher priority.
    // If it returns non-null value, then return it, and ignore the rest.
    platformMapper(t)?.let {
      Napier.d("platformSearchPhotoErrorMapper.map -> $it")
      return it
    }

    return when (t) {
      // Already mapped error
      is SearchPhotoError -> t

      // Server error
      is ResponseException -> SearchPhotoError.ServerError

      // Timeout error
      is HttpRequestTimeoutException,
      is ConnectTimeoutException,
      is SocketTimeoutException,
      -> SearchPhotoError.TimeoutError

      // Network error
      is ChannelReadException -> SearchPhotoError.NetworkError

      // Unexpected error
      else -> SearchPhotoError.Unexpected
    }
  }
}
