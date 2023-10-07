package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailError
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.koin.core.annotation.Singleton

@Singleton
internal actual class PlatformPhotoDetailErrorMapper actual constructor() : (Throwable) -> PhotoDetailError? {
  override fun invoke(t: Throwable): PhotoDetailError? =
    when (t) {
      is PhotoDetailError -> t
      is IOException ->
        when (t) {
          is UnknownHostException, is SocketException -> PhotoDetailError.NetworkError
          is SocketTimeoutException -> PhotoDetailError.TimeoutError
          else -> null
        }

      else -> null
    }
}
