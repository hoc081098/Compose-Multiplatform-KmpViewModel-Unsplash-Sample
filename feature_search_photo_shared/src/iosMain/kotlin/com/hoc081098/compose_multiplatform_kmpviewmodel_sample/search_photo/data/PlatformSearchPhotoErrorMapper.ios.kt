package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoError
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.network.sockets.SocketTimeoutException
import org.koin.core.annotation.Singleton
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet

@Singleton
internal actual class PlatformSearchPhotoErrorMapper actual constructor() : (Throwable) -> SearchPhotoError? {
  override fun invoke(t: Throwable): SearchPhotoError? =
    when (t) {
      is SearchPhotoError -> t
      is SocketTimeoutException -> SearchPhotoError.TimeoutError
      is DarwinHttpRequestException ->
        when {
          t.origin.domain == NSURLErrorDomain && t.origin.code in NETWORK_ERROR_CODES ->
            SearchPhotoError.NetworkError

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
