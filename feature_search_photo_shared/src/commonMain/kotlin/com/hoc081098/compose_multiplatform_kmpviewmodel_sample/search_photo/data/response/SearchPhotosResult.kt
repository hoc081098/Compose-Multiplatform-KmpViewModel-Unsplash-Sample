package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.search_photo.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchPhotosResult(
  @SerialName(value = "total") val total: Int,
  @SerialName(value = "total_pages") val totalPages: Int,
  @SerialName(value = "results") val results: List<CoverPhotoResponse>
)