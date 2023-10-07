package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class PhotoDetail(
  val id: String,
  val fullUrl: String,
  val description: String?,
  val alternativeDescription: String?,
  val createdAt: Instant,
  val updatedAt: Instant,
  val promotedAt: Instant?,
  val creator: PhotoCreator,
  val size: PhotoSize,
)

@Immutable
data class PhotoSize(
  val width: UInt,
  val height: UInt,
)

@Immutable
data class PhotoCreator(
  val id: String,
  val username: String,
  val name: String,
  val mediumProfileImageUrl: String?,
)
