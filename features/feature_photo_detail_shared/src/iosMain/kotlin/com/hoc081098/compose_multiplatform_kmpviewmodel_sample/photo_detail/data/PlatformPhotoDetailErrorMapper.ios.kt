package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.network.sockets.SocketTimeoutException
import org.koin.core.annotation.Singleton
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet

@Singleton
internal actual class PlatformPhotoDetailErrorMapper actual constructor() : (Throwable) -> PhotoDetailError? {
  override fun invoke(t: Throwable): PhotoDetailError? =
    when (t) {
      is PhotoDetailError -> t
      is SocketTimeoutException -> PhotoDetailError.TimeoutError
      is DarwinHttpRequestException ->
        when {
          t.origin.domain == NSURLErrorDomain && t.origin.code in NETWORK_ERROR_CODES ->
            PhotoDetailError.NetworkError

          else -> null
        }

      else -> null
    }

  private companion object {
    private val NETWORK_ERROR_CODES =
      setOf(
        NSURLErrorNotConnectedToInternet,
        NSURLErrorNetworkConnectionLost,
      )
  }
}
