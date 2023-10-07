package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.BuildKonfig
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.response.CoverPhotoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.path
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    UnsplashApi::class,
  ],
)
internal class KtorUnsplashApi(
  private val httpClient: HttpClient,
) : UnsplashApi {
  override suspend fun getPhotoDetailById(id: String) =
    httpClient
      .get(
        URLBuilder(BuildKonfig.UNSPLASH_BASE_URL)
          .apply {
            path("photos/$id")
          }.build(),
      ) {
        header(
          HttpHeaders.Authorization,
          "Client-ID ${BuildKonfig.UNSPLASH_CLIENT_ID}",
        )
      }.body<CoverPhotoResponse>()
}
