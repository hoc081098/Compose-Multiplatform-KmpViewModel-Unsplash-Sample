package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosResult(
  @SerialName(value = "total") val total: Int,
  @SerialName(value = "total_pages") val totalPages: Int,
  @SerialName(value = "results") val results: List<CoverPhotoResponse>
)