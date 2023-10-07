package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.BuildKonfig
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
  override suspend fun searchPhotos(query: String) =
    httpClient
      .get(
        URLBuilder(BuildKonfig.UNSPLASH_BASE_URL)
          .apply {
            path("search/photos")
            parameters.run {
              append("query", query)
              append("page", "1")
              append("per_page", "30")
            }
          }.build(),
      ) {
        header(
          HttpHeaders.Authorization,
          "Client-ID ${BuildKonfig.UNSPLASH_CLIENT_ID}",
        )
      }.body<SearchPhotosResult>()
}
