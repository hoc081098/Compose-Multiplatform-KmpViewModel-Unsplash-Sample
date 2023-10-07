package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.domain.SearchPhotoError
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.koin.core.annotation.Singleton

@Singleton
internal actual class PlatformSearchPhotoErrorMapper actual constructor() : (Throwable) -> SearchPhotoError? {
  override fun invoke(t: Throwable): SearchPhotoError? =
    when (t) {
      is SearchPhotoError -> t
      is IOException ->
        when (t) {
          is UnknownHostException, is SocketException -> SearchPhotoError.NetworkError
          is SocketTimeoutException -> SearchPhotoError.TimeoutError
          else -> null
        }

      else -> null
    }
}
