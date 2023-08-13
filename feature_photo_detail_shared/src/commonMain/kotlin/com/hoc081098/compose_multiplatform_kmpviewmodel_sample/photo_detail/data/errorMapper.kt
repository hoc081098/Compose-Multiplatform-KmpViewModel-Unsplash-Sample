package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import arrow.core.nonFatalOrThrow
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import io.github.aakira.napier.Napier
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.util.cio.ChannelReadException
import org.koin.core.annotation.Singleton

internal interface PhotoDetailErrorMapper : (Throwable) -> PhotoDetailError

@Singleton(
  binds = [],
)
internal expect class PlatformPhotoDetailErrorMapper constructor() : (Throwable) -> PhotoDetailError?

@Singleton(
  binds = [
    PhotoDetailErrorMapper::class,
  ],
)
internal class RealPhotoDetailErrorMapper(
  private val platformMapper: PlatformPhotoDetailErrorMapper,
) : PhotoDetailErrorMapper {
  override fun invoke(throwable: Throwable): PhotoDetailError {
    Napier.d("PhotoDetailErrorMapperImpl.map $throwable")

    val t = throwable.nonFatalOrThrow()

    // Platform mapper has higher priority.
    // If it returns non-null value, then return it, and ignore the rest.
    platformMapper(t)?.let {
      Napier.d("platformPhotoDetailErrorMapper.map -> $it")
      return it
    }

    return when (t) {
      // Already mapped error
      is PhotoDetailError -> t

      // Server error
      is ResponseException -> PhotoDetailError.ServerError

      // Timeout error
      is HttpRequestTimeoutException,
      is ConnectTimeoutException,
      is SocketTimeoutException,
      -> PhotoDetailError.TimeoutError

      // Network error
      is ChannelReadException -> PhotoDetailError.NetworkError

      // Unexpected error
      else -> PhotoDetailError.Unexpected
    }
  }
}
