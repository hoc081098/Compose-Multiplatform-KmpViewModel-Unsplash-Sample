@file:Suppress(
  "ktlint:standard:discouraged-comment-location",
  "ktlint:standard:max-line-length",
)

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.response

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UrlsResponse(
  @SerialName(value = "raw") val raw: String, // https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-4.0.3
  @SerialName(
    value = "full",
  ) val full: String, // https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-4.0.3&q=85&fm=jpg&crop=entropy&cs=srgb
  @SerialName(
    value = "regular",
  ) val regular: String, // https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max
  @SerialName(
    value = "small",
  ) val small: String, // https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max
  @SerialName(
    value = "thumb",
  ) val thumb: String, // https://images.unsplash.com/photo-1560089000-7433a4ebbd64?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max
  @SerialName(
    value = "small_s3",
  ) val smallS3: String, // https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1560089000-7433a4ebbd64
)

@Serializable
internal data class LinksResponse(
  @SerialName(value = "self") val self: String, // https://api.unsplash.com/photos/mzt0A967scs
  @SerialName(value = "html") val html: String, // https://unsplash.com/photos/mzt0A967scs
  @SerialName(value = "download") val download: String, // https://unsplash.com/photos/mzt0A967scs/download
  @SerialName(value = "download_location") val downloadLocation: String, // https://api.unsplash.com/photos/mzt0A967scs/download
)

@Serializable
internal data class CoverPhotoResponse(
  @SerialName(value = "id") val id: String, // mzt0A967scs
  @SerialName(value = "slug") val slug: String, // mzt0A967scs
  @SerialName(value = "created_at") val createdAt: Instant, // 2023-03-10T14:13:07Z
  @SerialName(value = "updated_at") val updatedAt: Instant, // 2023-04-15T00:20:56Z
  @SerialName(value = "promoted_at") val promotedAt: Instant?, // 2022-12-20T11:44:03Z
  @SerialName(value = "width") val width: Int, // 4672
  @SerialName(value = "height") val height: Int, // 7008
  @SerialName(value = "color") val color: String, // #262626
  @SerialName(value = "blur_hash") val blurHash: String, // LNBp;G?w%2aJRkt7V@WAOuWZWARO
  @SerialName(value = "description") val description: String?, // Tek it married
  @SerialName(value = "alt_description") val altDescription: String?, // a man holding a basketball standing next to a fence
  @SerialName(value = "urls") val urls: UrlsResponse,
  @SerialName(value = "links") val links: LinksResponse,
  @SerialName(value = "likes") val likes: Int, // 2
  @SerialName(value = "liked_by_user") val likedByUser: Boolean, // false
)
