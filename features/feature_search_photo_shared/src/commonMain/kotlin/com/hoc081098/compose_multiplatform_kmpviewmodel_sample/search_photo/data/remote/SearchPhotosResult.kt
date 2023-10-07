@file:Suppress("ktlint:standard:discouraged-comment-location")

package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.remote.response.CoverPhotoResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchPhotosResult(
  @SerialName(value = "total") val total: Int,
  @SerialName(value = "total_pages") val totalPages: Int,
  @SerialName(value = "results") val results: List<CoverPhotoResponse>,
)
